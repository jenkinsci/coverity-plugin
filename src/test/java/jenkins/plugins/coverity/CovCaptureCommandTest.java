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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CovCaptureCommandTest extends CommandTestBase {

    @Test
    public void CovCaptureCommand_IntermediateDirTest(){
        mocker.replay();

        ICovCommand covCaptureCommand = CommandFactory.getCovCaptureCommand(build, launcher, buildListener, null, StringUtils.EMPTY);
        List<String> covCaptureArguments = covCaptureCommand.getCommandLines();

        assertEquals(3, covCaptureArguments.size());

        assertTrue(covCaptureArguments.contains("cov-capture"));
        covCaptureArguments.remove("cov-capture");

        assertTrue(covCaptureArguments.contains("--dir"));
        covCaptureArguments.remove("--dir");

        assertTrue(covCaptureArguments.contains("TestDir"));
        covCaptureArguments.remove("TestDir");

        assertEquals(0, covCaptureArguments.size());
    }

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

        ICovCommand covCaptureCommand = CommandFactory.getCovCaptureCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
        List<String> covCaptureArguments = covCaptureCommand.getCommandLines();

        assertEquals(7, covCaptureArguments.size());

        assertTrue(covCaptureArguments.contains("cov-capture"));
        covCaptureArguments.remove("cov-capture");

        assertTrue(covCaptureArguments.contains("--dir"));
        covCaptureArguments.remove("--dir");

        assertTrue(covCaptureArguments.contains("TestDir"));
        covCaptureArguments.remove("TestDir");

        assertTrue(covCaptureArguments.contains("--java-coverage"));
        covCaptureArguments.remove("--java-coverage");

        assertTrue(covCaptureArguments.contains("Jacoco"));
        covCaptureArguments.remove("Jacoco");

        assertTrue(covCaptureArguments.contains("--java-test"));
        covCaptureArguments.remove("--java-test");

        assertTrue(covCaptureArguments.contains("junit"));
        covCaptureArguments.remove("junit");

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

        ICovCommand covCaptureCommand = CommandFactory.getCovCaptureCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
        List<String> covCaptureArguments = covCaptureCommand.getCommandLines();

        assertEquals(4, covCaptureArguments.size());

        assertTrue(covCaptureArguments.contains("cov-capture"));
        covCaptureArguments.remove("cov-capture");

        assertTrue(covCaptureArguments.contains("--dir"));
        covCaptureArguments.remove("--dir");

        assertTrue(covCaptureArguments.contains("TestDir"));
        covCaptureArguments.remove("TestDir");

        assertTrue(covCaptureArguments.contains("CustomTestCommand"));
        covCaptureArguments.remove("CustomTestCommand");

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

        CommandFactory.getCovCaptureCommand(build, launcher, buildListener, publisher, StringUtils.EMPTY);
    }
}
