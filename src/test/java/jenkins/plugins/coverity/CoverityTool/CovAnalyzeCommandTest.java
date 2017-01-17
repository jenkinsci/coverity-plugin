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

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

public class CovAnalyzeCommandTest extends CommandTestBase {

    @Test
    public void addMisraConfigurationTest() throws IOException, InterruptedException {
        File misraConfigFile = new File("misraConfigFile");
        try{
            misraConfigFile.createNewFile();

            InvocationAssistance invocationAssistance =
                    new InvocationAssistanceBuilder().
                            withUsingMisra(true).
                            withMisraConfigFile(misraConfigFile.getPath()).build();
            CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

            Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
            setExpectedArguments(new String[] {"cov-analyze", "--dir", "TestDir", "--misra-config", misraConfigFile.getPath()});
            covAnalyzeCommand.runCommand();
            consoleLogger.verifyLastMessage("[Coverity] cov-analyze command line arguments: " + actualArguments.toString());
        }finally {
            misraConfigFile.delete();
        }
    }

    @Test
    public void addMisraConfigurationTest_WithEmptyMisraConfigFilePath() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withUsingMisra(true).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covAnalyzeCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch(RuntimeException e) {
            assertEquals("Misra configuration file is required to run Misra analysis.", e.getMessage());
        }
    }

    @Test
    public void addMisraConfigurationTest_WithInvalidMisraConfigFilePath() throws IOException, InterruptedException {
        File misraConfigFile = new File("misraConfigFile");

        InvocationAssistance invocationAssistance =
                new InvocationAssistanceBuilder().
                        withUsingMisra(true).
                        withMisraConfigFile(misraConfigFile.getPath()).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covAnalyzeCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("Could not find MISRA configuration file at \"" + misraConfigFile.getAbsolutePath() + "\"", e.getMessage());
        }
    }

    @Test
    public void additionalArgumentsTest() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withAnalyzeArguments("additionalArgs").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-analyze", "--dir", "TestDir", "additionalArgs"});
        covAnalyzeCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-analyze command line arguments: " + actualArguments.toString());
    }

    @Test
    public void additionalArgumentsTest_WithParseException() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withAnalyzeArguments("\'").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covAnalyzeCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch(RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov analyze additional arguments.", e.getMessage());
        }
    }

    @Test
    public void addTestAdvisorConfigurationTest_WithEmptyStripPath() throws IOException, InterruptedException {
        File taPolicyFile = new File("taPolicyFile");
        try{
            taPolicyFile.createNewFile();

            TaOptionBlock taOptionBlock = new TaOptionBlock(
                    StringUtils.EMPTY, false, false, false,
                    StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                    StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                    false, false, false,
                    taPolicyFile.getPath(), "Path2Strip",
                    StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
            );
            CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

            Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
            setExpectedArguments(new String[] {"cov-analyze", "--dir", "TestDir", "--test-advisor", "--test-advisor-policy", taPolicyFile.getPath(), "--strip-path", "Path2Strip"});
            covAnalyzeCommand.runCommand();
            consoleLogger.verifyLastMessage("[Coverity] cov-analyze command line arguments: " + actualArguments.toString());
        }finally {
            taPolicyFile.delete();
        }
    }

    @Test
    public void addTestAdvisorConfigurationTest_WithEmptyTaPolicyFilePath() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covAnalyzeCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch(RuntimeException e) {
            assertEquals("Test Advisor Policy File is required to run the Test Advisor.", e.getMessage());
        }
    }

    @Test
    public void addTestAdvisorConfigurationTest_WithInvalidTaPolicyFilePath() throws IOException, InterruptedException {
        File taPolicyFile = new File("taPolicyFile");

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, false, false,
                taPolicyFile.getPath(), "Path2Strip",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covAnalyzeCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch(RuntimeException e) {
            assertEquals("Could not find test policy file at \"" + taPolicyFile.getAbsolutePath() + "\"", e.getMessage());
        }
    }

    @Test
    public void cannotExecuteTest() throws IOException, InterruptedException {
        CoverityPublisher publisher = new CoverityPublisherBuilder().build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covAnalyzeCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] Skipping command because it can't be executed");
    }
}
