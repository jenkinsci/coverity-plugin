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
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import com.coverity.ws.v9.ConfigurationService;
import com.coverity.ws.v9.ConfigurationServiceService;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.GroupIdDataObj;
import com.coverity.ws.v9.GroupDataObj;
import com.coverity.ws.v9.PermissionDataObj;
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.ProjectFilterSpecDataObj;
import com.coverity.ws.v9.RoleAssignmentDataObj;
import com.coverity.ws.v9.RoleDataObj;
import com.coverity.ws.v9.StreamDataObj;
import com.coverity.ws.v9.StreamFilterSpecDataObj;
import com.coverity.ws.v9.UserDataObj;
import com.google.common.collect.ImmutableList;

import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import jenkins.plugins.coverity.ws.WebServiceFactory;

/**
 * Represents one Coverity Integrity Manager server. Abstracts functions like getting streams and defects.
 */
public class CIMInstance {
    private static final Logger logger = Logger.getLogger(CIMStream.class.getName());
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
     * The commit port for the CIM server. This is only for cov-commit-defects.
     */
    private final int dataPort;

    /**
     * Username for connecting to the CIM server
     */
    private final String user;

    /**
     * Password for connecting to the CIM server
     */
    private final String password;

    /**
     * Use SSL
     */
    private final boolean useSSL;

    /**
     * cached webservice port for Configuration service
     */
    private transient ConfigurationServiceService configurationServiceService;

    /**
     * Cached result of user permissions check for this instance. This value is not saved and only updated as a result of
     * checking user permissions.
     * This is a part of fixing the issue in BZ 105657 (JENKINS-44724)
     */
    private transient FormValidation userPermissionsCheck;

    @DataBoundConstructor
    public CIMInstance(String name, String host, int port, String user, String password, boolean useSSL, int dataPort) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.useSSL = useSSL;
        this.dataPort = dataPort;
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

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public int getDataPort() {
        return dataPort;
    }

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
            int responseCode = WebServiceFactory.getInstance().getWSResponseCode(this);
            if(responseCode != 200) {
                return FormValidation.error("Coverity web services were not detected. Connection attempt responded with " +
                    responseCode + ", check Coverity Connect version (minimum supported version is " +
                    CoverityVersion.MINIMUM_SUPPORTED_VERSION.toString() + ").");
            }

            FormValidation userPermissionsValidation = checkUserPermissions();

            if (!userPermissionsValidation.kind.equals(Kind.OK))
                return userPermissionsValidation;

            return FormValidation.ok("Successfully connected to the instance.");
        } catch(UnknownHostException e) {
            return FormValidation.error("Host name unknown");
        } catch(ConnectException e) {
            return FormValidation.error("Connection refused");
        } catch(SocketException e) {
            return FormValidation.error("Error connecting to CIM. Please check your connection settings.");
        } catch (Throwable e) {
            String javaVersion = System.getProperty("java.version");
            if(javaVersion.startsWith("1.6.0_")) {
                int patch = Integer.parseInt(javaVersion.substring(javaVersion.indexOf('_') + 1));
                if(patch < 26) {
                    return FormValidation.error(e, "Please use Java 1.6.0_26 or later to run Jenkins.");
                }
            }
            return FormValidation.error(e, "An unexpected error occurred.");
        }
    }

    public ImmutableList<String> getCimInstanceCheckers() throws IOException, CovRemoteServiceException_Exception {
        final List<String> checkerNames = this.getConfigurationService().getCheckerNames();
        Collections.sort(checkerNames);

        return ImmutableList.copyOf(checkerNames);
    }

    /**
     * A user requires 3 sets of permissions in order to use Coverity plugin.
     * The required permissions are "WebService Access", "Commit To a Stream", and "View Issues".
     * This method check whether the configured user have all the required permissions.
     * Returns true if the user have all the permissions, otherwise, return false with
     * a list of missing permissions.
     */
    private FormValidation checkUserPermissions() throws IOException, CovRemoteServiceException_Exception {
        if (userPermissionsCheck != null)
            return userPermissionsCheck;

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("\"" + user + "\" does not have following permission(s): ");

        try {

            boolean canCommit = false, canViewIssues = false, canCommitGlobal = false, canViewIssuesGlobal = false;

            UserDataObj userData = getConfigurationService().getUser(user);
            if (userData != null){

                if (userData.isSuperUser()){
                    userPermissionsCheck = FormValidation.ok();
                    return userPermissionsCheck;
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
                warningMessage.append("\"" + user + "\" does not have following global permission(s): ");
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
                if (e.getMessage().contains("User " + user + " Doesn't have permissions to perform {invokeWS}")) {
                    return FormValidation.error(errorMessage.append("\"Access web services\"").toString());
                }
                return FormValidation.error(e.getMessage());
            }
            return FormValidation.error(e, "An unexpected error occurred.");
        }

        userPermissionsCheck = FormValidation.ok();
        return FormValidation.ok();
    }


    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + dataPort;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
