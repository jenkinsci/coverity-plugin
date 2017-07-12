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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sun.jersey.api.client.Client;

import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.TestableViewsService;
import jenkins.plugins.coverity.ws.WebServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, WebServiceFactory.class, Client.class})
public class CoverityViewResultsDescriptorTest {
    private CIMInstance cimInstance;

    @Before
    public void setup() {
        // setup jenkins
        Jenkins jenkins = mock(Jenkins.class);
        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);
        DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        cimInstance = new CIMInstance("test-cim-instance", "test-cim-instance", 8080, "admin", "password", false, 9080);
        final List<CIMInstance> cimInstances = Arrays.asList(cimInstance);
        when(descriptor.getInstances()).thenReturn(cimInstances);
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(descriptor);
    }

    /**
     * Sets up web service which will successfully test connection, return the {@link TestConfigurationService}
     */
    private TestConfigurationService setupConfigurationService(CIMInstance instance) {
        try {
            // setup web service factory to succeed a connect check
            final TestWebServiceFactory testWsFactory = new TestWebServiceFactory();
            final TestConfigurationService testConfigurationService;
            testConfigurationService = (TestConfigurationService)testWsFactory.getConfigurationService(instance);
            testConfigurationService.setupUser(cimInstance.getUser(), true, new HashMap<String, String[]>());

            PowerMockito.mockStatic(WebServiceFactory.class);
            when(WebServiceFactory.getInstance()).thenReturn(testWsFactory);

            return testConfigurationService;
        } catch (IOException e) {
            fail("failed to setup webservice");
        }

        return null;
    }

    @Test
    public void doFillInstanceItems_returnsGlobalPublisherIntances() {
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        final ListBoxModel result = descriptor.doFillConnectInstanceItems();

        assertEquals(1, result.size());
        assertEquals(cimInstance.getName(), result.get(0).name);
        assertEquals(cimInstance.getName(), result.get(0).value);
    }

    @Test
    public void doCheckViews_nullValuesRequired() {
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(null, null, null);

        assertEquals(Kind.ERROR, result.kind);
        assertThat(result.toString(), CoreMatchers.containsString("Coverity Connect Instance is required"));
        assertThat(result.toString(), CoreMatchers.containsString("Coverity Connect Project is required"));
        assertThat(result.toString(), CoreMatchers.containsString("Coverity Connect View is required"));
    }

    @Test
    public void doCheckViews_emptyValuesRequired() {
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        assertEquals(Kind.ERROR, result.kind);
        assertThat(result.toString(), CoreMatchers.containsString("Coverity Connect Instance is required"));
        assertThat(result.toString(), CoreMatchers.containsString("Coverity Connect Project is required"));
        assertThat(result.toString(), CoreMatchers.containsString("Coverity Connect View is required"));
    }

    @Test
    public void doCheck_withUnknownConnectInstance() {
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews("unknown-instance", "projectId", "viewName");

        assertEquals(Kind.ERROR, result.kind);
        assertThat(result.toString(), CoreMatchers.containsString("Unable to find Coverity Connect instance: unknown-instance"));
    }

    @Test
    public void doCheck_withInvalidConnectInstance() {
        final TestConfigurationService testConfigurationService = setupConfigurationService(cimInstance);
        // set up the user to have no permissions at all
        testConfigurationService.setupUser(cimInstance.getUser(), false, new HashMap<String, String[]>());
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "projectId", "viewName");

        assertEquals(Kind.ERROR, result.kind);
        assertThat(StringEscapeUtils.unescapeHtml(result.toString()),
            CoreMatchers.containsString("\"" + cimInstance.getUser() + "\" does not have following permission(s): \"Access web services\""));
    }

    @Test
    public void doCheck_withInvalidProjectId() {
        setupConfigurationService(cimInstance);
        TestableViewsService.setupWithViewApi(StringUtils.EMPTY);
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "projectId", "viewName");

        assertEquals(Kind.ERROR, result.kind);
        assertThat(StringEscapeUtils.unescapeHtml(result.toString()),
            CoreMatchers.containsString("Unable to find project 'projectId' on instance '" + cimInstance.getName() + "'."));
    }

    @Test
    public void doCheck_withInvalidProjectId_suggestsKnownProjects() {
        final TestConfigurationService testConfigurationService = setupConfigurationService(cimInstance);
        testConfigurationService.setupProjects("pipeline-project", 2, "s", 1);
        TestableViewsService.setupWithViewApi(StringUtils.EMPTY);
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "projectId", "viewName");

        assertEquals(Kind.ERROR, result.kind);
        final String errorMessage = StringEscapeUtils.unescapeHtml(result.toString());
        assertThat(errorMessage,
            CoreMatchers.containsString("Unable to find project 'projectId' on instance '" + cimInstance.getName() + "'."));
        assertThat(errorMessage,
            CoreMatchers.containsString("Available projects include: pipeline-project0, pipeline-project1"));
    }

    @Test
    public void doCheck_withInvalidConnectView() {
        final TestConfigurationService testConfigurationService = setupConfigurationService(cimInstance);
        testConfigurationService.setupProjects("pipeline-project", 1, "s", 1);
        TestableViewsService.setupWithViewApi(StringUtils.EMPTY);
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "pipeline-project0", "viewName");

        assertEquals(Kind.ERROR, result.kind);
        assertThat(StringEscapeUtils.unescapeHtml(result.toString()),
            CoreMatchers.containsString("Unable to find the view 'viewName' on instance '" + cimInstance.getName() + "'."));
    }

    @Test
    public void doCheck_withInvalidConnectView_suggestsKnownViews() {
        final TestConfigurationService testConfigurationService = setupConfigurationService(cimInstance);
        testConfigurationService.setupProjects("pipeline-project", 1, "s", 1);
        final String viewApiJsonResult = "{\"views\": [" +
            "    {" +
            "        \"id\": 54321," +
            "        \"type\": \"issues\"," +
            "        \"name\": \"Custom View\"," +
            "        \"groupBy\":false," +
            "        \"columns\": [" +
            "            {" +
            "                \"name\": \"cid\"," +
            "                \"label\": \"CID\"" +
            "            }," +
            "            {" +
            "                \"name\": \"checker\"," +
            "                \"label\": \"Checker\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayFunction\"," +
            "                \"label\": \"Function\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayFile\"," +
            "                \"label\": \"File\"" +
            "            }" +
            "        ]" +
            "    }" +
            "    {" +
            "        \"id\": 67890," +
            "        \"type\": \"issues\"," +
            "        \"name\": \"All Defects\"," +
            "        \"groupBy\":false," +
            "        \"columns\": [" +
            "            {" +
            "                \"name\": \"cid\"," +
            "                \"label\": \"CID\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayType\"," +
            "                \"label\": \"Type\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayImpact\"," +
            "                \"label\": \"Impact\"" +
            "            }," +
            "            {" +
            "                \"name\": \"firstDetected\"," +
            "                \"label\": \"First Detected\"" +
            "            }," +
            "            {" +
            "                \"name\": \"owner\"," +
            "                \"label\": \"Owner\"" +
            "            }       " +
            "        ]" +
            "    }" +
            "]}";
        TestableViewsService.setupWithViewApi(viewApiJsonResult);
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "pipeline-project0", "viewName");

        assertEquals(Kind.ERROR, result.kind);
        final String errorMessage = StringEscapeUtils.unescapeHtml(result.toString());
        assertThat(errorMessage,
            CoreMatchers.containsString("Unable to find the view 'viewName' on instance '" + cimInstance.getName() + "'."));
        assertThat(errorMessage,
            CoreMatchers.containsString("Available views include: Custom View, All Defects"));
    }

    @Test
    public void doCheck_withValidConfiguration() {
        final TestConfigurationService testConfigurationService = setupConfigurationService(cimInstance);
        testConfigurationService.setupProjects("pipeline-project", 1, "s", 1);
        final String viewApiJsonResult = "{\"views\": [" +
            "    {" +
            "        \"id\": 54321," +
            "        \"type\": \"issues\"," +
            "        \"name\": \"Custom View\"," +
            "        \"groupBy\":false," +
            "        \"columns\": [" +
            "            {" +
            "                \"name\": \"cid\"," +
            "                \"label\": \"CID\"" +
            "            }," +
            "            {" +
            "                \"name\": \"checker\"," +
            "                \"label\": \"Checker\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayFunction\"," +
            "                \"label\": \"Function\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayFile\"," +
            "                \"label\": \"File\"" +
            "            }" +
            "        ]" +
            "    }" +
            "]}";
        TestableViewsService.setupWithViewApi(viewApiJsonResult);
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "pipeline-project0", "Custom View");

        assertEquals(Kind.OK, result.kind);
        assertEquals("Successfully configured", result.getMessage());
    }

    @Test
    public void doCheck_withValidConfiguration_usingConnectViewId() {
        final TestConfigurationService testConfigurationService = setupConfigurationService(cimInstance);
        testConfigurationService.setupProjects("pipeline-project", 1, "s", 1);
        final String viewApiJsonResult = "{\"views\": [" +
            "    {" +
            "        \"id\": 54321," +
            "        \"type\": \"issues\"," +
            "        \"name\": \"Custom View\"," +
            "        \"groupBy\":false," +
            "        \"columns\": [" +
            "            {" +
            "                \"name\": \"cid\"," +
            "                \"label\": \"CID\"" +
            "            }," +
            "            {" +
            "                \"name\": \"checker\"," +
            "                \"label\": \"Checker\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayFunction\"," +
            "                \"label\": \"Function\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayFile\"," +
            "                \"label\": \"File\"" +
            "            }" +
            "        ]" +
            "    }" +
            "]}";
        TestableViewsService.setupWithViewApi(viewApiJsonResult);
        final CoverityViewResultsDescriptor descriptor = new CoverityViewResultsDescriptor();

        FormValidation result = descriptor.doCheckViews(cimInstance.getName(), "pipeline-project0", "54321");

        assertEquals(Kind.OK, result.kind);
        assertEquals("Successfully configured", result.getMessage());
    }
}
