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
import jenkins.plugins.coverity.EnvParser;
import jenkins.plugins.coverity.ParseException;
import jenkins.plugins.coverity.TaOptionBlock;
import org.apache.commons.lang.StringUtils;

public class CovCaptureCommand extends CovCommand {

    private static final String command = "cov-capture";

    public CovCaptureCommand(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener, CoverityPublisher publisher, String home, EnvVars envVars) {
        super(command, build, launcher, listener, publisher, home, envVars);
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        addIntermediateDir();
        addTaCommandArgs();
        addCustomTestCommand();
    }

    private void addCustomTestCommand(){
        if (publisher == null){
            return;
        }
        TaOptionBlock taOptionBlock = publisher.getTaOptionBlock();
        try{
            if (taOptionBlock != null && !StringUtils.isEmpty(taOptionBlock.getCustomTestCommand())){
                addArguments(EnvParser.tokenize(taOptionBlock.getCustomTestCommand()));
            }
        }catch(ParseException parseException){
            throw new RuntimeException("ParseException occurred during tokenizing the cov capture custom test command.");
        }
    }
}
