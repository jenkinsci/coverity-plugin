package jenkins.plugins.coverity;

import hudson.Util;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.util.*;


public class ScmOptionBlock {

    private final String scmSystem;
    private final String customTestTool;
    private final String scmToolArguments;
    private final String scmCommandArgs;
    private final String logFileLoc;
    private final String p4Port;
    private final String accRevRepo;

    @DataBoundConstructor
    public ScmOptionBlock(
            String scmSystem,
            String customTestTool,
            String scmToolArguments,
            String scmCommandArgs,
            String logFileLoc,
            String p4Port,
            String accRevRepo){

        this.scmSystem = scmSystem;
        this.customTestTool = Util.fixEmpty(customTestTool);
        this.scmToolArguments = Util.fixEmpty(scmToolArguments);
        this.scmCommandArgs = Util.fixEmpty(scmCommandArgs);
        this.logFileLoc = Util.fixEmpty(logFileLoc);
        this.p4Port = Util.fixEmpty(p4Port); // Required if perforce is selected
        this.accRevRepo = Util.fixEmpty(accRevRepo);   // Required if accurev is selected
    }

    public String getScmSystem(){return scmSystem;}

    public String getCustomTestTool(){return customTestTool;}

    public String getScmToolArguments(){return scmToolArguments;}

    public String getScmCommandArgs(){return scmCommandArgs;}

    public String getLogFileLoc(){return logFileLoc;}

    public String getP4Port(){return p4Port;}

    public String getAccRevRepo(){return accRevRepo;}

    public String checkScmConfig(){
        // Checking the required fields for specific SCM systems

        String errorText = "Errors with your SCM configuration. Please look into the specified issues: ";
        Boolean delim = true;
        if(this.scmSystem.equals("accurev") && this.accRevRepo == null){
            errorText += "[Error] Please specify AccuRev's source control repository under 'Advanced' \n";
            delim = false;
        }

        if(this.scmSystem.equals("perforce") && this.p4Port == null){
            errorText += "[Error] Please specify Perforce's port environment variable under 'Advanced'\n ";
            delim = false;
        }

        if(delim){
            errorText = "Pass";
        }
        return errorText;
    }
}
