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
import hudson.model.BuildListener;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityTempDir;
import jenkins.plugins.coverity.CoverityUtils;
import jenkins.plugins.coverity.InvocationAssistance;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CovCommand {

    private static final String intermediateDirArguments = "--dir";

    protected List<String> commandLine;
    protected AbstractBuild build;
    private Launcher launcher;
    protected BuildListener buildListener;
    protected CoverityPublisher publisher;
    private CoverityTempDir coverityTempDir;

    public CovCommand(String command, AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher, String home){
        this.build = build;
        this.launcher = launcher;
        this.buildListener = listener;
        this.publisher = publisher;
        this.coverityTempDir = build.getAction(CoverityTempDir.class);

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
        commandLine.add(coverityTempDir.getTempDir().getRemote());
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

        return CoverityUtils.runCmd(commandLine, build, launcher, buildListener, build.getEnvironment(buildListener), useAdvancedParser);
    }
    public List<String> getCommandLines() {
        return commandLine;
    }

    protected abstract void prepareCommand();
}
