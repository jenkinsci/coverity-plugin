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

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import hudson.util.Secret;
import jenkins.plugins.coverity.Utils.CIMInstanceBuilder;
import jenkins.plugins.coverity.Utils.CredentialUtil;
import org.junit.Assert;
import org.junit.Test;

import com.coverity.ws.v9.ConfigurationService;
import com.coverity.ws.v9.DefectService;

import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestDefectService;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Secret.class, CredentialsMatchers.class, CredentialsProvider.class})
public class WebServiceFactoryTest {
    private CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("cim-host").withPort(8080)
            .withUseSSL(false).withDefaultCredentialId().build();

    private URL getExpectedUrl(CIMInstance cim, String wsdl) throws MalformedURLException {
        return new URL(cim.isUseSSL() ? "https" : "http", cim.getHost(), cim.getPort(), wsdl);
    }

    @Test
    public void getDefectService_returns_DefectService_instance() throws IOException {
        CredentialUtil.setCredentialManager("test-user", "password");
        WebServiceFactory factory = new TestWebServiceFactory();

        DefectService result = factory.getDefectService(cimInstance);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TestDefectService);
        Assert.assertEquals(getExpectedUrl(cimInstance, WebServiceFactory.DEFECT_SERVICE_V9_WSDL), ((TestDefectService)result).getUrl());
    }

    @Test
    public void getDefectService_returns_same_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory();

        DefectService result = factory.getDefectService(cimInstance);

        Assert.assertNotNull(result);
        DefectService result2 = factory.getDefectService(cimInstance);

        Assert.assertSame(result, result2);
    }

    @Test
    public void getDefectService_returns_new_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory();

        DefectService result = factory.getDefectService(cimInstance);

        Assert.assertNotNull(result);

        CredentialUtil.setCredentialManager("test-user", "password");
        cimInstance = new CIMInstanceBuilder().withName("test instance 2").withHost("other-cim-host").withPort(8443)
                .withUseSSL(true).withDefaultCredentialId().build();

        DefectService result2 = factory.getDefectService(cimInstance);

        Assert.assertNotSame(result, result2);
        Assert.assertTrue(result2 instanceof TestDefectService);
        Assert.assertEquals(getExpectedUrl(cimInstance, WebServiceFactory.DEFECT_SERVICE_V9_WSDL), ((TestDefectService)result2).getUrl());
    }

    @Test
    public void getConfigurationService_returns_ConfigurationService_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory();

        ConfigurationService result = factory.getConfigurationService(cimInstance);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TestConfigurationService);
        Assert.assertEquals(getExpectedUrl(cimInstance, WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL), ((TestConfigurationService)result).getUrl());
    }

    @Test
    public void getConfigurationService_returns_same_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory();

        ConfigurationService result = factory.getConfigurationService(cimInstance);

        Assert.assertNotNull(result);
        ConfigurationService result2 = factory.getConfigurationService(cimInstance);

        Assert.assertSame(result, result2);
    }

    @Test
    public void getConfigurationService_returns_new_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory();

        ConfigurationService result = factory.getConfigurationService(cimInstance);

        Assert.assertNotNull(result);

        CredentialUtil.setCredentialManager("test-user", "password");
        cimInstance = new CIMInstanceBuilder().withName("test instance 2").withHost("other-cim-host").withPort(8443)
                .withUseSSL(true).withDefaultCredentialId().build();

        ConfigurationService result2 = factory.getConfigurationService(cimInstance);

        Assert.assertNotSame(result, result2);
        Assert.assertTrue(result2 instanceof TestConfigurationService);
        Assert.assertEquals(getExpectedUrl(cimInstance, WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL), ((TestConfigurationService)result2).getUrl());
    }
}
