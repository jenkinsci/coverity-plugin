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

import hudson.EnvVars;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.InvocationAssistance;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CovEmitJavaCommandTest extends CommandTestBase {

    @Test
    public void CovEmitJavaCommand_AddJavaWarFilesTest(){
        mocker.replay();
        List<String> javaWarFiles = new ArrayList<>();
        javaWarFiles.add("webapp1.war");
        javaWarFiles.add("webapp2.war");

        InvocationAssistance invocationAssistance = new InvocationAssistance(
                false, StringUtils.EMPTY, false, StringUtils.EMPTY, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                false, StringUtils.EMPTY, StringUtils.EMPTY, javaWarFiles, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, null, false
        );
        CoverityPublisher publisher = new CoverityPublisher(
                null, invocationAssistance, false, false, false, false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null
        );
        EnvVars envVars = new EnvVars();
        CovCommand covEmitJavaCommand = new CovEmitJavaCommand(build, launcher, listener, publisher, StringUtils.EMPTY, envVars, false);
        List<String> covEmitJavaArguments = covEmitJavaCommand.getCommandLines();

        assertEquals(7, covEmitJavaArguments.size());

        checkCommandLineArg(covEmitJavaArguments, "cov-emit-java");
        checkCommandLineArg(covEmitJavaArguments, "--dir");
        checkCommandLineArg(covEmitJavaArguments, "TestDir");
        checkCommandLineArg(covEmitJavaArguments, "--webapp-archive");
        checkCommandLineArg(covEmitJavaArguments, "webapp1.war");
        checkCommandLineArg(covEmitJavaArguments, "--webapp-archive");
        checkCommandLineArg(covEmitJavaArguments, "webapp2.war");

        assertEquals(0, covEmitJavaArguments.size());
    }
}
