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

import java.io.File;

public class CovAnalyzeCommand extends CovCommand {

    private static final String command = "cov-analyze";
    private static final String misraConfig = "--misra-config";
    private static final String testAdvisor = "--test-advisor";
    private static final String testAdvisorPolicyFile = "--test-advisor-policy";
    private static final String stripPath = "--strip-path";


    public CovAnalyzeCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars) {
        super(command, build, launcher, listener, publisher, home, envVars);
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addMisraConfiguration();
        addTaConfiguration();
        addAdditionalAnalysisArguments();
    }

    private void addMisraConfiguration(){
        InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        if (invocationAssistance != null && invocationAssistance.getIsUsingMisra()){
            String misraConfigFile = invocationAssistance.getMisraConfigFile();
            if (!StringUtils.isEmpty(misraConfigFile)){
                File configFile = new File(misraConfigFile);
                if (configFile.isFile()){
                    addArgument(misraConfig);
                    addArgument(misraConfigFile);
                }else{
                    throw new RuntimeException("Could not find MISRA configuration file at \"" + configFile.getAbsolutePath() + "\"");
                }
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
                File policyFile = new File(taPolicyFile);
                if (policyFile.isFile()){
                    addArgument(testAdvisor);
                    addArgument(testAdvisorPolicyFile);
                    addArgument(taPolicyFile);
                    addArgument(stripPath);
                    if (StringUtils.isEmpty(taOptionBlock.getTaStripPath())){
                        addArgument(build.getWorkspace().getRemote());
                    }else{
                        addArgument(taOptionBlock.getTaStripPath());
                    }
                }else{
                    throw new RuntimeException("Could not find test policy file at \"" + policyFile.getAbsolutePath() + "\"");
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
