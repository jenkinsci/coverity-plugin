/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.Stapler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoverityBuildStep extends Builder {

    private final BuildStep buildStep;

    @DataBoundConstructor
    public CoverityBuildStep(final BuildStep buildStep) {
        this.buildStep = buildStep;
    }

    public BuildStep getBuildStep() { return buildStep; }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        CoverityLauncherDecorator.CoverityBuildStep.set(true);
        return buildStep.perform(build, launcher, listener);
    }

    @Extension
    public static class CoverityBuildStepDescriptor extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {

            if (Project.class.isAssignableFrom(jobType)) {
                return true;
            }

            return false;
        }

        @Override
        public String getDisplayName() {
            return "Invoke Coverity Capture Build";
        }

        public List<? extends Descriptor<? extends BuildStep>> getAllowedBuilders(AbstractProject<?, ?> project) {
            if (project == null)
                project = Stapler.getCurrentRequest().findAncestorObject(AbstractProject.class);

            final List<BuildStepDescriptor<? extends Builder>> builders = new ArrayList<BuildStepDescriptor<? extends Builder>>();
            if (project == null)
                return builders;
            for (Descriptor<Builder> descriptor : Builder.all()) {
                if (!(descriptor instanceof BuildStepDescriptor)) {
                    continue;
                }
                if (descriptor instanceof CoverityBuildStepDescriptor) {
                    continue;
                }

                BuildStepDescriptor<? extends Builder> buildStepDescriptor = (BuildStepDescriptor) descriptor;
                if (buildStepDescriptor.isApplicable(project.getClass())){
                    builders.add(buildStepDescriptor);
                }
            }
            return builders;

        }
    }
}
