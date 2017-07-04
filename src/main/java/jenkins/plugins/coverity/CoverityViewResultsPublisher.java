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
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.ProjectFilterSpecDataObj;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ComboBoxModel;
import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.ws.CimCache;
import jenkins.tasks.SimpleBuildStep;

public class CoverityViewResultsPublisher extends Recorder implements SimpleBuildStep {
    private String cimInstance;
    private String connectView;
    private String projectId;

    @DataBoundConstructor
    public CoverityViewResultsPublisher(String cimInstance, String connectView, String projectId) {
        this.cimInstance = cimInstance;
        this.connectView = connectView;
        this.projectId = projectId;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) throws InterruptedException, IOException {
        // todo: get CIMInstance and call web service to find view matching connectView

    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return null;
    }


    public String getCimInstance() {
        return cimInstance;
    }

    @DataBoundSetter
    public void setCimInstance(String cimInstance) {
        this.cimInstance = Util.fixNull(cimInstance);
    }

    public String getConnectView() {
        return connectView;
    }

    @DataBoundSetter
    public void setConnectView(String connectView) {
        this.connectView = Util.fixNull(connectView);
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    public String getProjectId() {
        return projectId;
    }

    @DataBoundSetter
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Symbol("coverityResults")
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        public CoverityPublisher.DescriptorImpl getPublisherDescriptor() {
            return Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
        }

        public List<CIMInstance> getCimInstances() {
            return getPublisherDescriptor().getInstances();
        }

        public CIMInstance getInstance(String name) {
            for(CIMInstance instance : getCimInstances()) {
                if(instance.getName().equals(name)) {
                    return instance;
                }
            }
            return null;
        }

        public ListBoxModel doFillCimInstanceItems() {
            ListBoxModel result = new ListBoxModel();
            for(CIMInstance instance : getCimInstances()) {
                result.add(instance.getName());
            }
            return result;
        }

        public ComboBoxModel doFillProjectIdItems(@QueryParameter(value = "../cimInstance") String cimInstance) {
            ComboBoxModel items = new ComboBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if (instance != null) {
                final List<String> projects = CimCache.getInstance().getProjects(instance);
                items.addAll(projects);
            }

            return items;
        }

        /**
         * Checks the instance, view and project. To ensure valid configuration.
         * First, this check that the cimInstance string value is a configured global instance, then it checks that instance is valid.
         * Second, checks that the view is configured in that coverity connect instance and will provide warnings if the view is not.
         * Third, checks that the projectId exists
         */
        public FormValidation doCheckViews(@QueryParameter String cimInstance, @QueryParameter String connectView, @QueryParameter String projectId) {
            if (StringUtils.isEmpty(cimInstance)) {
                return FormValidation.error("Coverity Connect Instance is required");
            }

            if (StringUtils.isEmpty(connectView)) {
                return FormValidation.error("Coverity Connect View is required");
            }

            if (StringUtils.isEmpty(projectId)) {
                return FormValidation.error("Coverity Connect Project is required");
            }


            CIMInstance instance = getInstance(cimInstance);
            if (instance == null) {
                return FormValidation.error("Unable to find Coverity Connect instance: " + cimInstance);
            }

            FormValidation instanceCheck = instance.doCheck();
            if (!instanceCheck.kind.equals(Kind.OK)) {
                return instanceCheck;
            }

            try {
                ProjectFilterSpecDataObj projectFilterSpec = new ProjectFilterSpecDataObj();
                projectFilterSpec.setIncludeChildren(false);
                projectFilterSpec.setIncludeStreams(false);
                projectFilterSpec.setNamePattern(projectId);
                List<ProjectDataObj> projects = instance.getConfigurationService().getProjects(projectFilterSpec);
                if (projects.size() != 1) {

                    StringBuilder missingProjectMessage = new StringBuilder();
                    missingProjectMessage.append("Unable to find project '");
                    missingProjectMessage.append(projectId);
                    missingProjectMessage.append("' on instance '");
                    missingProjectMessage.append(cimInstance);
                    missingProjectMessage.append("'.");


                    projectFilterSpec.setNamePattern(null);
                    projects = instance.getConfigurationService().getProjects(projectFilterSpec);
                    if (projects.size() > 0) {
                        missingProjectMessage.append(" Available projects include: ");
                        for (ProjectDataObj project : projects) {
                            missingProjectMessage.append(project.getId().getName());
                            missingProjectMessage.append(", ");
                        }
                        missingProjectMessage.delete(missingProjectMessage.length() - 2, missingProjectMessage.length() + 1);
                    }

                    return FormValidation.error(missingProjectMessage.toString());
                }
            } catch (CovRemoteServiceException_Exception | IOException e) {
                return FormValidation.error("Unexpected error occurred when checking project");
            }

            return FormValidation.ok("Successfully configured");
        }
    }
}
