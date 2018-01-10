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
import jenkins.plugins.coverity.*;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;

public class CovAnalyzeCommand extends CoverityCommand {

    private static final String command = "cov-analyze";
    private static final String misraConfig = "--misra-config";
    private static final String testAdvisor = "--test-advisor";
    private static final String testAdvisorPolicyFile = "--test-advisor-policy";
    private static final String stripPath = "--strip-path";


    public CovAnalyzeCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars) {
        super(command, build, launcher, listener, publisher, home, envVars);
    }

    @Override
    protected void prepareCommand() {
        addMisraConfiguration();
        addTaConfiguration();
        addAdditionalAnalysisArguments();
        listener.getLogger().println("[Coverity] cov-analyze command line arguments: " + commandLine.toString());
    }

    @Override
    protected boolean canExecute() {
        if (publisher.getInvocationAssistance() != null ||
                publisher.getTaOptionBlock() != null) {
            return true;
        }

        return false;
    }

    private void addMisraConfiguration(){
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance != null && invocationAssistance.getIsUsingMisra()){
            String misraConfigFile = invocationAssistance.getMisraConfigFile();
            if (!StringUtils.isEmpty(misraConfigFile)){
                addArgument(misraConfig);
                addArgument(misraConfigFile);
            } else{
                throw new RuntimeException("Misra configuration file is required to run Misra analysis.");
            }
        }
    }

    private void addTaConfiguration(){
        TaOptionBlock taOptionBlock = publisher.getTaOptionBlock();
        if (taOptionBlock != null){
            String taPolicyFile = taOptionBlock.getPolicyFile();
            if (!StringUtils.isEmpty(taPolicyFile)){
                addArgument(testAdvisor);
                addArgument(testAdvisorPolicyFile);
                addArgument(taPolicyFile);

                List<TaStripPath> taStripPaths = taOptionBlock.getTaStripPaths();
                if (taStripPaths == null || taStripPaths.isEmpty()) {
                    addArgument(stripPath);
                    addArgument(build.getWorkspace().getRemote());
                }else {
                    for (TaStripPath path : taStripPaths) {
                        addArgument(stripPath);
                        addArgument(path.getTaStripPath());
                    }
                }
            }else{
                throw new RuntimeException("Test Advisor Policy File is required to run the Test Advisor.");
            }
        }
    }

    private void addAdditionalAnalysisArguments() {
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        try{
            if (invocationAssistance != null){
                String additionalArgument = invocationAssistance.getAnalyzeArguments();
                if (!StringUtils.isEmpty(additionalArgument)){
                    addArguments(EnvParser.tokenize(additionalArgument));
                }
            }
        }catch(ParseException parseException){
            throw new RuntimeException("ParseException occurred during tokenizing the cov analyze additional arguments.");
        }
    }
}
