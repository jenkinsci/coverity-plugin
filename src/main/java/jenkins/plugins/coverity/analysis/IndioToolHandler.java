package jenkins.plugins.coverity.analysis;

import com.coverity.ws.v9.*;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Executor;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.Result;
import hudson.util.ArgumentListBuilder;
import jenkins.plugins.coverity.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws InterruptedException, IOException , CovRemoteServiceException_Exception{

        EnvVars envVars = build.getEnvironment(listener);

        CoverityTempDir temp = build.getAction(CoverityTempDir.class);

        Node node = Executor.currentExecutor().getOwner().getNode();
        File workspace = build.getRootDir();
        String home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        TaOptionBlock testAnalysis = publisher.getTaOptionBlock();
        ScmOptionBlock scm = publisher.getScmOptionBlock();

        // Seting the new envVars after jenkins has modified its own
        CoverityUtils.setEnvVars(envVars);

        if(invocationAssistance != null && invocationAssistance.getSaOverride() != null) {
            home = new CoverityInstallation(CoverityUtils.evaluateEnvVars(invocationAssistance.getSaOverride(), build, listener)).forEnvironment(build.getEnvironment(listener)).getHome();
        }

        // If WAR files specified, emit them prior to running analysis
        // Do not check for presence of Java streams or Java in build
        String javaWarFile = invocationAssistance != null ? CoverityUtils.evaluateEnvVars(invocationAssistance.getJavaWarFile(), build,  listener) : null;

        if(javaWarFile != null) {
            listener.getLogger().println("[Coverity] Specified WAR file '" + javaWarFile + "' in config");

            boolean result = covEmitWar(build, launcher, listener, home, temp, javaWarFile);
            if(!result) {
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

                    for(String arg : testAnalysis.getCustomTestCommand().split(" ")) {
                        cmd.add(arg);
                    }

                    // Evaluation the cmd to replace any evironment variables
                    cmd = CoverityUtils.evaluateEnvVars(cmd, build,listener);

                    ArgumentListBuilder args = new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]));

                    listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                    int result = launcher.
                            launch().
                            cmds(args).
                            pwd(CoverityUtils.evaluateEnvVars(testAnalysis.getCustomWorkDir(), build, listener)).
                            stdout(listener).
                            stderr(listener.getLogger()).
                            join();

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
                        cmd.add(cimStream.getStream());
                        if(cim.isUseSSL()){
                            cmd.add("--ssl");
                        }

                        cmd.add("--user");
                        cmd.add(cim.getUser());
                        cmd.add("--merge");

                        // Evaluation the cmd to replace any evironment variables
                        cmd = CoverityUtils.evaluateEnvVars(cmd, build,listener);

                        ArgumentListBuilder args = new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]));

                        listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                        int result = launcher.
                                launch().
                                cmds(args).
                                envs(Collections.singletonMap("COVERITY_PASSPHRASE", cim.getPassword())).
                                stdout(listener).
                                stderr(listener.getLogger()).
                                join();

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
                    cmd.add(scm.getScmToolArguments());
                }

                if(scm.getScmCommandArgs() != null){
                    cmd.add("--command-arg");
                    cmd.add(scm.getScmCommandArgs());
                }

                if(scm.getLogFileLoc() != null){
                    cmd.add("--log");
                    cmd.add(scm.getLogFileLoc());
                }
                // Adding accurev's root repo, which is optional
                if(scm.getScmSystem().equals("accurev") && scm.getAccRevRepo() != null){
                    cmd.add("--project-root");
                    cmd.add(scm.getAccRevRepo());
                }

                // Perforce requires p4port to be set when running scm
                Map<String,String> env = new HashMap<String,String>();;
                if(scm.getScmSystem().equals("perforce")){
                    env.put("P4PORT",CoverityUtils.evaluateEnvVars(scm.getP4Port(), build, listener));
                }

                // Evaluation the cmd to replace any evironment variables
                cmd = CoverityUtils.evaluateEnvVars(cmd, build, listener);

                ArgumentListBuilder args = new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]));


                listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                int result = launcher.
                        launch().
                        cmds(args).
                        stdout(listener).
                        envs(env).
                        stderr(listener.getLogger()).
                        join();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] cov-import-scm returned " + result + ", aborting...");

                    build.setResult(Result.FAILURE);
                    return false;
                }
            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
            }
        }

        //what languages are we analyzing?
        String languageToAnalyze = null;
        for(CIMStream cimStream : publisher.getCimStreams()) {
            if(languageToAnalyze == null) {
                languageToAnalyze = cimStream.getLanguage();
            } else {
                //we should have failed already during the pre-build check if we get here
                assert (languageToAnalyze.equals(cimStream.getLanguage()));
            }
        }

        //run cov-analyze
        if(invocationAssistance != null || testAnalysis != null){
            InvocationAssistance effectiveIA = invocationAssistance;

            try {
                String covAnalyze = "cov-analyze";

                if(home != null) {
                    covAnalyze = new FilePath(launcher.getChannel(), home).child("bin").child(covAnalyze).getRemote();
                }

                CoverityLauncherDecorator.SKIP.set(true);


                List<String> cmd = new ArrayList<String>();
                cmd.add(covAnalyze);
                cmd.add("--dir");
                cmd.add(temp.getTempDir().getRemote());

                boolean isMisraAnalysis = effectiveIA.getIsUsingMisra();

                if(isMisraAnalysis) {
                    cmd.add("--cpp");
                    if(effectiveIA.getMisraConfigFile() != null && !effectiveIA.getMisraConfigFile().isEmpty()){
                        cmd.add("--misra-config " + effectiveIA.getMisraConfigFile());
                    } else {
                        throw new RuntimeException("Couldn't find MISRA configuration file.");
                    }
                } else if("ALL".equals(languageToAnalyze)) {
                    // do nothing, all languages are added by default
                } else if("JAVA".equals(languageToAnalyze)) {
                    cmd.add("--java");
                } else if("CXX".equals(languageToAnalyze)) {
                    cmd.add("--cpp");
                } else if("CSHARP".equals(languageToAnalyze)) {
                    cmd.add("--cs");
                } else {
                    throw new RuntimeException("Couldn't find a language to analyze.");
                }
                // Turning on test analysis and adding required policy file
                if(testAnalysis != null && !isMisraAnalysis){
                    cmd.add("--test-advisor");
                    cmd.add("--test-advisor-policy");
                    cmd.add(testAnalysis.getPolicyFile());
                    // Adding in strip paths
                    cmd.add("--strip-path");
                    if(testAnalysis.getTaStripPath() == null){
                        cmd.add(build.getWorkspace().getRemote());
                    }else{
                        cmd.add(testAnalysis.getTaStripPath());
                    }

                    if(effectiveIA == null){
                        cmd.add("--disable-default");
                    }
                }

                if(effectiveIA.getAnalyzeArguments() != null && !isMisraAnalysis) {
                    for(String arg : effectiveIA.getAnalyzeArguments().trim().replaceAll(" +", " ").split(" ")){
                        cmd.add(arg);
                    }
                }

                cmd = CoverityUtils.evaluateEnvVars(cmd, build, listener);

                listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());

                int result = launcher.
                        launch().
                        cmds(new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]))).
                        pwd(build.getWorkspace()).
                        stdout(listener).
                        join();

                if(result != 0) {
                    listener.getLogger().println("[Coverity] " + covAnalyze + " returned " + result + ", aborting...");
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
            String csharpMsvscaOutputFiles = CoverityUtils.evaluateEnvVars(invocationAssistance.getCsharpMsvscaOutputFiles(),build, listener);
            if(("CSHARP".equals(languageToAnalyze) || "ALL".equals(languageToAnalyze)) && (csharpMsvsca || csharpMsvscaOutputFiles != null)) {
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
                    }else if(version.compareToAnalysis(new CoverityVersion("gilroy")) && cim.isUseSSL()){
                        cmd.add("--https-port");
                        cmd.add(Integer.toString(cim.getPort()));
                    }else{
                        cmd.add("--port");
                        cmd.add(Integer.toString(cim.getPort()));
                    }

                    cmd.add("--stream");
                    cmd.add(cimStream.getStream());
                    cmd.add("--user");
                    cmd.add(cim.getUser());

                    if(invocationAssistance != null){
                        if(effectiveIA.getIsUsingMisra()){
                            cmd.add("--misra-only");
                        } else if(effectiveIA.getCommitArguments() != null) {
                            for(String arg : effectiveIA.getCommitArguments().split(" ")) {
                                cmd.add(arg);
                            }
                        }
                    }

                    // Evaluation the cmd to replace any evironment variables
                    cmd = CoverityUtils.evaluateEnvVars(cmd, build, listener);

                    ArgumentListBuilder args = new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]));

                    int result = launcher.
                            launch().
                            cmds(args).
                            envs(Collections.singletonMap("COVERITY_PASSPHRASE", cim.getPassword())).
                            stdout(listener).
                            stderr(listener.getLogger()).
                            join();

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
            listener.getLogger().println("Snapsho Size: " + snapshotIds.size());
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
                            boolean match = cimStream.getDefectFilters().matchesIndio(defect,listener);
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

                    if(!matchingDefects.isEmpty() && publisher.getMailSender() != null) {
                        publisher.getMailSender().execute(action, listener);
                    }

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

        DefectService ds = cim.getDefectServiceIndio();

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
