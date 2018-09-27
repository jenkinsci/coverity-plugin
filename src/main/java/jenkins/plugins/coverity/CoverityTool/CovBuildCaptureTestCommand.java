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
package jenkins.plugins.coverity.CoverityTool;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.EnvParser;
import jenkins.plugins.coverity.ParseException;
import jenkins.plugins.coverity.TaOptionBlock;
import org.apache.commons.lang.StringUtils;

public class CovBuildCaptureTestCommand extends CoverityCommand {

    private static final String command = "cov-build";

    public CovBuildCaptureTestCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars) {
        super(command, build, launcher, listener, publisher, home, envVars);
    }

    @Override
    protected void prepareCommand() {
        addArgument("--test-capture");
        addTaCommandArgs();
        addCustomTestCommand();
        listener.getLogger().println("[Coverity] cov-build capture test command line arguments: " + commandLine.toString());
    }

    @Override
    protected boolean canExecute() {
        if (publisher.getTaOptionBlock() == null) {
            return false;
        }

        if (StringUtils.isEmpty(publisher.getTaOptionBlock().getCustomTestCommand())) {
            return false;
        }

        return true;
    }
}
