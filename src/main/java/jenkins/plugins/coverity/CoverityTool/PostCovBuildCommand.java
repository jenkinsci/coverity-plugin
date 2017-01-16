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
import jenkins.plugins.coverity.*;
import org.apache.commons.lang.StringUtils;

public class PostCovBuildCommand extends Command {

    public PostCovBuildCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, EnvVars envVars) {
        super(build, launcher, listener, publisher, envVars);
    }

    @Override
    protected void prepareCommand() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        try{
            String postCovBuildCmd = invocationAssistance.getPostCovBuildCmd();
            if (!StringUtils.isEmpty(postCovBuildCmd)) {
                addArguments(EnvParser.tokenize(postCovBuildCmd));
            }
            listener.getLogger().println("[Coverity] post cov-build command: " + commandLine.toString());
        } catch(ParseException e) {
            throw new RuntimeException("ParseException occurred during tokenizing the post cov-build command.");
        }
    }

    @Override
    protected boolean canExecute() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance == null || !invocationAssistance.getIsUsingPostCovBuildCmd()) {
            return false;
        }

        return true;
    }
}
