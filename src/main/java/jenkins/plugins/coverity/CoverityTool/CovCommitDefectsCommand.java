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

public class CovCommitDefectsCommand extends CovCommand {

    private static final String command = "cov-commit-defects";
    private static final String hostArg = "--host";
    private static final String dataPort = "--dataport";
    private static final String httpsPort = "--https-port";
    private static final String port = "--port";
    private static final String useSslArg = "--ssl";
    private static final String onNewCertArg = "--on-new-cert";
    private static final String trustArg = "trust";
    private static final String certArg = "--cert";
    private static final String streamArg = "--stream";
    private static final String userArg = "--user";
    private static final String coverity_passphrase = "COVERITY_PASSPHRASE";

    private CIMInstance cimInstance;
    private CIMStream cimStream;
    private CoverityVersion version;
    private InvocationAssistance invocationAssistance;

    public CovCommitDefectsCommand(
            AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars,
            CIMStream cimStream, CIMInstance cimInstance, CoverityVersion version) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.cimStream = cimStream;
        this.cimInstance = cimInstance;
        this.version = version;

        if (publisher != null && publisher.getInvocationAssistance() != null) {
            invocationAssistance = publisher.getInvocationAssistance();
            if (cimStream.getInvocationAssistanceOverride() != null) {
                invocationAssistance = invocationAssistance.merge(cimStream.getInvocationAssistanceOverride());
            }
        }

        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addIntermediateDir();
        addHost();

        if (cimInstance.getDataPort() != 0){
            addDataPort();
            addSslConfiguration();
        } else if (cimInstance.isUseSSL()) {
            addHtppsPort();
            addSslConfiguration();
        } else {
            addPort();
        }

        addStream();
        addUserInfo();
        addCommitArguments();
    }

    private void addHost() {
        addArgument(hostArg);
        addArgument(cimInstance.getHost());
    }

    private void addDataPort() {
        addArgument(dataPort);
        addArgument(Integer.toString(cimInstance.getDataPort()));
    }

    private void addHtppsPort() {
        addArgument(httpsPort);
        addArgument(Integer.toString(cimInstance.getPort()));
    }

    private void addPort() {
        addArgument(port);
        addArgument(Integer.toString(cimInstance.getPort()));
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

    private void addStream() {
        addArgument(streamArg);
        addArgument(CoverityUtils.doubleQuote(cimStream.getStream(), invocationAssistance.getUseAdvancedParser()));
    }

    private void addUserInfo() {
        addArgument(userArg);
        addArgument(cimInstance.getUser());
        envVars.put(coverity_passphrase, cimInstance.getPassword());
    }

    private void addCommitArguments() {
        if (!StringUtils.isEmpty(invocationAssistance.getCommitArguments())) {
            try{
                addArguments(EnvParser.tokenize(invocationAssistance.getCommitArguments()));
            }catch(ParseException e) {
                throw new RuntimeException("ParseException occurred during tokenizing the cov-commit-defect commit arguments.");
            }
        }
    }
}
