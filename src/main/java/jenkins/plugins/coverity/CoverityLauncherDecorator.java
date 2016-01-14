/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import com.coverity.ws.v6.CovRemoteServiceException_Exception;
import com.coverity.ws.v6.StreamDataObj;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.LauncherDecorator;
import hudson.Proc;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Executor;
import hudson.model.Node;
import hudson.model.Queue;
import hudson.model.TaskListener;
import hudson.remoting.Channel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This makes sure that all commands executed (when invocation assistance is enabled) are run through cov-build.
 */
@Extension
public class CoverityLauncherDecorator extends LauncherDecorator {

    private static final Logger logger = Logger.getLogger(CoverityLauncherDecorator.class.getName());
    /**
     * A ThreadLocal that is used to disable cov-build when running other Coverity tools during the build.
     */
    public static ThreadLocal<Boolean> SKIP = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Launcher decorate(Launcher launcher, Node node) {
        Executor executor = Executor.currentExecutor();
        if(executor == null) {
            return launcher;
        }

        Queue.Executable exec = executor.getCurrentExecutable();
        if(!(exec instanceof AbstractBuild)) {
            return launcher;
        }
        AbstractBuild build = (AbstractBuild) exec;

        AbstractProject project = build.getProject();

        CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
       
        //Setting up code to allow environment variables in text fields
        EnvVars env;
        try{
            env = project.getEnvironment(node,launcher.getListener());
        }catch(Exception e){
            throw new RuntimeException("Error getting build environment variables", e);
        }

        if(publisher == null) {
            return launcher;
        }

        CoverityVersion version = CheckConfig.checkNode(publisher, build, launcher, launcher.getListener()).getVersion();

        TaOptionBlock ta = publisher.getTaOptionBlock();
        ScmOptionBlock scm = publisher.getScmOptionBlock();
        InvocationAssistance ii = publisher.getInvocationAssistance();



        FilePath temp;
        boolean isUsingTA = false;
        boolean isUsingMisra = false;
        if(ta != null){
            String taCheck = ta.checkTaConfig();
            if(!taCheck.equals("Pass")){
                throw new RuntimeException(taCheck);
            }
            isUsingTA = true;
        }

        if(ii != null){
            String taCheck = ii.checkIAConfig();
            if(!taCheck.equals("Pass")){
                throw new RuntimeException(taCheck);
            }
            isUsingMisra = ii.getIsUsingMisra();
        }

        if(isUsingTA && isUsingMisra){
            String errorText = "Errors with your \"Perform Coverity build/analyze/commit\" options: \n " +
                    "[Error] MISRA and Test Advisor options are not compatible. \n";
            throw new RuntimeException(errorText);
        }
        
        if(scm != null){
            String scmCheck = scm.checkScmConfig(version);
            if(!scmCheck.equals("Pass")){
                throw new RuntimeException(scmCheck);
            }
        }

        try {
            if(ii == null){
                FilePath coverityDir = node.getRootPath().child("coverity");
                coverityDir.mkdirs();
                temp = coverityDir.createTempDir("temp-", null);
            }else if(ii.getIntermediateDir() == null) {
                FilePath coverityDir = node.getRootPath().child("coverity");
                coverityDir.mkdirs();
                temp = coverityDir.createTempDir("temp-", null);
            } else {
                temp = new FilePath(node.getChannel(), ii.getIntermediateDir());
                temp.mkdirs();
            }

            if(ii != null){
                build.addAction(new CoverityTempDir(temp, ii.getIntermediateDir() == null));
            } else{
                build.addAction(new CoverityTempDir(temp, true));
            }
        } catch(IOException e) {
            throw new RuntimeException("Error while creating temporary directory for Coverity", e);
        } catch(InterruptedException e) {
            throw new RuntimeException("Interrupted while creating temporary directory for Coverity");
        }

        // Do not run cov-build if language is "CSHARP"
        boolean onlyCS = true;
        for(CIMStream cs : publisher.getCimStreams()) {
            if(!cs.getDefectFilters().checkConfig()){
                throw new RuntimeException("Defect Filters Configured incorrectly. Possibly new configuration have been " +
                        "added and needs configurations. \n Please check your build configuration before running another build.");
            }
            CIMInstance cim = publisher.getDescriptor().getInstance(cs.getInstance());
            String id = cs.getInstance() + "/" + cs.getStream();
            try {
                if(cim.getWsVersion().equals("v9")){
                    com.coverity.ws.v9.StreamDataObj stream = cim.getStreamIndio(cs.getStream());
                    if(stream == null) {
                        throw new RuntimeException("Could not find stream: " + id);
                    }
                    String language = stream.getLanguage();
                    if(!"CSHARP".equals(language)) {
                        onlyCS = false;
                        break;
                    }
                } else {
                    StreamDataObj stream = cim.getStream(cs.getStream());
                    if(stream == null) {
                        throw new RuntimeException("Could not find stream: " + id);
                    }
                    String language = stream.getLanguage();
                    if(!"CSHARP".equals(language)) {
                        onlyCS = false;
                        break;
                    }
                }
            } catch(CovRemoteServiceException_Exception e) {
                throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
            } catch(IOException e) {
                throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
            } catch (com.coverity.ws.v9.CovRemoteServiceException_Exception e) {
                throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
            }
        }
        //look for old-style stream format
        if(publisher.getCimInstance() != null) {
            CIMInstance cim = publisher.getDescriptor().getInstance(publisher.getCimInstance());
            String id = publisher.getCimInstance() + "/" + publisher.getStream();
            try {
                StreamDataObj stream = cim.getStream(publisher.getStream());
                if(stream == null) {
                    throw new RuntimeException("Could not find stream: " + id);
                }
                String language = stream.getLanguage();
                if(!"CSHARP".equals(language)) {
                    onlyCS = false;
                }
            } catch(CovRemoteServiceException_Exception e) {
                throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
            } catch(IOException e) {
                throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
            }
        }

        if(onlyCS && version.compareTo(CoverityVersion.VERSION_FRESNO) < 0) {
            logger.info("Only streams of type CSHARP were found, skipping cov-build");

            return launcher;
        }

        
        /**
         * Putting together the arguments for cov-build. The placeholder is there so that we can later determine the
         * exact location of cov-build runable.
         */
       
        List<String> args = new ArrayList<String>();
        if(ii != null || ta != null){
            args.add("cov-build-placeholder");
            // Adding the intermediate directory
            args.add("--dir");
            // Adding the build command for the code
            args.add(temp.getRemote());
        }

        // Adding in Test Analysis arguments into the code when no other test command is needed to run.
        if(ta != null){
            if(ta.getCustomTestCommand() == null){
                args.addAll(ta.getTaCommandArgs());
            }
        }

        String[] blacklist;
        if(ii != null) {
            if(ii.getBuildArguments() != null) {
                for(String arg : ii.getBuildArguments().split(" ")) {
                    args.add(arg);
                }
            }

            String blacklistTemp = ii.getCovBuildBlacklist();

            if(blacklistTemp != null) {
                blacklist = blacklistTemp.split(",");
                for(int i = 0; i < blacklist.length; i++) {
                    blacklist[i] = blacklist[i].trim();
                }
            } else {
                blacklist = new String[0];
            }
        } else{
            blacklist = new String[0];
        }

        // Evaluation the args to replace any evironment variables 
        args = CoverityUtils.evaluateEnvVars(args, env);
        launcher.getListener().getLogger().println(args.toString());

        return new DecoratedLauncher(launcher, blacklist, node, args.toArray(new String[args.size()]));
    }

