/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;
import hudson.EnvVars;
import hudson.Util;
import org.kohsuke.stapler.DataBoundConstructor;
import net.sf.json.JSONObject;

public class InvocationAssistance {
    private final String buildArguments;
    private final String analyzeArguments;
    private final String commitArguments;
    private final String csharpAssemblies;
    private final String javaWarFile;
    private final String csharpMsvscaOutputFiles;
    private final boolean csharpAutomaticAssemblies;
    private final boolean csharpMsvsca;
    private final String saOverride;
    private EnvVars envVars;

    private final boolean isUsingMisra;
    private final String misraConfigFile;
    private JSONObject misraMap;

    /**
     * Do not wrap any executables with these names with cov-build. Format is comma-separated list.
     */
    private final String covBuildBlacklist;

    /**
     * Absolute path to the intermediate directory that Coverity should use. Null to use the default.
     */
    private final String intermediateDir;

    public InvocationAssistance(String buildArguments, String analyzeArguments, String commitArguments, String intermediateDir, boolean isUsingMisra, String misraConfigFile, String csharpAssemblies, String javaWarFile, String csharpMsvscaOutputFiles, boolean csharpAutomaticAssemblies, boolean csharpMsvsca, String saOverride, String covBuildBlacklist) {
        this.isUsingMisra = isUsingMisra;
        this.misraConfigFile = misraConfigFile;
        this.intermediateDir = Util.fixEmpty(intermediateDir);
        this.buildArguments = Util.fixEmpty(buildArguments);
        this.analyzeArguments = Util.fixEmpty(analyzeArguments);
        this.commitArguments = Util.fixEmpty(commitArguments);
        this.csharpAssemblies = Util.fixEmpty(csharpAssemblies);
        this.javaWarFile = Util.fixEmpty(javaWarFile);
        this.csharpMsvscaOutputFiles = Util.fixEmpty(csharpMsvscaOutputFiles);
        this.csharpMsvsca = csharpMsvsca;
        this.csharpAutomaticAssemblies = csharpAutomaticAssemblies;
        this.saOverride = Util.fixEmpty(saOverride);
        this.covBuildBlacklist = Util.fixEmpty(covBuildBlacklist);
    }

    @DataBoundConstructor
    public InvocationAssistance(String buildArguments, String analyzeArguments, String commitArguments, String intermediateDir, JSONObject misraMap, String csharpAssemblies, String javaWarFile, String csharpMsvscaOutputFiles, boolean csharpAutomaticAssemblies, boolean csharpMsvsca, String saOverride, String covBuildBlacklist) {
        this.misraMap = misraMap;
        if(this.misraMap != null) {
            this.misraConfigFile = (String) misraMap.get("misraConfigFile");
        } else {
            this.misraConfigFile = null;
        }
        this.isUsingMisra = this.misraMap != null;
        this.intermediateDir = Util.fixEmpty(intermediateDir);
        this.buildArguments = Util.fixEmpty(buildArguments);
        this.analyzeArguments = Util.fixEmpty(analyzeArguments);
        this.commitArguments = Util.fixEmpty(commitArguments);
        this.csharpAssemblies = Util.fixEmpty(csharpAssemblies);
        this.javaWarFile = Util.fixEmpty(javaWarFile);
        this.csharpMsvscaOutputFiles = Util.fixEmpty(csharpMsvscaOutputFiles);
        this.csharpMsvsca = csharpMsvsca;
        this.csharpAutomaticAssemblies = csharpAutomaticAssemblies;
        this.saOverride = Util.fixEmpty(saOverride);
        this.covBuildBlacklist = Util.fixEmpty(covBuildBlacklist);
    }



    public String getBuildArguments() {
        return buildArguments;
    }

    public String getAnalyzeArguments() {
        return analyzeArguments;
    }

    public String getCommitArguments() {
        return commitArguments;
    }

    public String getIntermediateDir() {
        return intermediateDir;
    }

    public String getCsharpAssemblies() {
        return csharpAssemblies;
    }

    public String getJavaWarFile() {
        return javaWarFile;
    }

    public String getCsharpMsvscaOutputFiles() {
        return csharpMsvscaOutputFiles;
    }

    public boolean getCsharpMsvsca() {
        return csharpMsvsca;
    }

    public boolean getCsharpAutomaticAssemblies() {
        return csharpAutomaticAssemblies;
    }

    public String getSaOverride() {
        return saOverride;
    }

    public String getCovBuildBlacklist() {
        return covBuildBlacklist;
    }

    public boolean getIsUsingMisra() {
        return isUsingMisra;
    }

    public String getMisraConfigFile(){
        return misraConfigFile;
    }



    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        InvocationAssistance that = (InvocationAssistance) o;

