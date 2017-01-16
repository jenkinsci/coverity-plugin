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
import jenkins.plugins.coverity.ScmOptionBlock;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovImportScmCommandTest extends CommandTestBase {

    @Test
    public void CovImportScmCommand_PrepareCommandTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddCustomTestToolTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", "TestCustomTestTool", StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git", "--tool", "TestCustomTestTool"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddScmToolArgumentsTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, "TestScmToolArguments",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git", "--tool-arg", "TestScmToolArguments"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddScmCommandArgumentsTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                "TestScmCommandArguments", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git", "--command-arg", "TestScmCommandArguments"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddLogFileLocationTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, "TestLogFileLocation", StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git", "--log", "TestLogFileLocation"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddFileRegexTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, "*.java"
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git", "--filename-regex", "*.java"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddAccurevProjectRootTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "accurev", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                "TestAccurrevProjectRoot", StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "accurev", "--project-root", "TestAccurrevProjectRoot"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddP4PortTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "perforce", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, "1234",
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "perforce"});
        covImportScmCommand.runCommand();
        assertEquals("1234", envVars.get("P4PORT"));
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddScmAdditionalCommandTest() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, "AdditionalCommand", StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-import-scm", "--dir", "TestDir", "--scm", "git", "AdditionalCommand"});
        covImportScmCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-import-scm command line arguments: " + actualArguments.toString());
    }

    @Test
    public void CovImportScmCommand_AddScmAdditionalCommandTest_WithParseException() throws IOException, InterruptedException {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(
                "git", StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, "\'", StringUtils.EMPTY
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, scmOptionBlock
        );

        ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covImportScmCommand.runCommand();
            Assert.fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov import scm additional command.", e.getMessage());
        }
    }
}
