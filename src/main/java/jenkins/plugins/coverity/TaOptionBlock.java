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

import java.util.*;
import java.util.logging.Logger;

public class TaOptionBlock {

    private static final Logger logger = Logger.getLogger(CoverityPublisher.class.getName());

    // Deprecated field from 1.9.0 release
    private transient String taStripPath;

    private String customTestCommand;
    private boolean cOptionBlock;
    private boolean csOptionBlock;
    private String customTestTool;
    private String logFileLoc;
    private String csFramework;
    private String csCoverageTool;
    private String cxxCoverageTool;
    private String javaCoverageTool;
    private boolean junitFramework;
    private boolean junit4Framework;
    private final String policyFile;
    private String bullsEyeDir;
    private boolean covHistoryCheckbox;
    private boolean javaOptionBlock;
    private List<TaStripPath> taStripPaths;

    /*
        Generic Constructor that will hold all the elements we need for Test Advisor. All of these variables
        are fields within the Test Advisor Option Block
     */
    @DataBoundConstructor
    public TaOptionBlock(String policyFile) {
        this.policyFile = Util.fixEmpty(policyFile);    // Required
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

    @DataBoundSetter
    public void setCustomTestCommand(String customTestCommand){
        this.customTestCommand = Util.fixEmpty(customTestCommand);
    }

    public String getCustomTestCommand(){return customTestCommand;}

    @DataBoundSetter
    public void setcOptionBlock(boolean cOptionBlock){
        this.cOptionBlock = cOptionBlock;
    }

    public boolean getcOptionBlock(){return cOptionBlock;}

    @DataBoundSetter
    public void setCsOptionBlock(boolean csOptionBlock){
        this.csOptionBlock = csOptionBlock;
    }

    public boolean getCsOptionBlock(){return csOptionBlock;}

    @DataBoundSetter
    public void setJavaOptionBlock(boolean javaOptionBlock){
        this.javaOptionBlock = javaOptionBlock;
    }

    public boolean getJavaOptionBlock(){return javaOptionBlock;}

    @DataBoundSetter
    public void setCustomTestTool(String customTestTool){
        this.customTestTool = Util.fixEmpty(customTestTool);
    }

    public String getCustomTestTool(){return customTestTool;}

    @DataBoundSetter
    public void setLogFileLoc(String logFileLoc){
        this.logFileLoc = Util.fixEmpty(logFileLoc);
    }

    public String getLogFileLoc(){return logFileLoc ;}

    @DataBoundSetter
    public void setCsFramework(String csFramework){
        this.csFramework = csFramework;
    }

    public String getCsFramework(){return csFramework;}

    @DataBoundSetter
    public void setCsCoverageTool(String csCoverageTool){
        this.csCoverageTool = csCoverageTool;
    }

    public String getCsCoverageTool(){return csCoverageTool;}

    @DataBoundSetter
    public void setCxxCoverageTool(String cxxCoverageTool){
        this.cxxCoverageTool = cxxCoverageTool;
    }

    public String getCxxCoverageTool(){return cxxCoverageTool;}

    @DataBoundSetter
    public void setJavaCoverageTool(String javaCoverageTool){
        this.javaCoverageTool = javaCoverageTool;
    }

    public String getJavaCoverageTool(){return javaCoverageTool;}

    @DataBoundSetter
    public void setJunit4Framework(boolean junit4Framework){
        this.junit4Framework = junit4Framework;
    }

    public boolean getJunit4Framework(){return junit4Framework;}

    @DataBoundSetter
    public void setJunitFramework(boolean junitFramework){
        this.junitFramework = junitFramework;
    }

    public boolean getJunitFramework(){return junitFramework;}

    public String getPolicyFile(){return policyFile;}

    @DataBoundSetter
    public void setBullsEyeDir(String bullsEyeDir){
        this.bullsEyeDir = bullsEyeDir;
    }

    public String getBullsEyeDir(){return bullsEyeDir;}

    @DataBoundSetter
    public void setCovHistoryCheckbox(boolean covHistoryCheckbox){
        this.covHistoryCheckbox = covHistoryCheckbox;
    }

    public boolean getCovHistoryCheckbox(){return covHistoryCheckbox;}

    @DataBoundSetter
    public void setTaStripPaths(List<TaStripPath> taStripPaths){
        this.taStripPaths = taStripPaths;
    }

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
        if(StringUtils.isEmpty(this.policyFile)){
            errorText += "[Test Advisor] Policy file is not specified. \n";
            delim = false;
        }

        if ((this.javaOptionBlock && this.javaCoverageTool.equals("none"))
                || (this.cOptionBlock && this.cxxCoverageTool.equals("none"))
                || (this.csOptionBlock && this.csCoverageTool.equals("none"))) {
            errorText += "[Test Advisor] No Coverage tool was chosen \n";
            delim = false;
        }

        // Checking required field for bullseye
        if(this.cOptionBlock){
            if(this.cxxCoverageTool.equals("bullseye") && StringUtils.isEmpty(this.bullsEyeDir)){
                errorText += "[Test Advisor] Bulls eye requires the installation directory. \n";
                delim = false;
            }
        }

        if(delim){
            errorText = "Pass";
        }

        return errorText;
    }


}

