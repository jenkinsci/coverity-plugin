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

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, true,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                null, null
        );

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
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
                null, null
        );

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
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

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, true, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );

        TaOptionBlock taOptionBlock = new TaOptionBlock(
                StringUtils.EMPTY, false, false, true,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "Jacoco",
                true, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false
        );

        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                taOptionBlock, null
        );

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covBuildArguments = covBuildCommand.getCommandLines();

        assertEquals(7, covBuildArguments.size());

        checkCommandLineArg(covBuildArguments, "cov-build");
        checkCommandLineArg(covBuildArguments, "--dir");
        checkCommandLineArg(covBuildArguments, "TestDir");
        checkCommandLineArg(covBuildArguments, "--java-coverage");
        checkCommandLineArg(covBuildArguments, "Jacoco");
        checkCommandLineArg(covBuildArguments, "--java-test");
        checkCommandLineArg(covBuildArguments, "junit");

        assertEquals(0, covBuildArguments.size());
    }

    @Test
    public void CovBuildCommand_CommandForCompileSourcesTest_WithAdditionalBuildArguments() {
        mocker.replay();

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, true, false,
                "AdditionalBuildArguments", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, null, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                null, null
        );

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        List<String> covBuildArguments = covBuildCommand.getCommandLines();

        assertEquals(4, covBuildArguments.size());

        checkCommandLineArg(covBuildArguments, "cov-build");
        checkCommandLineArg(covBuildArguments, "--dir");
        checkCommandLineArg(covBuildArguments, "TestDir");
        checkCommandLineArg(covBuildArguments, "AdditionalBuildArguments");

        assertEquals(0, covBuildArguments.size());
    }
}
