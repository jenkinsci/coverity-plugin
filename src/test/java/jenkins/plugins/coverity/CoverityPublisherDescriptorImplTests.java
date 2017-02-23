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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

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
        DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        cimInstance = new CIMInstance("test-cim-instance", "test-cim-instance", 8080, "admin", "password", false, 9080);
        when(descriptor.getInstance(any(String.class))).thenReturn(cimInstance);
        when(jenkins.getDescriptorOrDie(CoverityPublisher.class)).thenReturn(descriptor);
    }

    @Test
    public void doLoadProjectsForInstance_returnsJsonReponse() throws ServletException, IOException, org.json.simple.parser.ParseException {
        final String projectName = "test-cim-project";
        final String streamName = "test-cim-stream";
        CIMStream stream = new CIMStream("test-cim-instance", projectName, streamName, null);
        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects(projectName, 2, streamName, 3);

        CoverityPublisher publisher = new CoverityPublisherBuilder().withCimStream(stream).build();

        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        StaplerRequest request = mock(StaplerRequest.class);
        when(request.getSubmittedForm()).thenReturn(PUBLISHER_FORM_OBJECT_JSON);
        when(request.bindJSON(eq(CoverityPublisher.class), any(JSONObject.class))).thenReturn(publisher);

        StaplerResponse response = mock(StaplerResponse.class);
        final ByteArrayOutputStream testableStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                testableStream.write(b);
            }
        };

        try {
            when(response.getOutputStream()).thenReturn(responseOutputStream);

            descriptor.doLoadProjectsForInstance(request, response);

            Mockito.verify(response).setContentType("application/json; charset=utf-8");
            String responseOutput = new String(testableStream.toByteArray());
            assertEquals(
                String.format("{\"projects\":[\"%1$s\",\"%1$s0\",\"%1$s1\"],\"selectedProject\":\"%1$s\",\"validSelection\":false}", projectName),
                responseOutput);
        } finally {
            responseOutputStream.close();
        }
    }

    @Test
    public void doLoadStreamsForProject_returnsJsonResponse() throws ServletException, IOException, org.json.simple.parser.ParseException {
        final String projectName = "test-cim-project";
        final String streamName = "test-cim-stream";
        CIMStream stream = new CIMStream("test-cim-instance", projectName + 1, streamName, null);
        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects(projectName, 2, streamName, 3);

        CoverityPublisher publisher = new CoverityPublisherBuilder().withCimStream(stream).build();

        DescriptorImpl descriptor = new CoverityPublisher.DescriptorImpl();

        StaplerRequest request = mock(StaplerRequest.class);
        when(request.getSubmittedForm()).thenReturn(PUBLISHER_FORM_OBJECT_JSON);
        when(request.bindJSON(eq(CoverityPublisher.class), any(JSONObject.class))).thenReturn(publisher);

        StaplerResponse response = mock(StaplerResponse.class);
        final ByteArrayOutputStream testableStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                testableStream.write(b);
            }
        };

        try {
            when(response.getOutputStream()).thenReturn(responseOutputStream);

            descriptor.doLoadStreamsForProject(request, response);

            Mockito.verify(response).setContentType("application/json; charset=utf-8");
            String responseOutput = new String(testableStream.toByteArray());
            assertEquals(
                String.format("{\"streams\":[\"%1$s\",\"%1$s0\",\"%1$s1\",\"%1$s2\"],\"selectedStream\":\"%1$s\",\"validSelection\":false}", streamName),
                responseOutput);
        } finally {
            responseOutputStream.close();
        }
    }
}
