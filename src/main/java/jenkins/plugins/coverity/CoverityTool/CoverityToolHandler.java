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
import jenkins.plugins.coverity.*;

import java.io.File;
import java.io.FilenameFilter;

/**
 * CoverityToolHandler handles the actual executing of Coverity executables. This class provides common functionality,
 * and {@link #getHandler(CoverityVersion)} to choose an appropriate subclass.}
 */
public abstract class CoverityToolHandler {
    /**
     * Find a suitable {@link CoverityToolHandler} to run analysis.
     * @param version the version of Coverity analysis present on the {@link hudson.model.Node} where the analysis will
     *                run.
     * @return A {@link CoverityToolHandler} that can run the given version of analysis. Otherwise returns null when
     *         the given version of analysis is not supported.
     */
    public static CoverityToolHandler getHandler(CoverityVersion version) {
        // A fail safe for testing so that when we have new versions of analysis and platform, the build steps will
        // Automatically go to JasperToolHandler
        if (version.isCodeName() && !version.containsCodeName()){
            return new JasperToolHandler(version);
        }

        if(version.compareTo(CoverityVersion.MINIMUM_SUPPORTED_VERSION) < 0) {
            return null;
        }
        if(version.compareTo(CoverityVersion.MINIMUM_SUPPORTED_VERSION) >= 0 && version.compareTo(CoverityVersion.VERSION_JASPER) < 0){
            return new IndioToolHandler(version);
        } else {
            return new JasperToolHandler(version);
        }
    }

    public abstract boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws Exception;

    protected File[] findMsvscaOutputFiles(String dirName) {
        File dir = new File(dirName);

        return CoverityUtils.listFilesAsArray(dir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith("CodeAnalysisLog.xml");
            }
        }, true);
    }
}
