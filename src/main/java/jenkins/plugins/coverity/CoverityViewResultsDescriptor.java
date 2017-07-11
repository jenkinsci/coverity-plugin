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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.QueryParameter;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.ProjectFilterSpecDataObj;
import com.google.common.collect.ImmutableSortedMap;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;

/**
 * Descriptor for the class {@link CoverityViewResultsPublisher}
 */
@Symbol("coverityResults")
@Extension
public class CoverityViewResultsDescriptor extends BuildStepDescriptor<Publisher> {

    public CoverityViewResultsDescriptor() {
        super(CoverityViewResultsPublisher.class);
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "Publish Coverity View Results";
    }

    private CoverityPublisher.DescriptorImpl getPublisherDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
    }

    private List<CIMInstance> getConnectInstances() {
        return getPublisherDescriptor().getInstances();
    }

    private CIMInstance getInstance(String name) {
        for(CIMInstance instance : getConnectInstances()) {
            if(instance.getName().equals(name)) {
                return instance;
            }
        }
        return null;
    }

    /**
     * Returns a list of names of available {@link CIMInstance} for connectInstance select items
     */
    public ListBoxModel doFillConnectInstanceItems() {
        ListBoxModel result = new ListBoxModel();
        for(CIMInstance instance : getConnectInstances()) {
            result.add(instance.getName());
        }
        return result;
    }

    /**
     * Checks the instance, view and project. To ensure valid configuration.
     * First, this check that the connectInstance string value is a configured global instance, then it checks that instance is valid.
     * Second, checks that the view is configured in that coverity connect instance and will provide warnings if the view is not.
     * Third, checks that the projectId exists
     */
    public FormValidation doCheckViews(@QueryParameter String connectInstance, @QueryParameter String projectId, @QueryParameter String connectView) {
        Collection<FormValidation> validations = new ArrayList<>();
        if (StringUtils.isEmpty(connectInstance)) {
            validations.add(FormValidation.error("Coverity Connect Instance is required"));
        }

        if (StringUtils.isEmpty(projectId)) {
            validations.add(FormValidation.error("Coverity Connect Project is required"));
        }

        if (StringUtils.isEmpty(connectView)) {
            validations.add(FormValidation.error("Coverity Connect View is required"));
        }

        if (validations.size() > 0)
            return FormValidation.aggregate(validations);


        CIMInstance instance = getInstance(connectInstance);
        if (instance == null) {
            return FormValidation.error("Unable to find Coverity Connect instance: " + connectInstance);
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
                missingProjectMessage.append("Unable to find project '" + projectId + "' on instance '" + connectInstance + "'.");

                projectFilterSpec.setNamePattern(null);
                projects = instance.getConfigurationService().getProjects(projectFilterSpec);
                if (projects.size() > 0) {
                    missingProjectMessage.append(" Available projects include: ");
                    for (ProjectDataObj project : projects) {
                        missingProjectMessage.append(project.getId().getName());
                        missingProjectMessage.append(", ");
                    }
                    // removes trailing comma and space
                    missingProjectMessage.delete(missingProjectMessage.length() - 2, missingProjectMessage.length());
                }

                validations.add(FormValidation.error(missingProjectMessage.toString()));
            }
        } catch (CovRemoteServiceException_Exception | IOException e) {
            return FormValidation.error("Unexpected error occurred when checking project");
        }

        try {
            final ImmutableSortedMap<Long, String> views = instance.getViews();

            if (!views.containsValue(connectView)) {
                Long connectViewIdentifier = null;
                try {
                    connectViewIdentifier = Long.parseLong(connectView);
                } catch (NumberFormatException e) {
                }

                if (!views.containsKey(connectViewIdentifier)) {
                    StringBuilder missingViewMessage = new StringBuilder();
                    missingViewMessage.append("Unable to find the view '" + connectView + "' on instance '" + connectInstance + "'.");
                    if (views.size() > 0) {
                        missingViewMessage.append(" Available views include: ");
                        for (String viewName : views.values()) {
                            missingViewMessage.append(viewName);
                            missingViewMessage.append(", ");
                        }
                        // removes trailing comma and space
                        missingViewMessage.delete(missingViewMessage.length() - 2, missingViewMessage.length());
                    }

                    validations.add(FormValidation.error(missingViewMessage.toString()));
                }
            }
        } catch (Exception e) {
            return FormValidation.error("Unexpected error occurred when checking views");
        }

        if (validations.size() > 0)
            return FormValidation.aggregate(validations);

        return FormValidation.ok("Successfully configured");
    }
}
