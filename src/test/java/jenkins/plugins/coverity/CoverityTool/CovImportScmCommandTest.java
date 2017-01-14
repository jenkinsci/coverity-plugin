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

import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.InvocationAssistance;
import jenkins.plugins.coverity.ScmOptionBlock;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovImportScmCommandTest extends CommandTestBase {

//    public ScmOptionBlock(
//            String scmSystem,
//            String customTestTool,
//            String scmToolArguments,
//            String scmCommandArgs,
//            String logFileLoc,
//            String p4Port,
//            String accRevRepo,
//            String scmAdditionalCmd,
//            String fileRegex){

    @Test
    public void CovImportScmCommand_PrepareCommandTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(5, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddCustomTestToolTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", "TestCustomTestTool", StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(7, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");
        checkCommandLineArg(covImportScmArguments, "--tool");
        checkCommandLineArg(covImportScmArguments, "TestCustomTestTool");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddScmToolArgumentsTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, "TestScmToolArguments",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(7, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");
        checkCommandLineArg(covImportScmArguments, "--tool-arg");
        checkCommandLineArg(covImportScmArguments, "TestScmToolArguments");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddScmCommandArgumentsTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                "TestScmCommandArguments", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(7, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");
        checkCommandLineArg(covImportScmArguments, "--command-arg");
        checkCommandLineArg(covImportScmArguments, "TestScmCommandArguments");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddLogFileLocationTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, "TestLogFileLocation", StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(7, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");
        checkCommandLineArg(covImportScmArguments, "--log");
        checkCommandLineArg(covImportScmArguments, "TestLogFileLocation");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddFileRegexTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, "*.java"
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(7, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");
        checkCommandLineArg(covImportScmArguments, "--filename-regex");
        checkCommandLineArg(covImportScmArguments, "*.java");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddAccurevProjectRootTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "accurev", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                "TestAccurrevProjectRoot", StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(7, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "accurev");
        checkCommandLineArg(covImportScmArguments, "--project-root");
        checkCommandLineArg(covImportScmArguments, "TestAccurrevProjectRoot");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddP4PortTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "perforce", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, "1234",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(5, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "perforce");

        assertEquals("1234", envVars.get("P4PORT"));
        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddScmAdditionalCommandTest() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, "AdditionalCommand", StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covImportScmArguments = covImportScmCommand.getCommandLines();

        assertEquals(6, covImportScmArguments.size());

        checkCommandLineArg(covImportScmArguments, "cov-import-scm");
        checkCommandLineArg(covImportScmArguments, "--dir");
        checkCommandLineArg(covImportScmArguments, "TestDir");
        checkCommandLineArg(covImportScmArguments, "--scm");
        checkCommandLineArg(covImportScmArguments, "git");
        checkCommandLineArg(covImportScmArguments, "AdditionalCommand");

        assertEquals(0, covImportScmArguments.size());
    }

    @Test
    public void CovImportScmCommand_AddScmAdditionalCommandTest_WithParseException() {
        mocker.replay();

        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, "\'", StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, scmOptionBlock
        );

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("ParseException occurred during tokenizing the cov import scm additional command.");

        CovCommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
    }
}
