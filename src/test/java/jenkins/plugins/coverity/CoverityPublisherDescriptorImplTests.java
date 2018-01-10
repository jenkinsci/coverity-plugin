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

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.thoughtworks.xstream.XStream;
import hudson.model.Descriptor;
import hudson.model.listeners.SaveableListener;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.Utils.CIMInstanceBuilder;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.Utils.CredentialUtil;
import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.WebServiceFactory;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, SaveableListener.class, WebServiceFactory.class, Secret.class, CredentialsMatchers.class, CredentialsProvider.class})
public class CoverityPublisherDescriptorImplTests {
    private static final JSONObject PUBLISHER_FORM_OBJECT_JSON = JSONObject.fromObject("{\"name\":\"test-job\",\"publisher\":{\"kind\":\"jenkins.plugins.coverity.CoverityPublisher\",\"unstable\":false,\"hideChart\":false,\"invocationAssistance\":{\"buildArguments\":\"\",\"intermediateDir\":\"\",\"csharpMsvsca\":false,\"analyzeArguments\":\"\",\"saOverride\":\"\",\"commitArguments\":\"\",\"javaWarFile\":\"\"},\"keepIntDir\":false,\"cimStream\":{\"instance\":\"test-cim-instance\",\"id\":null,\"defectFilters\":{\"cutOffDate\":\"\"},\"project\":\"test-cim-project\",\"stream\":\"test-cim-stream\"},\"skipFetchingDefects\":false,\"failBuild\":false,\"stapler-class\":\"jenkins.plugins.coverity.CoverityPublisher\"},\"properties\":{\"hudson-model-ParametersDefinitionProperty\":{},\"stapler-class-bag\":\"true\"}}");

    @Mock
    private Jenkins jenkins;

