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
package jenkins.plugins.coverity.Utils;

import jenkins.plugins.coverity.InvocationAssistance;
import jenkins.plugins.coverity.JavaWarFile;
import jenkins.plugins.coverity.MisraConfig;
import jenkins.plugins.coverity.PostCovAnalyze;
import jenkins.plugins.coverity.PostCovBuild;
import jenkins.plugins.coverity.ToolsOverride;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class InvocationAssistanceBuilder {

    private String buildArguments;
    private String analyzeArguments;
    private String commitArguments;
    private List<JavaWarFile> javaWarFiles;
    private boolean csharpMsvsca;
    private ToolsOverride toolsOverride;
    private MisraConfig misraConfig;
    private boolean isScriptSrc;
    private PostCovBuild postCovBuild;
    private PostCovAnalyze postCovAnalyze;
    private String intermediateDir;
    private boolean useAdvancedParser;

    public InvocationAssistanceBuilder withUseAdvancedParser(boolean useAdvancedParser) {
        this.useAdvancedParser = useAdvancedParser;
        return this;
    }

    public InvocationAssistanceBuilder withIntermediateDir(String intermediateDir) {
        this.intermediateDir = intermediateDir;
        return this;
    }

    public InvocationAssistanceBuilder withPostAnalyzeCmd(String postCovAnalyzeCmd) {
        this.postCovAnalyze =new PostCovAnalyze(postCovAnalyzeCmd);
        return this;
    }

    public InvocationAssistanceBuilder withPostCovBuildCmd(String postCovBuildCmd) {
        this.postCovBuild = new PostCovBuild(postCovBuildCmd);
        return this;
    }

    public InvocationAssistanceBuilder withIsScriptSrc(boolean isScriptSrc) {
        this.isScriptSrc = isScriptSrc;
        return this;
    }

    public InvocationAssistanceBuilder withMisraConfigFile(String misraConfigFile) {
        this.misraConfig = new MisraConfig(misraConfigFile);
        return this;
    }

    public InvocationAssistanceBuilder withToolsLocationOverride(String toolInstallation, String optionalOverridePath) {
        this.toolsOverride = new ToolsOverride(toolInstallation);
        if (!StringUtils.isEmpty(optionalOverridePath))
            this.toolsOverride.setToolsLocation(optionalOverridePath);
        return this;
    }

    public InvocationAssistanceBuilder withCSharpMsvsca(boolean csharpMsvsca) {
        this.csharpMsvsca = csharpMsvsca;
        return this;
    }

    public InvocationAssistanceBuilder withJavaWarFiles(List<JavaWarFile> javaWarFiles) {
        this.javaWarFiles = javaWarFiles;
        return this;
    }

    public InvocationAssistanceBuilder withBuildArguments(String buildArguemtns) {
        this.buildArguments = buildArguemtns;
        return this;
    }

    public InvocationAssistanceBuilder withAnalyzeArguments(String analyzeArguments) {
        this.analyzeArguments = analyzeArguments;
        return this;
    }

    public InvocationAssistanceBuilder withCommitArguments(String commitArguments) {
        this.commitArguments = commitArguments;
        return this;
    }

    public InvocationAssistance build() {
        InvocationAssistance invocationAssistance = new InvocationAssistance();
        invocationAssistance.setPostCovBuild(postCovBuild);
        invocationAssistance.setPostCovAnalyze(postCovAnalyze);
        invocationAssistance.setIsScriptSrc(isScriptSrc);
        invocationAssistance.setBuildArguments(buildArguments);
        invocationAssistance.setAnalyzeArguments(analyzeArguments);
        invocationAssistance.setCommitArguments(commitArguments);
        invocationAssistance.setIntermediateDir(intermediateDir);
        invocationAssistance.setMisraConfig(misraConfig);
        invocationAssistance.setJavaWarFiles(javaWarFiles);
        invocationAssistance.setCsharpMsvsca(csharpMsvsca);
        invocationAssistance.setToolsOverride(toolsOverride);
        invocationAssistance.setUseAdvancedParser(useAdvancedParser);

        return invocationAssistance;
    }
}
