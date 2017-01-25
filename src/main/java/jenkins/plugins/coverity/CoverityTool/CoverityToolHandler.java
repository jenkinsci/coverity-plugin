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

import java.io.File;
import java.io.FilenameFilter;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Executor;
import hudson.model.Node;
import hudson.model.Result;
import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.CIMStream;
import jenkins.plugins.coverity.CoverityInstallation;
import jenkins.plugins.coverity.CoverityLauncherDecorator;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityTempDir;
import jenkins.plugins.coverity.CoverityUtils;
import jenkins.plugins.coverity.CoverityVersion;
import jenkins.plugins.coverity.InvocationAssistance;
import jenkins.plugins.coverity.ScmOptionBlock;
import jenkins.plugins.coverity.TaOptionBlock;
import jenkins.plugins.coverity.ws.DefectReader;

/**
 * CoverityToolHandler handles the actual executing of Coverity executables.
 */
public class CoverityToolHandler {

    private CoverityVersion version;

    public CoverityToolHandler(CoverityVersion version) {
        this.version = version;
    }

    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws Exception {
        EnvVars envVars = build.getEnvironment(listener);

        CoverityTempDir temp = build.getAction(CoverityTempDir.class);

        Node node = Executor.currentExecutor().getOwner().getNode();
        String home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();

        boolean useAdvancedParser = false;
        if(invocationAssistance != null && invocationAssistance.getUseAdvancedParser()){
            useAdvancedParser = true;
        }

        if(invocationAssistance != null && invocationAssistance.getSaOverride() != null) {
            home = new CoverityInstallation(CoverityUtils.evaluateEnvVars(invocationAssistance.getSaOverride(), envVars, useAdvancedParser)).forEnvironment(build.getEnvironment(listener)).getHome();
        }

        CoverityUtils.checkDir(launcher.getChannel(), home);

        /**
         * Fix Bug 84077
         * Sets COV_ANALYSIS_ROOT and COV_IDIR so they are available to the scripts used, for instance, in the post
         * cov-build and cov-analyze commands.
         */
        envVars.put("COV_IDIR", temp.getTempDir().getRemote());
        envVars.put("COV_ANALYSIS_ROOT", home);

        //run cov-build for scripting language sources only.
        try {
            CoverityLauncherDecorator.SKIP.set(true);

            Command covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, home, envVars, false);
            int result = covBuildCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] cov-build returned " + result + ", aborting...");
                build.setResult(Result.FAILURE);
                return false;
            }

        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        //run post cov-build command.
        try {
            CoverityLauncherDecorator.SKIP.set(true);
            Command postCovBuildCommand = new PostCovBuildCommand(build, launcher, listener, publisher, envVars);
            int result = postCovBuildCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] post cov-build command returned " + result + ", aborting...");
                build.setResult(Result.FAILURE);
                return false;
            }

        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        // Run Cov-Emit-Java
        try {
            CoverityLauncherDecorator.SKIP.set(true);
            Command covEmitJavaCommand = new CovEmitJavaCommand(build, launcher, listener, publisher, home, envVars, useAdvancedParser);
            int result = covEmitJavaCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] cov-emit-java returned " + result + ", aborting...");
                build.setResult(Result.FAILURE);
                return false;
            }
        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        // Run Cov-Capture
        try {
            CoverityLauncherDecorator.SKIP.set(true);
            Command covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, home, envVars);
            int result = covCaptureCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] cov-capture returned " + result + ", aborting...");
                build.setResult(Result.FAILURE);
                return false;
            }
        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        // Run Cov Manage History
        for(CIMStream cimStream : publisher.getCimStreams()) {
            CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());
            try {
                CoverityLauncherDecorator.SKIP.set(true);

                Command covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, home, envVars, cimStream, cim, version);
                int result = covManageHistoryCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-manage-history returned " + result + ", aborting...");

                    build.setResult(Result.FAILURE);
                    return false;
                }
            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        // Run Cov Import Scm
        try {
            CoverityLauncherDecorator.SKIP.set(true);

            Command covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, home, envVars);
            int result = covImportScmCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] cov-import-scm returned " + result + ", aborting...");

                build.setResult(Result.FAILURE);
                return false;
            }
        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        //run cov-analyze
        try {
            CoverityLauncherDecorator.SKIP.set(true);
            Command covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, home, envVars);
            int result = covAnalyzeCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] cov-analyze returned " + result + ", aborting...");
                build.setResult(Result.FAILURE);
                return false;
            }

        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        //run post cov-analyze command.
        try {
            CoverityLauncherDecorator.SKIP.set(true);
            Command postCovAnalyzeCommand = new PostCovAnalyzeCommand(build, launcher, listener, publisher, envVars);
            int result = postCovAnalyzeCommand.runCommand();

            if(result != 0) {
                listener.getLogger().println("[Coverity] post cov-analyze command returned " + result + ", aborting...");
                build.setResult(Result.FAILURE);
                return false;
            }

        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        // Import Microsoft Visual Studio Code Anaysis results
        listener.getLogger().println("[Coverity] Searching for Microsoft Code Analysis results...");
        File[] outputFiles = findMsvscaOutputFiles(temp.getTempDir().getRemote());
        if (outputFiles == null || outputFiles.length == 0){
            listener.getLogger().println("[Coverity] MSVSCA No results found, skipping");
        }else{
            try{
                CoverityLauncherDecorator.SKIP.set(true);
                Command covImportMsvscaCommand = new CovImportMsvscaCommand(build, launcher, listener, publisher, home, envVars, outputFiles);
                int result = covImportMsvscaCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-import-msvsca returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            }finally{
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        //run cov-commit-defects
        for(CIMStream cimStream : publisher.getCimStreams()) {
            CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());
            CoverityLauncherDecorator.SKIP.set(true);

            try {
                Command covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, home, envVars, cimStream, cim, version);
                int result = covCommitDefectsCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-commit-defects returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }
            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        if(!publisher.isKeepIntDir() || temp.isDef()) {
            listener.getLogger().println("[Coverity] deleting intermediate directory");
            temp.getTempDir().deleteRecursive();
        } else {
            listener.getLogger().println("[Coverity] preserving intermediate directory: " + temp.getTempDir());
        }

        if(!publisher.isSkipFetchingDefects()) {
            DefectReader defectReader = new DefectReader(build, launcher, listener, publisher);
            Boolean result = defectReader.getLatestDefectsForBuild();

            if (!result)
                return false;
        }

        return true;
    }

    protected File[] findMsvscaOutputFiles(String dirName) {
        File dir = new File(dirName);

        return CoverityUtils.listFilesAsArray(dir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith("CodeAnalysisLog.xml");
            }
        }, true);
    }
}
