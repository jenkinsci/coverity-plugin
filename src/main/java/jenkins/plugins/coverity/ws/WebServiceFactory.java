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
package jenkins.plugins.coverity.ws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.coverity.ws.v9.ConfigurationService;
import com.coverity.ws.v9.ConfigurationServiceService;
import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.DefectServiceService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import jenkins.plugins.coverity.CIMInstance;

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
        DefectService defectService;
        synchronized(this) {
            if(!defectServiceMap.containsKey(cimInstance)) {
                defectService = createDefectService(cimInstance);
                defectServiceMap.put(cimInstance, defectService);
            }
            else {
                defectService = defectServiceMap.get(cimInstance);
            }
        }
        return defectService;
    }

    protected DefectService createDefectService(CIMInstance cimInstance) throws MalformedURLException {
        DefectServiceService defectServiceService = new DefectServiceService(
            new URL(getURL(cimInstance), DEFECT_SERVICE_V9_WSDL),
            new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

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
        ConfigurationService configurationService;
        synchronized (this){
            if(!configurationServiceMap.containsKey(cimInstance)) {
                configurationService = createConfigurationService(cimInstance);
                configurationServiceMap.put(cimInstance, configurationService);
            }
            else {
                configurationService = configurationServiceMap.get(cimInstance);
            }
        }
        return configurationService;
    }

    protected ConfigurationService createConfigurationService(CIMInstance cimInstance) throws MalformedURLException {
        ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
            new URL(getURL(cimInstance), CONFIGURATION_SERVICE_V9_WSDL),
            new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

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
    public ViewsService getViewService(CIMInstance instance) throws MalformedURLException, NoSuchAlgorithmException {
        URL baseUrl = getURL(instance);

        ClientConfig config = new DefaultClientConfig();
        if (instance.isUseSSL()) {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            final HTTPSProperties httpsProperties = new HTTPSProperties(HttpsURLConnection.getDefaultHostnameVerifier(), sslContext);
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, httpsProperties);
        }
        Client restClient = Client.create();
        restClient.addFilter(new HTTPBasicAuthFilter(instance.getCoverityUser(), instance.getCoverityPassword()));

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
     * Gets the response code for the {@link CIMInstance} V9 Web Services WSDL
     * @param cimInstance the CIM instance to get response for
     * @return the HTTP Status-Code from the WSDL, or -1
     */
    public int getWSResponseCode(CIMInstance cimInstance) {
        try {
            int responseCode = getWSResponseCode(new URL(getURL(cimInstance), WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL));
            return responseCode;
        } catch (MalformedURLException e) {
            return -1;
        }
    }

    /**
     * The response code for the given {@link URL}
     * It will return 200 and 401 respectively.
     * Returns -1 if no code can be discerned
     * @param url to check response
     * @return the HTTP Status-Code, or -1
     */
    private int getWSResponseCode(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
            return conn.getResponseCode();
        } catch(FileNotFoundException e) {
            return 404;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Attach an authentication handler to the web service, that uses the configured user and password
     */
    private void attachAuthenticationHandler(BindingProvider service, CIMInstance cimInstance) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(
                cimInstance.getCoverityUser(), cimInstance.getCoverityPassword())));
    }
}
