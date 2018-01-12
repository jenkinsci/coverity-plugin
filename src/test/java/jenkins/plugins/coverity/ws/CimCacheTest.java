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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import hudson.util.Secret;
import jenkins.plugins.coverity.Utils.CIMInstanceBuilder;
import jenkins.plugins.coverity.Utils.CredentialUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebServiceFactory.class, Secret.class, CredentialsMatchers.class, CredentialsProvider.class})
public class CimCacheTest {
    @Before
    public void setup() throws IOException {
        // setup web service factory
        final WebServiceFactory testWsFactory = new TestWebServiceFactory();
        PowerMockito.mockStatic(WebServiceFactory.class);
        when(WebServiceFactory.getInstance()).thenReturn(testWsFactory);
    }

    @Test
    public void getProjects_returnsProjectsForInstances() throws IOException {
        CredentialUtil.setCredentialManager("admin", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                                    .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        List<String> projects = CimCache.getInstance().getProjects(cimInstance);

        List<String> expectedProjectNames = new ArrayList<>(Arrays.asList("project0", "project1", "project2"));
        assertEquals(expectedProjectNames, projects);

        projects = CimCache.getInstance().getProjects(cimInstance);
        assertEquals(expectedProjectNames, projects);

        cimInstance = new CIMInstanceBuilder().withName("test-instance-2").withHost("test.coverity2.").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 2, "stream", 1);

        expectedProjectNames = new ArrayList<>(Arrays.asList("project0", "project1"));
        projects = CimCache.getInstance().getProjects(cimInstance);
        assertEquals(expectedProjectNames, projects);
    }

    @Test
    public void getStreams_returnsStreamsForInstances() throws IOException {
        CredentialUtil.setCredentialManager("admin", "password");
        CIMInstance cimInstance = new CIMInstanceBuilder().withName("test").withHost("test.coverity").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 2);

        List<String> streams = CimCache.getInstance().getStreams(cimInstance, "project1");

        List<String> expectedStreamNames = new ArrayList<>(Arrays.asList("stream0", "stream1"));
        assertEquals(expectedStreamNames, streams);

        streams = CimCache.getInstance().getStreams(cimInstance, "project0");
        assertEquals(expectedStreamNames, streams);

        cimInstance = new CIMInstanceBuilder().withName("test-instance-2").withHost("test.coverity2.").withPort(8080)
                .withUseSSL(false).withDefaultCredentialId().build();
        testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 1, "stream", 4);

        expectedStreamNames = new ArrayList<>(Arrays.asList("stream0", "stream1", "stream2", "stream3"));
        streams = CimCache.getInstance().getStreams(cimInstance, "project0");
        assertEquals(expectedStreamNames, streams);
        streams = CimCache.getInstance().getStreams(cimInstance, "unknownProject");
        assertEquals(new ArrayList<String>(), streams);
    }
}
