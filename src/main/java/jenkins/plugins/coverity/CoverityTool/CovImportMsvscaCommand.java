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
import jenkins.plugins.coverity.InvocationAssistance;

import java.io.File;

public class CovImportMsvscaCommand extends CoverityCommand {

    private static final String command = "cov-import-msvsca";
    private static final String appendFlag = "--append";

    private File[] outputFiles;

    public CovImportMsvscaCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars, File[] outputFiles) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.outputFiles = outputFiles;
    }

    @Override
    protected void prepareCommand() {
        addOutputFiles();
        listener.getLogger().println("[Coverity] cov-import-msvsca command line arguments: " + commandLine.toString());
    }

    @Override
    protected boolean canExecute() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance == null || !invocationAssistance.getCsharpMsvsca()) {
            return false;
        }

        if (outputFiles == null || outputFiles.length == 0) {
            return false;
        }

        return true;
    }

    private void addOutputFiles() {
        addArgument(appendFlag);
        for(File outputFile : outputFiles) {
            addArgument(outputFile.getAbsolutePath());
        }
    }
}
