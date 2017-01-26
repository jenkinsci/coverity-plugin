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

import java.util.List;

public class CovBuildScriptCommand extends CoverityCommand {

    private static final String command = "cov-build";
    private static final String noCommandArg = "--no-command";
    private static final String fileSystemCapture = "--fs-capture-search";

    public CovBuildScriptCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars) {
        super(command, build, launcher, listener, publisher, home, envVars);
    }

    @Override
    protected void prepareCommand() {
        addArgument(noCommandArg);
        addScriptSourcesArgs();
        addAdditionalBuildArguments();

        listener.getLogger().println("[Coverity] cov-build command line arguments for script sources: " + commandLine.toString());
    }

    @Override
    protected boolean canExecute() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance == null || !invocationAssistance.getIsScriptSrc()) {
            return false;
        }

        return true;
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
