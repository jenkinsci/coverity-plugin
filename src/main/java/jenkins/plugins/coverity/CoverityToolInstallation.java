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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.Saveable;
import hudson.model.TaskListener;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;
import hudson.tools.ToolPropertyDescriptor;
import hudson.util.DescribableList;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

/**
 * Coverity Static Analysis Tool {@link ToolInstallation}, this represents a named tool configuration with a path to
 * the install directory.
 * This was added to improve pipeline support for pipeline script which will use the built in tool step to get the
 * tool installation, then manually invoke Coverity tools.
 */
public class CoverityToolInstallation extends ToolInstallation implements NodeSpecific<CoverityToolInstallation>, EnvironmentSpecific<CoverityToolInstallation> {

    @DataBoundConstructor
    public CoverityToolInstallation(String name, String home) {
        super(name, home, new DescribableList<ToolProperty<?>,ToolPropertyDescriptor>(Saveable.NOOP));
    }

    @Override
    public CoverityToolInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
        return new CoverityToolInstallation(getName(), translateFor(node, log));
    }

    @Override
    public CoverityToolInstallation forEnvironment(EnvVars environment) {
        return new CoverityToolInstallation(getName(), environment.expand(getHome()));
    }

    /**\
     * {@link ToolDescriptor} for {@link CoverityToolInstallation}
     */
    @Extension
    @Symbol("coverity")
    public static final class CoverityToolInstallationDescriptor extends ToolDescriptor<CoverityToolInstallation> {

        @Override
        public String getDisplayName() {
            return "Coverity Static Analysis Tools";
        }

        /**
         * Override in order to remove the default "install automatically" checkbox for tool installation
         */
        @Override
        public List<ToolPropertyDescriptor> getPropertyDescriptors() {
            return Collections.emptyList();
        }

        @Override
        public DescribableList<ToolProperty<?>,ToolPropertyDescriptor> getDefaultProperties() {
            return new DescribableList<>(NOOP);
        }

        @Override
        public CoverityToolInstallation[] getInstallations() {
            return getDescriptor().getInstallations();
        }

        @Override
        public void setInstallations(CoverityToolInstallation... installations) {
            getDescriptor().setInstallations(installations);
        }

        private CoverityPublisher.DescriptorImpl getDescriptor() {
            return Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
        }

        @Override
        protected FormValidation checkHomeDirectory(File home) {
            try {
                return getDescriptor().doCheckAnalysisLocation(home.getAbsolutePath());
            } catch (IOException e) {
                return FormValidation.error("Unable to verify the \"Coverity Static Analysis\" directory version.");
            }
        }
    }
}