        if(csharpAutomaticAssemblies != that.csharpAutomaticAssemblies) return false;
        if(csharpMsvsca != that.csharpMsvsca) return false;
        if(analyzeArguments != null ? !analyzeArguments.equals(that.analyzeArguments) : that.analyzeArguments != null)
            return false;
        if(buildArguments != null ? !buildArguments.equals(that.buildArguments) : that.buildArguments != null)
            return false;
        if(commitArguments != null ? !commitArguments.equals(that.commitArguments) : that.commitArguments != null)
            return false;
        if(covBuildBlacklist != null ? !covBuildBlacklist.equals(that.covBuildBlacklist) : that.covBuildBlacklist != null)
            return false;
        if(csharpAssemblies != null ? !csharpAssemblies.equals(that.csharpAssemblies) : that.csharpAssemblies != null)
            return false;
        if(csharpMsvscaOutputFiles != null ? !csharpMsvscaOutputFiles.equals(that.csharpMsvscaOutputFiles) : that.csharpMsvscaOutputFiles != null)
            return false;
        if(intermediateDir != null ? !intermediateDir.equals(that.intermediateDir) : that.intermediateDir != null)
            return false;
        if(javaWarFile != null ? !javaWarFile.equals(that.javaWarFile) : that.javaWarFile != null) return false;
        if(saOverride != null ? !saOverride.equals(that.saOverride) : that.saOverride != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = buildArguments != null ? buildArguments.hashCode() : 0;
        result = 31 * result + (analyzeArguments != null ? analyzeArguments.hashCode() : 0);
        result = 31 * result + (commitArguments != null ? commitArguments.hashCode() : 0);
        result = 31 * result + (csharpAssemblies != null ? csharpAssemblies.hashCode() : 0);
        result = 31 * result + (javaWarFile != null ? javaWarFile.hashCode() : 0);
        result = 31 * result + (csharpMsvscaOutputFiles != null ? csharpMsvscaOutputFiles.hashCode() : 0);
        result = 31 * result + (csharpAutomaticAssemblies ? 1 : 0);
        result = 31 * result + (csharpMsvsca ? 1 : 0);
        result = 31 * result + (saOverride != null ? saOverride.hashCode() : 0);
        result = 31 * result + (covBuildBlacklist != null ? covBuildBlacklist.hashCode() : 0);
        result = 31 * result + (intermediateDir != null ? intermediateDir.hashCode() : 0);
        return result;
    }

    /**
     * For each variable in override, use that value, otherwise, use the value in this object.
     *
     * @param override source
     * @return a new, merged InvocationAssistance
     */
    public InvocationAssistance merge(InvocationAssistance override) {
        String analyzeArguments = override.getAnalyzeArguments() != null ? override.getAnalyzeArguments() : getAnalyzeArguments();
        String buildArguments = override.getBuildArguments() != null ? override.getBuildArguments() : getBuildArguments();
        String commitArguments = override.getCommitArguments() != null ? override.getCommitArguments() : getCommitArguments();
        String covBuildBlacklist = override.getCovBuildBlacklist() != null ? override.getCovBuildBlacklist() : getCovBuildBlacklist();
        String csharpAssemblies = override.getCsharpAssemblies() != null ? override.getCsharpAssemblies() : getCsharpAssemblies();
        String intermediateDir = override.getIntermediateDir() != null ? override.getIntermediateDir() : getIntermediateDir();
        String javaWarFile = override.getJavaWarFile() != null ? override.getJavaWarFile() : getJavaWarFile();
        String csharpMsvscaOutputFiles = override.getCsharpMsvscaOutputFiles() != null ? override.getCsharpMsvscaOutputFiles() : getCsharpMsvscaOutputFiles();
        boolean csharpAutomaticAssemblies = override.getCsharpAutomaticAssemblies();
        boolean csharpMsvsca = override.getCsharpMsvsca();
        String saOverride = override.getSaOverride() != null ? override.getSaOverride() : getSaOverride();
        boolean isUsingMisra = override.getIsUsingMisra();
        String misraConfigFile = override.getMisraConfigFile() != null ? override.getMisraConfigFile() : getMisraConfigFile();
        return new InvocationAssistance(buildArguments, analyzeArguments, commitArguments, intermediateDir, isUsingMisra, misraConfigFile, csharpAssemblies, javaWarFile, csharpMsvscaOutputFiles, csharpAutomaticAssemblies, csharpMsvsca, saOverride, covBuildBlacklist);
    }

    // Sets the environment varibles for the project so that we can replace environment varibles
    public void setEnvVars(EnvVars environment){
        this.envVars = environment;
    }

    public String checkIAConfig(){
        boolean delim = true;
        String errorText = "Errors with your \"Perform Coverity build/analyze/commit\" options: \n";
        // Making sure they pick a test language
        if(isUsingMisra){
            if(misraConfigFile == null){
                delim = false;
            } else if (misraConfigFile.isEmpty()){
                delim = false;
            } else if (misraConfigFile.trim().isEmpty()){
                delim = false;
            }
        }

        if(delim){
            errorText = "Pass";
        } else {
            errorText += "[Error] No MISRA configuration file was specified. \n";
        }

        return errorText;
    }

}
