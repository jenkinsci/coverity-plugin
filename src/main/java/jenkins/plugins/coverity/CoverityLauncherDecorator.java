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
import hudson.Launcher;
import hudson.LauncherDecorator;
import hudson.model.*;

/**
 * This makes sure that all commands executed (when invocation assistance is enabled) are run through cov-build.
 */
@Extension
public class CoverityLauncherDecorator extends LauncherDecorator {
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

        return new CoverityLauncher(launcher, node);
    }
}
