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
import jenkins.plugins.coverity.InvocationAssistance;
import jenkins.plugins.coverity.TaOptionBlock;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.Utils.InvocationAssistanceBuilder;
import jenkins.plugins.coverity.Utils.TaOptionBlockBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;

public class CovBuildScriptCommandTest extends CommandTestBase {

    @Test
    public void commandForScriptSourcesTest() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsScriptSrc(true).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covBuildScriptCommand = new CovBuildScriptCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--no-command", "--fs-capture-search", "$WORKSPACE"});
        covBuildScriptCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments for script sources: " + actualArguments.toString());
    }

    @Test
    public void commandForCompileSourcesTest_WithCaptureScriptSources() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsScriptSrc(true).withIsScriptSrc(true).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();
        Command covBuildScriptCommand = new CovBuildScriptCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--no-command", "--fs-capture-search", "$WORKSPACE"});

        covBuildScriptCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments for script sources: " + actualArguments.toString());
    }

    @Test
    public void commandForCompileSourcesTest_WithAdditionalBuildArguments() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsScriptSrc(true)
                .withBuildArguments("AdditionalBuildArguments").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covBuildScriptCommand = new CovBuildScriptCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--no-command", "--fs-capture-search", "$WORKSPACE", "AdditionalBuildArguments"});
        covBuildScriptCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments for script sources: " + actualArguments.toString());
    }

    @Test
    public void doesNotExecute_NoInvocationAssistance() throws IOException, InterruptedException {
        CoverityPublisher publisher = new CoverityPublisherBuilder().build();

        Command covBuildScriptCommand = new CovBuildScriptCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covBuildScriptCommand.runCommand();
        verifyNumberOfExecutedCommands(0);
    }

    @Test
    public void doesNotExecute_NoCaptureForScriptingCodes() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covBuildScriptCommand = new CovBuildScriptCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covBuildScriptCommand.runCommand();
        verifyNumberOfExecutedCommands(0);
    }
}
