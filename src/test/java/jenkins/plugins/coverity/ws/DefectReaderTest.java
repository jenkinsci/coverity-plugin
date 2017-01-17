/*******************************************************************************
 * Copyright (c) 2017 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.CIMStream;
import jenkins.plugins.coverity.CoverityBuildAction;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.DefectFilters;
import jenkins.plugins.coverity.TestableConsoleLogger;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestDefectService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jenkins.class)
public class DefectReaderTest {
    @Mock
    private Jenkins jenkins;

    @Mock
    private AbstractBuild<?, ?> build;

    @Mock
    private Launcher launcher;

    @Mock
    private BuildListener listener;

    @Mock
    private DescriptorImpl descriptor;

    private TestableConsoleLogger consoleLogger;
    private TestDefectService defectService;
    private final String cimInstanceName = "cim-instance";

    @Before
    public void setup() throws IOException {
        // setup jenkins
        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);

        // setup console logger
        consoleLogger = new TestableConsoleLogger();
        when(listener.getLogger()).thenReturn(consoleLogger.getPrintStream());

        // setup global configuration (DescriptorImpl) with TestWebService
        CIMInstance cimInstance = mock(CIMInstance.class);
        defectService = (TestDefectService)new TestWebServiceFactory().getDefectService(cimInstance);
        when(cimInstance.getDefectService()).thenReturn(defectService);
        when(descriptor.getInstance(cimInstanceName)).thenReturn(cimInstance);
    }

    @Test
    public void getLatestDefectsForBuild_withNoDefectFilters_addDefectsToBuildAction() throws Descriptor.FormException, ParseException, DatatypeConfigurationException {

        when(jenkins.getRootUrl()).thenReturn("rootUrl/");
        when(build.getUrl()).thenReturn("buildUrl/");

        List<CIMStream> cimStreams  = new ArrayList<>();
        cimStreams.add(new CIMStream(cimInstanceName, "test-project", "test-stream", null, "stream1", null));

        CoverityPublisher publisher = mock(CoverityPublisher.class);
        when(publisher.getCimStreams()).thenReturn(cimStreams);
        when(publisher.getDescriptor()).thenReturn(descriptor);

        defectService.setupMergedDefects(10);

        DefectReader reader = new DefectReader(build, launcher, listener, publisher);

        Boolean result = reader.getLatestDefectsForBuild();

        assertTrue(result);

        // assert build action added to build with expected defect count
        ArgumentCaptor<CoverityBuildAction> buildAction = ArgumentCaptor.forClass(CoverityBuildAction.class);
        verify(build).addAction(buildAction.capture());
        assertEquals(10, buildAction.getValue().getDefectIds().size());

        // verify all expected log messages were written
        consoleLogger.verifyMessages(
            "[Coverity] Fetching defects for stream test-stream",
            "[Coverity] Found 10 defects",
            "[Coverity] Found 10 defects matching all filters: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]",
            "Coverity details: rootUrl/buildUrl/coverity_cim-instance_test-project_test-stream");
    }

    @Test
    public void getLatestDefectsForBuild_withMatchingDefectFilters_addDefectsToBuildAction() throws Descriptor.FormException, ParseException, DatatypeConfigurationException {
        when(jenkins.getRootUrl()).thenReturn("rootUrl/");
        when(build.getUrl()).thenReturn("buildUrl/");

        List<CIMStream> cimStreams  = new ArrayList<>();
        DefectFilters defectFilters = new DefectFilters(
            Arrays.asList("Undecided"),
            new ArrayList<>(Arrays.asList("High", "Medium", "Low")),
            Arrays.asList("Unclassified"),
            Arrays.asList("Unspecified", "Major", "Moderate", "Minor"),
            Arrays.asList("Default.Other"),
            Arrays.asList("TEST_CHECKER"),
            "2017-01-01");
        cimStreams.add(new CIMStream(cimInstanceName, "test-project", "test-stream", defectFilters, "stream1", null));

        CoverityPublisher publisher = mock(CoverityPublisher.class);
        when(publisher.getCimStreams()).thenReturn(cimStreams);
        when(publisher.getDescriptor()).thenReturn(descriptor);

        defectService.setupMergedDefects(3);

        DefectReader reader = new DefectReader(build, launcher, listener, publisher);

        Boolean result = reader.getLatestDefectsForBuild();

        assertTrue(result);

        // assert build action added to build with expected defect count
        ArgumentCaptor<CoverityBuildAction> buildAction = ArgumentCaptor.forClass(CoverityBuildAction.class);
        verify(build).addAction(buildAction.capture());
        assertEquals(3, buildAction.getValue().getDefectIds().size());

        // verify all expected log messages were written
        consoleLogger.verifyMessages(
            "[Coverity] Fetching defects for stream test-stream",
            "[Coverity] Found 3 defects",
            "[Coverity] Found 3 defects matching all filters: [0, 1, 2]",
            "Coverity details: rootUrl/buildUrl/coverity_cim-instance_test-project_test-stream");
    }
}
