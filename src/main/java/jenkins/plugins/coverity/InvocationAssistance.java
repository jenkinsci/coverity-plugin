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

import hudson.Util;
import org.kohsuke.stapler.DataBoundConstructor;

public class InvocationAssistance {
	private final String buildArguments;
	private final String analyzeArguments;
	private final String commitArguments;
	private final String csharpAssemblies;
	private final String javaWarFile;
	private final String saOverride;

	/**
	 * Do not wrap any executables with these names with cov-build. Format is comma-separated list.
	 */
	private final String covBuildBlacklist;

	/**
	 * Absolute path to the intermediate directory that Coverity should use. Null to use the default.
	 */
	private final String intermediateDir;

	@DataBoundConstructor
	public InvocationAssistance(String buildArguments, String analyzeArguments, String commitArguments, String intermediateDir, String csharpAssemblies, String javaWarFile, String saOverride, String covBuildBlacklist) {
		this.intermediateDir = Util.fixEmpty(intermediateDir);
		this.buildArguments = Util.fixEmpty(buildArguments);
		this.analyzeArguments = Util.fixEmpty(analyzeArguments);
		this.commitArguments = Util.fixEmpty(commitArguments);
		this.csharpAssemblies = Util.fixEmpty(csharpAssemblies);
		this.javaWarFile = Util.fixEmpty(javaWarFile);
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

	public String getSaOverride() {
		return saOverride;
	}

	public String getCovBuildBlacklist() {
		return covBuildBlacklist;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		InvocationAssistance that = (InvocationAssistance) o;

		if(analyzeArguments != null ? !analyzeArguments.equals(that.analyzeArguments) : that.analyzeArguments != null)
			return false;
		if(buildArguments != null ? !buildArguments.equals(that.buildArguments) : that.buildArguments != null)
			return false;
		if(commitArguments != null ? !commitArguments.equals(that.commitArguments) : that.commitArguments != null)
			return false;
		if(csharpAssemblies != null ? !csharpAssemblies.equals(that.csharpAssemblies) : that.csharpAssemblies != null)
			return false;
		if(javaWarFile != null ? !javaWarFile.equals(that.javaWarFile) : that.javaWarFile != null)
			return false;
		if(saOverride != null ? !saOverride.equals(that.saOverride) : that.saOverride != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = buildArguments != null ? buildArguments.hashCode() : 0;
		result = 31 * result + (analyzeArguments != null ? analyzeArguments.hashCode() : 0);
		result = 31 * result + (commitArguments != null ? commitArguments.hashCode() : 0);
		result = 31 * result + (csharpAssemblies != null ? csharpAssemblies.hashCode() : 0);
		result = 31 * result + (javaWarFile != null ? javaWarFile.hashCode() : 0);
		result = 31 * result + (saOverride != null ? saOverride.hashCode() : 0);

		return result;
	}

	/**
	 * For each variable in override, use that value, otherwise, use the value in this object.
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
		String saOverride = override.getSaOverride() != null ? override.getSaOverride() : getSaOverride();
		return new InvocationAssistance(buildArguments, analyzeArguments, commitArguments, intermediateDir, csharpAssemblies, saOverride, covBuildBlacklist);
	}
}
