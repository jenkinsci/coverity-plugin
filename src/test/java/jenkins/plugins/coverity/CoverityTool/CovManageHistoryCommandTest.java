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
package jenkins.plugins.coverity.CoverityTool;


import com.coverity.ws.v9.SnapshotIdDataObj;
import jenkins.plugins.coverity.*;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.Utils.InvocationAssistanceBuilder;
import jenkins.plugins.coverity.Utils.TaOptionBlockBuilder;
import jenkins.plugins.coverity.ws.TestWebServiceFactory;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CovManageHistoryCommandTest extends CommandTestBase {

    private TestConfigurationService configurationService;
    private CIMInstance cimInstance;

    @Before
    public void setup() throws IOException, InterruptedException {
        super.setup();

        cimInstance = mock(CIMInstance.class);
        configurationService = (TestConfigurationService) new TestWebServiceFactory().getConfigurationService(cimInstance);
        when(cimInstance.getConfigurationService()).thenReturn(configurationService);

        List<SnapshotIdDataObj> snapshotList = new ArrayList<>();
        SnapshotIdDataObj testSnapshot = new SnapshotIdDataObj();
        testSnapshot.setId(10001L);
        snapshotList.add(testSnapshot);
        configurationService.setupSnapshotList(snapshotList);
    }

    @Test
    public void prepareCommandTest() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId");

        when(cimInstance.getHost()).thenReturn("Localhost");
        when(cimInstance.getPort()).thenReturn(8080);
        when(cimInstance.getUser()).thenReturn("TestUser");
        when(cimInstance.getPassword()).thenReturn("TestPassword");
        when(cimInstance.isUseSSL()).thenReturn(false);
        when(cimInstance.getDataPort()).thenReturn(0);

        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCovHistoryCheckBox(true).build();
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().
                        withCimStream(cimStream).
                        withInvocationAssistance(invocationAssistance).
                        withTaOptionBlock(taOptionBlock).build();

        Command covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-manage-history", "--dir", "TestDir", "download", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--user", "TestUser", "--merge"
        });
        covManageHistoryCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-manage-history command line arguments: " + actualArguments.toString());
    }

    @Test
    public void prepareCommandTest_WithSslConfiguration_ForIndio() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId");

        when(cimInstance.getHost()).thenReturn("Localhost");
        when(cimInstance.getPort()).thenReturn(8080);
        when(cimInstance.getUser()).thenReturn("TestUser");
        when(cimInstance.getPassword()).thenReturn("TestPassword");
        when(cimInstance.isUseSSL()).thenReturn(true);
        when(cimInstance.getDataPort()).thenReturn(0);

        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCovHistoryCheckBox(true).build();
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().
                        withCimStream(cimStream).
                        withInvocationAssistance(invocationAssistance).
                        withTaOptionBlock(taOptionBlock).build();

        Command covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_INDIO);
        setExpectedArguments(new String[] {
                "cov-manage-history", "--dir", "TestDir", "download", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--ssl", "--user", "TestUser", "--merge"
        });
        covManageHistoryCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-manage-history command line arguments: " + actualArguments.toString());
    }

    @Test
    public void prepareCommandTest_WithSslConfiguration_ForJasperOrHigher() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId");

        when(cimInstance.getHost()).thenReturn("Localhost");
        when(cimInstance.getPort()).thenReturn(8080);
        when(cimInstance.getUser()).thenReturn("TestUser");
        when(cimInstance.getPassword()).thenReturn("TestPassword");
        when(cimInstance.isUseSSL()).thenReturn(true);
        when(cimInstance.getDataPort()).thenReturn(0);

        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCovHistoryCheckBox(true).build();
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = mock(CoverityPublisher.class);

        when(publisher.getTaOptionBlock()).thenReturn(taOptionBlock);
        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStream()).thenReturn(cimStream);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        Command covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-manage-history", "--dir", "TestDir", "download", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--ssl", "--on-new-cert", "trust",
                "--cert", "TestCertFile", "--user", "TestUser", "--merge"
        });
        covManageHistoryCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-manage-history command line arguments: " + actualArguments.toString());
    }
}
