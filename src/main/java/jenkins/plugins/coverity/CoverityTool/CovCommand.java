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
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CovCommand {

    private static final String intermediateDirArguments = "--dir";
    private static final String covIdirEnvVar = "COV_IDIR";
    private static final String useSslArg = "--ssl";
    private static final String onNewCertArg = "--on-new-cert";
    private static final String trustArg = "trust";
    private static final String certArg = "--cert";

    protected List<String> commandLine;
    protected AbstractBuild build;
    private Launcher launcher;
    protected TaskListener listener;
    protected CoverityPublisher publisher;
    protected EnvVars envVars;

    public CovCommand(
            @Nonnull String command,
            @Nonnull AbstractBuild<?, ?> build,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener listener,
            @Nonnull CoverityPublisher publisher,
            String home,
            @Nonnull EnvVars envVars){

        Validate.notEmpty(command, "Command cannot be null empty or null");
        Validate.notNull(build, AbstractBuild.class.getName() + " object cannot be null");
        Validate.notNull(launcher, Launcher.class.getName() + " object cannot be null");
        Validate.notNull(listener, TaskListener.class.getName() + " object cannot be null");
        Validate.notNull(publisher, CoverityPublisher.class.getName() + " object cannot be null");
        Validate.notNull(envVars, EnvVars.class.getName() + " object cannot be null");

        this.build = build;
        this.launcher = launcher;
        this.listener = listener;
        this.publisher = publisher;
        this.envVars = envVars;

        commandLine = new ArrayList<>();
        addCommand(command, home);
        addIntermediateDir();
    }

    private void addCommand(String cmd, String home){
        String command = cmd;

        if (!StringUtils.isEmpty(home)){
            command = new FilePath(launcher.getChannel(), home).child("bin").child(command).getRemote();
        }
        commandLine.add(command);
    }

    protected void addIntermediateDir(){
        commandLine.add(intermediateDirArguments);
        String idir = envVars.get(covIdirEnvVar);
        if (!StringUtils.isEmpty(idir)){
            commandLine.add(idir);
        }else{
            CoverityTempDir tempDir = build.getAction(CoverityTempDir.class);
            commandLine.add(tempDir.getTempDir().getRemote());
        }
    }

    protected void addArgument(String args){
        commandLine.add(args);
    }

    protected void addArguments(List<String> args){
        commandLine.addAll(args);
    }

    public int runCommand() throws IOException, InterruptedException {
        boolean useAdvancedParser = false;
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance != null && invocationAssistance.getUseAdvancedParser()){
            useAdvancedParser = true;
        }

        return CoverityUtils.runCmd(commandLine, build, launcher, listener, envVars, useAdvancedParser);
    }

    public List<String> getCommandLines() {
        return commandLine;
    }

    protected void addTaCommandArgs(){
        if (publisher == null){
            return;
        }
        TaOptionBlock taOptionBlock = publisher.getTaOptionBlock();
        if (taOptionBlock != null){
            addArguments(taOptionBlock.getTaCommandArgs());
        }
    }

    protected void addSslConfiguration(CIMInstance cimInstance, CoverityVersion version) {
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

    protected abstract void prepareCommand();
}
