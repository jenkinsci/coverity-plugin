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
package jenkins.plugins.coverity;


import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;

public class CoverityViewResultsPublisher extends Recorder implements SimpleBuildStep {
    private String connectInstance;
    private String connectView;
    private String projectId;
    private boolean failPipeline;
    private boolean unstable;

    @DataBoundConstructor
    public CoverityViewResultsPublisher(String connectInstance, String connectView, String projectId) {
        this.connectInstance = connectInstance;
        this.connectView = connectView;
        this.projectId = projectId;
        failPipeline = false;
        unstable = false;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) throws InterruptedException, IOException {
        // todo: get CIMInstance and call web service to find view matching connectView

    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }


    public String getConnectInstance() {
        return connectInstance;
    }

    @DataBoundSetter
    public void setConnectInstance(String connectInstance) {
        this.connectInstance = Util.fixNull(connectInstance);
    }

    public String getConnectView() {
        return connectView;
    }

    @DataBoundSetter
    public void setConnectView(String connectView) {
        this.connectView = Util.fixNull(connectView);
    }

    @Override
    public CoverityViewResultsDescriptor getDescriptor() {
        return (CoverityViewResultsDescriptor) super.getDescriptor();
    }

    public String getProjectId() {
        return projectId;
    }

    @DataBoundSetter
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public boolean isFailPipeline() {
        return failPipeline;
    }

    @DataBoundSetter
    public void setFailPipeline(boolean failPipeline) {
        this.failPipeline = failPipeline;
    }

    public boolean isUnstable() {
        return unstable;
    }

    @DataBoundSetter
    public void setUnstable(boolean unstable) {
        this.unstable = unstable;
    }
}
