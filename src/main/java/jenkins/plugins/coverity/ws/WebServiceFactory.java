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
package jenkins.plugins.coverity.ws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.coverity.ws.v9.ConfigurationService;
import com.coverity.ws.v9.ConfigurationServiceService;
import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.DefectServiceService;

import jenkins.plugins.coverity.CIMInstance;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Factory for creating or getting web services
 */
public class WebServiceFactory {
    private static final Logger logger = Logger.getLogger(WebServiceFactory.class.getName());

    private static WebServiceFactory instance = null;

    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";
    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";
    public static final String CONFIGURATION_SERVICE_V9_WSDL = "/ws/v9/configurationservice?wsdl";

    private Map<CIMInstance, DefectService> defectServiceMap;
    private Map<CIMInstance, ConfigurationService> configurationServiceMap;
    private Map<CIMInstance, URL> configurationServiceUrlMap;
    private Map<CIMInstance, URL> defectServiceUrlMap;

    protected WebServiceFactory() {
        this.defectServiceMap = new HashMap<>();
        this.configurationServiceMap = new HashMap<>();
        this.configurationServiceUrlMap = new HashMap<>();
        this.defectServiceUrlMap = new HashMap<>();
    }

    public static WebServiceFactory getInstance() {
        // ignore for Coverity TA since unit tests will not run against a real Connect server
        //.*cov-begin-ignore
        synchronized (WebServiceFactory.class) {
            if (instance == null) {
                instance = new WebServiceFactory();
            }
            return instance;
        }
        //.*cov-end-ignore
    }

    /**
     * Returns a Defect service client using v9 web services.
     */
    public DefectService getDefectService(CIMInstance cimInstance) throws IOException {
        DefectService defectService = null;
        synchronized(this) {
            if(!defectServiceMap.containsKey(cimInstance)) {
                defectService = createDefectService(cimInstance);
                defectServiceMap.put(cimInstance, defectService);
                CheckWsResponse connectionResponse = getCheckWsResponse(cimInstance);
                if (connectionResponse.isConnected()){
                    if (!defectServiceMap.containsKey(cimInstance)){
                        defectService = createDefectService(cimInstance);
                        defectServiceMap.put(cimInstance, defectService);
                    }
                }
            }
            else {
                CheckWsResponse response = getCheckWsResponse(cimInstance);
                if (response.isConnected()){
                    if (!defectServiceMap.containsKey(cimInstance)){
                        defectService = createDefectService(cimInstance);
                        defectServiceMap.put(cimInstance, defectService);
                    } else{
                        defectService = defectServiceMap.get(cimInstance);
                    }
                }
            }
        }
        return defectService;
    }

    protected DefectService createDefectService(CIMInstance cimInstance) throws MalformedURLException {
        URL url = null;
        if (defectServiceUrlMap.containsKey(cimInstance)){
            url = defectServiceUrlMap.get(cimInstance);
        }else{
            url = new URL(getURL(cimInstance), WebServiceFactory.DEFECT_SERVICE_V9_WSDL);
        }

        DefectServiceService defectServiceService = new DefectServiceService(
                url, new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            DefectService defectService = defectServiceService.getDefectServicePort();
            attachAuthenticationHandler((BindingProvider) defectService, cimInstance);

            return defectService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    /**
     * Returns a Configuration service client using v9 web services.
     */
    public ConfigurationService getConfigurationService(CIMInstance cimInstance) throws IOException {
        ConfigurationService configurationService = null;
        synchronized (this){
            if(!configurationServiceMap.containsKey(cimInstance)) {
                configurationService = createConfigurationService(cimInstance);
                configurationServiceMap.put(cimInstance, configurationService);
                CheckWsResponse connectionResponse = getCheckWsResponse(cimInstance);
                if (connectionResponse.isConnected()){
                    if (!configurationServiceMap.containsKey(cimInstance)){
                        configurationService = createConfigurationService(cimInstance);
                        configurationServiceMap.put(cimInstance, configurationService);
                    }
                }
            }
            else {
                CheckWsResponse response = getCheckWsResponse(cimInstance);
                if (response.isConnected()){
                    if (!configurationServiceMap.containsKey(cimInstance)){
                        configurationService = createConfigurationService(cimInstance);
                        configurationServiceMap.put(cimInstance, configurationService);
                    } else{
                        configurationService = configurationServiceMap.get(cimInstance);
                    }
                }
            }
        }
        return configurationService;
    }

    protected ConfigurationService createConfigurationService(CIMInstance cimInstance) throws MalformedURLException {
        URL url = null;
        if (configurationServiceUrlMap.containsKey(cimInstance)){
            url = configurationServiceUrlMap.get(cimInstance);
        }else{
            url = new URL(getURL(cimInstance), WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
        }

        ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
                url, new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
            attachAuthenticationHandler((BindingProvider) configurationService, cimInstance);

            return configurationService;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    /**
     * Returns a new Views Service client
     */
    public ViewsService getViewService(CIMInstance instance) throws MalformedURLException, NoSuchAlgorithmException, KeyManagementException {
        URL baseUrl = null;
        if (configurationServiceUrlMap.containsKey(instance)){
            baseUrl = configurationServiceUrlMap.get(instance);
            baseUrl = new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), "/");
        }else{
            baseUrl = getURL(instance);
        }

        Client restClient = null;
        if (instance.isUseSSL()){
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, null, new SecureRandom());
            restClient = ClientBuilder.newBuilder()
                        .sslContext(sslContext)
                        .hostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier())
                        .build();
        } else {
            restClient = ClientBuilder.newClient();
        }

        HttpAuthenticationFeature httpAuthenticationFeature =
                HttpAuthenticationFeature.basic(instance.getCoverityUser(), instance.getCoverityPassword());
        restClient.register(httpAuthenticationFeature);

        return new ViewsService(baseUrl, restClient);
    }

