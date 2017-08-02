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

import com.coverity.ws.v9.SnapshotIdDataObj;
import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.*;
import com.coverity.ws.v9.StreamIdDataObj;
import com.coverity.ws.v9.SnapshotFilterSpecDataObj;

import java.util.List;

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

    public CovManageHistoryCommand(
            AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher,
            String home, EnvVars envVars, CIMStream cimStream, CIMInstance cimInstance) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.cimStream = cimStream;
        this.cimInstance = cimInstance;
    }

    @Override
    protected void prepareCommand() {
        addArgument(downloadArg);
        addCimStreamInfo();
        if (cimInstance.isUseSSL()){
            addArgument(useSslArg);
        }
        addSslConfiguration(cimInstance);
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

        if (!checkSnapshot()){
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
        addArgument(cimInstance.getCoverityUser());
        envVars.put(coverity_passphrase, cimInstance.getCoverityPassword());
    }

    private boolean checkSnapshot() {

        StreamIdDataObj streamId = new StreamIdDataObj();
        streamId.setName(cimStream.getStream());

        SnapshotFilterSpecDataObj filter = new SnapshotFilterSpecDataObj();
        try{
            List<SnapshotIdDataObj> snapshotList = cimInstance.getConfigurationService().getSnapshotsForStream(streamId, filter);
            if (snapshotList == null || snapshotList.isEmpty()) {
                return false;
            }
            return true;
        }catch(Exception e) {
            launcher.getListener().getLogger().println("Error occurred while checking the stream \"" + cimStream.getStream() + "\" has any snapshots");
            return false;
        }
    }
}
