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
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CovCommitDefectsCommandTest extends CommandTestBase {

    @Test
    public void CovCommitDefectsCommand_PrepareCommandTest() throws IOException, InterruptedException {
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
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
    }

    @Test
    public void CovCommitDefectsCommand_AddDataPortTest() throws IOException, InterruptedException {
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
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--dataport", "1234", "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
    }

    @Test
    public void CovCommitDefectsCommand_AddDataPortTest_WithSslConfiguration() throws IOException, InterruptedException {
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

        CoverityPublisher.DescriptorImpl descriptor = Mockito.mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = Mockito.mock(CoverityPublisher.class);

        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStreams()).thenReturn(cimStreamList);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--dataport", "1234", "--ssl", "--on-new-cert", "trust", "--cert", "TestCertFile",
                 "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
    }

    @Test
    public void CovCommitDefectsCommand_AddHttpsPortTest() throws IOException, InterruptedException {
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

        CoverityPublisher.DescriptorImpl descriptor = Mockito.mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = Mockito.mock(CoverityPublisher.class);

        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStreams()).thenReturn(cimStreamList);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--https-port", "8080", "--ssl", "--on-new-cert", "trust", "--cert", "TestCertFile",
                "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
    }

    @Test
    public void CovCommitDefectsCommand_AddCommitArgumentsTest() throws IOException, InterruptedException {
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
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--user", "TestUser", "AdditionalCommitArguments"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
    }

    @Test
    public void CovCommitDefectsCommand_AddCommitArgumentsTest_WithParseException() throws IOException, InterruptedException {
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

        CovCommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        try{
            covCommitDefectsCommand.runCommand();
            Assert.fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov-commit-defect commit arguments.", e.getMessage());
        }
    }
}
