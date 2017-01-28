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
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityUtils;
import jenkins.plugins.coverity.InvocationAssistance;

import java.io.File;
import java.io.FilenameFilter;

public class CovImportMsvscaCommand extends CoverityCommand {

    private static final String command = "cov-import-msvsca";
    private static final String appendFlag = "--append";

    private File[] outputFiles;
    private FilePath workSpace;

    public CovImportMsvscaCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars, FilePath workspace) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.workSpace = workspace;
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

        outputFiles = findMsvscaOutputFiles(workSpace.getRemote());
        if (outputFiles == null || outputFiles.length == 0) {
            listener.getLogger().println("[Coverity] MSVSCA No results found, skipping");
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

    private File[] findMsvscaOutputFiles(String dirName) {
        listener.getLogger().println("[Coverity] Searching for Microsoft Code Analysis results...");
        File dir = new File(dirName);

        return CoverityUtils.listFilesAsArray(dir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith("CodeAnalysisLog.xml");
            }
        }, true);
    }
}
