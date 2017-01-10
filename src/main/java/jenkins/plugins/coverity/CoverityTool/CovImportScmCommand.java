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

public class CovImportScmCommand extends CovCommand{

    private static final String command = "cov-import-scm";
    private static final String scmFlag = "--scm";
    private static final String toolFlag = "--tool";
    private static final String toolArgsFlag = "--tool-arg";
    private static final String commandArgFlag = "--command-arg";
    private static final String scmLog = "--log";
    private static final String scmFileRegex = "--filename-regex";
    private static final String projectRootFlag = "--project-root";

    private ScmOptionBlock scmOptionBlock;
    private boolean useAdvancedParser;

    public CovImportScmCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars) {
        super(command, build, launcher, listener, publisher, home, envVars);
        scmOptionBlock = publisher.getScmOptionBlock();
        if (publisher != null && publisher.getInvocationAssistance() != null){
            useAdvancedParser = publisher.getInvocationAssistance().getUseAdvancedParser();
        }
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addScmTool();
        addScmToolArguments();
        addScmCommandArguments();
        addLogFileLocation();
        addFileRegex();

        if (scmOptionBlock.getScmSystem().equalsIgnoreCase("accurev")) {
            addAccurevProjectRoot();
        }

        else if (scmOptionBlock.getScmSystem().equalsIgnoreCase("perforce")
                || scmOptionBlock.getScmSystem().equalsIgnoreCase("perforce2009")) {
            addP4port();
        }

        addScmAdditionalCommand();
    }

    private void addScmTool() {
        addArgument(scmFlag);
        addArgument(scmOptionBlock.getScmSystem());
        if(scmOptionBlock.getCustomTestTool() != null) {
            addArgument(toolFlag);
            addArgument(scmOptionBlock.getCustomTestTool());
        }
    }

    private void addScmToolArguments() {
        if(scmOptionBlock.getScmToolArguments() != null){
            addArgument(toolArgsFlag);
            addArgument(CoverityUtils.doubleQuote(scmOptionBlock.getScmToolArguments(), useAdvancedParser));
        }
    }

    private void addScmCommandArguments() {
        if(scmOptionBlock.getScmCommandArgs() != null){
            addArgument(commandArgFlag);
            addArgument(CoverityUtils.doubleQuote(scmOptionBlock.getScmCommandArgs(), useAdvancedParser));
        }
    }

    private void addLogFileLocation() {
        if(scmOptionBlock.getLogFileLoc() != null){
            addArgument(scmLog);
            addArgument(scmOptionBlock.getLogFileLoc());
        }
    }

    private void addFileRegex() {
        if(scmOptionBlock.getFileRegex() != null){
            addArgument(scmFileRegex);
            addArgument(scmOptionBlock.getFileRegex());
        }
    }

    private void addAccurevProjectRoot() {
        // Adding accurev's root repo, which is optional
        if(scmOptionBlock.getAccRevRepo() != null){
            addArgument(projectRootFlag);
            addArgument(scmOptionBlock.getAccRevRepo());
        }
    }

    private void addP4port() {
        if(!StringUtils.isEmpty(scmOptionBlock.getP4Port())){
            envVars.put("P4PORT",CoverityUtils.evaluateEnvVars(scmOptionBlock.getP4Port(), envVars, useAdvancedParser));
        }
    }

    private void addScmAdditionalCommand() {
        try{
            if(scmOptionBlock.getScmAdditionalCmd() != null) {
                addArguments(EnvParser.tokenize(scmOptionBlock.getScmAdditionalCmd()));
            }
        }catch(ParseException e) {
            throw new RuntimeException("ParseException occurred during tokenizing the cov import scm additional command.");
         }
    }
}
