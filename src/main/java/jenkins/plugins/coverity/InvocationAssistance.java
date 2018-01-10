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
package jenkins.plugins.coverity;

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Util;
import org.kohsuke.stapler.DataBoundSetter;

public class InvocationAssistance {

    // deprecated fields which were removed in plugin version 1.9
    private transient boolean isUsingMisra;
    private transient String misraConfigFile;
    private transient boolean isUsingPostCovBuildCmd;
    private transient String postCovBuildCmd;
    private transient boolean isUsingPostCovAnalyzeCmd;
    private transient String postCovAnalyzeCmd;

    // deprecated fields which were removed in plugin version 1.10
    private transient String saOverride;

    private String buildArguments;
    private String analyzeArguments;
    private String commitArguments;
    private List<String> javaWarFilesNames;
    private List<JavaWarFile> javaWarFiles;
    private boolean csharpMsvsca;
    private ToolsOverride toolsOverride;
    private MisraConfig misraConfig;
    private boolean isScriptSrc;
    private PostCovBuild postCovBuild;
    private PostCovAnalyze postCovAnalyze;

    /**
     * Absolute path to the intermediate directory that Coverity should use. Null to use the default.
     */
    private String intermediateDir;

    private boolean useAdvancedParser;

    @DataBoundConstructor
    public InvocationAssistance() {

    }

    /**
     * Implement readResolve to update the de-serialized object in the case transient data was found. Transient fields
     * will be read during de-serialization and readResolve allow updating the InvocationAssistance object after being created.
     */
    protected Object readResolve() {
        // Check for existing misraConfig
        if (isUsingMisra && misraConfigFile != null) {
            this.misraConfig = new MisraConfig(misraConfigFile);
        }

        // Check for existing postCovBuild
        if (isUsingPostCovBuildCmd && postCovBuildCmd != null) {
            this.postCovBuild = new PostCovBuild(postCovBuildCmd);
        }

        // Check for existing postCovAnalyze
        if (isUsingPostCovAnalyzeCmd && postCovAnalyzeCmd != null) {
            this.postCovAnalyze = new PostCovAnalyze(postCovAnalyzeCmd);
        }

        // Check for existing saOverride
        if (saOverride != null) {
            toolsOverride = new ToolsOverride(null);
            toolsOverride.setToolsLocation(saOverride);
            saOverride = null;
        }

        return this;
    }

    @DataBoundSetter
    public void setPostCovBuild(PostCovBuild postCovBuild){
        this.postCovBuild = postCovBuild;
    }

    public String getPostCovBuildCmd() {
        return postCovBuild != null ? postCovBuild.getPostCovBuildCmd() : null;
    }

    public boolean getIsUsingPostCovBuildCmd() {
        return postCovBuild != null;
    }

    public boolean getIsUsingPostCovAnalyzeCmd() {
        return postCovAnalyze != null;
    }

    @DataBoundSetter
    public void setPostCovAnalyze(PostCovAnalyze postCovAnalyze){
        this.postCovAnalyze = postCovAnalyze;
    }

    public String getPostCovAnalyzeCmd() {
        return postCovAnalyze != null ? postCovAnalyze.getPostCovAnalyzeCmd() : null;
    }

    @DataBoundSetter
    public void setIsScriptSrc(boolean isScriptSrc){
        this.isScriptSrc = isScriptSrc;
    }

    public boolean getIsScriptSrc() {
        return isScriptSrc;
    }

    @DataBoundSetter
    public void setBuildArguments(String buildArguments){
        this.buildArguments = Util.fixEmpty(buildArguments);
    }

    public String getBuildArguments() {
        return buildArguments;
    }

    @DataBoundSetter
    public void setAnalyzeArguments(String analyzeArguments){
        this.analyzeArguments = Util.fixEmpty(analyzeArguments);
    }

    public String getAnalyzeArguments() {
        return analyzeArguments;
    }

    @DataBoundSetter
    public void setCommitArguments(String commitArguments){
        this.commitArguments = Util.fixEmpty(commitArguments);
    }

    public String getCommitArguments() {
        return commitArguments;
    }

    @DataBoundSetter
    public void setIntermediateDir(String intermediateDir){
        this.intermediateDir = Util.fixEmpty(intermediateDir);
    }

    public String getIntermediateDir() {
        return intermediateDir;
    }

