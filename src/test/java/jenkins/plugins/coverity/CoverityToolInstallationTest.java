/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;


import hudson.XmlFile;
import hudson.model.Saveable;
import hudson.model.listeners.SaveableListener;
import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.CoverityToolInstallation.CoverityToolInstallationDescriptor;
import junit.framework.TestCase;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, SaveableListener.class})
public class CoverityToolInstallationTest extends TestCase {
    @Mock
    private Jenkins jenkins;

    @Rule
    public TemporaryFolder tempJenkinsRoot = new TemporaryFolder();

    @Before
    public void setup() {
        // setup jenkins
        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);
        when(jenkins.getRootDir()).thenReturn(tempJenkinsRoot.getRoot());
        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(descriptor);

        CoverityToolInstallationDescriptor toolDescriptor = new CoverityToolInstallationDescriptor();
        when(jenkins.getDescriptorOrDie(CoverityToolInstallation.class)).thenReturn(toolDescriptor);

        // setup SaveableListener to verify save
        PowerMockito.mockStatic(SaveableListener.class);
    }

    @Test
    public void descriptor_setInstallations_savesToCoverityPublisher() {
        final CoverityToolInstallation toolInstallation = new CoverityToolInstallation("covInstall", "/bin/cov-tool");
        final CoverityToolInstallationDescriptor toolDescriptor = (CoverityToolInstallationDescriptor)toolInstallation.getDescriptor();

        CoverityToolInstallation[] installations = toolDescriptor.getInstallations();

        assertEquals(0, installations.length);

        final CoverityToolInstallation[] newInstallations = new CoverityToolInstallation[1];
        newInstallations[0] = toolInstallation;
        toolDescriptor.setInstallations(newInstallations);

        installations = toolDescriptor.getInstallations();
        assertEquals(1, installations.length);
        assertSame(newInstallations[0], installations[0]);

        XmlFile expectedPublsherConfig = new XmlFile(new File(jenkins.getRootDir(),CoverityPublisher.class.getName() +".xml"));
        ArgumentCaptor<XmlFile> configFileArg = ArgumentCaptor.forClass(XmlFile.class);

        // verify static SaveableListener is called with expected publisher config
        PowerMockito.verifyStatic();
        SaveableListener.fireOnChange(any(Saveable.class), configFileArg.capture());

        assertEquals(expectedPublsherConfig.getFile(), configFileArg.getValue().getFile());
    }

    @Test
    public void descriptor_doCheckHome_validatesAnalysisLocation() throws IOException {
        final CoverityToolInstallation toolInstallation = new CoverityToolInstallation("covInstall", "/bin/cov-tool");
        final CoverityToolInstallationDescriptor toolDescriptor = (CoverityToolInstallationDescriptor)toolInstallation.getDescriptor();

        FormValidation result = toolDescriptor.doCheckHome(new File(StringUtils.EMPTY));
        assertEquals(Kind.OK, result.kind);

        result = toolDescriptor.doCheckHome(tempJenkinsRoot.newFolder("cov-install"));
        assertEquals(Kind.ERROR, result.kind);
        assertEquals("The specified Analysis installation directory doesn't contain a VERSION.xml file.",
            StringEscapeUtils.unescapeHtml(result.getMessage()));

        final URL resource =  this.getClass().getResource("/VERSION.xml");
        assertNotNull(resource);
        File versionFile = new File(resource.getPath());
        result = toolDescriptor.doCheckHome(versionFile.getParentFile());
        assertEquals(Kind.OK, result.kind);
        assertEquals("Analysis installation directory has been verified.",
            StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    public void descriptor_doCheckHome_withUnsupportedVersion() throws IOException {
        final CoverityToolInstallation toolInstallation = new CoverityToolInstallation("Unsupported Coverity Install", "unknown-directory");
        final CoverityToolInstallationDescriptor toolDescriptor = (CoverityToolInstallationDescriptor)toolInstallation.getDescriptor();

        final URL resource =  this.getClass().getResource("/UnsupportedCoverity/VERSION.xml");
        assertNotNull(resource);
        File versionFile = new File(resource.getPath());
        FormValidation result = toolDescriptor.doCheckHome(versionFile.getParentFile());
        assertEquals(Kind.ERROR, result.kind);
        assertEquals("Analysis version 6.6.2 detected. The minimum supported version is " + CoverityVersion.MINIMUM_SUPPORTED_VERSION.toString(),
                StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedNodeProperty_coverityInstallation_doCheckHomeWarnsForAnyValue() {
        final CoverityInstallation.DescriptorImpl descriptor = new CoverityInstallation.DescriptorImpl();
        when(jenkins.getDescriptorOrDie(CoverityInstallation.class)).thenReturn(descriptor);
        final CoverityInstallation installation = new CoverityInstallation("node/tools/override");

        final CoverityInstallation.DescriptorImpl installationDescriptor = (CoverityInstallation.DescriptorImpl)installation.getDescriptor();

        FormValidation formValidation = installationDescriptor.doCheckHome(installation.getHome());
        assertEquals("Expect validation warning for any given home value", Kind.WARNING, formValidation.kind);

        formValidation = installationDescriptor.doCheckHome(StringUtils.EMPTY);
        assertEquals("Expect validation ok for empty string", Kind.OK, formValidation.kind);

        formValidation = installationDescriptor.doCheckHome(null);
        assertEquals("Expect validation ok for null", Kind.OK, formValidation.kind);
    }
}
