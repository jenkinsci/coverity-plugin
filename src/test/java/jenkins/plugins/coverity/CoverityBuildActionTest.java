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
package jenkins.plugins.coverity;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.thoughtworks.xstream.XStream;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Run;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.WebServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, WebServiceFactory.class})
public class CoverityBuildActionTest {
    @Mock
    private Jenkins jenkins;

    private CIMInstance cimInstance;

    @Before
    public void setup() throws IOException {
        // setup web service factory
        final WebServiceFactory testWsFactory = new TestWebServiceFactory();
        PowerMockito.mockStatic(WebServiceFactory.class);
        when(WebServiceFactory.getInstance()).thenReturn(testWsFactory);

        // setup jenkins
        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);
        DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        cimInstance = new CIMInstance("test-cim-instance", "test-cim-instance", 8443, "admin", "password", true, 9080);
        when(descriptor.getInstance(any(String.class))).thenReturn(cimInstance);
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(descriptor);
    }

    @Test
    public void getUrl_returnsFormattedUrlForDefect() throws IOException, CovRemoteServiceException_Exception {
        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 1, "stream", 1);

        List<CoverityDefect> defects = new ArrayList<>();
        defects.add(new CoverityDefect(Long.valueOf(1234), "CHECKER_NAME", "functionDisplayName", "/path/to/class"));

        CoverityBuildAction coverityBuildAction = new CoverityBuildAction(mock(AbstractBuild.class), "project0", "stream1", cimInstance.getName(), defects);

        final String url = coverityBuildAction.getURL(defects.get(0));

        String expectedUrl = String.format(
            "https://%s:%d/sourcebrowser.htm?projectId=%s&mergedDefectId=%d",
            cimInstance.getHost(),
            cimInstance.getPort(),
            0,
            defects.get(0).getCid());
        assertEquals(expectedUrl, url);
    }

    @Test
    public void getDefects_forPre190Build_returnsDefectIds() {
        String oldBuildXml = "<jenkins.plugins.coverity.CoverityBuildAction plugin=\"coverity@1.8.1\">\n" +
            "      <defectIds>\n" +
            "        <long>10260</long>\n" +
            "        <long>10261</long>\n" +
            "      </defectIds>\n" +
            "      <projectId>generated-defects</projectId>\n" +
            "      <streamId>generated-defects</streamId>\n" +
            "      <cimInstance>d-ubuntu12x64-04_18080</cimInstance>\n" +
            "    </jenkins.plugins.coverity.CoverityBuildAction>";

        XStream xstream = new XStream2();

        final CoverityBuildAction coverityBuildAction = (CoverityBuildAction)xstream.fromXML(oldBuildXml);
        assertNotNull(coverityBuildAction);

        final List<CoverityDefect> defects = coverityBuildAction.getDefects();
        assertEquals(2, defects.size());

        assertEquals(Long.valueOf(10260), defects.get(0).getCid());
        assertEquals("---", defects.get(0).getCheckerName());
        assertEquals("View in Coverity Connect", defects.get(0).getFunctionDisplayName());
        assertEquals(StringUtils.EMPTY, defects.get(0).getFilePathname());

        assertEquals(Long.valueOf(10261), defects.get(1).getCid());
        assertEquals("---", defects.get(1).getCheckerName());
        assertEquals("View in Coverity Connect", defects.get(1).getFunctionDisplayName());
        assertEquals(StringUtils.EMPTY, defects.get(1).getFilePathname());
    }

    @Test
    public void getProjectActions_includesCoverityProjectAction() {
        CoverityBuildAction coverityBuildAction = new CoverityBuildAction(mock(Run.class), "project0", "stream1", cimInstance.getName(), new ArrayList<CoverityDefect>());

        final Collection<? extends Action> result = coverityBuildAction.getProjectActions();

        assertEquals(1, result.size());
        assertThat(result.iterator().next(), instanceOf(CoverityProjectAction.class));
    }

    @Test
    public void getProjectActions_withNullBuild() {
        CoverityBuildAction coverityBuildAction = new CoverityBuildAction(null, "project0", "stream1", cimInstance.getName(), new ArrayList<CoverityDefect>());

        final Collection<? extends Action> result = coverityBuildAction.getProjectActions();

        assertTrue(result.isEmpty());
        assertNull(coverityBuildAction.getBuild());
    }

    @Test
    public void getProjectActions_withJobMissingPublisher() {
        final FreeStyleBuild freeStyleBuild = getFreeStyleBuild();

        CoverityBuildAction coverityBuildAction = new CoverityBuildAction(freeStyleBuild, "project0", "stream1", cimInstance.getName(), new ArrayList<CoverityDefect>());

        final Collection<? extends Action> result = coverityBuildAction.getProjectActions();

        assertEquals(1, result.size());
        assertThat(result.iterator().next(), instanceOf(CoverityProjectAction.class));
    }

    @Test
    public void getProjectActions_withJobPublisher() {
        final CoverityPublisherBuilder publisherBuilder = new CoverityPublisherBuilder();
        publisherBuilder.withHideChart(false);
        final FreeStyleBuild freeStyleBuild = getFreeStyleBuild(publisherBuilder.build());

        CoverityBuildAction coverityBuildAction = new CoverityBuildAction(freeStyleBuild, "project0", "stream1", cimInstance.getName(), new ArrayList<CoverityDefect>());

        final Collection<? extends Action> result = coverityBuildAction.getProjectActions();

        assertEquals(1, result.size());
        assertThat(result.iterator().next(), instanceOf(CoverityProjectAction.class));
    }

    @Test
    public void getProjectActions_withPublisherHidesChart() {
        final CoverityPublisherBuilder publisherBuilder = new CoverityPublisherBuilder();
        publisherBuilder.withHideChart(true);

        final FreeStyleBuild freeStyleBuild = getFreeStyleBuild(publisherBuilder.build());

        CoverityBuildAction coverityBuildAction = new CoverityBuildAction(freeStyleBuild, "project0", "stream1", cimInstance.getName(), new ArrayList<CoverityDefect>());

        final Collection<? extends Action> result = coverityBuildAction.getProjectActions();

        assertTrue(result.isEmpty());
        assertSame(freeStyleBuild, coverityBuildAction.getBuild());
    }

    public FreeStyleBuild getFreeStyleBuild(CoverityPublisher... publishers) {
        final FreeStyleBuild freeStyleBuild = mock(FreeStyleBuild.class);
        final FreeStyleProject freeStyleProject = mock(FreeStyleProject.class);
        final DescribableList<Publisher, Descriptor<Publisher>> publisherList = new DescribableList<Publisher, Descriptor<Publisher>>(freeStyleProject, Arrays.asList(publishers));
        when(freeStyleProject.getPublishersList()).thenReturn(publisherList);
        when(freeStyleBuild.getProject()).thenReturn(freeStyleProject);
        return freeStyleBuild;
    }
}
