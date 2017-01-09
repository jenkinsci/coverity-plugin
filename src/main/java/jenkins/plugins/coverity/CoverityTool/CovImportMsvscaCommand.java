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
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.CoverityPublisher;

import java.io.File;

public class CovImportMsvscaCommand extends CovCommand {

    private static final String command = "cov-import-msvsca";
    private static final String appendFlag = "--append";

    private File[] outputFiles;

    public CovImportMsvscaCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars, File[] outputFiles) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.outputFiles = outputFiles;
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addIntermediateDir();
        addOutputFiles();
    }

    private void addOutputFiles() {
        addArgument(appendFlag);
        for(File outputFile : outputFiles) {
            addArgument(outputFile.getAbsolutePath());
        }
    }
}
