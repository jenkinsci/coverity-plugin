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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import hudson.console.ConsoleLogFilter;
import hudson.console.LineTransformationOutputStream;
import hudson.model.*;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.tools.ToolInstallation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityToolInstallation.CoverityToolInstallationDescriptor;
import jenkins.tasks.SimpleBuildWrapper;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * A simple build wrapper to contribute Coverity tools bin path to the PATH environment variable
 * as well as Coverity Connect Instance(CIMInstance) information, such as Host/Port/Credentials
 * to environment variables.
 */
public class CoverityEnvBuildWrapper extends SimpleBuildWrapper {
    private final String coverityToolName;
    private String connectInstance;
    private String hostVariable;
    private String portVariable;
    private String usernameVariable;
    private String passwordVariable;
    private String passwordToMask;

    @DataBoundConstructor
    public CoverityEnvBuildWrapper(String coverityToolName) {
        this.coverityToolName = coverityToolName;
    }

    public String getCoverityToolName() {
        return coverityToolName;
    }

    @DataBoundSetter
    public void setConnectInstance(String cimInstance) {
        this.connectInstance = cimInstance;
    }

    public String getConnectInstance() {
        return connectInstance;
    }

    @DataBoundSetter
    public void setHostVariable(String hostVariable) {
        this.hostVariable = hostVariable;
    }

    public String getHostVariable() {
        return hostVariable;
    }

    @DataBoundSetter
    public void setPortVariable(String portVariable) {
        this.portVariable = portVariable;
    }

    public String getPortVariable() {
        return portVariable;
    }

    @DataBoundSetter
    public void setUsernameVariable(String usernameVariable) {
        this.usernameVariable = usernameVariable;
    }

    public String getUsernameVariable() {
        return usernameVariable;
    }

    @DataBoundSetter
    public void setPasswordVariable(String passwordVariable) {
        this.passwordVariable = passwordVariable;
    }

    public String getPasswordVariable() {
        return passwordVariable;
    }

    @Override
    public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener, EnvVars initialEnvironment) throws IOException, InterruptedException {
        ToolInstallation covTools = getCoverityToolInstallation();
        if (covTools == null) {
            throw new IOException("Unable to find Coverity tools installation: [" + coverityToolName +"]. Please configure one for this Jenkins instance.");
        }

        final Computer computer = workspace.toComputer();
        if (computer == null) {
            throw new AbortException("Cannot get Coverity tools installation");
        }

        final Node node = computer.getNode();
        if (node == null) {
            throw new AbortException("Cannot get Coverity tools installation");
        }

        covTools = covTools.translate(node, initialEnvironment, listener);
        final EnvVars covEnvVars = new EnvVars();
        covTools.buildEnvVars(covEnvVars);

        if (StringUtils.isNotEmpty(connectInstance)) {
            // Add environment variables for CIMInstance information such as host, port, username, and password
            final CoverityPublisher.DescriptorImpl descriptor = Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
            CIMInstance instance = descriptor.getInstance(connectInstance);
            if (instance != null) {
                covEnvVars.put(StringUtils.isNotEmpty(hostVariable) ? hostVariable : "COVERITY_HOST", instance.getHost());
                covEnvVars.put(StringUtils.isNotEmpty(portVariable) ? portVariable : "COVERITY_PORT", String.valueOf(instance.getPort()));
                covEnvVars.put(StringUtils.isNotEmpty(usernameVariable) ? usernameVariable : "COV_USER", instance.getCoverityUser());
                covEnvVars.put(StringUtils.isNotEmpty(passwordVariable) ? passwordVariable : "COVERITY_PASSPHRASE", instance.getCoverityPassword());

                this.passwordToMask = instance.getCoverityPassword();
            }
        }

        for (Entry<String,String> entry : covEnvVars.entrySet()) {
            context.env(entry.getKey(), entry.getValue());
        }
    }

    public CoverityToolInstallation getCoverityToolInstallation() {
        final DescriptorImpl descriptor = this.getDescriptor();
        if (this.getDescriptor() != null && coverityToolName != null) {
            for (CoverityToolInstallation toolInstallation : descriptor.getInstallations()) {
                if (coverityToolName.equalsIgnoreCase(toolInstallation.getName()))
                    return toolInstallation;
            }
        }

        return null;
    }

    @Override
    public ConsoleLogFilter createLoggerDecorator(Run<?, ?> build) {
        return new FilterImpl(passwordToMask);
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Symbol("withCoverityEnv")
    @Extension
    public static final class DescriptorImpl extends BuildWrapperDescriptor {

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Binds Coverity Tool path and Coverity Connect Instance information to Environment Variables";
        }

        public ListBoxModel doFillCoverityToolNameItems() {
            ListBoxModel result = new ListBoxModel();
            for(CoverityToolInstallation toolInstallation : getInstallations()) {
                result.add(toolInstallation.getName());
            }
            return result;
        }

        public CoverityToolInstallation[] getInstallations() {
            final CoverityToolInstallationDescriptor descriptor = Jenkins.getInstance().getDescriptorByType(CoverityToolInstallationDescriptor.class);
            return descriptor.getInstallations();
        }

        public ListBoxModel doFillCimInstanceItems() {
            final CoverityPublisher.DescriptorImpl descriptor = Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
            ListBoxModel result = new ListBoxModel();

            for (CIMInstance instance : descriptor.getInstances()) {
                result.add(instance.getName());
            }

            return result;
        }
    }

    private static final class FilterImpl extends ConsoleLogFilter implements Serializable {
        private static final long serialVersionUID = 10L;

        private final String passwordToMask;

        FilterImpl(String passwordToMask) {
            this.passwordToMask = passwordToMask;
        }

        @Override
        public OutputStream decorateLogger(Run _ignore, OutputStream logger) throws IOException, InterruptedException {
            return new PasswordsMaskOutputStream(logger, passwordToMask);
        }
    }

    /** Similar to {@code MaskPasswordsOutputStream}. */
    public static final class PasswordsMaskOutputStream extends LineTransformationOutputStream {
        private static final String MASKED_PASSWORD = "******";
        private final OutputStream  logger;
        private final Pattern passwordsAsPattern;

        public PasswordsMaskOutputStream(OutputStream logger, String passwordToMask) {
            this.logger = logger;

            if (StringUtils.isNotEmpty(passwordToMask)) {
                StringBuilder regex = new StringBuilder().append('(');
                regex.append(Pattern.quote(passwordToMask));
                regex.append(')');

                this.passwordsAsPattern = Pattern.compile(regex.toString());
            } else{
                this.passwordsAsPattern = null;
            }
        }

        @Override
        protected void eol(byte[] bytes, int len) throws IOException {
            String line = new String(bytes, 0, len, StandardCharsets.UTF_8);
            if(passwordsAsPattern != null) {
                line = passwordsAsPattern.matcher(line).replaceAll(MASKED_PASSWORD);
            }
            logger.write(line.getBytes(StandardCharsets.UTF_8));
        }
    }
}
