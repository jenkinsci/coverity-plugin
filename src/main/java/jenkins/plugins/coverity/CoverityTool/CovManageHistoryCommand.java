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

public class CovManageHistoryCommand extends CoverityCommand {

    private static final String command = "cov-manage-history";
    private static final String downloadArg = "download";
    private static final String hostArg = "--host";
    private static final String portArg = "--port";
    private static final String streamArg = "--stream";
    private static final String userArg = "--user";
    private static final String mergeArg = "--merge";
    private static final String coverity_passphrase = "COVERITY_PASSPHRASE";

    private CIMInstance cimInstance;
    private CIMStream cimStream;
    private CoverityVersion version;

    public CovManageHistoryCommand(
            AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher,
            String home, EnvVars envVars, CIMStream cimStream, CIMInstance cimInstance, CoverityVersion version) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.cimStream = cimStream;
        this.cimInstance = cimInstance;
        this.version = version;
    }

    @Override
    protected void prepareCommand() {
        addArgument(downloadArg);
        addCimStreamInfo();
        addSslConfiguration(cimInstance, version);
        addUserInfo();
        addArgument(mergeArg);
        listener.getLogger().println("[Coverity] cov-manage-history command line arguments: " + commandLine.toString());
    }

    @Override
    protected boolean canExecute() {
        TaOptionBlock taOptionBlock = publisher.getTaOptionBlock();

        if (taOptionBlock == null || !taOptionBlock.getCovHistoryCheckbox()) {
            return false;
        }
        return true;
    }

    private void addCimStreamInfo(){
        addArgument(hostArg);
        addArgument(cimInstance.getHost());
        addArgument(portArg);
        addArgument(Integer.toString(cimInstance.getPort()));
        addArgument(streamArg);
        addArgument(CoverityUtils.doubleQuote(cimStream.getStream(), publisher.getInvocationAssistance().getUseAdvancedParser()));
    }

    private void addUserInfo() {
        addArgument(userArg);
        addArgument(cimInstance.getUser());
        envVars.put(coverity_passphrase, cimInstance.getPassword());
    }
}
