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
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.Utils.InvocationAssistanceBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CovCommitDefectsCommandTest extends CommandTestBase {

    @Test
    public void prepareCommandTest() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().withCimStreams(cimStreamList).
                        withInvocationAssistance(invocationAssistance).build();

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addMisraTest() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withUsingMisra(true).withMisraConfigFile("MISRA_cpp1008_1.config").build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().withCimStreams(cimStreamList).
                        withInvocationAssistance(invocationAssistance).build();

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_INDIO);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--user", "TestUser", "--misra-only"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addDataPortTest() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 1234);
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().withCimStreams(cimStreamList).
                        withInvocationAssistance(invocationAssistance).build();

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--dataport", "1234", "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addDataPortTest_WithSslConfiguration() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 1234);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = mock(CoverityPublisher.class);

        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStreams()).thenReturn(cimStreamList);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--ssl", "--dataport", "1234", "--on-new-cert", "trust", "--cert", "TestCertFile",
                 "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addDataPortTest_WithIndio() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 1234);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = mock(CoverityPublisher.class);

        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStreams()).thenReturn(cimStreamList);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_INDIO);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost", "--dataport", "1234",
                "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addHttpsPortTest() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = mock(CoverityPublisher.class);

        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStreams()).thenReturn(cimStreamList);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--ssl", "--https-port", "8080", "--on-new-cert", "trust", "--cert", "TestCertFile",
                "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addHttpsPortTest_WithIndio() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", true, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        SSLConfigurations sslConfigurations = new SSLConfigurations(true, null);
        sslConfigurations.setCertFileName("TestCertFile");

        CoverityPublisher.DescriptorImpl descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        CoverityPublisher publisher = mock(CoverityPublisher.class);

        when(publisher.getDescriptor()).thenReturn(descriptor);
        when(publisher.getCimStreams()).thenReturn(cimStreamList);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(publisher.getInvocationAssistance()).thenReturn(invocationAssistance);
        when(descriptor.getSslConfigurations()).thenReturn(sslConfigurations);

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_INDIO);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost", "--https-port", "8080",
                "--stream", "TestStream", "--user", "TestUser"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addCommitArgumentsTest() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);

        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withCommitArguments("AdditionalCommitArguments").build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().withCimStreams(cimStreamList).
                        withInvocationAssistance(invocationAssistance).build();

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        setExpectedArguments(new String[] {
                "cov-commit-defects", "--dir", "TestDir", "--host", "Localhost",
                "--port", "8080", "--stream", "TestStream", "--user", "TestUser", "AdditionalCommitArguments"
        });
        covCommitDefectsCommand.runCommand();
        assertEquals("TestPassword", envVars.get("COVERITY_PASSPHRASE"));
        consoleLogger.verifyLastMessage("[Coverity] cov-commit-defects command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addCommitArgumentsTest_WithParseException() throws IOException, InterruptedException {
        CIMStream cimStream = new CIMStream("TestInstance", "TestProject", "TestStream", null, "TestId", null);
        List<CIMStream> cimStreamList = new ArrayList<>();
        cimStreamList.add(cimStream);

        CIMInstance cimInstance = new CIMInstance("TestInstance", "Localhost", 8080, "TestUser", "TestPassword", false, 0);
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withCommitArguments("\'").build();
        CoverityPublisher publisher =
                new CoverityPublisherBuilder().withCimStreams(cimStreamList).
                        withInvocationAssistance(invocationAssistance).build();

        Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, cimStream, cimInstance, CoverityVersion.VERSION_JASPER);
        try{
            covCommitDefectsCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov-commit-defect commit arguments.", e.getMessage());
        }
    }
}
