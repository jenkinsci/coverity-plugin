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

import com.coverity.ws.v9.*;
import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.*;
import jenkins.plugins.coverity.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        TaOptionBlock testAnalysis = publisher.getTaOptionBlock();
        ScmOptionBlock scm = publisher.getScmOptionBlock();

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
        if(home != null) {
            envVars.put("COV_ANALYSIS_ROOT", home);
        }

        //run cov-build for scripting language sources only.
        if(invocationAssistance != null && invocationAssistance.getIsScriptSrc() && !invocationAssistance.getIsCompiledSrc()){
            try {
                CoverityLauncherDecorator.SKIP.set(true);

                ICommand covBuildCommand = new CovBuildCommand(build, launcher, listener, publisher, home, envVars);
                int result = covBuildCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-build returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        //run post cov-build command.
        if(invocationAssistance != null && invocationAssistance.getIsUsingPostCovBuildCmd() &&
                invocationAssistance.getPostCovBuildCmd() != null && !invocationAssistance.getPostCovBuildCmd().isEmpty()){
            try {
                CoverityLauncherDecorator.SKIP.set(true);
                ICommand postCovBuildCommand = new PostCovBuildCommand(build, launcher, listener, publisher, envVars);
                int result = postCovBuildCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] post cov-build command returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        // Run Cov-Emit-Java
        if(invocationAssistance != null){
            if (invocationAssistance.getJavaWarFiles() != null && !invocationAssistance.getJavaWarFiles().isEmpty()){
                try {
                    CoverityLauncherDecorator.SKIP.set(true);
                    ICommand covEmitJavaCommand = new CovEmitJavaCommand(build, launcher, listener, publisher, home, envVars, useAdvancedParser);
                    int result = covEmitJavaCommand.runCommand();

                    if(result != 0) {
                        listener.getLogger().println("[Coverity] cov-emit-java returned " + result + ", aborting...");
                        build.setResult(Result.FAILURE);
                        return false;
                    }
                } finally {
                    CoverityLauncherDecorator.SKIP.set(false);
                }
            }
        }

        // Run Cov-Capture
        if(testAnalysis != null){
            if(testAnalysis.getCustomTestCommand() != null){
                try {
                    CoverityLauncherDecorator.SKIP.set(true);
                    ICommand covCaptureCommand = new CovCaptureCommand(build, launcher, listener, publisher, home, envVars);
                    int result = covCaptureCommand.runCommand();

                    if(result != 0) {
                        listener.getLogger().println("[Coverity] cov-capture returned " + result + ", aborting...");
                        build.setResult(Result.FAILURE);
                        return false;
                    }
                } finally {
                    CoverityLauncherDecorator.SKIP.set(false);
                }
            }
        }

        // Run Cov Manage History
        if(testAnalysis != null){
            if(testAnalysis.getCovHistoryCheckbox()){
                for(CIMStream cimStream : publisher.getCimStreams()) {
                    CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());
                    try {
                        CoverityLauncherDecorator.SKIP.set(true);

                        ICommand covManageHistoryCommand = new CovManageHistoryCommand(build, launcher, listener, publisher, home, envVars, cimStream, cim, version);
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
            }
        }

        // Run Cov Import Scm
        if(scm != null && !scm.getScmSystem().equals("none")){

            try {
                CoverityLauncherDecorator.SKIP.set(true);

                ICommand covImportScmCommand = new CovImportScmCommand(build, launcher, listener, publisher, home, envVars);
                int result = covImportScmCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-import-scm returned " + result + ", aborting...");

                    build.setResult(Result.FAILURE);
                    return false;
                }
            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        //run cov-analyze
        if(invocationAssistance != null || testAnalysis != null){
            InvocationAssistance effectiveIA = invocationAssistance;

            try {
                CoverityLauncherDecorator.SKIP.set(true);
                ICommand covAnalyzeCommand = new CovAnalyzeCommand(build, launcher, listener, publisher, home, envVars);
                int result = covAnalyzeCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-analyze returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        //run post cov-analyze command.
        if(invocationAssistance != null && invocationAssistance.getIsUsingPostCovAnalyzeCmd() &&
                invocationAssistance.getPostCovAnalyzeCmd() != null && !invocationAssistance.getPostCovAnalyzeCmd().isEmpty()){
            try {
                CoverityLauncherDecorator.SKIP.set(true);
                ICommand postCovAnalyzeCommand = new PostCovAnalyzeCommand(build, launcher, listener, publisher, envVars);
                int result = postCovAnalyzeCommand.runCommand();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] post cov-analyze command returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        // Import Microsoft Visual Studio Code Anaysis results
        if(invocationAssistance != null) {
            if(invocationAssistance.getCsharpMsvsca()) {
                listener.getLogger().println("[Coverity] Searching for Microsoft Code Analysis results...");
                File[] outputFiles = findMsvscaOutputFiles(temp.getTempDir().getRemote());
                if (outputFiles == null || outputFiles.length == 0){
                    listener.getLogger().println("[Coverity] MSVSCA No results found, skipping");
                }else{
                    try{
                        CoverityLauncherDecorator.SKIP.set(true);
                        ICommand covImportMsvscaCommand = new CovImportMsvscaCommand(build, launcher, listener, publisher, home, envVars, outputFiles);
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
            }
        }

        //run cov-commit-defects
        for(CIMStream cimStream : publisher.getCimStreams()) {
            CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());
            if(invocationAssistance != null) {
                CoverityLauncherDecorator.SKIP.set(true);

                try {
                    ICommand covCommitDefectsCommand = new CovCommitDefectsCommand(build, launcher, listener, publisher, home, envVars, cimStream, cim, version);
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
        }

        //keep if keepIntDir is set, or if the int dir is the default (keepIntDir is only useful if a custom int
        //dir is set)
        if(temp != null) {
            //same as !(keepIntDir && !temp.def)
            if(!publisher.isKeepIntDir() || temp.isDef()) {
                listener.getLogger().println("[Coverity] deleting intermediate directory");
                temp.getTempDir().deleteRecursive();
            } else {
                listener.getLogger().println("[Coverity] preserving intermediate directory: " + temp.getTempDir());
            }
        }

        if(!publisher.isSkipFetchingDefects()) {
            Pattern snapshotPattern = Pattern.compile(".*New snapshot ID (\\d*) added.");
            BufferedReader reader = new BufferedReader(build.getLogReader());
            String line = null;
            List<Long> snapshotIds = new ArrayList<Long>();
            try {
                while((line = reader.readLine()) != null) {
                    Matcher m = snapshotPattern.matcher(line);
                    if(m.matches()) {
                        snapshotIds.add(Long.parseLong(m.group(1)));
                    }
                }
            } finally {
                reader.close();
            }
            listener.getLogger().println("Cim Streams: " + publisher.getCimStreams().size());
            listener.getLogger().println("Snapshot Size: " + snapshotIds.size());
            if(snapshotIds.size() != publisher.getCimStreams().size()) {
                listener.getLogger().println("[Coverity] Wrong number of snapshot IDs found in build log");
                build.setResult(Result.FAILURE);
                return false;
            }

            listener.getLogger().println("[Coverity] Found snapshot IDs " + snapshotIds);

            for(int i = 0; i < publisher.getCimStreams().size(); i++) {
                CIMStream cimStream = publisher.getCimStreams().get(i);
                long snapshotId = snapshotIds.get(i);
                try {
                    CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());

                    listener.getLogger().println("[Coverity] Fetching defects for stream " + cimStream.getStream());

                    List<MergedDefectDataObj> defects = getDefectsForSnapshot(cim, cimStream, snapshotId,listener);

                    listener.getLogger().println("[Coverity] Found " + defects.size() + " defects");

                    List<Long> matchingDefects = new ArrayList<Long>();
                    // Loop through all defects
                    for(MergedDefectDataObj defect : defects) {
                        //matchingDefects.add(defect.getCid()); All the code needed when trying to get cim checkers
                        //When there is no defect filter, we just add it to the matching defects
                        if(cimStream.getDefectFilters() == null) {
                            matchingDefects.add(defect.getCid());
                        } else {

                            // Check to see if defectFilter matches the defect
                            boolean match = cimStream.getDefectFilters().matches(defect,listener);
                            if(match) {
                                matchingDefects.add(defect.getCid());
                            }
                        }
                    }

                    if(!matchingDefects.isEmpty()) {
                        listener.getLogger().println("[Coverity] Found " + matchingDefects.size() + " defects matching all filters: " + matchingDefects);
                        if(publisher.isFailBuild()) {
                            if(build.getResult().isBetterThan(Result.FAILURE)) {
                                build.setResult(Result.FAILURE);
                            }
                        }

                        // if the user wants to mark the build as unstable when defects are found, then we
                        // notify the publisher to do so.
                        if(publisher.isUnstable()){
                            publisher.setUnstableBuild(true);

                        }

                    } else {
                        listener.getLogger().println("[Coverity] No defects matched all filters.");
                    }

                    CoverityBuildAction action = new CoverityBuildAction(build, cimStream.getProject(), cimStream.getStream(), cimStream.getInstance(), matchingDefects);
                    build.addAction(action);

                    String rootUrl = Hudson.getInstance().getRootUrl();
                    if(rootUrl != null) {
                        listener.getLogger().println("Coverity details: " + Hudson.getInstance().getRootUrl() + build.getUrl() + action.getUrlName());
                    }

                } catch(CovRemoteServiceException_Exception e) {
                    e.printStackTrace(listener.error("[Coverity] An error occurred while fetching defects"));
                    build.setResult(Result.FAILURE);
                    return false;
                }
            }
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

    public List<MergedDefectDataObj> getDefectsForSnapshot(CIMInstance cim, CIMStream cimStream, long snapshotId, BuildListener listener) throws IOException, CovRemoteServiceException_Exception {
        int defectSize = 3000; // Maximum amount of defect to pull
        int pageSize = 1000; // Size of page to be pulled
        List<MergedDefectDataObj> mergeList = new ArrayList<MergedDefectDataObj>();

        DefectService ds = cim.getDefectService();

        StreamIdDataObj streamId = new StreamIdDataObj();
        streamId.setName(cimStream.getStream());
        List<StreamIdDataObj> streamIds = new ArrayList<StreamIdDataObj>();
        streamIds.add(streamId);

        MergedDefectFilterSpecDataObj filter = new MergedDefectFilterSpecDataObj();

        PageSpecDataObj pageSpec = new PageSpecDataObj();

        SnapshotScopeSpecDataObj snapshotScope = new SnapshotScopeSpecDataObj();
        snapshotScope.setShowSelector(Long.toString(snapshotId));

        // The loop will pull up to the maximum amount of defect, doing per page size
        for(int pageStart = 0; pageStart < defectSize; pageStart += pageSize){
            pageSpec.setPageSize(pageSize);
            pageSpec.setStartIndex(pageStart);
            pageSpec.setSortAscending(true);
            mergeList.addAll(ds.getMergedDefectsForStreams(streamIds, filter, pageSpec, snapshotScope).getMergedDefects());
        }
        return mergeList;
    }
}
