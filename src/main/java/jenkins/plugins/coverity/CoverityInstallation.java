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

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.EnvironmentSpecific;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Node-specific location for the Coverity Static Analysis tools.
 */
public class CoverityInstallation extends NodeProperty<Node> implements EnvironmentSpecific<CoverityInstallation> {

    private final String home;

    @DataBoundConstructor
    public CoverityInstallation(String home) {
        this.home = Util.fixEmpty(home);
    }

    /**
     * The location of the static analysis tools on this node. e.g. "C:\Program Files\Coverity\Coverity Static Analysis"
     * Not including the bin folder.
     * <p/>
     * If null, it is expected that the tools are found on the PATH.
     */
    public String getHome() {
        return home;
    }

    public CoverityInstallation forEnvironment(EnvVars environment) {
        if(home == null) return this;
        return new CoverityInstallation(environment.expand(getHome()));
    }

    @Extension
    public static class DescriptorImpl extends NodePropertyDescriptor {

        public DescriptorImpl() {
        }

        @Override
        public String getDisplayName() {
            return "Coverity Static Analysis";
        }

        @Override
        public boolean isApplicable(Class<? extends Node> targetType) {
            return targetType != Hudson.class;
        }

        public FormValidation doCheckHome(@QueryParameter String home) {
            if (StringUtils.isNotEmpty(home)) {
                return FormValidation.warning("Static Analysis Location is deprecated in Coverity plugin version 1.10 and later. " +
                    "Please use the Coverity Static Analysis Tool global configuration instead " +
                    "(you can override the global path with a node specific values if necessary).");
            }

            return FormValidation.ok();
        }
    }
}
