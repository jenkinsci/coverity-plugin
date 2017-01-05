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
package jenkins.plugins.coverity.analysis;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import jenkins.plugins.coverity.CoverityPublisher;

public class CommandFactory {

    public static ICovCommand getCovAnalyzeCommand(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher, String home) {
        return new CovAnalyzeCommand(build, launcher, listener, publisher, home).getCovAnalyzeCommand();
    }

    public static ICovCommand getCovCaptureCommand(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher, String home) {
        return new CovCaptureCommand(build, launcher, listener, publisher, home).getCovCaptureCommand();
    }
}
