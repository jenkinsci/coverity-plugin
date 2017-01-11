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
import jenkins.plugins.coverity.TaOptionBlock;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovCaptureCommandTest extends CommandTestBase {

    @Test
    public void CovCaptureCommand_TestAdvisorConfigurationTest() {
        mocker.replay();

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, true,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "Jacoco",
                true, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, taOptionBlock, null
        );

        CovCommand covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covCaptureArguments = covCaptureCommand.getCommandLines();

        assertEquals(7, covCaptureArguments.size());

        checkCommandLineArg(covCaptureArguments, "cov-capture");
        checkCommandLineArg(covCaptureArguments, "--dir");
        checkCommandLineArg(covCaptureArguments, "TestDir");
        checkCommandLineArg(covCaptureArguments, "--java-coverage");
        checkCommandLineArg(covCaptureArguments, "Jacoco");
        checkCommandLineArg(covCaptureArguments, "--java-test");
        checkCommandLineArg(covCaptureArguments, "junit");

        assertEquals(0, covCaptureArguments.size());
    }

    @Test
    public void CovCaptureCommand_CustomTestCommandTest() {
        mocker.replay();

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                "CustomTestCommand", false, false, false,
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

        CovCommand covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covCaptureArguments = covCaptureCommand.getCommandLines();

        assertEquals(4, covCaptureArguments.size());

        checkCommandLineArg(covCaptureArguments, "cov-capture");
        checkCommandLineArg(covCaptureArguments, "--dir");
        checkCommandLineArg(covCaptureArguments, "TestDir");
        checkCommandLineArg(covCaptureArguments, "CustomTestCommand");

        assertEquals(0, covCaptureArguments.size());
    }

    @Test
    public void CovCaptureCommand_CustomTestCommandTest_WithParseException() {
        mocker.replay();

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                "\'", false, false, false,
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
        expectedException.expectMessage("ParseException occurred during tokenizing the cov capture custom test command.");

        CovCommand covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
    }
}
