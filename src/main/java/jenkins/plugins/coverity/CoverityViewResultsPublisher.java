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

import java.io.PrintStream;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import jenkins.plugins.coverity.ws.ViewIssuesReader;
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
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) {
        final PrintStream logger = listener.getLogger();
        logger.println("[Coverity] Publish Coverity View Results { "+
            "connectInstance:'" + connectInstance + "', " +
            "projectId:'" + projectId + "', " +
            "connectView:'" + connectView +
            "}");

        CIMInstance instance = getInstance();

        if (instance == null) {
            logger.println("[Coverity] Unable to find Coverity Connect instance: " + connectInstance);
            run.setResult(Result.FAILURE);
            return;
        }

        if (StringUtils.isEmpty(projectId) || StringUtils.isEmpty(connectView)) {
            logger.println("[Coverity] Coverity Connect project and view are required. But was Project: '" + projectId +
                "' View: '" + connectView + "'");
            run.setResult(Result.FAILURE);
            return;
        }

        try {
            ViewIssuesReader reader = new ViewIssuesReader(run, listener.getLogger(), this);
            reader.getIssuesFromConnectView();
            final CoverityBuildAction buildAction = run.getAction(CoverityBuildAction.class);
            if (failPipeline && buildAction.getDefects().size() > 0) {
                run.setResult(Result.FAILURE);
            } else if (unstable && buildAction.getDefects().size() > 0) {
                run.setResult(Result.UNSTABLE);
            }
        } catch (Exception e) {
            logger.println("[Coverity] Error Publishing Coverity View Results");
            logger.println(e.toString());
            run.setResult(Result.FAILURE);
        }

        logger.println("[Coverity] Finished Publishing Coverity View Results");
    }

    public CIMInstance getInstance() {
        return getDescriptor().getInstance(connectInstance);
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
