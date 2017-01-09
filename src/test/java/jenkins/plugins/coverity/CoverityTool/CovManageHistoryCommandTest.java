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

import hudson.XmlFile;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

public class CovManageHistoryCommandTest extends CommandTestBase {

    @Test
    public void CovManageHistoryCommand_PrepareCommandTest() throws IOException {
        mocker.replay();

        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                cimStreamList, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        CovCommand covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covManageHistoryArguments = covManageHistoryCommand.getCommandLines();

        assertEquals(13, covManageHistoryArguments.size());

        checkCommandLineArg(covManageHistoryArguments, "cov-manage-history");
        checkCommandLineArg(covManageHistoryArguments, "--dir");
        checkCommandLineArg(covManageHistoryArguments, "TestDir");
        checkCommandLineArg(covManageHistoryArguments, "download");
        checkCommandLineArg(covManageHistoryArguments, "--host");
        checkCommandLineArg(covManageHistoryArguments, "Localhost");
        checkCommandLineArg(covManageHistoryArguments, "--port");
        checkCommandLineArg(covManageHistoryArguments, "8080");
        checkCommandLineArg(covManageHistoryArguments, "--stream");
        checkCommandLineArg(covManageHistoryArguments, "TestStream");
        checkCommandLineArg(covManageHistoryArguments, "--user");
        checkCommandLineArg(covManageHistoryArguments, "TestUser");
        checkCommandLineArg(covManageHistoryArguments, "--merge");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covManageHistoryArguments.size());
    }

    @Test
    public void CovManageHistoryCommand_PrepareCommandTest_WithSslConfiguration_ForIndio() throws IOException {
        mocker.replay();

        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                cimStreamList, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        CovCommand covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_INDIO);
        List<String> covManageHistoryArguments = covManageHistoryCommand.getCommandLines();

        assertEquals(14, covManageHistoryArguments.size());

        checkCommandLineArg(covManageHistoryArguments, "cov-manage-history");
        checkCommandLineArg(covManageHistoryArguments, "--dir");
        checkCommandLineArg(covManageHistoryArguments, "TestDir");
        checkCommandLineArg(covManageHistoryArguments, "download");
        checkCommandLineArg(covManageHistoryArguments, "--host");
        checkCommandLineArg(covManageHistoryArguments, "Localhost");
        checkCommandLineArg(covManageHistoryArguments, "--port");
        checkCommandLineArg(covManageHistoryArguments, "8080");
        checkCommandLineArg(covManageHistoryArguments, "--stream");
        checkCommandLineArg(covManageHistoryArguments, "TestStream");
        checkCommandLineArg(covManageHistoryArguments, "--ssl");
        checkCommandLineArg(covManageHistoryArguments, "--user");
        checkCommandLineArg(covManageHistoryArguments, "TestUser");
        checkCommandLineArg(covManageHistoryArguments, "--merge");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covManageHistoryArguments.size());
    }

    @Test
    public void CovManageHistoryCommand_PrepareCommandTest_WithSslConfiguration_ForJasperOrHigher() throws IOException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mocker.createMock(CoverityPublisher.DescriptorImpl.class);
        expect(descriptor.getSslConfigurations()).andReturn(sslConfigurations);

        CoverityPublisher publisher = mocker.createMock(CoverityPublisher.class);
        expect(publisher.getDescriptor()).andReturn(descriptor);
        expect(publisher.getInvocationAssistance()).andReturn(invocationAssistance);
        expect(publisher.getCimStreams()).andReturn(cimStreamList);
        mocker.replay();

        CovCommand covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covManageHistoryArguments = covManageHistoryCommand.getCommandLines();

        assertEquals(18, covManageHistoryArguments.size());

        checkCommandLineArg(covManageHistoryArguments, "cov-manage-history");
        checkCommandLineArg(covManageHistoryArguments, "--dir");
        checkCommandLineArg(covManageHistoryArguments, "TestDir");
        checkCommandLineArg(covManageHistoryArguments, "download");
        checkCommandLineArg(covManageHistoryArguments, "--host");
        checkCommandLineArg(covManageHistoryArguments, "Localhost");
        checkCommandLineArg(covManageHistoryArguments, "--port");
        checkCommandLineArg(covManageHistoryArguments, "8080");
        checkCommandLineArg(covManageHistoryArguments, "--stream");
        checkCommandLineArg(covManageHistoryArguments, "TestStream");
        checkCommandLineArg(covManageHistoryArguments, "--ssl");
        checkCommandLineArg(covManageHistoryArguments, "--on-new-cert");
        checkCommandLineArg(covManageHistoryArguments, "trust");
        checkCommandLineArg(covManageHistoryArguments, "--cert");
        checkCommandLineArg(covManageHistoryArguments, "TestCertFile");
        checkCommandLineArg(covManageHistoryArguments, "--user");
        checkCommandLineArg(covManageHistoryArguments, "TestUser");
        checkCommandLineArg(covManageHistoryArguments, "--merge");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covManageHistoryArguments.size());
    }
}
