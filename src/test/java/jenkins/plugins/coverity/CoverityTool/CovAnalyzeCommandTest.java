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
import jenkins.plugins.coverity.Utils.TaOptionBlockBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

public class CovAnalyzeCommandTest extends CommandTestBase {

    @Test
    public void addMisraConfigurationTest() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance =
                new InvocationAssistanceBuilder().
                        withMisraConfigFile("misraConfigFile").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-analyze", "--dir", "TestDir", "--misra-config", "misraConfigFile"});
        covAnalyzeCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-analyze command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addMisraConfigurationTest_WithEmptyMisraConfigFilePath() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withMisraConfigFile("").build();
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
    public void addTestAdvisorConfigurationTest_WithMultipleStripPaths() throws IOException, InterruptedException {
        List<TaStripPath> taStripPathList = new ArrayList<TaStripPath>();
        TaStripPath path1 = new TaStripPath("StripPath1");
        TaStripPath path2 = new TaStripPath("StripPath2");
        taStripPathList.add(path1);
        taStripPathList.add(path2);

        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withPolicyFile("taPolicyFile").withStripPath(taStripPathList).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-analyze", "--dir", "TestDir", "--test-advisor", "--test-advisor-policy", "taPolicyFile",
                "--strip-path", "StripPath1", "--strip-path", "StripPath2"});
        covAnalyzeCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-analyze command line arguments: " + actualArguments.toString());
    }

    @Test
    public void addTestAdvisorConfigurationTest_WithEmptyTaPolicyFilePath() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().build();
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
    public void canExecuteTest() throws IOException, InterruptedException {
        CoverityPublisher publisher = new CoverityPublisherBuilder().build();
        Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covAnalyzeCommand.runCommand();
        verifyNumberOfExecutedCommands(0);
    }
}
