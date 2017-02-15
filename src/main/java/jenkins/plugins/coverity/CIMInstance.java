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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
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
import com.coverity.ws.v9.MergedDefectDataObj;
import com.coverity.ws.v9.MergedDefectFilterSpecDataObj;
import com.coverity.ws.v9.MergedDefectsPageDataObj;
import com.coverity.ws.v9.PageSpecDataObj;
import com.coverity.ws.v9.PermissionDataObj;
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.ProjectFilterSpecDataObj;
import com.coverity.ws.v9.RoleAssignmentDataObj;
import com.coverity.ws.v9.RoleDataObj;
import com.coverity.ws.v9.SnapshotScopeSpecDataObj;
import com.coverity.ws.v9.StreamDataObj;
import com.coverity.ws.v9.StreamFilterSpecDataObj;
import com.coverity.ws.v9.StreamIdDataObj;
import com.coverity.ws.v9.UserDataObj;
import com.google.common.collect.ImmutableList;

import hudson.util.FormValidation;
import jenkins.plugins.coverity.ws.WebServiceFactory;

/**
 * Represents one Coverity Integrity Manager server. Abstracts functions like getting streams and defects.
 */
public class CIMInstance {
    private static final Logger logger = Logger.getLogger(CIMStream.class.getName());

    /**
     * Pattern to ignore streams - this is used to filter out internal DA streams, which are irrelevant to this plugin
     */
    public static final String STREAM_NAME_IGNORE_PATTERN = "__internal_.*";

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

    public List<StreamDataObj> getStaticStreams(String projectId) throws IOException, CovRemoteServiceException_Exception {
        /*
            to get a list of all static streams for one project,
            - get a list of streams for the project
            - get a list of all static streams
            - compute the intersection
            there should be a better way, but I can't see it.

            you can't filter on project, and StreamDataObj does not have project or type information
         */

        ProjectDataObj project = getProject(projectId);
        List<StreamDataObj> result = new ArrayList<StreamDataObj>();
        if (project != null){
            for(StreamDataObj stream : project.getStreams()) {
                if(!stream.getId().getName().matches(STREAM_NAME_IGNORE_PATTERN)) {
                    result.add(stream);
                }
            }
        }
        return result;
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

    public FormValidation doCheck() throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("\"" + user + "\" does not have following permission(s): ");

        try {
            URL url = WebServiceFactory.getInstance().getURL(this);
            int responseCode = getURLResponseCode(new URL(url, WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL));
            if(responseCode != 200) {
                return FormValidation.error("Coverity web services were not detected. Connection attempt responded with " +
                    responseCode + ", check Coverity Connect version (minimum supported version is " +
                    CoverityVersion.MINIMUM_SUPPORTED_VERSION.getEffectiveVersion().getEffectiveVersion() + ").");
            }

            List<String> missingPermission = new ArrayList<String>();;
            if (!checkUserPermission(missingPermission) && !missingPermission.isEmpty()){
                for (String permission : missingPermission){
                    errorMessage.append("\"" + permission + "\" ");
                }
                return FormValidation.error(errorMessage.toString());
            }

            return FormValidation.ok("Successfully connected to the instance.");
        } catch(UnknownHostException e) {
            return FormValidation.error("Host name unknown");
        } catch(ConnectException e) {
            return FormValidation.error("Connection refused");
        } catch(SocketException e) {
            return FormValidation.error("Error connecting to CIM. Please check your connection settings.");
        } catch(SOAPFaultException e){
            if (StringUtils.isNotEmpty(e.getMessage())){
                if (e.getMessage().contains("User " + user + " Doesn't have permissions to perform {invokeWS}")) {
                    return FormValidation.error(errorMessage.append("\"Access web services\"").toString());
                }
                return FormValidation.error(e.getMessage());
            }
            return FormValidation.error(e, "An unexpected error occurred.");
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

    private int getURLResponseCode(URL url) throws IOException {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
            return conn.getResponseCode();
        } catch(FileNotFoundException e) {
            return 404;
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
    private boolean checkUserPermission(List<String> missingPermissions) throws IOException, com.coverity.ws.v9.CovRemoteServiceException_Exception{

        boolean canCommit = false;
        boolean canViewIssues = false;

        UserDataObj userData = getConfigurationService().getUser(user);
        if (userData != null){

            if (userData.isSuperUser()){
                return true;
            }

            for (RoleAssignmentDataObj role : userData.getRoleAssignments()){
                RoleDataObj roleData = getConfigurationService().getRole(role.getRoleId());
                if (roleData != null){
                    for (PermissionDataObj permission : roleData.getPermissionDataObjs()){
                        if (permission.getPermissionValue().equalsIgnoreCase("commitToStream")){
                            canCommit = true;
                        } else if (permission.getPermissionValue().equalsIgnoreCase("viewDefects")){
                            canViewIssues = true;
                        }
                        if (canCommit && canViewIssues){
                            return true;
                        }
                    }
                }
            }
        }

        if (!canCommit){
            missingPermissions.add("Commit to a stream");
        }
        if (!canViewIssues){
            missingPermissions.add("View issues");
        }

        return false;
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
