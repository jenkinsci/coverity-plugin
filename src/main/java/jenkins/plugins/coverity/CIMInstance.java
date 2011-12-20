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

import com.coverity.ws.v3.*;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import hudson.util.Secret;
import org.kohsuke.stapler.*;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents one Coverity Integrity Manager server
 */
public class CIMInstance {

    public static final String COVERITY_V3_NAMESPACE = "http://ws.coverity.com/v3";

    public static final String ADMINISTRATION_SERVICE_V1_WSDL = "/ws/v1/administrationservice?wsdl";
    public static final String ADMINISTRATION_SERVICE_V2_WSDL = "/ws/v2/administrationservice?wsdl";
    public static final String ADMINISTRATION_SERVICE_V3_WSDL = "/ws/v3/administrationservice?wsdl";
    public static final String CONFIGURATION_SERVICE_V3_WSDL = "/ws/v3/configurationservice?wsdl";
    public static final String DEFECT_SERVICE_V3_WSDL = "/ws/v3/defectservice?wsdl";

    public static final String STREAM_TYPE_STATIC = "STATIC";
    public static final String STREAM_TYPE_SOURCE = "SOURCE";

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
     * Username for connecting to the CIM server
     */
    private final String user;

    /**
     * Password for connecting to the CIM server
     */
    private final Secret password;

    /**
     * Use SSL
     */
    private final boolean useSSL;

    /**
     * cached webservice port for Administration service
     */
    private transient AdministrationServiceService administrationServiceService;

    /**
     * cached webservice port for Defect service
     */
    private transient DefectServiceService defectServiceService;

    /**
     * cached webservice port for Configuration service
     */
    private transient ConfigurationServiceService configurationServiceService;

    @DataBoundConstructor
    public CIMInstance(String name, String host, int port, String user, Secret password, boolean useSSL) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.useSSL = useSSL;
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

    public Secret getPassword() {
        return password;
    }

