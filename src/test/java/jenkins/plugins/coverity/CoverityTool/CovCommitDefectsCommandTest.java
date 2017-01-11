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

import jenkins.plugins.coverity.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

public class CovCommitDefectsCommandTest extends CommandTestBase {

    @Test
    public void CovCommitDefectsCommand_PrepareCommandTest() {
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

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covCommitDefectsArguments = covCommitDefectsCommand.getCommandLines();

        assertEquals(11, covCommitDefectsArguments.size());

        checkCommandLineArg(covCommitDefectsArguments, "cov-commit-defects");
        checkCommandLineArg(covCommitDefectsArguments, "--dir");
        checkCommandLineArg(covCommitDefectsArguments, "TestDir");
        checkCommandLineArg(covCommitDefectsArguments, "--host");
        checkCommandLineArg(covCommitDefectsArguments, "Localhost");
        checkCommandLineArg(covCommitDefectsArguments, "--port");
        checkCommandLineArg(covCommitDefectsArguments, "8080");
        checkCommandLineArg(covCommitDefectsArguments, "--stream");
        checkCommandLineArg(covCommitDefectsArguments, "TestStream");
        checkCommandLineArg(covCommitDefectsArguments, "--user");
        checkCommandLineArg(covCommitDefectsArguments, "TestUser");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covCommitDefectsArguments.size());
    }

    @Test
    public void CovCommitDefectsCommand_AddDataPortTest() {
        mocker.replay();

        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 1234);

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

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covCommitDefectsArguments = covCommitDefectsCommand.getCommandLines();

        assertEquals(11, covCommitDefectsArguments.size());

        checkCommandLineArg(covCommitDefectsArguments, "cov-commit-defects");
        checkCommandLineArg(covCommitDefectsArguments, "--dir");
        checkCommandLineArg(covCommitDefectsArguments, "TestDir");
        checkCommandLineArg(covCommitDefectsArguments, "--host");
        checkCommandLineArg(covCommitDefectsArguments, "Localhost");
        checkCommandLineArg(covCommitDefectsArguments, "--dataport");
        checkCommandLineArg(covCommitDefectsArguments, "1234");
        checkCommandLineArg(covCommitDefectsArguments, "--stream");
        checkCommandLineArg(covCommitDefectsArguments, "TestStream");
        checkCommandLineArg(covCommitDefectsArguments, "--user");
        checkCommandLineArg(covCommitDefectsArguments, "TestUser");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covCommitDefectsArguments.size());
    }

    @Test
    public void CovCommitDefectsCommand_AddDataPortTest_WithSslConfiguration() {

        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 1234);

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mocker.createMock(CoverityPublisher.DescriptorImpl.class);


        CoverityPublisher publisher = mocker.createMock(CoverityPublisher.class);
        expect(publisher.getDescriptor()).andReturn(descriptor);
        expect(publisher.getCimStreams()).andReturn(cimStreamList);
        expect(publisher.getInvocationAssistance()).andReturn(invocationAssistance);
        expect(publisher.getInvocationAssistance()).andReturn(invocationAssistance);
        expect(descriptor.getSslConfigurations()).andReturn(sslConfigurations);
        mocker.replay();

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covCommitDefectsArguments = covCommitDefectsCommand.getCommandLines();

        assertEquals(16, covCommitDefectsArguments.size());

        checkCommandLineArg(covCommitDefectsArguments, "cov-commit-defects");
        checkCommandLineArg(covCommitDefectsArguments, "--dir");
        checkCommandLineArg(covCommitDefectsArguments, "TestDir");
        checkCommandLineArg(covCommitDefectsArguments, "--host");
        checkCommandLineArg(covCommitDefectsArguments, "Localhost");
        checkCommandLineArg(covCommitDefectsArguments, "--dataport");
        checkCommandLineArg(covCommitDefectsArguments, "1234");
        checkCommandLineArg(covCommitDefectsArguments, "--stream");
        checkCommandLineArg(covCommitDefectsArguments, "TestStream");
        checkCommandLineArg(covCommitDefectsArguments, "--ssl");
        checkCommandLineArg(covCommitDefectsArguments, "--on-new-cert");
        checkCommandLineArg(covCommitDefectsArguments, "trust");
        checkCommandLineArg(covCommitDefectsArguments, "--cert");
        checkCommandLineArg(covCommitDefectsArguments, "TestCertFile");
        checkCommandLineArg(covCommitDefectsArguments, "--user");
        checkCommandLineArg(covCommitDefectsArguments, "TestUser");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covCommitDefectsArguments.size());
    }

    @Test
    public void CovCommitDefectsCommand_AddHttpsPortTest() {
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


        CoverityPublisher publisher = mocker.createMock(CoverityPublisher.class);
        expect(publisher.getDescriptor()).andReturn(descriptor);
        expect(publisher.getCimStreams()).andReturn(cimStreamList);
        expect(publisher.getInvocationAssistance()).andReturn(invocationAssistance);
        expect(publisher.getInvocationAssistance()).andReturn(invocationAssistance);
        expect(descriptor.getSslConfigurations()).andReturn(sslConfigurations);
        mocker.replay();

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covCommitDefectsArguments = covCommitDefectsCommand.getCommandLines();

        assertEquals(16, covCommitDefectsArguments.size());

        checkCommandLineArg(covCommitDefectsArguments, "cov-commit-defects");
        checkCommandLineArg(covCommitDefectsArguments, "--dir");
        checkCommandLineArg(covCommitDefectsArguments, "TestDir");
        checkCommandLineArg(covCommitDefectsArguments, "--host");
        checkCommandLineArg(covCommitDefectsArguments, "Localhost");
        checkCommandLineArg(covCommitDefectsArguments, "--https-port");
        checkCommandLineArg(covCommitDefectsArguments, "8080");
        checkCommandLineArg(covCommitDefectsArguments, "--stream");
        checkCommandLineArg(covCommitDefectsArguments, "TestStream");
        checkCommandLineArg(covCommitDefectsArguments, "--ssl");
        checkCommandLineArg(covCommitDefectsArguments, "--on-new-cert");
        checkCommandLineArg(covCommitDefectsArguments, "trust");
        checkCommandLineArg(covCommitDefectsArguments, "--cert");
        checkCommandLineArg(covCommitDefectsArguments, "TestCertFile");
        checkCommandLineArg(covCommitDefectsArguments, "--user");
        checkCommandLineArg(covCommitDefectsArguments, "TestUser");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covCommitDefectsArguments.size());
    }

    @Test
    public void CovCommitDefectsCommand_AddCommitArgumentsTest() {
        mocker.replay();

        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, "AdditionalCommitArguments", StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                cimStreamList, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        List<String> covCommitDefectsArguments = covCommitDefectsCommand.getCommandLines();

        assertEquals(12, covCommitDefectsArguments.size());

        checkCommandLineArg(covCommitDefectsArguments, "cov-commit-defects");
        checkCommandLineArg(covCommitDefectsArguments, "--dir");
        checkCommandLineArg(covCommitDefectsArguments, "TestDir");
        checkCommandLineArg(covCommitDefectsArguments, "--host");
        checkCommandLineArg(covCommitDefectsArguments, "Localhost");
        checkCommandLineArg(covCommitDefectsArguments, "--port");
        checkCommandLineArg(covCommitDefectsArguments, "8080");
        checkCommandLineArg(covCommitDefectsArguments, "--stream");
        checkCommandLineArg(covCommitDefectsArguments, "TestStream");
        checkCommandLineArg(covCommitDefectsArguments, "--user");
        checkCommandLineArg(covCommitDefectsArguments, "TestUser");
        checkCommandLineArg(covCommitDefectsArguments, "AdditionalCommitArguments");

        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));

        assertEquals(0, covCommitDefectsArguments.size());
    }

    @Test
    public void CovCommitDefectsCommand_AddCommitArgumentsTest_WithParseException() {
        mocker.replay();

        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, "\'", StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                cimStreamList, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("ParseException occurred during tokenizing the cov-commit-defect commit arguments.");

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
    }
}
