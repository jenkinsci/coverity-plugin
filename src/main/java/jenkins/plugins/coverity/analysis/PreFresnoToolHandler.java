package jenkins.plugins.coverity.analysis;

import com.coverity.ws.v6.CovRemoteServiceException_Exception;
import com.coverity.ws.v6.MergedDefectDataObj;
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
import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.CIMStream;
import jenkins.plugins.coverity.CoverityBuildAction;
import jenkins.plugins.coverity.CoverityInstallation;
import jenkins.plugins.coverity.CoverityLauncherDecorator;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityTempDir;
import jenkins.plugins.coverity.InvocationAssistance;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws InterruptedException, IOException, CovRemoteServiceException_Exception {

        EnvVars envVars = build.getEnvironment(listener);


        CoverityTempDir temp = build.getAction(CoverityTempDir.class);

        Node node = Executor.currentExecutor().getOwner().getNode();
        String home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if(invocationAssistance != null && invocationAssistance.getSaOverride() != null) {
            home = new CoverityInstallation(invocationAssistance.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
        }

        // If WAR files specified, emit them prior to running analysis
        // Do not check for presence of Java streams or Java in build
        String javaWarFile = invocationAssistance != null ? invocationAssistance.getJavaWarFile() : null;
        if(javaWarFile != null) {
            listener.getLogger().println("[Coverity] Specified WAR file '" + javaWarFile + "' in config");

            boolean result = covEmitWar(build, launcher, listener, home, temp, javaWarFile);
            if(!result) {
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
                language = publisher.getLanguage(cimStream);
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


                        listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());
                        if(effectiveIA.getAnalyzeArguments() != null) {
                            for(String arg : Util.tokenize(envVars.expand(teffectiveIA.getAnalyzeArguments()))) {
                                cmd.add(arg);
                            }
                        }

                        int result = launcher.
                                launch().
                                cmds(new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]))).
                                pwd(build.getWorkspace()).
                                stdout(listener).
                                join();

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

        // Import Microsoft Visual Studio Code Anaysis results
        if(invocationAssistance != null) {
            boolean csharpMsvsca = invocationAssistance.getCsharpMsvsca();
            String csharpMsvscaOutputFiles = invocationAssistance.getCsharpMsvscaOutputFiles();
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
                language = publisher.getLanguage(cimStream);
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
                    cmd.add(cimStream.getStream());
                    cmd.add("--user");
                    cmd.add(cim.getUser());

                    if(effectiveIA.getCommitArguments() != null) {
                        for(String arg : Util.tokenize(envVars.expand(teffectiveIA.getCommitArguments()))) {
                            cmd.add(arg);
                        }
                    }

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
                    publisher.getDescriptor().updateCheckers(publisher.getLanguage(cimStream), checkers);

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
                        listener.getLogger().println("[Coverity] Found " + defects.size() + " defects matching all filters: " + matchingDefects);
                        if(publisher.isFailBuild()) {
                            if(build.getResult().isBetterThan(Result.FAILURE)) {
                                build.setResult(Result.FAILURE);
                            }
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
}
