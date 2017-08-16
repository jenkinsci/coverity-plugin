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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import jenkins.plugins.coverity.Utils.CIMInstanceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.thoughtworks.xstream.XStream;

import hudson.util.XStream2;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.WebServiceFactory;
import net.sf.json.JSONObject;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, WebServiceFactory.class})
public class CoverityPublisherDescriptorImplTests {
    private static final JSONObject PUBLISHER_FORM_OBJECT_JSON = JSONObject.fromObject("{\"name\":\"test-job\",\"publisher\":{\"kind\":\"jenkins.plugins.coverity.CoverityPublisher\",\"unstable\":false,\"hideChart\":false,\"invocationAssistance\":{\"buildArguments\":\"\",\"intermediateDir\":\"\",\"csharpMsvsca\":false,\"analyzeArguments\":\"\",\"saOverride\":\"\",\"commitArguments\":\"\",\"javaWarFile\":\"\"},\"keepIntDir\":false,\"cimStream\":{\"instance\":\"test-cim-instance\",\"id\":null,\"defectFilters\":{\"cutOffDate\":\"\"},\"project\":\"test-cim-project\",\"stream\":\"test-cim-stream\"},\"skipFetchingDefects\":false,\"failBuild\":false,\"stapler-class\":\"jenkins.plugins.coverity.CoverityPublisher\"},\"properties\":{\"hudson-model-ParametersDefinitionProperty\":{},\"stapler-class-bag\":\"true\"}}");

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
        cimInstance = new CIMInstanceBuilder().withName("test-cim-instance").withHost("test-cim-instance").withPort(8080)
                .withUser("admin").withPassword("password").withUseSSL(false).withCredentialId("")
                .build();
    }

    @Test
    public void doLoadProjectsForInstance_returnsJsonReponse() throws ServletException, IOException, org.json.simple.parser.ParseException {
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