    @Rule
    public TemporaryFolder tempJenkinsRoot = new TemporaryFolder();

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
        when(jenkins.getRootDir()).thenReturn(tempJenkinsRoot.getRoot());
        CredentialUtil.setCredentialManager("admin", "password");
        cimInstance = new CIMInstanceBuilder().withName("test-cim-instance").withHost("test-cim-instance").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();
    }

    @Test
    public void newInstance_createsNewPublisher_withDefectFiltersConfigured() throws Descriptor.FormException, IOException {
        when(jenkins.getDescriptorOrDie(CIMStream.class)).thenReturn(new CIMStream.DescriptorImpl());
        StaplerRequest request = mock(StaplerRequest.class);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("proj", 1, "test-cim-stream", 2);
        testConfigurationService.setupComponents("Default", "Other", "Ignored Component");
        testConfigurationService.setupCheckerNames("CHECKER1", "CHECKER2", "IGNORED_CHECKER");

        DefectFilters defectFilters = new DefectFilters();
        defectFilters.setCheckers(Arrays.asList("CHECKER1", "CHECKER2"));
        defectFilters.setComponents(Arrays.asList("Default", "Other"));
        CIMStream stream = new CIMStream("test-cim-instance", "test-cim-project", "test-cim-stream1");
        stream.setDefectFilters(defectFilters);
        CoverityPublisherBuilder publisherBuilder = new CoverityPublisherBuilder().withCimStream(stream);
        when(request.bindJSON(eq(CoverityPublisher.class), any(JSONObject.class))).thenReturn(publisherBuilder.build());

        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();
        descriptor.setInstances(Arrays.asList(cimInstance));
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(descriptor);

        CoverityPublisher publisher = (CoverityPublisher) descriptor.newInstance(request, PUBLISHER_FORM_OBJECT_JSON);

        assertNotNull(publisher);
        assertNotNull(publisher.getCimStream());
        DefectFilters filters = publisher.getCimStream().getDefectFilters();
        assertNotNull(filters);
        assertArrayEquals(defectFilters.getCheckersList().toArray(), filters.getCheckersList().toArray());
        assertArrayEquals(new String[] {"IGNORED_CHECKER"}, filters.getIgnoredChecker().toArray());
        assertArrayEquals(defectFilters.getComponents().toArray(), filters.getComponents().toArray());
        assertArrayEquals(new String[] {"Ignored Component"}, filters.getIgnoredComponents().toArray());
    }

    @Test
    public void doLoadProjectsForInstance_returnsJsonResponse() throws ServletException, IOException, org.json.simple.parser.ParseException {
        final String projectName = "test-cim-project";
        final String streamName = "test-cim-stream";
        CIMStream stream = new CIMStream("test-cim-instance", projectName, streamName);
        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects(projectName, 2, streamName, 3);

        CoverityPublisher publisher = new CoverityPublisherBuilder().withCimStream(stream).build();

        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();
        descriptor.setInstances(Arrays.asList(cimInstance));

        StaplerRequest request = mock(StaplerRequest.class);
        when(request.getSubmittedForm()).thenReturn(PUBLISHER_FORM_OBJECT_JSON);
        when(request.bindJSON(eq(CoverityPublisher.class), any(JSONObject.class))).thenReturn(publisher);

        StaplerResponse response = mock(StaplerResponse.class);
        final ByteArrayOutputStream testableStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream = getServletOutputStream(testableStream);

        try {
            when(response.getOutputStream()).thenReturn(responseOutputStream);

            descriptor.doLoadProjectsForInstance(request, response);

            Mockito.verify(response).setContentType("application/json; charset=utf-8");
            String responseOutput = new String(testableStream.toByteArray(), StandardCharsets.UTF_8);
            assertEquals(
                String.format("{\"projects\":[\"%1$s0\",\"%1$s1\",\"%1$s\"],\"selectedProject\":\"%1$s\",\"validSelection\":false}", projectName),
                responseOutput);
        } finally {
            responseOutputStream.close();
        }
    }

    @Test
    public void doLoadStreamsForProject_returnsJsonResponse() throws ServletException, IOException, org.json.simple.parser.ParseException {
        final String projectName = "test-cim-project";
        final String streamName = "test-cim-stream";
        CIMStream stream = new CIMStream("test-cim-instance", projectName + 1, streamName);
        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects(projectName, 2, streamName, 3);

        CoverityPublisher publisher = new CoverityPublisherBuilder().withCimStream(stream).build();

        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();
        descriptor.setInstances(Arrays.asList(cimInstance));

        StaplerRequest request = mock(StaplerRequest.class);
        when(request.getSubmittedForm()).thenReturn(PUBLISHER_FORM_OBJECT_JSON);
        when(request.bindJSON(eq(CoverityPublisher.class), any(JSONObject.class))).thenReturn(publisher);

        StaplerResponse response = mock(StaplerResponse.class);
        final ByteArrayOutputStream testableStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream = getServletOutputStream(testableStream);

        try {
            when(response.getOutputStream()).thenReturn(responseOutputStream);

            descriptor.doLoadStreamsForProject(request, response);

            Mockito.verify(response).setContentType("application/json; charset=utf-8");
            String responseOutput = new String(testableStream.toByteArray(), StandardCharsets.UTF_8);
            assertEquals(
                String.format("{\"streams\":[\"%1$s0\",\"%1$s1\",\"%1$s2\",\"%1$s\"],\"selectedStream\":\"%1$s\",\"validSelection\":false}", streamName),
                responseOutput);
        } finally {
            responseOutputStream.close();
        }
    }

    @Test
    public void doDefectFiltersConfig_initializesFilter() throws ServletException, IOException, CovRemoteServiceException_Exception {
        final String projectName = "test-cim-project";
        final String streamName = "test-cim-stream";
        CIMStream stream = new CIMStream("test-cim-instance", projectName + "0", streamName + "0");
        stream.setDefectFilters(new DefectFilters());
        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects(projectName, 1, streamName, 1);
        testConfigurationService.setupCheckerNames("CHECKER1", "CHECKER2");
        testConfigurationService.setupComponents("Default", "Other");
        testConfigurationService.setupDefaultAttributes();

        CoverityPublisher publisher = new CoverityPublisherBuilder().withCimStream(stream).build();

        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();
        descriptor.setInstances(Arrays.asList(cimInstance));
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(descriptor);
        when(jenkins.getDescriptorOrDie(CIMStream.class)).thenReturn(new CIMStream.DescriptorImpl());

        StaplerRequest request = mock(StaplerRequest.class);
        when(request.getSubmittedForm()).thenReturn(PUBLISHER_FORM_OBJECT_JSON);
        when(request.bindJSON(eq(CoverityPublisher.class), any(JSONObject.class))).thenReturn(publisher);

        StaplerResponse response = mock(StaplerResponse.class);
        final ByteArrayOutputStream testableStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream = getServletOutputStream(testableStream);

        try {
            when(response.getOutputStream()).thenReturn(responseOutputStream);

            descriptor.doDefectFiltersConfig(request, response);

            Mockito.verify(response).forward(any(CIMStream.DescriptorImpl.class), eq("defectFilters"), same(request));

            // verify the filter has expected default attribute values
            DefectFilters filters = publisher.getCimStream().getDefectFilters();
            assertNotNull(filters);
            assertEquals(Arrays.asList("CHECKER1", "CHECKER2"), filters.getCheckersList());
            assertEquals(Arrays.asList("Default", "Other"), filters.getComponents());
            assertEquals(Arrays.asList("Unclassified", "Pending", "Bug", "Untested"), filters.getClassifications());
            assertEquals(Arrays.asList("Undecided", "Fix Required", "Fix Submitted", "Modeling Required", "Ignore"), filters.getActions());
            assertEquals(Arrays.asList("Unspecified", "Major", "Moderate", "Minor"), filters.getSeverities());
            assertEquals(Arrays.asList("High", "Medium", "Low"), filters.getImpacts());

        } finally {
            responseOutputStream.close();
        }
    }

    @Test
    public void getSslConfigurations_forPre192Settings() {
        String oldXml = "<jenkins.plugins.coverity.CoverityPublisher_-DescriptorImpl plugin=\"coverity@1.8.1\">\n" +
            "      <sslConfigurations>\n" +
            "        <trustNewSelfSignedCert>true</trustNewSelfSignedCert>\n" +
            "        <certFileName>C:\\customcerts\\mycertifcate.cer</certFileName>\n" +
            "      </sslConfigurations>\n" +
            "    </jenkins.plugins.coverity.CoverityPublisher_-DescriptorImpl>";

        XStream xstream = new XStream2();

        final CoverityPublisher.DescriptorImpl descriptor = (CoverityPublisher.DescriptorImpl)xstream.fromXML(oldXml);
        assertNotNull(descriptor);

        final SSLConfigurations sslConfigurations = descriptor.getSslConfigurations();
        assertNotNull(sslConfigurations);

        assertTrue(sslConfigurations.isTrustNewSelfSignedCert());
        assertEquals("C:\\customcerts\\mycertifcate.cer", sslConfigurations.getCertFileName());
    }

    @Test
    public void doCheckHome_doCheckHomeWarnsForAnyValue() {
        final DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        FormValidation formValidation = descriptor.doCheckHome("node/tools/override");
        assertEquals("Expect validation warning for any given home value", FormValidation.Kind.WARNING, formValidation.kind);

        formValidation = descriptor.doCheckHome(StringUtils.EMPTY);
        assertEquals("Expect validation ok for empty string", FormValidation.Kind.OK, formValidation.kind);

        formValidation = descriptor.doCheckHome(null);
        assertEquals("Expect validation ok for null", FormValidation.Kind.OK, formValidation.kind);
    }

    /**
     * Verifies the readResolve will create a new 'default' tool installation from previous 'home' String value
     */
    @Test
    public void defaultCoverityToolInstallationCreated_fromPre110HomeValue() {
        final String toolsPath = "C:\\Program Files\\Coverity\\Coverity Static Analysis";
        final String version19Xml = "<jenkins.plugins.coverity.CoverityPublisher_-DescriptorImpl plugin=\"coverity@1.9.2\">\n" +
                "  <home>" + toolsPath + "</home>\n" +
                "  <instances/>\n" +
                "  <installations/>\n" +
                "</jenkins.plugins.coverity.CoverityPublisher_-DescriptorImpl>";

        XStream xstream = new XStream2();

        final CoverityPublisher.DescriptorImpl descriptor = (CoverityPublisher.DescriptorImpl)xstream.fromXML(version19Xml);
        assertNotNull(descriptor);

        final CoverityToolInstallation[] toolInstallations = descriptor.getInstallations();
        assertEquals(1, toolInstallations.length);
        assertEquals(toolsPath, toolInstallations[0].getHome());
    }

    /**
     * Verifies the readResolve will not create a additional 'default' tool installation when an installation and the
     * previous 'home' String value both exist
     */
    @Test
    public void additionalCoverityToolNotCreated_whenExistingHomeValueAlreadyIsDefault() {
        final String toolsPath = "C:\\Program Files\\Coverity\\Coverity Static Analysis";
        final String version110Xml = "<jenkins.plugins.coverity.CoverityPublisher_-DescriptorImpl plugin=\"coverity@1.10.0\">\n" +
                "  <home>" + toolsPath + "</home>\n" +
                "  <instances/>\n" +
                "  <installations>\n" +
                "    <jenkins.plugins.coverity.CoverityToolInstallation>\n" +
                "      <name>" + CoverityToolInstallation.DEFAULT_NAME + "</name>\n" +
                "      <home>" + toolsPath + "</home>\n" +
                "    </jenkins.plugins.coverity.CoverityToolInstallation>\n" +
                "  </installations>\n" +
                "</jenkins.plugins.coverity.CoverityPublisher_-DescriptorImpl>";

        XStream xstream = new XStream2();

        final CoverityPublisher.DescriptorImpl descriptor = (CoverityPublisher.DescriptorImpl)xstream.fromXML(version110Xml);
        assertNotNull(descriptor);

        final CoverityToolInstallation[] toolInstallations = descriptor.getInstallations();
        assertEquals(1, toolInstallations.length);
        assertEquals(toolsPath, toolInstallations[0].getHome());
    }

    @Test
    public void doFillToolInstallationNameItems_returnsInstallations() {
        PowerMockito.mockStatic(SaveableListener.class);

        final DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        ListBoxModel installationNameItems = descriptor.doFillToolInstallationNameItems();

        assertNotNull(installationNameItems);
        assertEquals(0, installationNameItems.size());

        descriptor.setInstallations(
                new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, "default/path/to/coverity"),
                new CoverityToolInstallation("Additional install", "alternate/path/to/coverity"));

        installationNameItems = descriptor.doFillToolInstallationNameItems();

        assertNotNull(installationNameItems);
        assertEquals(2, installationNameItems.size());
        assertEquals(CoverityToolInstallation.DEFAULT_NAME, installationNameItems.get(0).name);
        assertEquals(CoverityToolInstallation.DEFAULT_NAME, installationNameItems.get(0).value);
        assertEquals("Additional install", installationNameItems.get(1).name);
        assertEquals("Additional install", installationNameItems.get(1).value);
    }


    @Test
    public void doCheckPostCovBuildCmdTest(){
        final CoverityPublisher.DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        FormValidation result = descriptor.doCheckPostCovBuildCmd(StringUtils.EMPTY);
        assertEquals(result.kind, FormValidation.Kind.ERROR);
        assertEquals(result.getMessage(), "Post cov-build command cannot be empty!");

        result = descriptor.doCheckPostCovBuildCmd("Test Post cov-build command");
        assertEquals(result.kind, FormValidation.Kind.OK);
    }

    @Test
    public void doCheckPostAnalyzeCmdTest(){
        final CoverityPublisher.DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        FormValidation result = descriptor.doCheckPostCovAnalyzeCmd(StringUtils.EMPTY);
        assertEquals(result.kind, FormValidation.Kind.ERROR);
        assertEquals(result.getMessage(), "Post cov-analyze command cannot be empty!");

        result = descriptor.doCheckPostCovAnalyzeCmd("Test Post cov-analyze command");
        assertEquals(result.kind, FormValidation.Kind.OK);
    }

    @Test
    public void doCheckUserTest(){
        final CoverityPublisher.DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        FormValidation result = descriptor.doCheckUser(StringUtils.EMPTY);
        assertEquals(result.kind, FormValidation.Kind.OK);

        result = descriptor.doCheckUser("TestUser");
        assertEquals(result.kind, FormValidation.Kind.WARNING);
        assertEquals(result.getMessage(), "User is deprecated in Coverity plugin version 1.10 and later. Please use Credentials above for more secure username.");
    }

    @Test
    public void doCheckPasswordTest(){
        final CoverityPublisher.DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        FormValidation result = descriptor.doCheckPassword(StringUtils.EMPTY);
        assertEquals(result.kind, FormValidation.Kind.OK);

        result = descriptor.doCheckPassword("TestPassword");
        assertEquals(result.kind, FormValidation.Kind.WARNING);
        assertEquals(result.getMessage(), "Password is deprecated in Coverity plugin version 1.10 and later. Please use Credentials above for more secure password.");
    }

    @Test
    public void getInstanceTest(){
        CoverityPublisher publisher = new CoverityPublisherBuilder().build();
        final CoverityPublisher.DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        CIMInstance result = descriptor.getInstance(publisher);
        assertNull(result);

        CIMInstance instance = new CIMInstance("instance", "localhost", 8080, "defaultCredentialID");
        descriptor.setInstances(Arrays.asList(instance));

        CIMStream stream = new CIMStream("instance", "project", "stream");
        stream.setCredentialId("OverriddenCredentialId");
        publisher = new CoverityPublisherBuilder().withCimStream(stream).build();

        result = descriptor.getInstance(publisher);
        assertNotNull(result);
        assertEquals("OverriddenCredentialId", result.getCredentialId());
    }

    private ServletOutputStream getServletOutputStream(final ByteArrayOutputStream testableStream) {
        return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                }

                @Override
                public void write(int b) throws IOException {
                    testableStream.write(b);
                }
            };
    }
}
