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
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.TaOptionBlock;
import jenkins.plugins.coverity.Utils.TaOptionBlockBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CovCaptureCommandTest extends CommandTestBase {

    @Test
    public void addTAdvisorConfigurationTest() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock =
                new TaOptionBlockBuilder().
                        withJavaOptionBlock(true).
                        withJavaCoverageTool("Jacoco").
                        withJunitFramework(true).
                        withCustomTestCommand("CustomTestCommand").
                        build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-capture", "--dir", "TestDir", "--java-coverage", "Jacoco", "--java-test", "junit", "CustomTestCommand"});
        covCaptureCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-capture command line arguments: " + actualArguments.toString());
    }

    @Test
    public void customTestCommandTest() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCustomTestCommand("CustomTestCommand").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        setExpectedArguments(new String[] {"cov-capture", "--dir", "TestDir", "CustomTestCommand"});
        covCaptureCommand.runCommand();
        consoleLogger.verifyLastMessage("[Coverity] cov-capture command line arguments: " + actualArguments.toString());
    }

    @Test
    public void customTestCommandTest_WithParseException() throws IOException, InterruptedException {
        TaOptionBlock taOptionBlock = new TaOptionBlockBuilder().withCustomTestCommand("\'").build();
        CoverityPublisher publisher = new CoverityPublisherBuilder().withTaOptionBlock(taOptionBlock).build();

        Command covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covCaptureCommand.runCommand();
            fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov capture custom test command.", e.getMessage());
        }
    }
}
