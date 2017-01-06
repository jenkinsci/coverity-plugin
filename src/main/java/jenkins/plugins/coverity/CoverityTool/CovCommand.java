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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CovCommand {

    private static final String intermediateDirArguments = "--dir";
    private static final String covIdirEnvVar = "$COV_IDIR";

    protected List<String> commandLine;
    protected AbstractBuild build;
    private Launcher launcher;
    protected TaskListener listener;
    protected CoverityPublisher publisher;
    private EnvVars envVars;

    public CovCommand(String command, AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars){
        this.build = build;
        this.launcher = launcher;
        this.listener = listener;
        this.publisher = publisher;
        this.envVars = envVars;

        commandLine = new ArrayList<>();
        addCommand(command, home);
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
        String idir = envVars.expand(covIdirEnvVar);
        if (!StringUtils.isEmpty(idir) && !idir.equalsIgnoreCase(covIdirEnvVar)){
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

        return CoverityUtils.runCmd(commandLine, build, launcher, listener, build.getEnvironment(listener), useAdvancedParser);
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

    protected abstract void prepareCommand();
}
