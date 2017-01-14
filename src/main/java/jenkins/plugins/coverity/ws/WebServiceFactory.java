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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.DefectServiceService;

import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.ClientAuthenticationHandlerWSS;

/**
 * Factory for creating or getting
 */
public class WebServiceFactory {
    private static final Logger logger = Logger.getLogger(WebServiceFactory.class.getName());

    private static WebServiceFactory instance = null;

    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";

    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";

    private Map<CIMInstance, DefectService> defectServiceMap;

    protected WebServiceFactory(Map<CIMInstance, DefectService> defectServiceMap) {
        this.defectServiceMap = defectServiceMap;

        if(this.defectServiceMap == null) {
            this.defectServiceMap = new HashMap<>();
        }
    }

    public static WebServiceFactory getInstance() {
        if (instance == null) {
            synchronized (WebServiceFactory.class) {
                if (instance == null) {
                    instance = new WebServiceFactory(null);
                }
            }
        }
        return instance;
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
     * The root URL for the CIM instance
     *
     * @return a url
     * @throws MalformedURLException should not happen if host is valid
     */
    public URL getURL(CIMInstance cimInstance) throws MalformedURLException {
        return new URL(cimInstance.isUseSSL() ? "https" : "http", cimInstance.getHost(), cimInstance.getPort(), "/");
    }

    /**
     * Attach an authentication handler to the web service, that uses the configured user and password
     */
    private void attachAuthenticationHandler(BindingProvider service, CIMInstance cimInstance) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(cimInstance.getUser(), cimInstance.getPassword())));
    }
}
