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

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.Domain;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.util.Secret;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.coverity.ws.v9.ConfigurationService;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.GroupDataObj;
import com.coverity.ws.v9.GroupIdDataObj;
import com.coverity.ws.v9.PermissionDataObj;
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.ProjectFilterSpecDataObj;
import com.coverity.ws.v9.RoleAssignmentDataObj;
import com.coverity.ws.v9.RoleDataObj;
import com.coverity.ws.v9.StreamDataObj;
import com.coverity.ws.v9.StreamFilterSpecDataObj;
import com.coverity.ws.v9.UserDataObj;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.ws.ViewContents;
import jenkins.plugins.coverity.ws.ViewsService;
import jenkins.plugins.coverity.ws.WebServiceFactory;
import jenkins.plugins.coverity.ws.WebServiceFactory.CheckWsResponse;

/**
 * Represents one Coverity Integrity Manager server. Abstracts functions like getting streams and defects.
 */
public class CIMInstance {

    // deprecated fields which were removed in plugin version 1.11
    private transient String user;
    private transient String password;

    private static final Logger logger = Logger.getLogger(CIMStream.class.getName());
    private static final String migratedCredentialId = "Migrated-Coverity-Credential";
    /**
     * The id for this instance, used as a key in CoverityPublisher
     */
    private final String name;

    /**
     * The host name for the CIM server
     */
    private final String host;

    /**
     * The port for the CIM server (this is the HTTP port and not the data port)
     */
    private final int port;

    /**
     * Use SSL
     */
    private boolean useSSL;

    /**
     * Credential ID from configured Credentials to use
     */
    private String credentialId;

    /**
     * Cached last username that was successfully check for this instance. This value is not saved and only updated as a result of
     * checking user permissions.
     * This is a part of fixing the issue in BZ 105657 (JENKINS-44724)
     */
    private transient String lastSuccessfulUser;

