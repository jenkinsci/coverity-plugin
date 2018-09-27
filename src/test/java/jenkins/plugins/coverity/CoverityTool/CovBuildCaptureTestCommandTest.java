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
package jenkins.plugins.coverity.CoverityTool;

import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.TaOptionBlock;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.Utils.TaOptionBlockBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CovBuildCaptureTestCommandTest extends CommandTestBase {

    @Test
    public void addTAdvisorConfigurationTest_Java() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock =
                new TaOptionBlockBuilder().
                        withJavaOptionBlock(true).
                        withJavaCoverageTool("Jacoco").
                        withJunitFramework(true).
                        withCustomTestCommand("CustomTestCommand").
                        withJunit4Framework(true).
                        build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covBuildCaptureTestCommand = new CovBuildCaptureTestCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--test-capture", "--java-coverage",
                "Jacoco", "--java-test", "junit", "--java-test", "junit4", "CustomTestCommand"});
        covBuildCaptureTestCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build capture test command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addTAdvisorConfigurationTest_Cxx() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock =
                new TaOptionBlockBuilder().
                        withCoptionBlock(true).
                        withCxxCoverageTool("bullseye").
                        withBullsEyeDir("bullseyeDir").
                        withCustomTestCommand("CustomTestCommand").
                        build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covBuildCaptureTestCommand = new CovBuildCaptureTestCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--test-capture", "--c-coverage", "bullseye", "--bullseye-dir", "bullseyeDir", "CustomTestCommand"});
        covBuildCaptureTestCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build capture test command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addTAdvisorConfigurationTest_Cs() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock =
                new TaOptionBlockBuilder().
                        withCsOptionBlock(true).
                        withCsCoverageTool("opencover").
                        withCsFramework("nunit").
                        withCustomTestCommand("CustomTestCommand").
                        build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covBuildCaptureTestCommand = new CovBuildCaptureTestCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--test-capture", "--cs-coverage", "opencover", "--cs-test", "nunit", "CustomTestCommand"});
        covBuildCaptureTestCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build capture test command line arguments: " + actualArguments.toString());
    }

    @Test
    public void customTestCommandTest() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCustomTestCommand("CustomTestCommand").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covBuildCaptureTestCommand = new CovBuildCaptureTestCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--test-capture", "CustomTestCommand"});
        covBuildCaptureTestCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build capture test command line arguments: " + actualArguments.toString());
    }

    @Test
    public void customTestCommandTest_WithParseException() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCustomTestCommand("\'").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covBuildCaptureTestCommand = new CovBuildCaptureTestCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covBuildCaptureTestCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov capture custom test command.", e.getMessage());
        }
    }

    @Test
    public void doesNotExecute_WithNoTaOptionBlock() throws IOException, InterruptedException {
        CoverityPublisher publisher = new CoverityPublisherBuilder().build();

        Command covBuildCaptureTestCommand = new CovBuildCaptureTestCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covBuildCaptureTestCommand.runCommand();
        verifyNumberOfExecutedCommands(0);
    }

    @Test
    public void doesNotExecute_WithNoCustomTestCommand() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covCaptureCommand.runCommand();
        verifyNumberOfExecutedCommands(0);
    }
}