    /**
     * A decorated {@link Launcher} that puts the given set of arguments as a prefix to any commands that it invokes.
     */
    public class DecoratedLauncher extends Launcher {
        private final Launcher decorated;
        private final String[] prefix;
        private final String[] blacklist;
        private final String toolsDir;
        private final Node node;

        public DecoratedLauncher(Launcher decorated, String[] blacklist, Node node, String... prefix) {
            super(decorated);
            this.decorated = decorated;
            this.prefix = prefix;
            this.blacklist = blacklist;
            this.node = node;
            this.toolsDir = node.getRootPath().child("tools").getRemote();
        }

        private String[] getPrefix() {
            String[] tp = prefix.clone();
            if(tp.length > 0){    
                tp[0] = getCovBuild();
            }
            return tp;
        }

        @Override
        public Proc launch(ProcStarter starter) throws IOException {
            if(!SKIP.get()) {
                if(isBlacklisted(starter.cmds().get(0))) {
                    logger.info(starter.cmds().get(0) + " is blacklisted, skipping cov-build");
                    return decorated.launch(starter);
                }

                // Creating a hashmap of the environment variables so that we can evalute the cov-build command
                Map<String,String> envMap = new HashMap<String,String>();

                for(String env : starter.envs()){
                    String[] split = env.split("=",2);
                    envMap.put(env.split("=",2)[0],env.split("=",2)[1]);
                }

                List<String> cmds = starter.cmds();

                //skip jdk installations
                String lastArg = cmds.get(cmds.size() - 1);
                if(lastArg.startsWith(toolsDir) && lastArg.endsWith(".sh")) {
                    logger.info(lastArg + " is a tools script, skipping cov-build");
                    return decorated.launch(starter);
                }

                cmds.addAll(0, Arrays.asList(getPrefix()));
                // parsing the command and replacing all environment variable with their respective value
                cmds = CoverityUtils.evaluateEnvVars(cmds,new EnvVars(envMap));
                starter.cmds(cmds);
                boolean[] masks = starter.masks();
                if(masks == null) {
                    masks = new boolean[cmds.size()];
                    starter.masks(masks);
                } else {
                    starter.masks(prefix(masks));
                }
            }
            return decorated.launch(starter);
        }

