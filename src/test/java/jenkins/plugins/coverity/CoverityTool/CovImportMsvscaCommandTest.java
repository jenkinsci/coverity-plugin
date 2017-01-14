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
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovImportMsvscaCommandTest extends CommandTestBase {

    @Test
    public void CovImportMsvscaCommand_AddMsvscaOutputFilesTest(){
        mocker.replay();

        File analysisLog1 = new File("CodeAnalysisLog1.xml");
        File analysisLog2 = new File("CodeAnalysisLog2.xml");
        File[] outputFiles = new File[] {analysisLog1, analysisLog2};

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                null, null
        );

        CovCommand covImportMsvscaCommand = new CovImportMsvscaCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, outputFiles);
        List<String> covImportMsvscaArguments = covImportMsvscaCommand.getCommandLines();

        assertEquals(6, covImportMsvscaArguments.size());

        checkCommandLineArg(covImportMsvscaArguments, "cov-import-msvsca");
        checkCommandLineArg(covImportMsvscaArguments, "--dir");
        checkCommandLineArg(covImportMsvscaArguments, "TestDir");
        checkCommandLineArg(covImportMsvscaArguments, "--append");
        checkCommandLineArg(covImportMsvscaArguments, analysisLog1.getAbsolutePath());
        checkCommandLineArg(covImportMsvscaArguments, analysisLog2.getAbsolutePath());

        assertEquals(0, covImportMsvscaArguments.size());
    }
}
