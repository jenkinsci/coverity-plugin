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
import java.io.IOException;

public class CovImportMsvscaCommandTest extends CommandTestBase {

    @Test
    public void CovImportMsvscaCommand_AddMsvscaOutputFilesTest() throws IOException, InterruptedException {

        File analysisLog1 = new File("CodeAnalysisLog1.xml");
        File analysisLog2 = new File("CodeAnalysisLog2.xml");
        File[] outputFiles = new File[] {analysisLog1, analysisLog2};

        CoverityPublisher publisher = new CoverityPublisher(
                null, null, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );

        CovCommand covImportMsvscaCommand = new CovImportMsvscaCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, outputFiles);
        setExpectedArguments(new String[] {
                "cov-import-msvsca", "--dir", "TestDir", "--append", analysisLog1.getAbsolutePath(), analysisLog2.getAbsolutePath()
        });
        covImportMsvscaCommand.runCommand();
    }
}
