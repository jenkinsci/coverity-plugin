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

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.util.ArgumentListBuilder;
import jenkins.plugins.coverity.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

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

    public static Collection<File> listFiles(
            File directory,
            FilenameFilter filter,
            boolean recurse) {
        Vector<File> files = new Vector<File>();
        File[] entries = directory.listFiles();

        for(File entry : entries) {
            if(filter == null || filter.accept(directory, entry.getName())) {
                files.add(entry);
            }

            if(recurse && entry.isDirectory()) {
                files.addAll(listFiles(entry, filter, recurse));
            }
        }

        return files;
    }

    public static File[] listFilesAsArray(
            File directory,
            FilenameFilter filter,
            boolean recurse) {
        Collection<File> files = listFiles(directory, filter, recurse);

        File[] arr = new File[files.size()];
        return files.toArray(arr);
    }

    public abstract boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws Exception;

    public File[] findMsvscaOutputFiles(String dirName) {
        File dir = new File(dirName);

        return listFilesAsArray(dir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith("CodeAnalysisLog.xml");
            }
        }, true);
    }

    public boolean importMsvsca(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, String home,
                                CoverityTempDir temp, boolean csharpMsvsca) throws IOException, InterruptedException {
        String covImportMsvsca = "cov-import-msvsca";
        covImportMsvsca = new FilePath(launcher.getChannel(), home).child("bin").child(covImportMsvsca).getRemote();
        EnvVars envVars = build.getEnvironment(listener);

        List<String> importCmd = new ArrayList<String>();
        importCmd.add(covImportMsvsca);
        importCmd.add("--dir");
        importCmd.add(temp.getTempDir().getRemote());
        importCmd.add("--append");

        listener.getLogger().println("[Coverity] Searching for Microsoft Code Analysis results...");
        File[] msvscaOutputFiles = {};

        if(csharpMsvsca) {
            msvscaOutputFiles = findMsvscaOutputFiles(build.getWorkspace().getRemote());
        }

        for(File outputFile : msvscaOutputFiles) {
            importCmd.add(outputFile.getAbsolutePath());
        }

        if(msvscaOutputFiles.length == 0) {
            listener.getLogger().println("[MSVSCA] MSVSCA No results found, skipping");
        } else {
            listener.getLogger().println("[MSVSCA] MSVSCA Import cmd so far is: " + importCmd.toString());

            InvocationAssistance invocationAssistance = CoverityUtils.getInvocationAssistance(build);
            boolean useAdvancedParser = false;

            if(invocationAssistance != null && invocationAssistance.getUseAdvancedParser()){
                useAdvancedParser = true;
            }

            int importResult = CoverityUtils.runCmd(importCmd, build, launcher, listener, envVars, useAdvancedParser);

            if(importResult != 0) {
                listener.getLogger().println("[Coverity] " + covImportMsvsca + " returned " + importResult + ", aborting...");
                return false;
            }
        }

        return true;
    }
}
