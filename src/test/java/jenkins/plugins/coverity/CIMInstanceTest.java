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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import hudson.util.Secret;
import jenkins.plugins.coverity.Utils.CIMInstanceBuilder;
import jenkins.plugins.coverity.Utils.CredentialUtil;
import jenkins.plugins.coverity.ws.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.StreamDataObj;

import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import jenkins.plugins.coverity.Utils.TestableConsoleLogger;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;


@RunWith(PowerMockRunner.class)
@PrepareForTest({WebServiceFactory.class, Client.class, ClientBuilder.class, SSLContext.class, Secret.class, CredentialsMatchers.class, CredentialsProvider.class})
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*" , "javax.crypto.*"})
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
        CredentialUtil.setCredentialManager("admin", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                                    .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        Long result = cimInstance.getProjectKey("project1");

        assertEquals(Long.valueOf((long)1), result);
    }

    @Test
    public void getProjectKey_forUnknownProject() throws IOException, CovRemoteServiceException_Exception {
        CredentialUtil.setCredentialManager("admin", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        Long result = cimInstance.getProjectKey("unknown-project");

        assertNull(result);
    }

    @Test
    public void getStream_returnsMatchingStream() throws IOException, CovRemoteServiceException_Exception {
        final String streamId = "stream0";
        CredentialUtil.setCredentialManager("admin", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("admin", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        exception.expect(IOException.class);
        exception.expectMessage("An error occurred while retrieving streams for the given project. Could not find stream: " + streamId);
        cimInstance.getStream(streamId);
    }

    @Test
    public void doCheck_invalidWSResponseCode() {
        testWsFactory.setWSResponseCode(401, "failed response message");
        final String expectedErrorMessage = "Connection check failed." + System.lineSeparator() +
            "Check Coverity Web Service Response: { Code=401, Message=\"failed response message\", URL=}" + System.lineSeparator() +
            "(check that the values entered for this instance are correct and ensure the Coverity Connect version is at least " +
            CoverityVersion.MINIMUM_SUPPORTED_VERSION.toString() + ")";
        CredentialUtil.setCredentialManager("admin", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.ERROR, result.kind);
        assertEquals(expectedErrorMessage, StringEscapeUtils.unescapeHtml(result.getMessage()).replace("<br>", "\n"));
    }

    @Test
    public void doCheck_superUser() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CredentialUtil.setCredentialManager("admin", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupUser("cim-user", true, new HashMap<String, String[]>());

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_missingCommitPermission() throws IOException {
        final String expectedErrorMessage ="\"cim-user\" does not have following permission(s): \"Commit to a stream\" ";
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

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
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("streamOwner", new String[]{"invokeWS"});
        testConfigurationService.setupUser("cim-user", false, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_allGroupRoleAssignments() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("groupRole", new String[]{"invokeWS", "commitToStream", "viewDefects"});
        Map<String, String[]> groupRoles = new HashMap<>();
        groupRoles.put("userGroup", new String[]{"groupRole"});
        testConfigurationService.setupUser(cimInstance.getCoverityUser(), groupRoles, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_groupBuiltInRoleServerAdmin() throws IOException {
        final String expectedSuccessMessage = "Successfully connected to the instance.";
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("serverAdmin", new String[]{"invokeWS"});
        Map<String, String[]> groupRoles = new HashMap<>();
        groupRoles.put("userGroup", new String[]{"serverAdmin"});
        testConfigurationService.setupUser(cimInstance.getCoverityUser(), groupRoles, rolePermissions);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.OK, result.kind);
        assertEquals(expectedSuccessMessage, result.getMessage());
    }

    @Test
    public void doCheck_nonGlobalRoleAssignments() throws IOException {
        final String expectedWarningMessage ="\"cim-user\" does not have following global permission(s): \"Commit to a stream\" \"View issues\" ";
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("someRole", new String[]{"invokeWS", "commitToStream", "viewDefects"});
        testConfigurationService.setupUser(cimInstance.getCoverityUser(), false, rolePermissions, false);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.WARNING, result.kind);
        assertEquals(expectedWarningMessage, StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    public void doCheck_nonGlobalBuiltInRole() throws IOException {
        final String expectedWarningMessage ="\"cim-user\" does not have following global permission(s): \"Commit to a stream\" \"View issues\" ";
        CredentialUtil.setCredentialManager("cim-user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        Map<String, String[]> rolePermissions = new HashMap<>();
        rolePermissions.put("serverAdmin", new String[]{"invokeWS"});
        Map<String, String[]> groupRoles = new HashMap<>();
        groupRoles.put("Administrator", new String[]{"serverAdmin"});
        testConfigurationService.setupUser(cimInstance.getCoverityUser(), groupRoles, rolePermissions, false);

        FormValidation result = cimInstance.doCheck();

        assertEquals(Kind.WARNING, result.kind);
        assertEquals(expectedWarningMessage, StringEscapeUtils.unescapeHtml(result.getMessage()));
    }

    @Test
    public void getViews_returnsAvailableIssuesViews() throws MalformedURLException, NoSuchAlgorithmException {
        final String viewApiJsonResult = "{\"views\": [" +
            "    {" +
            "        \"id\": 12345," +
            "        \"type\": \"files\"," +
            "        \"name\": \"All Files\"," +
            "        \"groupBy\":false,        " +
            "        \"columns\": [" +
            "            {" +
            "                \"name\": \"cid\"," +
            "                \"label\": \"CID\"" +
            "            }," +
            "            {" +
            "                \"name\": \"displayType\"," +
            "                \"label\": \"Type\"" +
            "            }" +
            "        ]" +
            "    }," +
            "    {" +
            "        \"id\": 54321," +
            "        \"type\": \"issuesByProject\"," +
            "        \"name\": \"All In Project\"," +
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
        CredentialUtil.setCredentialManager("user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("instance").withHost("host").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        final Map<Long, String> result = cimInstance.getViews();

        assertEquals(1, result.size());
        assertTrue(result.containsKey(Long.valueOf(67890)));
        assertEquals("All Defects", result.get(Long.valueOf(67890)));
    }

    @Test
    public void getViews_handlesExceptions() throws NoSuchAlgorithmException {
        PowerMockito.mockStatic(SSLContext.class);
        when(SSLContext.getInstance("SSL")).thenThrow(new NoSuchAlgorithmException());
        CredentialUtil.setCredentialManager("user", "password");

        CIMInstance cimInstance = new CIMInstanceBuilder().withName("instance").withHost("host").withPort(8080)
                .withUseSSL(true).withDefaultCredentialId().build();

        final Map<Long, String> result = cimInstance.getViews();

        assertEquals(0, result.size());
    }

    @Test
    public void getIssuesForView_returnIssues() throws Exception {
        final TestableConsoleLogger testableConsoleLogger = new TestableConsoleLogger();
        final String viewContentsApiJsonResult = "{\"viewContentsV1\": {" +
            "    \"offset\": 0," +
            "    \"totalRows\": 2," +
            "    \"columns\": [" +
            "        {" +
            "            \"name\": \"cid\"," +
            "            \"label\": \"CID\"" +
            "        }," +
            "        {" +
            "            \"name\": \"displayType\"," +
            "            \"label\": \"Type\"" +
            "        }," +
            "        {" +
            "            \"name\": \"displayImpact\"," +
            "            \"label\": \"Impact\"" +
            "        }," +
            "        {" +
            "            \"name\": \"status\"," +
            "            \"label\": \"Status\"" +
            "        }," +
            "        {" +
            "            \"name\": \"checker\"," +
            "            \"label\": \"Checker\"" +
            "        }," +
            "        {" +
            "            \"name\": \"owner\"," +
            "            \"label\": \"Owner\"" +
            "        }," +
            "        {" +
            "            \"name\": \"classification\"," +
            "            \"label\": \"Classification\"" +
            "        }," +
            "        {" +
            "            \"name\": \"displayFile\"," +
            "            \"label\": \"File\"" +
            "        }" +
            "        {" +
            "            \"name\": \"displayFunction\"," +
            "            \"label\": \"Function\"" +
            "        }" +
            "    ]," +
            "    \"rows\": [" +
            "        {" +
            "            \"cid\": 12345," +
            "            \"displayType\": \"Insufficient function coverage\"," +
            "            \"displayImpact\": \"Low\"," +
            "            \"status\": \"New\"," +
            "            \"checker\": \"TA.FUNCTION_INSUFFICIENTLY_TESTED\"," +
            "            \"owner\": \"Unassigned\"," +
            "            \"classification\": \"Unclassified\"," +
            "            \"displayFile\": \"source.cpp\"" +
            "            \"displayFunction\": \"main\"" +
            "        }," +
            "        {" +
            "            \"cid\": 12345," +
            "            \"displayType\": \"Insufficient function coverage\"," +
            "            \"displayImpact\": \"Low\"," +
            "            \"status\": \"New\"," +
            "            \"checker\": \"TA.FUNCTION_INSUFFICIENTLY_TESTED\"," +
            "            \"owner\": \"Unassigned\"," +
            "            \"classification\": \"Unclassified\"," +
            "            \"displayFile\": \"source.cpp\"" +
            "            \"displayFunction\": \"main\"" +
            "        }," +
            "        {" +
            "            \"cid\": 54321," +
            "            \"displayType\": \"Insufficient function coverage\"," +
            "            \"displayImpact\": \"Low\"," +
            "            \"status\": \"New\"," +
            "            \"checker\": \"TA.FUNCTION_INSUFFICIENTLY_TESTED\"," +
            "            \"owner\": \"Unassigned\"," +
            "            \"classification\": \"Unclassified\"," +
            "            \"displayFile\": \"sourceTest.cpp\"" +
            "            \"displayFunction\": \"test\"" +
            "        }" +
            "    ]" +
            "}}";
        TestableViewsService.setupViewContentsApi("view0", 200, viewContentsApiJsonResult);
        CredentialUtil.setCredentialManager("user", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("instance").withHost("host").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        final List<CoverityDefect> issuesVorView = cimInstance.getIssuesVorView("project0", "view0", testableConsoleLogger.getPrintStream());

        assertEquals(2, issuesVorView.size());
        assertEquals(12345L, (long)issuesVorView.get(0).getCid());
        assertEquals("TA.FUNCTION_INSUFFICIENTLY_TESTED", issuesVorView.get(0).getCheckerName());
        assertEquals("source.cpp", issuesVorView.get(0).getFilePathname());
        assertEquals("main", issuesVorView.get(0).getFunctionDisplayName());

        assertEquals(54321L, (long)issuesVorView.get(1).getCid());
        assertEquals("TA.FUNCTION_INSUFFICIENTLY_TESTED", issuesVorView.get(1).getCheckerName());
        assertEquals("sourceTest.cpp", issuesVorView.get(1).getFilePathname());
        assertEquals("test", issuesVorView.get(1).getFunctionDisplayName());
    }

    @Test
    public void getIssuesForView_logsMissingColumns() throws Exception {
        final TestableConsoleLogger testableConsoleLogger = new TestableConsoleLogger();
        final String viewContentsApiJsonResult = "{\"viewContentsV1\": {" +
            "    \"offset\": 0," +
            "    \"totalRows\": 1," +
            "    \"columns\": [" +
            "        {" +
            "            \"name\": \"displayType\"," +
            "            \"label\": \"Type\"" +
            "        }," +
            "    ]," +
            "    \"rows\": [" +
            "        {" +
            "            \"displayType\": \"Insufficient function coverage\"," +
            "        }," +
            "    ]" +
            "}}";
        TestableViewsService.setupViewContentsApi("view0", 200, viewContentsApiJsonResult);
        CredentialUtil.setCredentialManager("user", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("instance").withHost("host").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        final List<CoverityDefect> issuesVorView = cimInstance.getIssuesVorView("project0", "view0", testableConsoleLogger.getPrintStream());

        assertEquals(1, issuesVorView.size());
        assertNull(issuesVorView.get(0).getCid());
        assertNull(issuesVorView.get(0).getCheckerName());
        assertNull(issuesVorView.get(0).getFilePathname());
        assertNull(issuesVorView.get(0).getFunctionDisplayName());
        testableConsoleLogger.verifyMessages("[Coverity] Retrieving issues for project \"project0\" and view \"view0\"",
            "[Coverity] Warning: Issues view \"view0\" is missing column \"cid\"",
            "[Coverity] Warning: Issues view \"view0\" is missing column \"checker\"",
            "[Coverity] Warning: Issues view \"view0\" is missing column \"displayFile\"",
            "[Coverity] Warning: Issues view \"view0\" is missing column \"displayFunction\"",
            "[Coverity] Found 1 issues for project \"project0\" and view \"view0\"");
    }

    @Test
    public void getIssuesForView_handlesPagingAndLogsProgress() throws Exception {
        final TestableConsoleLogger testableConsoleLogger = new TestableConsoleLogger();

        final String viewContentsApiJsonResultHeader = "{\"viewContentsV1\": {" +
                "    \"offset\": 0," +
                "    \"totalRows\": 3000," +
                "    \"columns\": [" +
                "        {" +
                "            \"name\": \"cid\"," +
                "            \"label\": \"CID\"" +
                "        }," +
                "        {" +
                "            \"name\": \"checker\"," +
                "            \"label\": \"Checker\"" +
                "        }," +
                "        {" +
                "            \"name\": \"displayFile\"," +
                "            \"label\": \"File\"" +
                "        }" +
                "        {" +
                "            \"name\": \"displayFunction\"," +
                "            \"label\": \"Function\"" +
                "        }" +
                "    ]," +
                "    \"rows\": [";

        ViewsService mockViewsService = mock(ViewsService.class);
        Answer<ViewContents> contents = new Answer<ViewContents>() {
            public ViewContents answer(InvocationOnMock mock) throws Throwable {
                int pageSize = (int)mock.getArguments()[2];
                int offSet = (int)mock.getArguments()[3];

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(viewContentsApiJsonResultHeader.concat(constructViewContentsResult(pageSize, offSet)));

                return new ViewContents((JSONObject)json.get("viewContentsV1"));
            }
        };

        when(mockViewsService.getViewContents(Matchers.anyString(), Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt()))
                .thenAnswer(contents);
        testWsFactory.setMockViewsService(mockViewsService);

        CredentialUtil.setCredentialManager("user", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("instance").withHost("host").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        final List<CoverityDefect> issuesVorView = cimInstance.getIssuesVorView("project0", "view0", testableConsoleLogger.getPrintStream());

        assertEquals(3000, issuesVorView.size());
        testableConsoleLogger.verifyMessages("[Coverity] Retrieving issues for project \"project0\" and view \"view0\"",
            "[Coverity] Retrieving issues for project \"project0\" and view \"view0\" (fetched 1,000 of 3,000)",
            "[Coverity] Retrieving issues for project \"project0\" and view \"view0\" (fetched 2,000 of 3,000)",
            "[Coverity] Found 3,000 issues for project \"project0\" and view \"view0\"");
    }

    private String constructViewContentsResult(int pageSize, int offSet) {
        final StringBuilder viewContentsApiJsonResult = new StringBuilder();

        for (int i = offSet; i < offSet + pageSize; i++) {
            viewContentsApiJsonResult.append(
                    "        {" +
                            "            \"cid\": " + i + "," +
                            "            \"checker\": \"FORWARD_NULL\"," +
                            "            \"displayFile\": \"source.cpp\"" +
                            "            \"displayFunction\": \"test" + i + "\"" +
                            "        },");
        }

        viewContentsApiJsonResult.append(
                "    ]" +
                        "}}");

        return viewContentsApiJsonResult.toString();
    }
}