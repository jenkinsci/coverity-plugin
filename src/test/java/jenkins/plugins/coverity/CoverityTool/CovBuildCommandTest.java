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
import jenkins.plugins.coverity.TaOptionBlock;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovBuildCommandTest extends CommandTestBase {

    @Test
    public void CovBuildCommand_CommandForScriptSourcesTest() {
        mocker.replay();

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, listener, null, StringUtils.EMPTY, false, envVars);
        List<String> covBuildArguments = covBuildCommand.getCommandLines();

        assertEquals(6, covBuildArguments.size());

        checkCommandLineArg(covBuildArguments, "cov-build");
        checkCommandLineArg(covBuildArguments, "--dir");
        checkCommandLineArg(covBuildArguments, "TestDir");
        checkCommandLineArg(covBuildArguments, "--no-command");
        checkCommandLineArg(covBuildArguments, "--fs-capture-search");
        checkCommandLineArg(covBuildArguments, "$WORKSPACE");

        assertEquals(0, covBuildArguments.size());
    }

    @Test
    public void CovBuildCommand_CommandForCompileSourcesTest_WithCaptureScriptSources() {
        mocker.replay();

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, true, true,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, true, envVars);
        List<String> covBuildArguments = covBuildCommand.getCommandLines();

        assertEquals(5, covBuildArguments.size());

        checkCommandLineArg(covBuildArguments, "cov-build");
        checkCommandLineArg(covBuildArguments, "--dir");
        checkCommandLineArg(covBuildArguments, "TestDir");
        checkCommandLineArg(covBuildArguments, "--fs-capture-search");
        checkCommandLineArg(covBuildArguments, "$WORKSPACE");

        assertEquals(0, covBuildArguments.size());
    }

    @Test
    public void CovBuildCommand_CommandForCompileSourcesTest_WithTaOptions() {
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
}
