/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import com.coverity.ws.v6.*;
import com.coverity.ws.v9.SnapshotScopeSpecDataObj;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents one Coverity Integrity Manager server. Abstracts functions like getting streams and defects.
 */
public class CIMInstance {

    public static final String COVERITY_V5_NAMESPACE = "http://ws.coverity.com/v6";
    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";

    public static final String CONFIGURATION_SERVICE_V5_WSDL = "/ws/v6/configurationservice?wsdl";
    public static final String DEFECT_SERVICE_V5_WSDL = "/ws/v6/defectservice?wsdl";

    public static final String CONFIGURATION_SERVICE_V9_WSDL = "/ws/v9/configurationservice?wsdl";
    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";

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
     * cached webservice port for Defect service
     */
    private transient com.coverity.ws.v6.DefectServiceService defectServiceService;

    /**
     * cached webservice port for Configuration service
     */
    private transient com.coverity.ws.v6.ConfigurationServiceService configurationServiceService;

    /**
     * cached webservice port for Defect service for Indio
     */
    private transient com.coverity.ws.v9.DefectServiceService defectServiceServiceIndio;

    /**
     * cached webservice port for Configuration service for Indio
     */
    private transient com.coverity.ws.v9.ConfigurationServiceService configurationServiceServiceIndio;

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
     * The root URL for the CIM instance
     *
     * @return a url
     * @throws MalformedURLException should not happen if host is valid
     */
    public URL getURL() throws MalformedURLException {
        return new URL(isUseSSL() ? "https" : "http", host, port, "/");
    }

