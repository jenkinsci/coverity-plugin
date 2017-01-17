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
package jenkins.plugins.coverity.Utils;

import jenkins.plugins.coverity.InvocationAssistance;
import jenkins.plugins.coverity.JavaWarFile;

import java.util.List;

public class InvocationAssistanceBuilder {

    private String buildArguments;
    private String analyzeArguments;
    private String commitArguments;
    private String csharpAssemblies;
    private List<String> javaWarFilesNames;
    private List<JavaWarFile> javaWarFiles;
    private boolean csharpAutomaticAssemblies;
    private boolean csharpMsvsca;
    private String saOverride;
    private boolean isUsingMisra;
    private String misraConfigFile;
    private boolean isCompiledSrc;
    private boolean isScriptSrc;
    private boolean isUsingPostCovBuildCmd;
    private String postCovBuildCmd;
    private boolean isUsingPostCovAnalyzeCmd;
    private String postCovAnalyzeCmd;
    private String intermediateDir;
    private String covBuildBlacklist;
    private boolean useAdvancedParser;

    public InvocationAssistanceBuilder withUseAdvancedParser(boolean useAdvancedParser) {
        this.useAdvancedParser = useAdvancedParser;
        return this;
    }

    public InvocationAssistanceBuilder withCovBuildBlackList(String covBuildBlacklist) {
        this.covBuildBlacklist = covBuildBlacklist;
        return this;
    }

    public InvocationAssistanceBuilder withIntermediateDir(String intermediateDir) {
        this.intermediateDir = intermediateDir;
        return this;
    }

    public InvocationAssistanceBuilder withPostAnalyzeCmd(String postCovAnalyzeCmd) {
        this.postCovAnalyzeCmd = postCovAnalyzeCmd;
        return this;
    }

    public InvocationAssistanceBuilder withUsingPostAnalyzeCmnd(boolean isUsingPostCovAnalyzeCmd) {
        this.isUsingPostCovAnalyzeCmd = isUsingPostCovAnalyzeCmd;
        return this;
    }

    public InvocationAssistanceBuilder withPostCovBuildCmd(String postCovBuildCmd) {
        this.postCovBuildCmd = postCovBuildCmd;
        return this;
    }

    public InvocationAssistanceBuilder withUsingPostCovBuildCmd(boolean isUsingPostCovBuildCmd) {
        this.isUsingPostCovBuildCmd = isUsingPostCovBuildCmd;
        return this;
    }

    public InvocationAssistanceBuilder withIsScriptSrc(boolean isScriptSrc) {
        this.isScriptSrc = isScriptSrc;
        return this;
    }

    public InvocationAssistanceBuilder withIsCompiledSrc(boolean isCompiledSrc) {
        this.isCompiledSrc = isCompiledSrc;
        return this;
    }

    public InvocationAssistanceBuilder withMisraConfigFile(String misraConfigFile) {
        this.misraConfigFile = misraConfigFile;
        return this;
    }

    public InvocationAssistanceBuilder withUsingMisra(boolean isUsingMisra) {
        this.isUsingMisra = isUsingMisra;
        return this;
    }

    public InvocationAssistanceBuilder withSaOverride(String saOverride) {
        this.saOverride = saOverride;
        return this;
    }

    public InvocationAssistanceBuilder withCSharpMsvsca(boolean csharpMsvsca) {
        this.csharpMsvsca = csharpMsvsca;
        return this;
    }

    public InvocationAssistanceBuilder withCSharpAutomaticAssemblies(boolean csharpAutomaticAssemblies) {
        this.csharpAutomaticAssemblies = csharpAutomaticAssemblies;
        return this;
    }

    public InvocationAssistanceBuilder withJavaWarFiles(List<JavaWarFile> javaWarFiles) {
        this.javaWarFiles = javaWarFiles;
        return this;
    }

    public InvocationAssistanceBuilder withJavaWarFilesNames(List<String> javaWarFilesNames) {
        this.javaWarFilesNames = javaWarFilesNames;
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

    public InvocationAssistanceBuilder withCSharpAssemblies(String csharpAssemblies) {
        this.csharpAssemblies = csharpAssemblies;
        return this;
    }

    public InvocationAssistance build() {
        return new InvocationAssistance(
                isUsingPostCovBuildCmd,
                postCovBuildCmd,
                isUsingPostCovAnalyzeCmd,
                postCovAnalyzeCmd,
                isCompiledSrc,
                isScriptSrc,
                buildArguments,
                analyzeArguments,
                commitArguments,
                intermediateDir,
                isUsingMisra,
                misraConfigFile,
                csharpAssemblies,
                javaWarFilesNames,
                csharpAutomaticAssemblies,
                csharpMsvsca,
                saOverride,
                covBuildBlacklist,
                javaWarFiles,
                useAdvancedParser);
    }
}