    @DataBoundConstructor
    public CIMInstance(String name, String host, int port, String credentialId) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.credentialId = credentialId;
    }

    protected Object readResolve() {
        createCredentials(user, password);

        // Setting the migrated credential to use
        if (StringUtils.isEmpty(credentialId)) {
            credentialId = migratedCredentialId;
        }

        return this;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    @DataBoundSetter
    public void setUseSSL(boolean useSSL){
        this.useSSL = useSSL;
    }

    public String getCredentialId() { return credentialId; }

    /**
     * Returns a Defect service client using v9 web services.
     */
    public DefectService getDefectService() throws IOException {
        return WebServiceFactory.getInstance().getDefectService(this);
    }

    /**
     * Returns a Configuration service client using v9 web services.
     */
    public ConfigurationService getConfigurationService() throws IOException {
        return WebServiceFactory.getInstance().getConfigurationService(this);
    }

    public ProjectDataObj getProject(String projectId) throws IOException, CovRemoteServiceException_Exception {
        List<ProjectDataObj> projects = new ArrayList<>();
        try {
            ProjectFilterSpecDataObj filterSpec = new ProjectFilterSpecDataObj();
            filterSpec.setNamePattern(projectId);
            projects = getConfigurationService().getProjects(filterSpec);
        }
        catch (Exception e)
        {
            logger.warning("Error getting project " + projectId + " from instance " + name + " (" + host + ":" + port + ")");
        }

        if(projects.size() == 0) {
            return null;
        } else {
            return projects.get(0);
        }
    }

    private transient Map<String, Long> projectKeys;

    public Long getProjectKey(String projectId) throws IOException, CovRemoteServiceException_Exception {
        if(projectKeys == null) {
            projectKeys = new ConcurrentHashMap<String, Long>();
        }

        Long result = projectKeys.get(projectId);
        if(result == null) {
            ProjectDataObj project = getProject(projectId);
            if (project != null) {
                result = project.getProjectKey();
                projectKeys.put(projectId, result);
            }
        }
        return result;
    }

    public List<ProjectDataObj> getProjects() throws IOException, CovRemoteServiceException_Exception {
        try {
            return getConfigurationService().getProjects(new ProjectFilterSpecDataObj());
        }
        catch (Exception e)
        {
            logger.warning("Error getting projects from instance " + name + " (" + host + ":" + port + ")");
        }

        return new ArrayList<>();
    }

    /**
     * Returns a StreamDataObj for a given streamId. This object must be not null in order to avoid null pointer exceptions.
     * If the stream is not found an exception explaining the issue is raised.
     */
    public StreamDataObj getStream(String streamId) throws IOException, CovRemoteServiceException_Exception {
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.setNamePattern(streamId);

        List<StreamDataObj> streams = getConfigurationService().getStreams(filter);
        if(streams == null || streams.isEmpty()) {
            throw new IOException("An error occurred while retrieving streams for the given project. Could not find stream: " + streamId);
        } else {
            StreamDataObj streamDataObj = streams.get(0);
            if(streamDataObj == null){
                throw new IOException("An error occurred while retrieving streams for the given project. Could not find stream: " + streamId);
            } else {
                return streams.get(0);
            }
        }
    }

    public FormValidation doCheck() {
        try {
            CheckWsResponse responseCode = WebServiceFactory.getInstance().getCheckWsResponse(this);
            if(responseCode.getResponseCode() != 200) {
                return FormValidation.error("Connection check failed." + System.lineSeparator() +
                    responseCode.toString() + System.lineSeparator() +
                    "(check that the values entered for this instance are correct and ensure the Coverity Connect version is at least " +
                    CoverityVersion.MINIMUM_SUPPORTED_VERSION.toString() + ")");
            }

            FormValidation userPermissionsValidation = checkUserPermissions();

            if (!userPermissionsValidation.kind.equals(Kind.OK))
                return userPermissionsValidation;

            return FormValidation.ok("Successfully connected to the instance.");
        } catch (WebServiceException e) {
            if (StringUtils.containsIgnoreCase(e.getMessage(), "Unauthorized")) {
                return FormValidation.error("User authentication failed." + System.lineSeparator() +
                    e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            return FormValidation.error(e,
                "Web service error occurred. " + System.lineSeparator() +
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (Throwable e) {
            String javaVersion = System.getProperty("java.version");
            if(javaVersion.startsWith("1.6.0_")) {
                int patch = Integer.parseInt(javaVersion.substring(javaVersion.indexOf('_') + 1));
                if(patch < 26) {
                    return FormValidation.error(e, "Please use Java 1.6.0_26 or later to run Jenkins.");
                }
            }
            return FormValidation.error(e,
                "An unexpected error occurred. " +  System.lineSeparator() +
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public ImmutableList<String> getCimInstanceCheckers() throws IOException, CovRemoteServiceException_Exception {
        final List<String> checkerNames = this.getConfigurationService().getCheckerNames();
        Collections.sort(checkerNames);

        return ImmutableList.copyOf(checkerNames);
    }

    /**
     * Returns a Map of available Coverity connect views for this instance, using the numeric identifier as the key
     * and name as value
     */
    public ImmutableSortedMap<Long, String> getViews() {
        Map<? extends Long, ? extends String> views;
        try {
            views = WebServiceFactory.getInstance().getViewService(this).getViews();
        } catch (MalformedURLException | NoSuchAlgorithmException e) {
            return ImmutableSortedMap.of();
        }
        return ImmutableSortedMap.copyOf(views);
    }

    public List<CoverityDefect> getIssuesVorView(String projectId, String connectView, PrintStream outputLogger) throws Exception {
        final ArrayList<CoverityDefect> coverityDefects = new ArrayList<>();

        try {
            final ViewsService viewService = WebServiceFactory.getInstance().getViewService(this);
            int pageSize, defectSize;
            pageSize = defectSize = 1000; // Size of page to be pulled
            for(int pageStart = 0; pageStart < defectSize; pageStart += pageSize){
                if (pageStart >= pageSize) {
                    outputLogger.println(MessageFormat.format("[Coverity] Retrieving issues for project \"{0}\" and view \"{1}\" (fetched {2} of {3})", projectId, connectView, pageStart, defectSize));
                } else {
                    outputLogger.println(MessageFormat.format("[Coverity] Retrieving issues for project \"{0}\" and view \"{1}\"", projectId, connectView));
                }

                final ViewContents viewContents = viewService.getViewContents(projectId, connectView, pageSize, pageStart);
                if (!viewContents.getColumns().contains("cid")) {
                    outputLogger.println(MessageFormat.format("[Coverity] Warning: Issues view \"{0}\" is missing column \"cid\"", connectView));
                }

                if (!viewContents.getColumns().contains("checker")) {
                    outputLogger.println(MessageFormat.format("[Coverity] Warning: Issues view \"{0}\" is missing column \"checker\"", connectView));
                }

                if (!viewContents.getColumns().contains("displayFile")) {
                    outputLogger.println(MessageFormat.format("[Coverity] Warning: Issues view \"{0}\" is missing column \"displayFile\"", connectView));
                }

                if (!viewContents.getColumns().contains("displayFunction")) {
                    outputLogger.println(MessageFormat.format("[Coverity] Warning: Issues view \"{0}\" is missing column \"displayFunction\"", connectView));
                }

                for (Map<String, Object> row : viewContents.getRows()){
                    final Long cid = row.get("cid") != null ? Long.parseLong(row.get("cid").toString()) : null;
                    final String checker = row.get("checker") != null ? row.get("checker").toString() : null;
                    final String displayFunction = row.get("displayFunction") != null ? row.get("displayFunction").toString() : null;
                    final String displayFile = row.get("displayFile") != null ? row.get("displayFile").toString() : null;
                    coverityDefects.add(new CoverityDefect(cid, checker, displayFunction, displayFile));
                }

                defectSize = viewContents.getTotalRows().intValue();
            }

            outputLogger.println(MessageFormat.format("[Coverity] Found {0} issues for project \"{1}\" and view \"{2}\"", coverityDefects.size(), projectId, connectView));

            } catch (MalformedURLException | NoSuchAlgorithmException e) {
                throw new Exception(e);
        }

        return coverityDefects;
    }

    /**
     * A user requires 3 sets of permissions in order to use Coverity plugin.
     * The required permissions are "WebService Access", "Commit To a Stream", and "View Issues".
     * This method check whether the configured user have all the required permissions.
     * Returns true if the user have all the permissions, otherwise, return false with
     * a list of missing permissions.
     */
    private FormValidation checkUserPermissions() throws IOException, CovRemoteServiceException_Exception {
        String username = getCoverityUser();

        if (StringUtils.isNotEmpty(lastSuccessfulUser)
                && lastSuccessfulUser.equalsIgnoreCase(username)){
            return FormValidation.ok();
        }

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("\"" + username + "\" does not have following permission(s): ");

        try {

            boolean canCommit = false, canViewIssues = false, canCommitGlobal = false, canViewIssuesGlobal = false;

            UserDataObj userData = getConfigurationService().getUser(username);
            if (userData != null){

                if (userData.isSuperUser()){
                    lastSuccessfulUser = username;
                    return FormValidation.ok();
                }

                // Start the queue with direct role assignments to the user
                final LinkedList<RoleAssignmentDataObj> roleAssignments = new LinkedList<>(userData.getRoleAssignments());
                final Iterator<String> groupNameIterator = userData.getGroups().iterator();
                while (!(canCommitGlobal && canViewIssuesGlobal) && (!roleAssignments.isEmpty() || groupNameIterator.hasNext())) {
                    // No more role assignments, add more from the next available group
                    if (roleAssignments.isEmpty()) {
                        final GroupIdDataObj groupId = new GroupIdDataObj();
                        groupId.setName(groupNameIterator.next());
                        final GroupDataObj group = getConfigurationService().getGroup(groupId);
                        if (group != null) {
                            roleAssignments.addAll(group.getRoleAssignments());
                        }
                    }
                    // The user still has direct role assignments or a few more were added from a group
                    if (!roleAssignments.isEmpty()) {
                        final RoleAssignmentDataObj roleAssignment = roleAssignments.removeFirst();

                        // first check for built-in roles which contain required permissions
                        if (roleAssignment.getRoleId().getName().equals("serverAdmin") ||
                            roleAssignment.getRoleId().getName().equals("streamOwner") ||
                            roleAssignment.getRoleId().getName().equals("projectOwner")) {
                            if (roleAssignment.getType().equals("global"))
                                canCommit = canCommitGlobal = canViewIssues = canViewIssuesGlobal = true;
                            else
                                canCommit = canViewIssues = true;
                            continue;
                        }

                        final RoleDataObj roleData = getConfigurationService().getRole(roleAssignment.getRoleId());

                        if (roleData != null) {
                            for (PermissionDataObj permission : roleData.getPermissionDataObjs()) {
                                if (permission.getPermissionValue().equalsIgnoreCase("commitToStream")) {
                                    if (roleAssignment.getType().equals("global"))
                                        canCommit = canCommitGlobal = true;
                                    else
                                        canCommit = true;
                                } else if (permission.getPermissionValue().equalsIgnoreCase("viewDefects")) {
                                    if (roleAssignment.getType().equals("global"))
                                        canViewIssues = canViewIssuesGlobal = true;
                                    else
                                        canViewIssues = true;
                                }
                            }
                        }
                    }
                }
            }

            if (!canCommit){
                errorMessage.append("\"Commit to a stream\" ");
            }
            if (!canViewIssues){
                errorMessage.append("\"View issues\" ");
            }

            if (!canCommit || !canViewIssues)
                return FormValidation.error(errorMessage.toString());

            // check for missing global permissions to warn users
            if (!canCommitGlobal || !canViewIssuesGlobal) {
                StringBuilder warningMessage = new StringBuilder();
                warningMessage.append("\"" + username + "\" does not have following global permission(s): ");
                if (!canCommitGlobal){
                    warningMessage.append("\"Commit to a stream\" ");
                }
                if (!canViewIssuesGlobal){
                    warningMessage.append("\"View issues\" ");
                }
                return FormValidation.warning(warningMessage.toString());
            }
        } catch(SOAPFaultException e){
            if (StringUtils.isNotEmpty(e.getMessage())){
                if (StringUtils.containsIgnoreCase(e.getMessage(), "User " + username + " Doesn't have permissions to perform {invokeWS}")) {
                    return FormValidation.error(errorMessage.append("\"Access web services\"").toString());
                }
                return FormValidation.error(e.getMessage());
            }
            return FormValidation.error(e, "An unexpected error occurred.");
        }

        lastSuccessfulUser = username;
        logger.info(username + " has all the permissions!");
        return FormValidation.ok();
    }

    public String getCoverityUser(){
        String username = retrieveCredentialInfo(true);
        return StringUtils.isNotEmpty(username) ? username : this.user;
    }

    public String getCoverityPassword(){
        String password = retrieveCredentialInfo(false);
        return StringUtils.isNotEmpty(password) ? password : this.password;
    }

    private String retrieveCredentialInfo(boolean getUsername){
        if (!StringUtils.isEmpty(credentialId)){
            StandardCredentials credentials = CredentialsMatchers.firstOrNull(
                    CredentialsProvider.lookupCredentials(
                            StandardCredentials.class, Jenkins.getInstance(), ACL.SYSTEM, Collections.<DomainRequirement>emptyList()
                    ), CredentialsMatchers.withId(credentialId)
            );

            if (credentials != null && credentials instanceof UsernamePasswordCredentials) {
                UsernamePasswordCredentials c = (UsernamePasswordCredentials)credentials;
                if (getUsername){
                    return c.getUsername();
                } else{
                    return c.getPassword().getPlainText();
                }
            }
        }

        return StringUtils.EMPTY;
    }

    private void createCredentials(String username, String password) {
        try{
            UsernamePasswordCredentialsImpl credential = new UsernamePasswordCredentialsImpl(
                    CredentialsScope.GLOBAL, name + "_" + username, "Migrated Coverity Credential", username, password);

            CredentialsStore store = CredentialsProvider.lookupStores(Jenkins.getInstance()).iterator().next();
            store.addCredentials(Domain.global(), credential);
        } catch (IOException ioe) {
            logger.warning("Migrating username and password into credentials encountered IOException"
            + "\nPlease try to resolve this issue by adding credentials manually");
        }
    }

    public CIMInstance cloneWithCredential(String credentialId) {
        CIMInstance instance = new CIMInstance(name, host, port, credentialId);
        instance.setUseSSL(useSSL);
        return instance;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (credentialId != null ? credentialId.hashCode() : 0);
        return result;
    }
}
