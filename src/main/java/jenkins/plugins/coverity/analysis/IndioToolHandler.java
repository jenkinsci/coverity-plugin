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
package jenkins.plugins.coverity.analysis;

import com.coverity.ws.v9.*;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Executor;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.Result;
import jenkins.plugins.coverity.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similar to other handlers, but this one uses v9 ws.
 */
public class IndioToolHandler extends CoverityToolHandler {

    CoverityVersion version;

    public IndioToolHandler(CoverityVersion version){
        this.version = version;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws Exception {

        EnvVars envVars = build.getEnvironment(listener);

        CoverityTempDir temp = build.getAction(CoverityTempDir.class);

        Node node = Executor.currentExecutor().getOwner().getNode();
        File workspace = build.getRootDir();
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
                String covBuild = CoverityUtils.getCovBuild(listener, node);

                CoverityLauncherDecorator.SKIP.set(true);

                List<String> cmd = new ArrayList<String>();
                cmd.add(covBuild);
                cmd.add("--dir");
                cmd.add(temp.getTempDir().getRemote());
                cmd.add("--no-command");
                cmd.add("--fs-capture-search");
                cmd.add("$WORKSPACE");

                listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVars, useAdvancedParser);

                if(result != 0) {
                    listener.getLogger().println("[Coverity] " + covBuild + " returned " + result + ", aborting...");
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
                String postCovBuild = invocationAssistance.getPostCovBuildCmd();

                CoverityLauncherDecorator.SKIP.set(true);

                List<String> cmd = new ArrayList<String>();
                cmd.addAll(EnvParser.tokenize(postCovBuild));

                listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVars, useAdvancedParser);

                if(result != 0) {
                    listener.getLogger().println("[Coverity] " + postCovBuild + " returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        // If WAR files specified, emit them prior to running analysis
        // Do not check for presence of Java streams or Java in build
        List<String> warFiles = new ArrayList<String>();
        if(invocationAssistance != null){
            List<String> givenWarFiles = invocationAssistance.getJavaWarFilesNames();
            if(givenWarFiles != null && !givenWarFiles.isEmpty()){
                for(String givenJar : givenWarFiles){
                    String javaWarFile = invocationAssistance != null ? CoverityUtils.evaluateEnvVars(givenJar, envVars, useAdvancedParser) : null;
                    if(javaWarFile != null) {
                        listener.getLogger().println("[Coverity] Specified WAR file '" + javaWarFile + "' in config");
                        warFiles.add(javaWarFile);
                    }
                }
            }
        }

        if(warFiles != null && !warFiles.isEmpty()){
            boolean resultCovEmitWar = covEmitWar(build, launcher, listener, home, temp, warFiles);
            if(!resultCovEmitWar) {
                build.setResult(Result.FAILURE);
                return false;
            }
        }

        // Run Cov-Capture
        if(testAnalysis != null){
            if(testAnalysis.getCustomTestCommand() != null){
                try {
                    listener.getLogger().println(testAnalysis.getTaCommandArgs().toString());

                    String covCapture = "cov-capture";

                    if(home != null) {
                        covCapture = new FilePath(launcher.getChannel(), home).child("bin").child(covCapture).getRemote();
                    }

                    CoverityLauncherDecorator.SKIP.set(true);

                    List<String> cmd = new ArrayList<String>();
                    cmd.add(covCapture);
                    cmd.add("--dir");
                    cmd.add(temp.getTempDir().getRemote());
                    cmd.addAll(testAnalysis.getTaCommandArgs());
                    cmd.addAll(EnvParser.tokenize(testAnalysis.getCustomTestCommand()));

                    listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                    int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVars, useAdvancedParser);

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
                        String covManageHistory = "cov-manage-history";

                        if(home != null) {
                            covManageHistory = new FilePath(launcher.getChannel(), home).child("bin").child(covManageHistory).getRemote();
                        }

                        CoverityLauncherDecorator.SKIP.set(true);

                        boolean useDataPort = cim.getDataPort() != 0;

                        List<String> cmd = new ArrayList<String>();
                        cmd.add(covManageHistory);
                        cmd.add("--dir");
                        cmd.add(temp.getTempDir().getRemote());
                        cmd.add("download");
                        cmd.add("--host");
                        cmd.add(cim.getHost());
                        cmd.add("--port");
                        cmd.add(Integer.toString(cim.getPort()));
                        cmd.add("--stream");
                        cmd.add(CoverityUtils.doubleQuote(cimStream.getStream(), useAdvancedParser));
                        if(cim.isUseSSL()){
                            cmd.add("--ssl");
                        }

                        cmd.add("--user");
                        cmd.add(cim.getUser());
                        cmd.add("--merge");

                        listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                        EnvVars envVarsWithPassphrase = new EnvVars(envVars);
                        envVarsWithPassphrase.put("COVERITY_PASSPHRASE", cim.getPassword());
                        int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVarsWithPassphrase, useAdvancedParser);

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
                String covImportScm = "cov-import-scm";

                if(home != null) {
                    covImportScm = new FilePath(launcher.getChannel(), home).child("bin").child(covImportScm).getRemote();
                }

                CoverityLauncherDecorator.SKIP.set(true);



                List<String> cmd = new ArrayList<String>();
                cmd.add(covImportScm);
                cmd.add("--dir");
                cmd.add(temp.getTempDir().getRemote());
                cmd.add("--scm");
                cmd.add(scm.getScmSystem());
                if(scm.getCustomTestTool() != null){
                    cmd.add("--tool");
                    cmd.add(scm.getCustomTestTool());
                }

                if(scm.getScmToolArguments() != null){
                    cmd.add("--tool-arg");
                    cmd.add(CoverityUtils.doubleQuote(scm.getScmToolArguments(), useAdvancedParser));
                }

                if(scm.getScmCommandArgs() != null){
                    cmd.add("--command-arg");
                    cmd.add(CoverityUtils.doubleQuote(scm.getScmCommandArgs(), useAdvancedParser));
                }

                if(scm.getLogFileLoc() != null){
                    cmd.add("--log");
                    cmd.add(scm.getLogFileLoc());
                }

                if(scm.getFileRegex() != null){
                    cmd.add("--filename-regex");
                    cmd.add(scm.getFileRegex());
                }

                // Adding accurev's root repo, which is optional
                if(scm.getScmSystem().equals("accurev") && scm.getAccRevRepo() != null){
                    cmd.add("--project-root");
                    cmd.add(scm.getAccRevRepo());
                }

                // Perforce requires p4port to be set when running scm
                Map<String,String> env = new HashMap<String,String>();;
                if(scm.getScmSystem().equals("perforce")){
                    env.put("P4PORT",CoverityUtils.evaluateEnvVars(scm.getP4Port(), envVars, useAdvancedParser));
                }

                if(scm.getScmAdditionalCmd() != null) {
                    cmd.addAll(EnvParser.tokenize(scm.getScmAdditionalCmd()));
                }

                listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVars, useAdvancedParser);

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
                ICovCommand covAnalyzeCommand = CommandFactory.getCovAnalyzeCommand(build, launcher, listener, publisher, home);

                listener.getLogger().println("[Coverity] cov-analyze command line arguments: " + covAnalyzeCommand.getCommandLines().toString());

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
                String postCovAnalyzeCmd = invocationAssistance.getPostCovAnalyzeCmd();

                CoverityLauncherDecorator.SKIP.set(true);

                List<String> cmd = new ArrayList<String>();
                cmd.addAll(EnvParser.tokenize(postCovAnalyzeCmd));

                listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVars, useAdvancedParser);

                if(result != 0) {
                    listener.getLogger().println("[Coverity] " + postCovAnalyzeCmd + " returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        // Import Microsoft Visual Studio Code Anaysis results
        if(invocationAssistance != null) {
            boolean csharpMsvsca = invocationAssistance.getCsharpMsvsca();
            if(csharpMsvsca) {
                boolean result = importMsvsca(build, launcher, listener, home, temp, csharpMsvsca);
                if(!result) {
                    build.setResult(Result.FAILURE);
                    return false;
                }
            }
        }

        //run cov-commit-defects
        for(CIMStream cimStream : publisher.getCimStreams()) {
            CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());

            if(invocationAssistance != null || testAnalysis != null) {
                InvocationAssistance effectiveIA = invocationAssistance;
                if(invocationAssistance != null){
                    if(cimStream.getInvocationAssistanceOverride() != null) {
                        effectiveIA = invocationAssistance.merge(cimStream.getInvocationAssistanceOverride());
                    }
                }

                try {
                    String covCommitDefects = "cov-commit-defects";

                    if(home != null) {
                        covCommitDefects = new FilePath(launcher.getChannel(), home).child("bin").child(covCommitDefects).getRemote();
                    }

                    CoverityLauncherDecorator.SKIP.set(true);

                    boolean useDataPort = cim.getDataPort() != 0;

                    List<String> cmd = new ArrayList<String>();
                    cmd.add(covCommitDefects);
                    cmd.add("--dir");
                    cmd.add(temp.getTempDir().getRemote());
                    cmd.add("--host");
                    cmd.add(cim.getHost());

                    // For versions greater than gilroy (7.5.0), we want to use the --https-port for https connetions
                    // since it was added for gilroy and beyond
                    if(useDataPort){
                        cmd.add("--dataport");
                        cmd.add(Integer.toString(cim.getDataPort()));
                    }else if(cim.isUseSSL()){
                        cmd.add("--https-port");
                        cmd.add(Integer.toString(cim.getPort()));
                    }else{
                        cmd.add("--port");
                        cmd.add(Integer.toString(cim.getPort()));
                    }

                    cmd.add("--stream");
                    cmd.add(CoverityUtils.doubleQuote(cimStream.getStream(), useAdvancedParser));
                    cmd.add("--user");
                    cmd.add(cim.getUser());

                    if(invocationAssistance != null){
                        // --misra-only option should only be used on Indio, it is deprecated on newer versions.
                        if(effectiveIA.getIsUsingMisra()){
                            cmd.add("--misra-only");
                        } else if(effectiveIA.getCommitArguments() != null) {
                            cmd.addAll(EnvParser.tokenize(effectiveIA.getCommitArguments()));
                        }
                    }

                    EnvVars envVarsWithPassphrase = new EnvVars(envVars);
                    envVarsWithPassphrase.put("COVERITY_PASSPHRASE", cim.getPassword());
                    int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVarsWithPassphrase, useAdvancedParser);

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
