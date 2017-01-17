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

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.EnvParser;
import jenkins.plugins.coverity.InvocationAssistance;
import org.apache.commons.lang.StringUtils;

public class CovBuildCommand extends CoverityCommand {

    private static final String command = "cov-build";
    private static final String noCommandArg = "--no-command";
    private static final String fileSystemCapture = "--fs-capture-search";
    private boolean catpureCompileSrc;

    public CovBuildCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars, boolean catpureCompileSrc) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.catpureCompileSrc = catpureCompileSrc;
    }

    @Override
    protected void prepareCommand() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance != null){
            if (!catpureCompileSrc && invocationAssistance.getIsScriptSrc()){
                prepareCovBuildCommandForScriptSources();
            } else if (catpureCompileSrc && invocationAssistance.getIsCompiledSrc()){
                prepareCovBuildCommandForCompileSources();
            }
        }
        listener.getLogger().println("[Coverity] cov-build command line arguments: " + commandLine.toString());
    }

    @Override
    protected boolean canExecute() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance == null) {
            return false;
        }

        if (!catpureCompileSrc && !invocationAssistance.getIsScriptSrc()) {
            return false;
        }

        if (catpureCompileSrc && !invocationAssistance.getIsCompiledSrc()) {
            return false;
        }

        return true;
    }

    private void prepareCovBuildCommandForScriptSources() {
        addArgument(noCommandArg);
        addScriptSourcesArgs();
    }

    private void prepareCovBuildCommandForCompileSources() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();

        // It is possible for users to run cov-build for compiled sources and script sources at the same time.
        if (invocationAssistance.getIsScriptSrc()) {
            addScriptSourcesArgs();
        }

        addTaCommandArgs();
        addAdditionalBuildArguments();
    }

    private void addScriptSourcesArgs() {
        addArgument(fileSystemCapture);
        addArgument("$WORKSPACE");
    }

    private void addAdditionalBuildArguments() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        String buildArgs = invocationAssistance.getBuildArguments();
        if (!StringUtils.isEmpty(buildArgs)){
            addArguments(EnvParser.tokenizeWithRuntimeException(buildArgs));
        }
    }
}
