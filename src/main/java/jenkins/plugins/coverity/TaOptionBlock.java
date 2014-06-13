/*******************************************************************************
 * Copyright (c) 2014 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.Util;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.util.*;

public class TaOptionBlock {
    private final String customTestCommand;
    private final boolean cOptionBlock;
    private final boolean csOptionBlock;
    private final String scmSystem;
    private final String customTestTool;
    private final String scmToolArguments;
    private final String scmCommandArgs;
    private final String logFileLoc;
    private final String csFramework;
    private final String csCoverageTool;
    private final String cxxCoverageTool;
    private final String javaCoverageTool;
    private final boolean junitFramework;
    private final boolean junit4Framework;
    private final boolean scmOptionBlock;
    private final String policyFile;
    private final String taStripPath;
    private final String p4Port;
    private final String accRevRepo;
    private final String bullsEyeDir;
    private final String customWorkDir;
    private final boolean covHistoryCheckbox;


    private final boolean javaOptionBlock;
    /*
        Generic Contructor that will hold all the elements we need for Test Advisor. All of these variables 
        are fields within the Test Advisor Option Block
     */
    @DataBoundConstructor
    public TaOptionBlock(String customTestCommand,
                         boolean cOptionBlock,
                         boolean csOptionBlock,
                         boolean javaOptionBlock,
                         String scmSystem,
                         String customTestTool,
                         String scmToolArguments,
                         String scmCommandArgs,
                         String logFileLoc,
                         String csFramework,
                         String csCoverageTool,
                         String cxxCoverageTool,
                         String javaCoverageTool,
                         boolean junitFramework,
                         boolean junit4Framework,
                         boolean scmOptionBlock,
                         String policyFile,
                         String taStripPath,
                         String p4Port,
                         String accRevRepo,
                         String bullsEyeDir,
                         String customWorkDir,
                         boolean covHistoryCheckbox) {
        this.customTestCommand = Util.fixEmpty(customTestCommand);
        this.cOptionBlock = cOptionBlock;
        this.csOptionBlock = csOptionBlock;
        this.javaOptionBlock = javaOptionBlock;
        this.scmSystem = scmSystem;
        this.customTestTool = Util.fixEmpty(customTestTool);
        this.scmToolArguments = Util.fixEmpty(scmToolArguments);
        this.scmCommandArgs = Util.fixEmpty(scmCommandArgs);
        this.logFileLoc = Util.fixEmpty(logFileLoc);
        this.csFramework = csFramework;
        this.csCoverageTool = csCoverageTool;     // Required if other two are not selected
        this.cxxCoverageTool = cxxCoverageTool;    // Required if other two are not selected
        this.javaCoverageTool = javaCoverageTool; // Required if other two are not selected
        this.junit4Framework = junit4Framework;
        this.junitFramework = junitFramework;
        this.scmOptionBlock = scmOptionBlock;
        this.policyFile = Util.fixEmpty(policyFile);    // Required
        this.taStripPath = Util.fixEmpty(taStripPath); // Required
        this.p4Port = Util.fixEmpty(p4Port); // Required if perforce is selected
        this.accRevRepo = Util.fixEmpty(accRevRepo);   // Required if accurev is selected
        this.bullsEyeDir = Util.fixEmpty(bullsEyeDir); // Required if bulls eye is selected
        this.customWorkDir = Util.fixEmpty(customWorkDir); // Required if a custom command is issued
        this.covHistoryCheckbox = covHistoryCheckbox;
    }
    /*
    Required functions needed for jenkins to access all of the Test Advisor Data.
     */
    public String getCustomTestCommand(){return customTestCommand;}

    public boolean getCOptionBlock(){return cOptionBlock;}

    public boolean getCsOptionBlock(){return csOptionBlock;}

    public boolean getJavaOptionBlock(){return javaOptionBlock;}

    public String getScmSystem(){return scmSystem;}

    public String getCustomTestTool(){return customTestTool;}

    public String getScmToolArguments(){return scmToolArguments;}

    public String getScmCommandArgs(){return scmCommandArgs;}

    public String getLogFileLoc(){return logFileLoc;}

    public String getCsFramework(){return csFramework;}

    public String getCsCoverageTool(){return csCoverageTool;}

    public String getCxxCoverageTool(){return cxxCoverageTool;}

    public String getJavaCoverageTool(){return javaCoverageTool;}

    public boolean getJunit4Framework(){return junit4Framework;}

    public boolean getJunitFramework(){return junitFramework;}

    public boolean getScmOptionBlock(){return scmOptionBlock;}

    public String getPolicyFile(){return policyFile;}

    public String getTaStripPath(){return taStripPath;}

    public String getP4Port(){return p4Port;}

    public String getAccRevRepo(){return accRevRepo;}

    public String getBullsEyeDir(){return bullsEyeDir;}

    public String getCustomWorkDir(){return customWorkDir;}

    public boolean getCovHistoryCheckbox(){return covHistoryCheckbox;}



    /*
    Get Test Advisor Command Arguments
        - Builds the command line arguments for Test Advisor's cov-build and cov-capture command. 
     */
    public List<String> getTaCommandArgs(){

        List<String> args = new ArrayList<String>();

        if(this.cOptionBlock){
            // Adding C/C++ coverage switch
            args.add("--c-coverage");
            // Checking to make sure there is a coverage tool, and add that to the arguments
            if(!this.cxxCoverageTool.equals("none")){
                args.add(this.cxxCoverageTool);
                // Bullseye requires the directory of where bullseye is installed.
                if(this.cxxCoverageTool.equals("bullseye")){
                    args.add("--bullseye-dir");
                    args.add(this.bullsEyeDir);
                }
            }

        }

        // Checking to see if c# tests were selected and adding all arguments
        if(this.csOptionBlock){
            args.add("--cs-coverage");
            // Adding C# coverage switch
            if(!this.csCoverageTool.equals("none")){
                args.add(this.csCoverageTool);
            }

            // checking to see if C# framework is selected and add it to arguments
            if(!this.csFramework.equals("none")){
                args.add("--cs-test");
                args.add(this.csFramework);
            }
        }

        // Checking to see if java coverage was checked and adding all java arguments
        if(this.javaOptionBlock){
            args.add("--java-coverage");

            if(!javaCoverageTool.equals("none")){
                args.add(this.javaCoverageTool);
            }
            // Adding junit framework into arguments
            if(this.junitFramework){
                args.add("--java-test");
                args.add("junit");
            }
            // Adding junit4 framework into arguments
            if(this.junit4Framework){
                args.add("--java-test");
                args.add("junit4");
            }
        }

        return args;
    }

    /*
    Check Test Advisor Config
        - Ran before the build, to check that all of the fields are filled out correctly. 
        - There are some special cases that are also checked and also making sure that all required fields 
        are filled out. 
     */
    public String checkTaConfig(){
        boolean delim = true;
        String errorText = "Errors with your Test Analysis configuration. Please look into the specified issues: \n";
        // Making sure they pick a test language
        if(!this.javaOptionBlock && !this.cOptionBlock && !this.csOptionBlock){
            errorText += "[Error] No Coverage language was chosen, please pick at least one \n";
            delim = false;
        }
        /*Strip paths are reqired for TA
        if(this.taStripPath == null){
            errorText += "[Error] No Strip Path was specified. \n";
            delim = false;
        }*/

        // Making sure that a policy file exist and is specified
        if(this.policyFile == null){
            errorText += "[Error] Policy file is not specified. \n";
            delim = false;
        }else{
            File policy = new File(this.policyFile);
            if(!policy.exists()){
                errorText += "[Error] Could not locate Policy File. \n";
                delim = false;
            }
        }
        // Checking the required fields for specific SCM systems
        if(this.scmOptionBlock){
            if(this.scmSystem.equals("accurev") && this.accRevRepo == null){
                errorText += "[Error] Please specify AccuRev's source control repository under 'Advanced' \n";
                delim = false;
            }

            if(this.scmSystem.equals("perforce") && this.p4Port == null){
                errorText += "[Error] Please specify Perforce's port environment variable under 'Advanced'\n ";
                delim = false;
            }
        }
        // Checking required field for bullseye
        if(this.cOptionBlock){
            if(this.cxxCoverageTool.equals("bullseye") && this.bullsEyeDir == null){
                errorText += "[Error] Bulls eye requires the installation directory. \n";
                delim = false;
            }
        }
        // Checking to see if a working directory is specified for a custom test command so that seperate test
        // commands work correctly
        if(this.customTestCommand != null && this.customWorkDir == null){
            errorText += "[Error] When running a custom test command, a working directory must be specified \n";
            delim = false;
        }else if(this.customTestCommand != null && this.customWorkDir != null){
            File workDir = new File(this.customWorkDir);
            if(!workDir.exists()){
                errorText += "[Error] Could not find the custom working directory. \n";
                delim = false;
            }
        }

        if(delim){
            errorText = "Pass";
        }

        return errorText;
    }


}

