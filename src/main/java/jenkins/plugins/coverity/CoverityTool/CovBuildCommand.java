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
package jenkins.plugins.coverity.CoverityTool;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import jenkins.plugins.coverity.CoverityPublisher;

public class CovBuildCommand extends CovCommand {

    private static final String command = "cov-build";
    private static final String noCommandArg = "--no-command";
    private static final String fileSystemCapture = "--fs-capture-search";
    private boolean forCompiledSrc;

    public CovBuildCommand(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher, String home, boolean forCompiledSrc) {
        super(command, build, launcher, listener, publisher, home);
        this.forCompiledSrc = forCompiledSrc;
        prepareCommand();
    }

    @Override
    protected void prepareCommand() {
        if (!forCompiledSrc){
            prepareCovBuildCommandForScriptSources();
        }else{
            prepareCovBuildCommandForCompileSources();
        }
    }

    private void prepareCovBuildCommandForScriptSources() {
        addIntermediateDir();
        addArgument(noCommandArg);
        addArgument(fileSystemCapture);
        addArgument("$WORKSPACE");
    }

    private void prepareCovBuildCommandForCompileSources() {

    }
}
