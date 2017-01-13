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
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CovCaptureCommandTest extends CommandTestBase {

    @Test
    public void CovCaptureCommand_TestAdvisorConfigurationTest() throws IOException, InterruptedException {
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
        setExpectedArguments(new String[] {"cov-capture", "--dir", "TestDir", "--java-coverage", "Jacoco", "--java-test", "junit"});
        covCaptureCommand.runCommand();
    }

    @Test
    public void CovCaptureCommand_CustomTestCommandTest() throws IOException, InterruptedException {
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
        setExpectedArguments(new String[] {"cov-capture", "--dir", "TestDir", "CustomTestCommand"});
        covCaptureCommand.runCommand();
    }

    @Test
    public void CovCaptureCommand_CustomTestCommandTest_WithParseException() throws IOException, InterruptedException {
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

        CovCommand covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars);
        try{
            covCaptureCommand.runCommand();
            Assert.fail("RuntimeException should have been thrown");
        }catch (RuntimeException e) {
            assertEquals("ParseException occurred during tokenizing the cov capture custom test command.", e.getMessage());
        }
    }
}
