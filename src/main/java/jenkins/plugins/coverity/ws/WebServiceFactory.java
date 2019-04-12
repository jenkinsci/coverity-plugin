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
    private static int REDIRECTION_MAX_TRY = 10;

    private Map<CIMInstance, DefectService> defectServiceMap;
    private Map<CIMInstance, ConfigurationService> configurationServiceMap;

    protected WebServiceFactory() {
        this.defectServiceMap = new HashMap<>();
        this.configurationServiceMap = new HashMap<>();
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
            }
            ensureServiceConnection(cimInstance, WebServiceType.DefectService);
            return defectServiceMap.get(cimInstance);
        }
    }

    protected DefectService createDefectService(CIMInstance cimInstance) throws MalformedURLException {
        URL url = getURL(cimInstance, WebServiceType.DefectService);

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
        synchronized (this){
            if(!configurationServiceMap.containsKey(cimInstance)) {
                ConfigurationService configurationService = createConfigurationService(cimInstance);
                configurationServiceMap.put(cimInstance, configurationService);
            }
            ensureServiceConnection(cimInstance, WebServiceType.ConfigurationService);
            return configurationServiceMap.get(cimInstance);
        }
    }

    private void ensureServiceConnection(CIMInstance cimInstance, WebServiceType serviceType) {
        try{
            CheckWsResponse connectionResponse = getCheckWsResponse(cimInstance);
            synchronized (this){
                if (connectionResponse.isConnected()){
                    if (serviceType == WebServiceType.ConfigurationService){
                        if (!configurationServiceMap.containsKey(cimInstance)){
                            configurationServiceMap.put(cimInstance, createConfigurationService(cimInstance));
                        }
                    }else if (serviceType == WebServiceType.DefectService){
                        if (!defectServiceMap.containsKey(cimInstance)){
                            defectServiceMap.put(cimInstance, createDefectService(cimInstance));
                        }
                    }
                }
            }
        }catch (MalformedURLException e){
            logger.log(java.util.logging.Level.ALL, "MalformedURLException occurred");
        }
    }

    protected ConfigurationService createConfigurationService(CIMInstance cimInstance) throws MalformedURLException {
        URL url = getURL(cimInstance, WebServiceType.ConfigurationService);

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
        URL url = getURL(instance, WebServiceType.ViewService);

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

        return new ViewsService(url, restClient);
    }

    /**
     * The root URL for the CIM instance for web service type
     *
     * @return a url
     * @throws MalformedURLException should not happen if host is valid
     */
    protected URL getURL(CIMInstance cimInstance, WebServiceType serviceType) throws MalformedURLException {
        URL baseUrl = CimServiceUrlCache.getInstance().getURL(cimInstance);
        if (baseUrl == null){
            baseUrl = new URL(cimInstance.isUseSSL() ? "https" : "http", cimInstance.getHost(), cimInstance.getPort(), "/");
        }

        switch(serviceType){
            case ConfigurationService:
                return new URL(baseUrl, WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
            case DefectService:
                return new URL(baseUrl, WebServiceFactory.DEFECT_SERVICE_V9_WSDL);
            default:
                return baseUrl;
        }
    }

    /**
     * Gets the response code and message as {@link CheckWsResponse} for the {@link CIMInstance} V9 Web Services WSDL
     * @param cimInstance the CIM instance to get response for
     * @return A {@link CheckWsResponse} with HTTP Status-Code and message from the WSDL, or -1 and an exception message
     */
    public CheckWsResponse getCheckWsResponse(CIMInstance cimInstance) {
        try {
            URL url = getURL(cimInstance, WebServiceType.ConfigurationService);

            CheckWsResponse response = getCheckWsResponse(url, cimInstance, 1);
            if (response.isConnected()){
                synchronized (this){
                    CimServiceUrlCache.getInstance().cacheURL(cimInstance, new URL(response.getBaseUrl()));
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
    public CheckWsResponse getCheckWsResponse(URL url, CIMInstance cimInstance, int currentTryNumber) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();

            int responseCode = conn.getResponseCode();
            if (responseCode == 301 || responseCode == 302 || responseCode == 307 || responseCode == 308){
                if (currentTryNumber == REDIRECTION_MAX_TRY){
                    return new CheckWsResponse(responseCode, "Max try redirection. ", url.toString());
                }
                resetWebServices(cimInstance);
                return getCheckWsResponse(new URL(conn.getHeaderField("Location")), cimInstance, currentTryNumber+1);
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
            CimServiceUrlCache.getInstance().removeURL(cimInstance);
        }
    }

    /**
     * A response from the web service URL check with the HTTP Status-Code and response message
     */
    public static class CheckWsResponse {
        private final int responseCode;
        private final String responseMessage;
        private final String serviceUrl;
        private final String baseUrl;

        public CheckWsResponse(int responseCode, String responseMessage, String serviceUrl) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.serviceUrl = serviceUrl;
            this.baseUrl = generateBaseUrl();
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getServiceUrl(){
            return this.serviceUrl;
        }

        public String getBaseUrl(){
            return this.baseUrl;
        }

        @Override
        public String toString() {
            return "Check Coverity Web Service Response: {" +
                " Code=" + responseCode +
                ", Message=\"" + responseMessage + "\"" +
                ", URL=" + serviceUrl+
                '}';
        }

        public boolean isConnected(){
            return responseCode == 200;
        }

        private String generateBaseUrl(){
            try{
                URL url = new URL(serviceUrl);
                URL baseUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), "/");
                return baseUrl.toString();
            }catch (MalformedURLException e){
                logger.log(java.util.logging.Level.ALL, "MalformedURLException occurred during getting the base url");
                return StringUtils.EMPTY;
            }
        }
    }

    public enum WebServiceType {
        ConfigurationService,
        DefectService,
        ViewService
    }
}