    public boolean isUseSSL() {
        return useSSL;
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
     * Returns a Defect service client
     */
    public DefectService getDefectService() throws IOException {
        synchronized (this) {
            if (defectServiceService == null) {
                defectServiceService = new DefectServiceService(
                        new URL(getURL(), DEFECT_SERVICE_V3_WSDL),
                        new QName(COVERITY_V3_NAMESPACE, "DefectServiceService"));
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            DefectService defectService = defectServiceService.getDefectServicePort();
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
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(
                new ClientAuthenticationHandlerWSS(user, password.getPlainText())));
    }

    /**
     * Returns a Configuration service client
     */
    public ConfigurationService getConfigurationService() throws IOException {
        synchronized (this) {
            if (configurationServiceService == null) {
                // Create a Web Services port to the server
                configurationServiceService = new ConfigurationServiceService(
                        new URL(getURL(), CONFIGURATION_SERVICE_V3_WSDL),
                        new QName(COVERITY_V3_NAMESPACE, "ConfigurationServiceService"));
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
     * Returns an Administration service client
     */
    public AdministrationService getAdministrationService() throws IOException {
        synchronized (this) {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            if (administrationServiceService == null) {
                // Create a Web Services port to the server
                administrationServiceService = new AdministrationServiceService(
                        new URL(getURL(), ADMINISTRATION_SERVICE_V3_WSDL),
                        new QName(COVERITY_V3_NAMESPACE, "AdministrationServiceService"));
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            AdministrationService administrationService = administrationServiceService.getAdministrationServicePort();
            attachAuthenticationHandler((BindingProvider) administrationService);

            return administrationService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    public List<MergedDefectDataObj> getDefects(String streamId, List<Long> defectIds) throws IOException, CovRemoteServiceException_Exception {
        MergedDefectFilterSpecDataObj filterSpec1 = new MergedDefectFilterSpecDataObj();
        StreamIdDataObj stream = new StreamIdDataObj();
        stream.setName(streamId);
        stream.setType(STREAM_TYPE_STATIC);
        filterSpec1.getStreamIdIncludeList().add(stream);
        PageSpecDataObj pageSpec = new PageSpecDataObj();
        pageSpec.setPageSize(2500);

        List<MergedDefectDataObj> result = new ArrayList<MergedDefectDataObj>();
        int defectCount = 0;
        MergedDefectsPageDataObj defects = null;
        do {
            pageSpec.setStartIndex(defectCount);
            defects = getDefectService().getMergedDefectsForStreams(Arrays.asList(stream), filterSpec1, pageSpec);
            for (MergedDefectDataObj defect : defects.getMergedDefects()) {
                if (defectIds.contains(defect.getCid())) {
                    result.add(defect);
                }
            }
            defectCount += defects.getMergedDefects().size();
        } while (defectCount < defects.getTotalNumberOfRecords());

        return result;
    }

    public ProjectDataObj getProject(String projectId) throws IOException, CovRemoteServiceException_Exception {
        ProjectFilterSpecDataObj filterSpec = new ProjectFilterSpecDataObj();
        filterSpec.setNamePattern(projectId);
        List<ProjectDataObj> projects = getConfigurationService().getProjects(filterSpec);
        if (projects.size() == 0) {
            return null;
        } else {
            return projects.get(0);
        }
    }

    private transient Map<String, Long> projectKeys;

    public Long getProjectKey(String projectId) throws IOException, CovRemoteServiceException_Exception {
        if (projectKeys == null) {
            projectKeys = new ConcurrentHashMap<String, Long>();
        }

        Long result = projectKeys.get(projectId);
        if (result == null) {
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
        for (StreamDataObj stream : project.getStreams()) {
            if (stream.getId().getType().equals("STATIC")) {
                result.add(stream);
            }
        }

        return result;
    }

    public StreamDataObj getStream(String streamId) throws IOException, CovRemoteServiceException_Exception {
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.getTypeList().add(STREAM_TYPE_SOURCE);
        filter.setNamePattern(streamId);

        List<StreamDataObj> streams = getConfigurationService().getStreams(filter);
        if (streams.isEmpty()) {
            return null;
        } else {
            return streams.get(0);
        }
    }

    public FormValidation doCheck() throws IOException {
        try {
            URL url = getURL();
            int responseCode = getURLResponseCode(new URL(url, ADMINISTRATION_SERVICE_V3_WSDL));
            if (responseCode != 200) {
                if (getURLResponseCode(new URL(url, ADMINISTRATION_SERVICE_V2_WSDL)) == 200) {
                    return FormValidation.error("Detected Coverity web services v2, but v3 is required");
                } else if (getURLResponseCode(new URL(url, ADMINISTRATION_SERVICE_V1_WSDL)) == 200) {
                    return FormValidation.error("Detected Coverity web services v1, but v3 is required");
                } else {
                    return FormValidation.error("Connected successfully, but Coverity web services were not detected.");
                }
            }
            getAdministrationService().getServerTime();
            return FormValidation.ok("Successfully connected to the instance.");
        } catch (UnknownHostException e) {
            return FormValidation.error("Host name unknown");
        } catch (ConnectException e) {
            return FormValidation.error("Connection refused");
        } catch (Throwable e) {
            String javaVersion = System.getProperty("java.version");
            if (javaVersion.startsWith("1.6.0_")) {
                int patch = Integer.parseInt(javaVersion.substring(javaVersion.indexOf('_') + 1));
                if (patch < 26) {
                    return FormValidation.error(e, "Please use Java 1.6.0_26 or later to run Jenkins.");
                }
            }
            return FormValidation.error(e, "An unexpected error occurred.");
        }
    }

    private FormValidation error(Throwable t) {
        if (Hudson.getInstance().hasPermission(Hudson.ADMINISTER)) {
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
        } catch (FileNotFoundException e) {
            return 404;
        }

    }

}