    /**
     * Returns a Defect service client using v6 web services.
     */
    public com.coverity.ws.v6.DefectService getDefectService() throws IOException {
        synchronized(this) {
            if(defectServiceService == null) {
                defectServiceService = new com.coverity.ws.v6.DefectServiceService(
                        new URL(getURL(), DEFECT_SERVICE_V5_WSDL),
                        new QName(COVERITY_V5_NAMESPACE, "DefectServiceService"));
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            com.coverity.ws.v6.DefectService defectService = defectServiceService.getDefectServicePort();
            attachAuthenticationHandler((BindingProvider) defectService);

            return defectService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    /**
     * Returns a Defect service client using v9 web services.
     */
    public com.coverity.ws.v9.DefectService getDefectServiceIndio() throws IOException {
        synchronized(this) {
            if(defectServiceServiceIndio == null) {
                defectServiceServiceIndio = new com.coverity.ws.v9.DefectServiceService(
                        new URL(getURL(), DEFECT_SERVICE_V9_WSDL),
                        new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            com.coverity.ws.v9.DefectService defectService = defectServiceServiceIndio.getDefectServicePort();
            attachAuthenticationHandler((BindingProvider) defectService);

            return defectService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    /**
     * Attach an authentication handler to the web service, that uses the configured user and password
     */
    private void attachAuthenticationHandler(BindingProvider service) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(user, password)));
    }

    /**
     * Returns a Configuration service client using v6 web services.
     */
    public ConfigurationService getConfigurationService() throws IOException {
        synchronized(this) {
            if(configurationServiceService == null) {
                // Create a Web Services port to the server
                configurationServiceService = new ConfigurationServiceService(
                        new URL(getURL(), CONFIGURATION_SERVICE_V5_WSDL),
                        new QName(COVERITY_V5_NAMESPACE, "ConfigurationServiceService"));
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
            attachAuthenticationHandler((BindingProvider) configurationService);

            return configurationService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    /**
     * Returns a Configuration service client using v9 web services.
     */
    public com.coverity.ws.v9.ConfigurationService getConfigurationServiceIndio() throws IOException {
        synchronized(this) {
            if(configurationServiceServiceIndio == null) {
                // Create a Web Services port to the server
                configurationServiceServiceIndio = new com.coverity.ws.v9.ConfigurationServiceService(
                        new URL(getURL(), CONFIGURATION_SERVICE_V9_WSDL),
                        new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            com.coverity.ws.v9.ConfigurationService configurationService = configurationServiceServiceIndio.getConfigurationServicePort();
            attachAuthenticationHandler((BindingProvider) configurationService);

            return configurationService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    public String getWsVersion(){
        com.coverity.ws.v9.ConfigurationService simpleWsCall = null;
        // Set the default ws version value to v6.
        String wsVersion = "v6";
        try {
            String testMessage = this.getConfigurationServiceIndio().getVersion().getExternalVersion();
            if(testMessage != null){
                wsVersion = "v9";
            }
        } catch (Exception e) {
        }
        return wsVersion;
    }

    public List<com.coverity.ws.v9.MergedDefectDataObj> getDefectsIndio(String streamId, List<Long> defectIds) throws IOException, com.coverity.ws.v9.CovRemoteServiceException_Exception {
        com.coverity.ws.v9.MergedDefectFilterSpecDataObj filterSpec = new com.coverity.ws.v9.MergedDefectFilterSpecDataObj();
        com.coverity.ws.v9.StreamIdDataObj stream = new com.coverity.ws.v9.StreamIdDataObj();
        stream.setName(streamId);
        List<com.coverity.ws.v9.StreamIdDataObj> streamList = new ArrayList<com.coverity.ws.v9.StreamIdDataObj>();
        streamList.add(stream);
        com.coverity.ws.v9.PageSpecDataObj pageSpec = new com.coverity.ws.v9.PageSpecDataObj();
        pageSpec.setPageSize(2500);

        SnapshotScopeSpecDataObj snapshotScopeSpecDataObj = new SnapshotScopeSpecDataObj();

        List<com.coverity.ws.v9.MergedDefectDataObj> result = new ArrayList<com.coverity.ws.v9.MergedDefectDataObj>();
        int defectCount = 0;
        com.coverity.ws.v9.MergedDefectsPageDataObj defects = null;
        do {
            pageSpec.setStartIndex(defectCount);
            defects = getDefectServiceIndio().getMergedDefectsForStreams(streamList, filterSpec, pageSpec, snapshotScopeSpecDataObj);
            for(com.coverity.ws.v9.MergedDefectDataObj defect : defects.getMergedDefects()) {
                if(defectIds.contains(defect.getCid())) {
                    result.add(defect);
                }
            }
            defectCount += defects.getMergedDefects().size();
        } while(defectCount < defects.getTotalNumberOfRecords());

        return result;
    }

    public List<MergedDefectDataObj> getDefects(String streamId, List<Long> defectIds) throws IOException, CovRemoteServiceException_Exception {
        MergedDefectFilterSpecDataObj filterSpec1 = new MergedDefectFilterSpecDataObj();
        StreamIdDataObj stream = new StreamIdDataObj();
        stream.setName(streamId);
        PageSpecDataObj pageSpec = new PageSpecDataObj();
        pageSpec.setPageSize(2500);

        List<MergedDefectDataObj> result = new ArrayList<MergedDefectDataObj>();
        int defectCount = 0;
        MergedDefectsPageDataObj defects = null;
        do {
            pageSpec.setStartIndex(defectCount);
            defects = getDefectService().getMergedDefectsForStreams(Arrays.asList(stream), filterSpec1, pageSpec);
            for(MergedDefectDataObj defect : defects.getMergedDefects()) {
                if(defectIds.contains(defect.getCid())) {
                    result.add(defect);
                }
            }
            defectCount += defects.getMergedDefects().size();
        } while(defectCount < defects.getTotalNumberOfRecords());

        return result;
    }

    public ProjectDataObj getProject(String projectId) throws IOException, CovRemoteServiceException_Exception {
        ProjectFilterSpecDataObj filterSpec = new ProjectFilterSpecDataObj();
        filterSpec.setNamePattern(projectId);
        List<ProjectDataObj> projects = getConfigurationService().getProjects(filterSpec);
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
            result = getProject(projectId).getProjectKey();
            projectKeys.put(projectId, result);
        }
        return result;
    }

    public List<ProjectDataObj> getProjects() throws IOException, CovRemoteServiceException_Exception {
        return getConfigurationService().getProjects(new ProjectFilterSpecDataObj());
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
        for(StreamDataObj stream : project.getStreams()) {
            if(!stream.getId().getName().matches(STREAM_NAME_IGNORE_PATTERN)) {
                result.add(stream);
            }
        }

        return result;
    }

    public StreamDataObj getStream(String streamId) throws IOException, CovRemoteServiceException_Exception {
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.setNamePattern(streamId);

        List<StreamDataObj> streams = getConfigurationService().getStreams(filter);
        if(streams.isEmpty()) {
            return null;
        } else {
            return streams.get(0);
        }
    }

    public com.coverity.ws.v9.StreamDataObj getStreamIndio(String streamId) throws IOException, com.coverity.ws.v9.CovRemoteServiceException_Exception {
        com.coverity.ws.v9.StreamFilterSpecDataObj filter = new com.coverity.ws.v9.StreamFilterSpecDataObj();
        filter.setNamePattern(streamId);

        List<com.coverity.ws.v9.StreamDataObj> streams = getConfigurationServiceIndio().getStreams(filter);
        if(streams.isEmpty()) {
            return null;
        } else {
            return streams.get(0);
        }
    }

    public FormValidation doCheck() throws IOException {
        try {
            URL url = getURL();
            // Fix:77968 by using different wsdl for v9 and v6.
            if(this.getWsVersion().equals("v9")){
                int responseCode = getURLResponseCode(new URL(url, CONFIGURATION_SERVICE_V9_WSDL));
                if(responseCode != 200) {
                    return FormValidation.error("Connected successfully, but Coverity web services were not detected.");
                }
                getConfigurationServiceIndio().getServerTime();
                return FormValidation.ok("Successfully connected to the instance.");
            } else {
                int responseCode = getURLResponseCode(new URL(url, CONFIGURATION_SERVICE_V5_WSDL));
                if(responseCode != 200) {
                    return FormValidation.error("Connected successfully, but Coverity web services were not detected.");
                }
                getConfigurationService().getServerTime();
                return FormValidation.ok("Successfully connected to the instance.");
            }
        } catch(UnknownHostException e) {
            return FormValidation.error("Host name unknown");
        } catch(ConnectException e) {
            return FormValidation.error("Connection refused");
        } catch(SocketException e) {
            return FormValidation.error("Error connecting to CIM. Please check your connection settings.");
        } catch(Throwable e) {
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

    private FormValidation error(Throwable t) {
        if(Hudson.getInstance().hasPermission(Hudson.ADMINISTER)) {
            return FormValidation.error(t, t.getMessage());
        } else {
            return FormValidation.error(t.getMessage());
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

    public String getCimInstanceCheckers() {
        List<String> checkers = new ArrayList<String>();

        if(this.getWsVersion().equals("v9")){
            try {
                checkers.addAll(this.getConfigurationServiceIndio().getCheckerNames());
            } catch(Exception e) {
            }
        } else{
            try {
                CheckerPropertyFilterSpecDataObj checkerPropFilter = new CheckerPropertyFilterSpecDataObj();
                List<CheckerPropertyDataObj> checkerPropertyList = this.getConfigurationService().getCheckerProperties(checkerPropFilter);
                for(CheckerPropertyDataObj checkerProp : checkerPropertyList){
                    CheckerSubcategoryIdDataObj checkerSub = checkerProp.getCheckerSubcategoryId();
                    if(!checkers.contains(checkerSub.getCheckerName())){
                        checkers.add(checkerSub.getCheckerName());
                    }
                }
            } catch(Exception e) {
            }
        }
        Collections.sort(checkers);
        return StringUtils.join(checkers, '\n');
    }

}
