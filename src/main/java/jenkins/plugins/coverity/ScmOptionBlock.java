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
package jenkins.plugins.coverity;

import hudson.Util;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class ScmOptionBlock {

    private final String scmSystem;
    private String customTestTool;
    private String scmToolArguments;
    private String scmCommandArgs;
    private String logFileLoc;

    /*
     * Required field if perforce or perforce2009 is configured
     */
    private String p4Port;

    /*
     * Required field if accurev is configured
     */
    private String accRevRepo;
    private String scmAdditionalCmd;
    private String fileRegex;

    @DataBoundConstructor
    public ScmOptionBlock(String scmSystem){
        this.scmSystem = scmSystem;
    }

    public String getScmSystem(){return scmSystem;}

    @DataBoundSetter
    public void setCustomTestTool(String customTestTool){
        this.customTestTool = Util.fixEmpty(customTestTool);
    }

    public String getCustomTestTool(){return customTestTool;}

    @DataBoundSetter
    public void setScmToolArguments(String scmToolArguments){
        this.scmToolArguments = Util.fixEmpty(scmToolArguments);
    }

    public String getScmToolArguments(){return scmToolArguments;}

    @DataBoundSetter
    public void setScmCommandArgs(String scmCommandArgs){
        this.scmCommandArgs = Util.fixEmpty(scmCommandArgs);
    }

    public String getScmCommandArgs(){return scmCommandArgs;}

    @DataBoundSetter
    public void setLogFileLoc(String logFileLoc){
        this.logFileLoc = Util.fixEmpty(logFileLoc);
    }

    public String getLogFileLoc(){return logFileLoc;}

    @DataBoundSetter
    public void setP4Port(String p4Port){
        this.p4Port = Util.fixEmpty(p4Port);
    }

    public String getP4Port(){return p4Port;}

    @DataBoundSetter
    public void setAccRevRepo(String accRevRepo){
        this.accRevRepo = Util.fixEmpty(accRevRepo);
    }

    public String getAccRevRepo(){return accRevRepo;}

    @DataBoundSetter
    public void setScmAdditionalCmd(String scmAdditionalCmd){
        this.scmAdditionalCmd = Util.fixEmpty(scmAdditionalCmd);
    }

    public String getScmAdditionalCmd() {
        return scmAdditionalCmd;
    }

    @DataBoundSetter
    public void setFileRegex(String fileRegex){
        this.fileRegex = Util.fixEmpty(fileRegex);
    }

    public String getFileRegex() {
        return fileRegex;
    }

    public String checkScmConfig(){
        // Checking the required fields for specific SCM systems

        String errorText = StringUtils.EMPTY;
        Boolean delim = true;
        if(this.scmSystem.equals("accurev") && StringUtils.isEmpty(this.accRevRepo)){
            errorText += "[SCM] Please specify AccuRev's source control repository under 'Advanced' \n";
            delim = false;
        }

        if(this.scmSystem.equals("perforce") && StringUtils.isEmpty(this.p4Port)){
            errorText += "[SCM] Please specify Perforce's port environment variable under 'Advanced'\n ";
            delim = false;
        }

        if(delim){
            errorText = "Pass";
        }
        return errorText;
    }
}
