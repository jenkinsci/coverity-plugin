/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
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
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.*;
import hudson.remoting.Channel;
import hudson.tasks.Builder;
import jenkins.plugins.coverity.CoverityTool.CovBuildCompileCommand;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A decorated {@link Launcher} that puts the given set of arguments as a prefix to any commands that it invokes.
 */
public class CoverityLauncher extends Launcher {

    private static final Logger logger = Logger.getLogger(CoverityLauncherDecorator.class.getName());

    private final Launcher decorated;
    private String[] prefix;
    private final String toolsDir;
    private final Node node;
    private EnvVars envVars;

    public CoverityLauncher(Launcher decorated, Node node) {
        super(decorated);
        this.decorated = decorated;
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
        EnvVars buildEnvVars = CoverityUtils.getBuildEnvVars(listener);
        if (envVars == null || envVars.isEmpty()) {
            envVars = buildEnvVars;
        } else if (buildEnvVars != null) {
            envVars.overrideAll(buildEnvVars);
        }

        AbstractBuild build = CoverityUtils.getBuild();
        AbstractProject project = build.getProject();

        CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
        if (publisher == null)
            return decorated.launch(starter);

        // Setup(or resolve) intermediate directory
        setupIntermediateDirectory(build, this.getListener(), node);

        // Any Coverity Post-build action such as cov-analyze, cov-import-scm, etc will not be wrapped
        // with the cov-build.
        if (CoverityLauncherDecorator.CoverityPostBuildAction.get()) {
            String[] starterEnvVars = starter.envs();
            starterEnvVars = CoverityUtils.addEnvVars(starterEnvVars, envVars);
            starter = starter.envs(starterEnvVars);
            return decorated.launch(starter);
        }

        boolean isCoverityBuildStepEnabled = false;

        // Check if there are any Coverity Build Step configured.
        // This is required to support backward compatibility.
        if (project instanceof Project) {
            List<Builder> builders = ((Project)project).getBuilders();
            for (Builder buildStep : builders) {
                if (buildStep.getDescriptor() instanceof CoverityBuildStep.CoverityBuildStepDescriptor) {
                    isCoverityBuildStepEnabled = true;
                    break;
                }
            }
        }

        // The first condition is for the case where there are no coverity build steps.
        // Then we want to wrap the build steps with cov-build. This is to support backward compatibility
        // The second condition is for the case where there are coverity build step and
        // the current build step is the coverity build step.
        if (!isCoverityBuildStepEnabled
                || (isCoverityBuildStepEnabled && CoverityLauncherDecorator.CoverityBuildStep.get())) {

            List<String> cmds = starter.cmds();
            final InvocationAssistance invocationAssistance = CoverityUtils.getInvocationAssistance();
            final CoverityToolInstallation installation = CoverityUtils.findToolInstallationForBuild(node, envVars, this.getListener());
            if (installation != null) {
                String home = installation.getHome();
                if (invocationAssistance != null) {
                    List<String> args = new CovBuildCompileCommand(build, decorated, decorated.getListener(), publisher, home, envVars).constructArguments();
                    prefix = args.toArray(new String[args.size()]);
                    cmds.addAll(0, args);
                } else {
                    prefix = new String[0];
                }
            }

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

            starter = starter.cmds(cmds);
            boolean[] masks = starter.masks();
            if(masks == null) {
                masks = new boolean[cmds.size()];
                starter = starter.masks(masks);
            } else {
                starter = starter.masks(prefix(masks));
            }

            CoverityLauncherDecorator.CoverityBuildStep.set(false);
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
    public void setupIntermediateDirectory(@Nonnull AbstractBuild<?,?> build, @Nonnull TaskListener listener, @Nonnull Node node){
        Validate.notNull(build, AbstractBuild.class.getName() + " object can't be null");
        Validate.notNull(listener, TaskListener.class.getName() + " object can't be null");
        Validate.notNull(node, Node.class.getName() + " object can't be null");
        Validate.notNull(envVars, EnvVars.class.getName() + " object can't be null");
        if(!envVars.containsKey("COV_IDIR")){
            FilePath temp = null;
            InvocationAssistance invocationAssistance = CoverityUtils.getInvocationAssistance(build);
            try {
                if(invocationAssistance == null || invocationAssistance.getIntermediateDir() == null ||
                        invocationAssistance.getIntermediateDir().isEmpty()){
                    FilePath coverityDir = node.getRootPath().child("coverity");
                    coverityDir.mkdirs();
                    temp = coverityDir.createTempDir("temp-", null);
                } else {
                    String customIdir = EnvParser.interpolateRecursively(invocationAssistance.getIntermediateDir(), 1, envVars);
                    if(customIdir == null || customIdir.isEmpty()){
                        throw new Exception("The specified Intermediate Directory is not valid: " + invocationAssistance.getIntermediateDir());
                    }

                    File idir = new File(customIdir);
                    String workspace = envVars.get("WORKSPACE");
                    if (idir != null && !idir.isAbsolute() && StringUtils.isNotEmpty(workspace)) {
                        customIdir = FilenameUtils.concat(workspace, customIdir);
                        if (StringUtils.isNotEmpty(customIdir)) {

                            if (node.toComputer().isUnix()){
                                customIdir = customIdir.replace("\\", "/");
                            } else{
                                customIdir = customIdir.replace("/", "\\");
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty(customIdir)){
                        temp = new FilePath(node.getChannel(), customIdir);
                        temp.mkdirs();
                    }
                }

                if(invocationAssistance != null){
                    build.addAction(new CoverityTempDir(temp, invocationAssistance.getIntermediateDir() == null));
                } else{
                    build.addAction(new CoverityTempDir(temp, true));
                }
            } catch(IOException e) {
                CoverityUtils.handleException("Error while creating temporary directory for Coverity", build, listener, e);
            } catch(InterruptedException e) {
                CoverityUtils.handleException("Interrupted while creating temporary directory for Coverity", build, listener, e);
            } catch (ParseException e) {
                CoverityUtils.handleException(e.getMessage(), build, listener, e);
            } catch (Exception e){
                if (temp != null){
                    CoverityUtils.handleException("An error occurred while setting intermediate directory: " + temp.getRemote(), build, listener, e);
                } else{
                    CoverityUtils.handleException("An error occurred while setting intermediate directory", build, listener, e);
                }

            }
            if (temp != null) {
                envVars.put("COV_IDIR", temp.getRemote());
            }
        }
    }
}

