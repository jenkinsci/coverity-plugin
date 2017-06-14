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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.StreamDataObj;

import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.WebServiceFactory;


@RunWith(PowerMockRunner.class)
@PrepareForTest(WebServiceFactory.class)
public class CIMInstanceTest {
    private TestWebServiceFactory testWsFactory;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() throws IOException {
        // setup web service factory
        testWsFactory = new TestWebServiceFactory();
        PowerMockito.mockStatic(WebServiceFactory.class);
        when(WebServiceFactory.getInstance()).thenReturn(testWsFactory);
    }

    @Test
    public void getProjectKey_forExistingProject() throws IOException, CovRemoteServiceException_Exception {

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        Long result = cimInstance.getProjectKey("project1");

        assertEquals(Long.valueOf((long)1), result);
    }

    @Test
    public void getProjectKey_forUnknownProject() throws IOException, CovRemoteServiceException_Exception {

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        Long result = cimInstance.getProjectKey("unknown-project");

        assertNull(result);
    }

    @Test
    public void getStream_returnsMatchingStream() throws IOException, CovRemoteServiceException_Exception {
        final String streamId = "stream0";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        StreamDataObj result = cimInstance.getStream(streamId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(streamId, result.getId().getName());
    }

    @Test
    public void getStreams_throwsWithNoStreams() throws IOException, CovRemoteServiceException_Exception {
        final String streamId = "stream1";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        exception.expect(IOException.class);
        exception.expectMessage("An error occurred while retrieving streams for the given project. Could not find stream: " + streamId);
        cimInstance.getStream(streamId);
    }

    @Test
    public void doCheck_invalidWSResponseCode() {
        testWsFactory.setWSResponseCode(401);
        final String expectedErrorMessage = "Coverity web services were not detected. Connection attempt responded with 401, check Coverity Connect version (minimum supported version is " +
            CoverityVersion.MINIMUM_SUPPORTED_VERSION.toString() + ").";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.ERROR, result.kind);
        assertEquals(expectedErrorMessage, result.getMessage());
    }

    @Test
    public void doCheck_superUser() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupUser("cim-user", true, new HashMap<String, String[]>());

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_missingCommitPermission() throws IOException {
        final String expectedErrorMessage ="\"cim-user\" does not have following permission(s): \"Commit to a stream\" ";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("someRole", new String[]{"invokeWS", "viewDefects"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.ERROR, result.kind);
        assertEquals(expectedErrorMessage, StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    public void doCheck_missingViewIssuesPermission() throws IOException {
        final String expectedErrorMessage ="\"cim-user\" does not have following permission(s): \"View issues\" ";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("someRole", new String[]{"invokeWS", "commitToStream"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.ERROR, result.kind);
        assertEquals(expectedErrorMessage, StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    public void doCheck_missingInvokeWebServicesPermission() throws IOException, CovRemoteServiceException_Exception {
        final String expectedErrorMessage ="\"cim-user\" does not have following permission(s): \"Access web services\"";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("someRole", new String[]{});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.ERROR, result.kind);
        assertEquals(expectedErrorMessage, StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    public void doCheck_allRoleAssignments() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("someRole", new String[]{"invokeWS", "commitToStream", "viewDefects"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_builtInRoleServerAdmin() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("serverAdmin", new String[]{"invokeWS"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_builtInRoleProjectOwner() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("projectOwner", new String[]{"invokeWS"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_builtInRoleStreamOwner() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "cim-user", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("streamOwner", new String[]{"invokeWS"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }
}