    @DataBoundSetter
    public void setJavaWarFiles(List<JavaWarFile> javaWarFiles){
        List<String> tempJavaWarFilesPaths = new ArrayList<String>();
        if (javaWarFiles != null && !javaWarFiles.isEmpty()) {
            for (JavaWarFile javaWarFile : javaWarFiles) {
                tempJavaWarFilesPaths.add(javaWarFile.getWarFile());
            }
        }
        this.javaWarFilesNames = tempJavaWarFilesPaths;
        this.javaWarFiles = javaWarFiles;
    }

    public List<JavaWarFile> getJavaWarFiles() {
        return javaWarFiles;
    }

    public List<String> getJavaWarFilesNames() {
        return javaWarFilesNames;
    }

    @DataBoundSetter
    public void setCsharpMsvsca(boolean csharpMsvsca){
        this.csharpMsvsca = csharpMsvsca;
    }

    public boolean getCsharpMsvsca() {
        return csharpMsvsca;
    }

    @DataBoundSetter
    public void setToolsOverride(ToolsOverride toolsOverride){
        this.toolsOverride = toolsOverride;
    }

    public ToolsOverride getToolsOverride() {
        return toolsOverride;
    }

    @DataBoundSetter
    public void setMisraConfig(MisraConfig misraConfig){
        this.misraConfig = misraConfig;
    }

    public boolean getIsUsingMisra() {
        return misraConfig != null;
    }

    public String getMisraConfigFile() {
        return misraConfig != null ? misraConfig.getMisraConfigFile() : null;
    }

    @DataBoundSetter
    public void setUseAdvancedParser(boolean useAdvancedParser){
        this.useAdvancedParser = useAdvancedParser;
    }

    public boolean getUseAdvancedParser() {
        return useAdvancedParser;
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
        String intermediateDir = override.getIntermediateDir() != null ? override.getIntermediateDir() : getIntermediateDir();
        boolean csharpMsvsca = override.getCsharpMsvsca();
        ToolsOverride toolsOverrideOverride;
        if (override.saOverride!= null) {
            toolsOverrideOverride = new ToolsOverride(null);
            toolsOverrideOverride.setToolsLocation(override.saOverride);
        } else {
            toolsOverrideOverride = this.toolsOverride;
        }
        MisraConfig misraConfig = override.isUsingMisra ? new MisraConfig(override.misraConfigFile) : null;
        boolean isScriptSrc = override.getIsScriptSrc();
        PostCovBuild postBuild = override.isUsingPostCovBuildCmd ? new PostCovBuild(override.postCovBuildCmd) : null;
        PostCovAnalyze postCovAnalyze = override.isUsingPostCovAnalyzeCmd ? new PostCovAnalyze(override.postCovAnalyzeCmd) : null;
        List<JavaWarFile> javaWarFiles = override.getJavaWarFiles();
        boolean useAdvancedParser = override.getUseAdvancedParser();

        InvocationAssistance invocationAssistance = new InvocationAssistance();
        invocationAssistance.setPostCovBuild(postBuild);
        invocationAssistance.setPostCovAnalyze(postCovAnalyze);
        invocationAssistance.setIsScriptSrc(isScriptSrc);
        invocationAssistance.setBuildArguments(buildArguments);
        invocationAssistance.setAnalyzeArguments(analyzeArguments);
        invocationAssistance.setCommitArguments(commitArguments);
        invocationAssistance.setIntermediateDir(intermediateDir);
        invocationAssistance.setMisraConfig(misraConfig);
        invocationAssistance.setJavaWarFiles(javaWarFiles);
        invocationAssistance.setCsharpMsvsca(csharpMsvsca);
        invocationAssistance.setToolsOverride(toolsOverrideOverride);
        invocationAssistance.setUseAdvancedParser(useAdvancedParser);

        return invocationAssistance;
    }

    public String checkIAConfig() {
        boolean delim = true;
        String errorText = "Errors with your \"Perform Coverity build/analyze/commit\" options: \n";
        // Making sure they specified misra config
        if (getIsUsingMisra()) {
            if (getMisraConfigFile() == null) {
                delim = false;
            } else if (getMisraConfigFile().isEmpty()) {
                delim = false;
            } else if (getMisraConfigFile().trim().isEmpty()) {
                delim = false;
            }
        }

        if (delim) {
            errorText = "Pass";
        } else {
            errorText += "[Error] No MISRA configuration file was specified. \n";
        }

        return errorText;
    }
}
