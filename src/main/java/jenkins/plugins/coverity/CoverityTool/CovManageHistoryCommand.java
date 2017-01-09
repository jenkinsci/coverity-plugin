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

public class CovManageHistoryCommand extends CovCommand {

    private static final String command = "cov-manage-history";
    private static final String downloadArg = "download";
    private static final String hostArg = "--host";
    private static final String portArg = "--port";
    private static final String streamArg = "--stream";
    private static final String userArg = "--user";
    private static final String useSslArg = "--ssl";
    private static final String onNewCertArg = "--on-new-cert";
    private static final String trustArg = "trust";
    private static final String certArg = "--cert";
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
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addIntermediateDir();
        addArgument(downloadArg);
        addCimStreamInfo();
        addSslConfiguration();
        addUserInfo();
        addArgument(mergeArg);
    }

    private void addCimStreamInfo(){
        addArgument(hostArg);
        addArgument(cimInstance.getHost());
        addArgument(portArg);
        addArgument(Integer.toString(cimInstance.getPort()));
        addArgument(streamArg);
        addArgument(CoverityUtils.doubleQuote(cimStream.getStream(), publisher.getInvocationAssistance().getUseAdvancedParser()));
    }

    private void addSslConfiguration() {
        if(cimInstance.isUseSSL()){
            addArgument(useSslArg);

            if (version.compareTo(CoverityVersion.VERSION_JASPER) >= 0) {
                boolean isTrustNewSelfSignedCert = false;
                String certFileName = null;
                SSLConfigurations sslConfigurations = publisher.getDescriptor().getSslConfigurations();
                if(sslConfigurations != null){
                    isTrustNewSelfSignedCert = sslConfigurations.isTrustNewSelfSignedCert();
                    certFileName = sslConfigurations.getCertFileName();

                    if(isTrustNewSelfSignedCert){
                        addArgument(onNewCertArg);
                        addArgument(trustArg);
                    }
                    if(certFileName != null){
                        addArgument(certArg);
                        addArgument(certFileName);
                    }
                }
            }
        }
    }

    private void addUserInfo() {
        addArgument(userArg);
        addArgument(cimInstance.getUser());
        envVars.put(coverity_passphrase, cimInstance.getPassword());
    }
}
