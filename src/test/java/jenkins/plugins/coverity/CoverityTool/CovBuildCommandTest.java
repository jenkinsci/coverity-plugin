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

public class CovBuildCommandTest extends CommandTestBase {

    @Test
    public void commandForScriptSourcesTest() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsScriptSrc(true).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--no-command", "--fs-capture-search", "$WORKSPACE"});
        covBuildCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments: " + actualArguments.toString());
    }

    @Test
    public void commandForCompileSourcesTest_WithCaptureScriptSources() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsScriptSrc(true).withIsCompiledSrc(true).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--fs-capture-search", "$WORKSPACE"});
        covBuildCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments: " + actualArguments.toString());
    }

    @Test
    public void commandForCompileSourcesTest_WithTaOptions() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsCompiledSrc(true).build();
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withJavaOptionBlock(true).withJavaCoverageTool("Jacoco").withJunitFramework(true).build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).
                withTaOptionBlock(taOptionBlock).build();

        Command covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "--java-coverage", "Jacoco", "--java-test", "junit"});
        covBuildCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments: " + actualArguments.toString());
    }

    @Test
    public void commandForCompileSourcesTest_WithAdditionalBuildArguments() throws IOException, InterruptedException {
        InvocationAssistance invocationAssistance = new InvocationAssistanceBuilder().withIsCompiledSrc(true)
                .withBuildArguments("AdditionalBuildArguments").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withInvocationAssistance(invocationAssistance).build();

        Command covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-build", "--dir", "TestDir", "AdditionalBuildArguments"});
        covBuildCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-build command line arguments: " + actualArguments.toString());
    }

    @Test
    public void cannotExecuteTest() throws IOException, InterruptedException {
        CoverityPublisher publisher = new CoverityPublisherBuilder().build();

        Command covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        covBuildCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] Skipping command because it can't be executed");
    }
}
