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
package jenkins.plugins.coverity;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.LauncherDecorator;
import hudson.Proc;
import hudson.model.*;
import hudson.remoting.Channel;
import hudson.tasks.Builder;
import jenkins.plugins.coverity.CoverityTool.CovBuildCompileCommand;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
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
    public static ThreadLocal<Boolean> CoverityPostBuildAction = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public static ThreadLocal<Boolean> CoverityBuildStep = new ThreadLocal<Boolean>() {
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
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();

        boolean isUsingTA = false;
        boolean isUsingMisra = false;
        if(ta != null){
            String taCheck = ta.checkTaConfig();
            if(!taCheck.equals("Pass")){
                throw new RuntimeException(taCheck);
            }
            isUsingTA = true;
        }

        if(invocationAssistance != null){
            String taCheck = invocationAssistance.checkIAConfig();
            if(!taCheck.equals("Pass")){
                throw new RuntimeException(taCheck);
            }
            isUsingMisra = invocationAssistance.getIsUsingMisra();
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

        String home = publisher.getDescriptor().getHome(node, env);
        if(invocationAssistance != null && invocationAssistance.getSaOverride() != null) {
            home = new CoverityInstallation(
                    CoverityUtils.evaluateEnvVars(invocationAssistance.getSaOverride(), env, invocationAssistance.getUseAdvancedParser())).forEnvironment(env).getHome();
        }

        setupIntermediateDirectory(build, launcher.getListener(), node, env);
        List<String> args = new CovBuildCompileCommand(build, launcher, launcher.getListener(), publisher, home, env).constructArguments();

        return new DecoratedLauncher(launcher, node, args.toArray(new String[args.size()]));
    }

    /**
     * Resolves environment variables on the specified intermediate directory. If the result is a null object or the
     * path is empty an exception is thrown.
     */
    public FilePath resolveIntermediateDirectory(AbstractBuild<?,?> build, TaskListener listener, Node node, EnvVars envVars, String idirInput){
        FilePath idirFilePath = null;
        try {
            String idir = EnvParser.interpolateRecursively(idirInput, 1, envVars);
            if(idir == null || idir.isEmpty()){
                throw new Exception("The specified Intermediate Directory is not valid: " + idirInput);
            }
            idirFilePath = new FilePath(node.getChannel(), idir);
        } catch (ParseException e) {
            CoverityUtils.handleException(e.getMessage(), build, listener, e);
        } catch (Exception e){
            CoverityUtils.handleException("An error occured while setting intermediate directory: " + idirInput, build, listener, e);
        }
        return idirFilePath;
    }

    /**
     * Sets the value of environment variable "COV_IDIR" and creates necessary directories. This variable is used as argument of "--dir" for
     * cov-build.
     *
     * If the environment variable has already being set then that value is used. Otherwise, the value of
     * this variable is set to either a temporary directory or the idir specified on the UI under the
     * Intermediate Directory option.
     *
     * Notice this variable must be resolved before running cov-build. Also this method creates necessary directories.
     */
    public void setupIntermediateDirectory(@Nonnull AbstractBuild<?,?> build, @Nonnull TaskListener listener, @Nonnull Node node, @Nonnull EnvVars envVars){
        Validate.notNull(build, AbstractBuild.class.getName() + " object can't be null");
        Validate.notNull(listener, TaskListener.class.getName() + " object can't be null");
        Validate.notNull(node, Node.class.getName() + " object can't be null");
        Validate.notNull(envVars, EnvVars.class.getName() + " object can't be null");
        if(!envVars.containsKey("COV_IDIR")){
            FilePath temp;
            InvocationAssistance invocationAssistance = CoverityUtils.getInvocationAssistance(build);
            try {
                if(invocationAssistance == null || invocationAssistance.getIntermediateDir() == null ||
                        invocationAssistance.getIntermediateDir().isEmpty()){
                    FilePath coverityDir = node.getRootPath().child("coverity");
                    coverityDir.mkdirs();
                    temp = coverityDir.createTempDir("temp-", null);
                } else {
                    // Gets a not null nor empty intermediate directory.
                    temp = resolveIntermediateDirectory(build, listener, node, envVars, invocationAssistance.getIntermediateDir());
                    temp.mkdirs();
                }

                if(invocationAssistance != null){
                    build.addAction(new CoverityTempDir(temp, invocationAssistance.getIntermediateDir() == null));
                } else{
                    build.addAction(new CoverityTempDir(temp, true));
                }
            } catch(IOException e) {
                throw new RuntimeException("Error while creating temporary directory for Coverity", e);
            } catch(InterruptedException e) {
                throw new RuntimeException("Interrupted while creating temporary directory for Coverity");
            }
            envVars.put("COV_IDIR", temp.getRemote());
        }
    }


    /**
     * A decorated {@link Launcher} that puts the given set of arguments as a prefix to any commands that it invokes.
     */
    public class DecoratedLauncher extends Launcher {
        private final Launcher decorated;
        private final String[] prefix;
        private final String toolsDir;
        private final Node node;

        public DecoratedLauncher(Launcher decorated, Node node, String... prefix) {
            super(decorated);
            this.decorated = decorated;
            this.prefix = prefix;
            this.node = node;
            this.toolsDir = node.getRootPath().child("tools").getRemote();
        }

        private String[] getPrefix() {
            String[] tp = prefix.clone();
            if(tp.length > 0){    
                tp[0] = CoverityUtils.getCovBuild(decorated.getListener(), node);
            }
            return tp;
        }

        @Override
        public Proc launch(ProcStarter starter) throws IOException {
            // Any Coverity Post-build action such as cov-analyze, cov-import-scm, etc will not be wrapped
            // with the cov-build.
            if (CoverityPostBuildAction.get()) {
                return decorated.launch(starter);
            }

            EnvVars envVars = CoverityUtils.getBuildEnvVars(listener);
            boolean isCoverityBuildStepEnabled = false;

            // Check if there are any Coverity Build Step configured.
            // This is required to support backward compatibility.
            List<Builder> builders = ((Project)CoverityUtils.getBuild().getProject()).getBuilders();
            for (Builder buildStep : builders) {
                if (buildStep.getDescriptor() instanceof CoverityBuildStep.CoverityBuildStepDescriptor) {
                    isCoverityBuildStepEnabled = true;
                    break;
                }
            }

            // The first condition is for the case where there are no coverity build steps.
            // Then we want to wrap the build steps with cov-build. This is to support backward compatibility
            // The second condition is for the case where there are coverity build step and
            // the current build step is the coverity build step.
            if (!isCoverityBuildStepEnabled
                    || (isCoverityBuildStepEnabled && CoverityBuildStep.get())) {

                InvocationAssistance invocationAssistance = CoverityUtils.getInvocationAssistance();

                List<String> cmds = starter.cmds();

                cmds.addAll(0, Arrays.asList(prefix.clone()));

                boolean useAdvancedParser = false;

                if(invocationAssistance != null && invocationAssistance.getUseAdvancedParser()){
                    useAdvancedParser = true;
                }

                //skip jdk installations
                String lastArg = cmds.get(cmds.size() - 1);
                if(lastArg.startsWith(toolsDir) && lastArg.endsWith(".sh")) {
                    logger.info(lastArg + " is a tools script, skipping cov-build");
                    return decorated.launch(starter);
                }

                /**
                 * Overrides environment variables on the ProcStarter.
                 *
                 * First, get the environment variables from the ProcStarter. Then, add more environment variables to it.
                 * Finally, creates a new ProcStarter object identically to the first one but with overridden variables.
                 */
                String[] starterEnvVars = starter.envs();
                starterEnvVars = CoverityUtils.addEnvVars(starterEnvVars, envVars);
                starter = starter.envs(starterEnvVars);

                cmds = CoverityUtils.prepareCmds(cmds, envVars, useAdvancedParser);

                /**
                 * Display cov-build command on the console. Coverity's parser is used in order to resolve environment
                 * variables on that command just for display purposes.
                 */
                getListener().getLogger().println(CoverityUtils.prepareCmds(cmds, envVars, true).toString());

                starter = starter.cmds(cmds);
                boolean[] masks = starter.masks();
                if(masks == null) {
                    masks = new boolean[cmds.size()];
                    starter = starter.masks(masks);
                } else {
                    starter = starter.masks(prefix(masks));
                }

                CoverityBuildStep.set(false);
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
    }
}
