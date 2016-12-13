/*******************************************************************************
 * Copyright (c) 2016 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity.analysis;

import com.coverity.ws.v6.*;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Executor;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.Result;
import hudson.EnvVars;
import hudson.util.ArgumentListBuilder;
import jenkins.plugins.coverity.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FresnoToolHandler calls cov-analyze and cov-commit-defects in a way consistent with all supported versions of
 * Coverity analysis before fresno.
 */
public class PreFresnoToolHandler extends CoverityToolHandler {
    @Override
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

        /**
         * Fix Bug 84077
         * Sets COV_ANALYSIS_ROOT and COV_IDIR so they are available to the scripts used, for instance, in the post
         * cov-build and cov-analyze commands.
         */
        envVars.put("COV_IDIR", temp.getTempDir().getRemote());
        if(home != null) {
            envVars.put("COV_ANALYSIS_ROOT", home);
        }

        CoverityUtils.checkDir(launcher.getChannel(), home);

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


        if(warFiles != null && !warFiles.isEmpty()) {
            boolean resultCovEmitWar = covEmitWar(build, launcher, listener, home, temp, warFiles);
            if (!resultCovEmitWar) {
                build.setResult(Result.FAILURE);
                return false;
            }
        }

        Set<String> analyzedLanguages = new HashSet<String>();

        //run cov-analyze
        for(CIMStream cimStream : publisher.getCimStreams()) {
            CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());

            String language = null;
            try {
                language = getLanguage(cimStream, cim);
            } catch(CovRemoteServiceException_Exception e) {
                e.printStackTrace(listener.error("Error while retrieving stream information for " + cimStream.getStream()));
                return false;
            }

