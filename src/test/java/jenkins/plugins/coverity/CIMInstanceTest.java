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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.StreamDataObj;

import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import jenkins.plugins.coverity.ws.WebServiceFactory;


@RunWith(PowerMockRunner.class)
@PrepareForTest(WebServiceFactory.class)
public class CIMInstanceTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() throws IOException {
        // setup web service factory
        final WebServiceFactory testWsFactory = new TestWebServiceFactory();
        PowerMockito.mockStatic(WebServiceFactory.class);
        when(WebServiceFactory.getInstance()).thenReturn(testWsFactory);
    }

    @Test
    public void getProjectKey_forExistingProject() throws IOException, CovRemoteServiceException_Exception {

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        Long result = cimInstance.getProjectKey("project1");

        assertEquals(Long.valueOf((long)1), result);
    }

    @Test
    public void getProjectKey_forUnknownProject() throws IOException, CovRemoteServiceException_Exception {

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        Long result = cimInstance.getProjectKey("unknown-project");

        assertNull(result);
    }

    @Test
    public void getStream_returnsMatchingStream() throws IOException, CovRemoteServiceException_Exception {
        final String streamId = "stream0";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        TestConfigurationService testConfigurationService = (TestConfigurationService)WebServiceFactory.getInstance().getConfigurationService(cimInstance);
        testConfigurationService.setupProjects("project", 3, "stream", 1);

        StreamDataObj result = cimInstance.getStream(streamId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(streamId, result.getId().getName());
    }

    @Test
    public void getStreams_throwsWithNoStreams() throws IOException, CovRemoteServiceException_Exception {
        final String streamId = "stream1";

        CIMInstance cimInstance = new CIMInstance("test", "test.coverity", 8080, "admin", "password", false, 9080);

        exception.expect(IOException.class);
        exception.expectMessage("An error occurred while retrieving streams for the given project. Could not find stream: " + streamId);
        cimInstance.getStream(streamId);
    }
}
