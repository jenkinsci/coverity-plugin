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
import jenkins.plugins.coverity.ParseException;
import org.apache.commons.lang.StringUtils;

public class PostCovAnalyzeCommand extends CommandBase {

    public PostCovAnalyzeCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, EnvVars envVars) {
        super(build, launcher, listener, publisher, envVars);
    }

    @Override
    protected void prepareCommand() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        try{
            if (invocationAssistance != null) {
                String postCovAnalyzeCmd = invocationAssistance.getPostCovAnalyzeCmd();
                if (!StringUtils.isEmpty(postCovAnalyzeCmd)) {
                    addArguments(EnvParser.tokenize(postCovAnalyzeCmd));
                }
            }
        } catch(ParseException e) {
            throw new RuntimeException("ParseException occurred during tokenizing the post cov-analyze command.");
        }
    }
}