    /**
     * The root URL for the CIM instance
     *
     * @return a url
     * @throws MalformedURLException should not happen if host is valid
     */
    protected URL getURL(CIMInstance cimInstance) throws MalformedURLException {
        return new URL(cimInstance.isUseSSL() ? "https" : "http", cimInstance.getHost(), cimInstance.getPort(), "/");
    }

    /**
     * Gets the response code and message as {@link CheckWsResponse} for the {@link CIMInstance} V9 Web Services WSDL
     * @param cimInstance the CIM instance to get response for
     * @return A {@link CheckWsResponse} with HTTP Status-Code and message from the WSDL, or -1 and an exception message
     */
    public CheckWsResponse getCheckWsResponse(CIMInstance cimInstance) {
        try {
            URL url;
            // Decide which URL to use
            if (configurationServiceUrlMap.containsKey(cimInstance)){
                url = configurationServiceUrlMap.get(cimInstance);
            }else{
                url = new URL(getURL(cimInstance), WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
            }

            CheckWsResponse response = getCheckWsResponse(url, cimInstance);
            if (response.isConnected()){
                synchronized (this){
                    configurationServiceUrlMap.put(cimInstance, new URL(response.getServiceUrl()));
                }
            }
            return response;
        } catch (MalformedURLException e) {
            return new CheckWsResponse(-1, e.getClass().getSimpleName() + ": " + e.getMessage(), StringUtils.EMPTY);
        }
    }

    /**
     * The {@link CheckWsResponse} for the given {@link URL}
     * It will return 200 and 401 respectively.
     * Returns -1 if no code can be discerned
     * @param url to check response
     * @return A {@link CheckWsResponse} with HTTP Status-Code and message from the WSDL, or -1 and an exception message
     */
    private CheckWsResponse getCheckWsResponse(URL url, CIMInstance cimInstance) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();

            int responseCode = conn.getResponseCode();
            if (responseCode == 301 || responseCode == 302 || responseCode == 307 || responseCode == 308){
                resetWebServices(cimInstance);
                return getCheckWsResponse(new URL(conn.getHeaderField("Location")), cimInstance);
            }

            return new CheckWsResponse(conn.getResponseCode(), conn.getResponseMessage(), url.toString());
        } catch(FileNotFoundException e) {
            return new CheckWsResponse(404, "URL '" + url + "' not found", StringUtils.EMPTY);
        } catch (IOException e) {
            return new CheckWsResponse(-1, e.getClass().getSimpleName() + ": " + e.getMessage(), StringUtils.EMPTY);
        }
    }

    /**
     * Attach an authentication handler to the web service, that uses the configured user and password
     */
    private void attachAuthenticationHandler(BindingProvider service, CIMInstance cimInstance) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(
                cimInstance.getCoverityUser(), cimInstance.getCoverityPassword())));
    }

    private void resetWebServices(CIMInstance cimInstance){
        synchronized (this){
            this.configurationServiceMap.remove(cimInstance);
            this.defectServiceMap.remove(cimInstance);
            this.configurationServiceUrlMap.remove(cimInstance);
            this.defectServiceUrlMap.remove(cimInstance);
        }
    }

    /**
     * A response from the web service URL check with the HTTP Status-Code and response message
     */
    public static class CheckWsResponse {
        private final int responseCode;
        private final String responseMessage;
        private final String serviceUrl;

        public CheckWsResponse(int responseCode, String responseMessage, String serviceUrl) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.serviceUrl = serviceUrl;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getServiceUrl(){
            return this.serviceUrl;
        }

        @Override
        public String toString() {
            return "Check Coverity Web Service Response: {" +
                " Code=" + responseCode +
                ", Message=\"" + responseMessage + "\" " +
                ", URL=" + serviceUrl +
                '}';
        }

        public boolean isConnected(){
            return responseCode == 200;
        }
    }
}
