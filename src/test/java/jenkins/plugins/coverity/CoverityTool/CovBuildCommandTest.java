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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovBuildCommandTest extends CommandTestBase {

    @Test
    public void CovBuildCommand_CommandForScriptSourcesTest() {
        mocker.replay();

        CovCommand covBuildCommand = new CovBuildCommand(build, launcher, buildListener, null, StringUtils.EMPTY, false);
        List<String> covBuildArguments = covBuildCommand.getCommandLines();

        assertEquals(6, covBuildArguments.size());

        checkCommandLineArg(covBuildArguments, "cov-build");
        checkCommandLineArg(covBuildArguments, "--dir");
        checkCommandLineArg(covBuildArguments, "TestDir");
        checkCommandLineArg(covBuildArguments, "--no-command");
        checkCommandLineArg(covBuildArguments, "--fs-capture-search");
        checkCommandLineArg(covBuildArguments, "$WORKSPACE");
    }
}
