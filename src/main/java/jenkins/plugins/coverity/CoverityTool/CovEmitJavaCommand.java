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
import jenkins.plugins.coverity.CoverityUtils;
import jenkins.plugins.coverity.InvocationAssistance;

import java.util.ArrayList;
import java.util.List;

public class CovEmitJavaCommand extends CovCommand {

    private static final String command = "cov-emit-java";
    private static final String webAppArchive = "--webapp-archive";
    private EnvVars envVars;
    private boolean useAdvancedParser;

    public CovEmitJavaCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars, boolean useAdvancedParser) {
        super(command, build, launcher, listener, publisher, home, envVars);
        this.envVars = envVars;
        this.useAdvancedParser = useAdvancedParser;
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addJavaWarFiles();
    }

    private void addJavaWarFiles(){
        List<String> javaWarFiles = new ArrayList<String>();
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if(invocationAssistance != null){
            List<String> givenWarFiles = invocationAssistance.getJavaWarFilesNames();
            if(givenWarFiles != null && !givenWarFiles.isEmpty()){
                for(String givenJar : givenWarFiles){
                    String javaWarFile = CoverityUtils.evaluateEnvVars(givenJar, envVars, useAdvancedParser);
                    if(javaWarFile != null) {
                        javaWarFiles.add(javaWarFile);
                    }
                }
            }
        }

        if(javaWarFiles != null && !javaWarFiles.isEmpty()){
            for(String javaWarFile : javaWarFiles){
                addArgument(webAppArchive);
                addArgument(javaWarFile);
            }
        }
    }
}
