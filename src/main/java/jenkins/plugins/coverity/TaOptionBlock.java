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

import java.util.*;
import java.util.logging.Logger;

public class TaOptionBlock {

    private static final Logger logger = Logger.getLogger(CoverityPublisher.class.getName());

    // Deprecated field from 1.9.0 release
    private transient String taStripPath;

    private final String customTestCommand;
    private final boolean cOptionBlock;
    private final boolean csOptionBlock;
    private final String customTestTool;
    private final String logFileLoc;
    private final String csFramework;
    private final String csCoverageTool;
    private final String cxxCoverageTool;
    private final String javaCoverageTool;
    private final boolean junitFramework;
    private final boolean junit4Framework;
    private final String policyFile;
    private final String bullsEyeDir;
    private final String customWorkDir;
    private final boolean covHistoryCheckbox;
    private final boolean javaOptionBlock;
    private List<TaStripPath> taStripPaths;

    /*
        Generic Constructor that will hold all the elements we need for Test Advisor. All of these variables
        are fields within the Test Advisor Option Block
     */
    @DataBoundConstructor
    public TaOptionBlock(String customTestCommand,
                         boolean cOptionBlock,
                         boolean csOptionBlock,
                         boolean javaOptionBlock,
                         String customTestTool,
                         String logFileLoc,
                         String csFramework,
                         String csCoverageTool,
                         String cxxCoverageTool,
                         String javaCoverageTool,
                         boolean junitFramework,
                         boolean junit4Framework,
                         String policyFile,
                         List<TaStripPath> taStripPaths,
                         String bullsEyeDir,
                         String customWorkDir,
                         boolean covHistoryCheckbox) {
        this.customTestCommand = Util.fixEmpty(customTestCommand);
        this.cOptionBlock = cOptionBlock;
        this.csOptionBlock = csOptionBlock;
        this.javaOptionBlock = javaOptionBlock;
        this.customTestTool = Util.fixEmpty(customTestTool);
        this.logFileLoc = Util.fixEmpty(logFileLoc);
        this.csFramework = csFramework;
        this.csCoverageTool = csCoverageTool;     // Required if other two are not selected
        this.cxxCoverageTool = cxxCoverageTool;    // Required if other two are not selected
        this.javaCoverageTool = javaCoverageTool; // Required if other two are not selected
        this.junit4Framework = junit4Framework;
        this.junitFramework = junitFramework;
        this.policyFile = Util.fixEmpty(policyFile);    // Required
        this.bullsEyeDir = Util.fixEmpty(bullsEyeDir); // Required if bulls eye is selected
        this.customWorkDir = Util.fixEmpty(customWorkDir); // Required if a custom command is issued
        this.covHistoryCheckbox = covHistoryCheckbox;
        this.taStripPaths = taStripPaths;   // Required
    }

    /**
     * Implement readResolve to update the de-serialized object in the case transient data was found. Transient fields
     * will be read during de-serialization and readResolve allow updating the TaOptionBlock object after being created.
     */
    protected Object readResolve() {
        // Check for old data values taStripPath being set
        if(taStripPath != null) {
            logger.info("Old data format detected. Converting to new format.");
            convertTransientDataFields();
        }

        return this;
    }

    /**
     * Converts the old data values taStripPath to a {@link TaStripPath} object
     * and adds to the configured taStripPaths.
     */
    private void convertTransientDataFields() {
        TaStripPath stripPath = new TaStripPath(taStripPath);

        if(taStripPaths == null) {
            this.taStripPaths = new ArrayList<TaStripPath>();
        }
        taStripPaths.add(stripPath);
    }

    /*
    Required functions needed for jenkins to access all of the Test Advisor Data.
     */
    public String getCustomTestCommand(){return customTestCommand;}

    public boolean getCOptionBlock(){return cOptionBlock;}

    public boolean getCsOptionBlock(){return csOptionBlock;}

    public boolean getJavaOptionBlock(){return javaOptionBlock;}

    public String getCustomTestTool(){return customTestTool;}

    public String getLogFileLoc(){return logFileLoc ;}

    public String getCsFramework(){return csFramework;}

    public String getCsCoverageTool(){return csCoverageTool;}

    public String getCxxCoverageTool(){return cxxCoverageTool;}

    public String getJavaCoverageTool(){return javaCoverageTool;}

    public boolean getJunit4Framework(){return junit4Framework;}

    public boolean getJunitFramework(){return junitFramework;}

    public String getPolicyFile(){return policyFile;}

    public String getBullsEyeDir(){return bullsEyeDir;}

    public String getCustomWorkDir(){return customWorkDir;}

    public boolean getCovHistoryCheckbox(){return covHistoryCheckbox;}

    public List<TaStripPath> getTaStripPaths() { return taStripPaths; }

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
        - Ran when "Check Configuration" button is clicked on the configuration page.
        - There are some special cases that are also checked and also making sure that all required fields 
        are filled out. 
     */
    public String checkTaConfig(){
        boolean delim = true;
        String errorText = StringUtils.EMPTY;
        // Making sure they pick a test language
        if(!this.javaOptionBlock && !this.cOptionBlock && !this.csOptionBlock){
            errorText += "[Test Advisor] No Coverage language was chosen, please pick at least one \n";
            delim = false;
        }

        // Making sure that a policy file is specified
        if(this.policyFile == null){
            errorText += "[Test Advisor] Policy file is not specified. \n";
            delim = false;
        }

        // Checking required field for bullseye
        if(this.cOptionBlock){
            if(this.cxxCoverageTool.equals("bullseye") && this.bullsEyeDir == null){
                errorText += "[Test Advisor] Bulls eye requires the installation directory. \n";
                delim = false;
            }
        }
        // Checking to see if a working directory is specified for a custom test command so that separate test
        // commands work correctly
        if(this.customTestCommand != null && this.customWorkDir == null){
            errorText += "[Test Advisor] When running a custom test command, a working directory must be specified \n";
            delim = false;
        }

        if(delim){
            errorText = "Pass";
        }

        return errorText;
    }


}

