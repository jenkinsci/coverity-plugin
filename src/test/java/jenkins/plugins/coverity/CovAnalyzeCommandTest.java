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

import jenkins.plugins.coverity.analysis.CommandFactory;
import jenkins.plugins.coverity.analysis.ICovCommand;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovAnalyzeCommandTest extends CommandTestBase {

    @Test
    public void CovAnalyzeCommand_MisraConfigurationTest() throws IOException {
        mocker.replay();

        File misraConfigFile = new File("misraConfigFile");
        misraConfigFile.createNewFile();

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                true, misraConfigFile.getPath(), StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        ICovCommand covAnalyzeCommand = CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
        List<String> covAnalyzeArguments = covAnalyzeCommand.getCommandLines();

        assertEquals(5, covAnalyzeArguments.size());

        checkCommandLineArg(covAnalyzeArguments, "cov-analyze");
        checkCommandLineArg(covAnalyzeArguments, "--dir");
        checkCommandLineArg(covAnalyzeArguments, "TestDir");
        checkCommandLineArg(covAnalyzeArguments, "--misra-config");
        checkCommandLineArg(covAnalyzeArguments, misraConfigFile.getPath());

        assertEquals(0, covAnalyzeArguments.size());

        misraConfigFile.delete();
    }

    @Test
    public void CovAnalyzeCommand_MisraConfigurationTest_WithEmptyMisraConfigFilePath() {
        mocker.replay();

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                true, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Misra configuration file is required to run Misra analysis.");

        CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
    }

    @Test
    public void CovAnalyzeCommand_MisraConfigurationTest_WithInvalidMisraConfigFilePath() {
        mocker.replay();

        File misraConfigFile = new File("misraConfigFile");

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                true, misraConfigFile.getPath(), StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Could not find MISRA configuration file at \"" + misraConfigFile.getAbsolutePath() + "\"");

        CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
    }

    @Test
    public void CovAnalyzeCommand_AdditionalArgumentsTest() {
        mocker.replay();

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, "additionalArgs", StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        ICovCommand covAnalyzeCommand = CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
        List<String> covAnalyzeArguments = covAnalyzeCommand.getCommandLines();

        assertEquals(4, covAnalyzeArguments.size());

        checkCommandLineArg(covAnalyzeArguments, "cov-analyze");
        checkCommandLineArg(covAnalyzeArguments, "--dir");
        checkCommandLineArg(covAnalyzeArguments, "TestDir");
        checkCommandLineArg(covAnalyzeArguments, "additionalArgs");

        assertEquals(0, covAnalyzeArguments.size());
    }

    @Test
    public void CovAnalyzeCommand_AdditionalArgumentsTest_WithParseException() {
        mocker.replay();

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, "\'", StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("ParseException occurred during tokenizing the cov analyze additional arguments.");

        CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
    }

    @Test
    public void CovAnalyzeCommand_TestAdvisorConfigurationTest_WithEmptyStripPath() throws IOException {
        mocker.replay();

        File taPolicyFile = new File("taPolicyFile");
        taPolicyFile.createNewFile();

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, false, false,
                taPolicyFile.getPath(), "Path2Strip",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, taOptionBlock, null
        );

        ICovCommand covAnalyzeCommand = CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
        List<String> covAnalyzeArguments = covAnalyzeCommand.getCommandLines();

        assertEquals(8, covAnalyzeArguments.size());

        checkCommandLineArg(covAnalyzeArguments, "cov-analyze");
        checkCommandLineArg(covAnalyzeArguments, "--dir");
        checkCommandLineArg(covAnalyzeArguments, "TestDir");
        checkCommandLineArg(covAnalyzeArguments, "--test-advisor");
        checkCommandLineArg(covAnalyzeArguments, "--test-advisor-policy");
        checkCommandLineArg(covAnalyzeArguments, taPolicyFile.getPath());
        checkCommandLineArg(covAnalyzeArguments, "--strip-path");
        checkCommandLineArg(covAnalyzeArguments, "Path2Strip");

        assertEquals(0, covAnalyzeArguments.size());

        taPolicyFile.delete();
    }

    @Test
    public void CovAnalyzeCommand_TestAdvisorConfigurationTest_WithEmptyTaPolicyFilePath() {
        mocker.replay();

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, taOptionBlock, null
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Test Advisor Policy File is required to run the Test Advisor.");

        CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
    }

    @Test
    public void CovAnalyzeCommand_TestAdvisorConfigurationTest_WithInvalidTaPolicyFilePath() {
        mocker.replay();

        File taPolicyFile = new File("taPolicyFile");

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, false, false,
                taPolicyFile.getPath(), "Path2Strip",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, taOptionBlock, null
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Could not find test policy file at \"" + taPolicyFile.getAbsolutePath() + "\"");

        CommandFactory.getCovAnalyzeCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
    }
}