            if(invocationAssistance != null) {
                InvocationAssistance effectiveIA = invocationAssistance;
                if(cimStream.getInvocationAssistanceOverride() != null) {
                    effectiveIA = invocationAssistance.merge(cimStream.getInvocationAssistanceOverride());
                }

                try {
                    if("CSHARP".equals(language) && effectiveIA.getCsharpAssemblies() != null) {
                        String csharpAssembliesStr = effectiveIA.getCsharpAssemblies();
                        listener.getLogger().println("[Coverity] C# Project detected, assemblies to analyze are: " + csharpAssembliesStr);
                    }

                    String covAnalyze = null;
                    if("JAVA".equals(language)) {
                        covAnalyze = "cov-analyze-java";
                    } else if("CSHARP".equals(language)) {
                        covAnalyze = "cov-analyze-cs";
                    } else {
                        covAnalyze = "cov-analyze";
                    }

                    if(home != null) {
                        covAnalyze = new FilePath(launcher.getChannel(), home).child("bin").child(covAnalyze).getRemote();
                    }

                    CoverityLauncherDecorator.SKIP.set(true);

                    if(!analyzedLanguages.contains(language)) {
                        List<String> cmd = new ArrayList<String>();
                        cmd.add(covAnalyze);
                        cmd.add("--dir");
                        cmd.add(temp.getTempDir().getRemote());

                        // For C# add the list of assemblies
                        if("CSHARP".equals(language)) {
                            String csharpAssemblies = effectiveIA.getCsharpAssemblies();
                            if(csharpAssemblies != null) {
                                cmd.add(csharpAssemblies);
                            }
                        }

                        boolean csharpAutomaticAssemblies = invocationAssistance.getCsharpAutomaticAssemblies();
                        if(csharpAutomaticAssemblies) {
                            listener.getLogger().println("[Coverity] Searching for C# assemblies...");
                            File[] automaticAssemblies = findAssemblies(build.getWorkspace().getRemote());

                            for(File assembly : automaticAssemblies) {
                                cmd.add(assembly.getAbsolutePath());
                            }
                        }

                        if(effectiveIA.getAnalyzeArguments() != null) {
                            cmd.addAll(EnvParser.tokenize(effectiveIA.getAnalyzeArguments()));
                        }

                        listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                        int result = CoverityUtils.runCmd(cmd, build, launcher, listener, envVars, useAdvancedParser);

                        analyzedLanguages.add(language);

                        if(result != 0) {
                            listener.getLogger().println("[Coverity] " + covAnalyze + " returned " + result + ", aborting...");
                            build.setResult(Result.FAILURE);
                            return false;
                        }
                    } else {
                        listener.getLogger().println("Skipping analysis, because language " + language + " has already been analyzed");
                    }
                } finally {
                    CoverityLauncherDecorator.SKIP.set(false);
                }
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
        if(invocationAssistance != null && invocationAssistance.getCsharpMsvscaOutputFiles() != null) {
            boolean csharpMsvsca = invocationAssistance.getCsharpMsvsca();
            String csharpMsvscaOutputFiles = CoverityUtils.evaluateEnvVars(invocationAssistance.getCsharpMsvscaOutputFiles(), envVars, useAdvancedParser);
            if(analyzedLanguages.contains("CSHARP") && (csharpMsvsca || csharpMsvscaOutputFiles != null)) {
                boolean result = importMsvsca(build, launcher, listener, home, temp, csharpMsvsca, csharpMsvscaOutputFiles);
                if(!result) {
                    build.setResult(Result.FAILURE);
                    return false;
                }
            }
        }

        //run cov-commit-defects
        for(CIMStream cimStream : publisher.getCimStreams()) {
            CIMInstance cim = publisher.getDescriptor().getInstance(cimStream.getInstance());

            String language = null;
            try {
                language = getLanguage(cimStream, cim);
            } catch(CovRemoteServiceException_Exception e) {
                e.printStackTrace(listener.error("Error while retrieving stream information for " + cimStream.getStream()));
                return false;
            }

            if(invocationAssistance != null) {
                InvocationAssistance effectiveIA = invocationAssistance;
                if(cimStream.getInvocationAssistanceOverride() != null) {
                    effectiveIA = invocationAssistance.merge(cimStream.getInvocationAssistanceOverride());
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
                    cmd.add(useDataPort ? "--dataport" : "--port");
                    cmd.add(useDataPort ? Integer.toString(cim.getDataPort()) : Integer.toString(cim.getPort()));
                    cmd.add("--stream");
                    cmd.add(CoverityUtils.doubleQuote(cimStream.getStream(), useAdvancedParser));
                    cmd.add("--user");
                    cmd.add(cim.getUser());

                    if(effectiveIA.getCommitArguments() != null) {
                        cmd.addAll(EnvParser.tokenize(effectiveIA.getCommitArguments()));
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

                    Set<String> checkers = new HashSet<String>();
                    for(MergedDefectDataObj defect : defects) {
                        checkers.add(defect.getCheckerName());
                    }
                    publisher.getDescriptor().updateCheckers(getLanguage(cimStream, cim), checkers);

                    List<Long> matchingDefects = new ArrayList<Long>();

                    cimStream.getDefectFilters().createImpactMap(cim);


                    for(MergedDefectDataObj defect : defects) {
                        //matchingDefects.add(defect.getCid());
                        if(cimStream.getDefectFilters() == null) {
                            matchingDefects.add(defect.getCid());
                        } else {
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

    public StreamDataObj getStream(String streamId, CIMInstance cimInstance) throws IOException, CovRemoteServiceException_Exception {
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.setNamePattern(streamId);

        List<StreamDataObj> streams = cimInstance.getConfigurationService().getStreams(filter);
        if(streams.isEmpty()) {
            return null;
        } else {
            return streams.get(0);
        }
    }

    public String getLanguage(CIMStream cimStream, CIMInstance cimInstance) throws IOException, CovRemoteServiceException_Exception {
        String domain = getStream(cimStream.getStream(), cimInstance).getLanguage();
        return "MIXED".equals(domain) ? cimStream.getLanguage() : domain;
    }

    public List<MergedDefectDataObj> getDefectsForSnapshot(CIMInstance cim, CIMStream cimStream, long snapshotId, BuildListener listener) throws IOException, CovRemoteServiceException_Exception  {
        int defectSize = 3000; // Maximum amount of defect to pull
        int pageSize = 1000; // Size of page to be pulled
        List<MergedDefectDataObj> mergeList = new ArrayList<MergedDefectDataObj>();
        DefectFilters defectFilter = cimStream.getDefectFilters();

        DefectService ds = cim.getDefectService();

        PageSpecDataObj pageSpec = new PageSpecDataObj();

        StreamIdDataObj streamId = new StreamIdDataObj();
        streamId.setName(cimStream.getStream());

        MergedDefectFilterSpecDataObj filter = new MergedDefectFilterSpecDataObj();


        StreamSnapshotFilterSpecDataObj sfilter = new StreamSnapshotFilterSpecDataObj();
        SnapshotIdDataObj snapid = new SnapshotIdDataObj();
        snapid.setId(snapshotId);

        sfilter.setStreamId(streamId);

        sfilter.getSnapshotIdIncludeList().add(snapid);
        filter.getStreamSnapshotFilterSpecIncludeList().add(sfilter);
        // The loop will pull up to the maximum amount of defect, doing per page size
        for(int pageStart = 0; pageStart < defectSize; pageStart += pageSize){
            pageSpec.setPageSize(pageSize);
            pageSpec.setStartIndex(pageStart);
            pageSpec.setSortAscending(true);
            mergeList.addAll(ds.getMergedDefectsForStreams(Arrays.asList(streamId), filter, pageSpec).getMergedDefects());

        }
        return mergeList;
    }
}