        @Override
        public Channel launchChannel(String[] cmd, OutputStream out, FilePath workDir, Map<String, String> envVars) throws IOException, InterruptedException {
            String lastArg = cmd[cmd.length - 1];
            if(lastArg.startsWith(toolsDir) && lastArg.endsWith(".sh")) {
                logger.info(lastArg + " is a tools script, skipping cov-build");
                decorated.launchChannel(cmd, out, workDir, envVars);
            }
            return decorated.launchChannel(prefix(cmd), out, workDir, envVars);
        }

        /*
         * When running remotely, overriding this method makes sure that the platform is detected correctly.
         */
        @Override
        public boolean isUnix() {
            return decorated.isUnix();
        }

        @Override
        public void kill(Map<String, String> modelEnvVars) throws IOException, InterruptedException {
            decorated.kill(modelEnvVars);
        }

        private String[] prefix(String[] args) {
            if(isBlacklisted(args[0])) {
                return args;
            }
            String[] newArgs = new String[args.length + prefix.length];
            System.arraycopy(getPrefix(), 0, newArgs, 0, prefix.length);
            System.arraycopy(args, 0, newArgs, prefix.length, args.length);
            return newArgs;
        }

        private boolean[] prefix(boolean[] args) {
            boolean[] newArgs = new boolean[args.length + prefix.length];
            System.arraycopy(args, 0, newArgs, prefix.length, args.length);
            return newArgs;
        }

        private boolean isBlacklisted(String cmd) {
            for(String s : blacklist) {
                if(s.equals(cmd)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * getCovBuild
         *
         * Retrieves the location of cov-build executable/sh from the system and returns the string of the
         * path
         * @return  string of cov-build's path
         */

        private String getCovBuild() {
            Executor executor = Executor.currentExecutor();
            Queue.Executable exec = executor.getCurrentExecutable();
            AbstractBuild build = (AbstractBuild) exec;
            AbstractProject project = build.getProject();
            CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
            InvocationAssistance ii = publisher.getInvocationAssistance();

            String covBuild = "cov-build";
            TaskListener listener = decorated.getListener();
            String home = null;
            try {
                home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
            } catch(IOException e) {
                e.printStackTrace();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            if(ii != null){
                if(ii.getSaOverride() != null) {
                    try {
                        home = new CoverityInstallation(ii.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
                        CoverityUtils.checkDir(node.getChannel(), home);
                    } catch(IOException e) {
                        e.printStackTrace();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(home != null) {
                covBuild = new FilePath(node.getChannel(), home).child("bin").child(covBuild).getRemote();
            }

            return covBuild;
        }
    }
}
