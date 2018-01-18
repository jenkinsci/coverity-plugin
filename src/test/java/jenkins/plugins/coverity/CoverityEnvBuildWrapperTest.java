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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import hudson.model.*;
import hudson.util.Secret;
import jenkins.plugins.coverity.Utils.CIMInstanceBuilder;
import jenkins.plugins.coverity.Utils.CredentialUtil;
import jenkins.plugins.coverity.Utils.TestableConsoleLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.remoting.VirtualChannel;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.tools.ToolLocationNodeProperty;
import hudson.util.DescribableList;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.CoverityToolInstallation.CoverityToolInstallationDescriptor;
import jenkins.tasks.SimpleBuildWrapper.Context;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, ToolLocationNodeProperty.class, Secret.class, CredentialsMatchers.class, CredentialsProvider.class})
public class CoverityEnvBuildWrapperTest {

    private CoverityToolInstallation toolInstallation;
    private Jenkins jenkins;
    private DescriptorImpl descriptor;

    @Before
    public void setup() {
        jenkins = mock(Jenkins.class);
        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);

        descriptor = mock(CoverityPublisher.DescriptorImpl.class);
        toolInstallation = new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, "C:\\Program Files\\Coverity\\Coverity Static Analysis");
        final CoverityToolInstallation[] toolInstallations = new CoverityToolInstallation[]{toolInstallation};
        when(descriptor.getInstallations()).thenReturn(toolInstallations);
        when(jenkins.getDescriptorOrDie(CoverityEnvBuildWrapper.class)).thenReturn(new CoverityEnvBuildWrapper.DescriptorImpl());
        when(jenkins.getDescriptorByType(CoverityToolInstallationDescriptor.class)).thenReturn(new CoverityToolInstallationDescriptor());
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(descriptor);
    }

    @Test
    public void descriptor_doFillCoverityToolNameItems_returnsInstallationsNames() {
        final CoverityEnvBuildWrapper.DescriptorImpl descriptor = new CoverityEnvBuildWrapper.DescriptorImpl();

        final ListBoxModel result = descriptor.doFillCoverityToolNameItems();

        assertEquals(1, result.size());
        assertEquals(toolInstallation.getName(), result.get(0).name);
        assertEquals(toolInstallation.getName(), result.get(0).value);
    }

    @Test
    public void setUp_addCoverityBinToPath() throws IOException, InterruptedException {
        final Context context = new Context();
        final FilePath workspace = setupFilePath();

        final CoverityEnvBuildWrapper buildWrapper = new CoverityEnvBuildWrapper(toolInstallation.getName());

        buildWrapper.setUp(context, mock(Run.class), workspace, mock(Launcher.class), mock(TaskListener.class), new EnvVars());

        final Map<String, String> env = context.getEnv();
        assertTrue(env.containsKey("PATH+COVERITY"));
        assertEquals(env.get("PATH+COVERITY"), toolInstallation.getHome() + "/bin");
    }

    @Test
    public void setUp_addCoverityConnectInformation_WithDefault() throws IOException, InterruptedException {
        final Context context = new Context();
        final FilePath workspace = setupFilePath();
        final CIMInstance instance = new CIMInstanceBuilder()
                .withName("TestInstance").withHost("localhost").withPort(8080).withDefaultCredentialId().build();

        when(descriptor.getInstance(Matchers.anyString())).thenReturn(instance);
        CredentialUtil.setCredentialManager("TestUser", "TestPassword");

        final CoverityEnvBuildWrapper buildWrapper = new CoverityEnvBuildWrapper(toolInstallation.getName());
        buildWrapper.setCimInstance("TestInstance");

        buildWrapper.setUp(context, mock(Run.class), workspace, mock(Launcher.class), mock(TaskListener.class), new EnvVars());

        final Map<String, String> env = context.getEnv();

        assertTrue(env.containsKey("COVERITY_HOST"));
        assertEquals(env.get("COVERITY_HOST"), "localhost");

        assertTrue(env.containsKey("COVERITY_PORT"));
        assertEquals(env.get("COVERITY_PORT"), "8080");

        assertTrue(env.containsKey("COV_USER"));
        assertEquals(env.get("COV_USER"), "TestUser");

        assertTrue(env.containsKey("COVERITY_PASSPHRASE"));
        assertEquals(env.get("COVERITY_PASSPHRASE"), "TestPassword");
    }

    @Test
    public void setUp_addCoverityConnectInformation_WithCustomizedVariables() throws IOException, InterruptedException {
        final Context context = new Context();
        final FilePath workspace = setupFilePath();
        final CIMInstance instance = new CIMInstanceBuilder()
                .withName("TestInstance").withHost("localhost").withPort(8080).withDefaultCredentialId().build();

        when(descriptor.getInstance(Matchers.anyString())).thenReturn(instance);
        CredentialUtil.setCredentialManager("TestUser", "TestPassword");

        final CoverityEnvBuildWrapper buildWrapper = new CoverityEnvBuildWrapper(toolInstallation.getName());
        buildWrapper.setCimInstance("TestInstance");
        buildWrapper.setHostVariable("TEST_HOST");
        buildWrapper.setPortVariable("TEST_PORT");
        buildWrapper.setUsernameVariable("TEST_USER");
        buildWrapper.setPasswordVariable("TEST_PASSWORD");

        buildWrapper.setUp(context, mock(Run.class), workspace, mock(Launcher.class), mock(TaskListener.class), new EnvVars());

        final Map<String, String> env = context.getEnv();
        assertFalse(env.containsKey("COVERITY_HOST"));
        assertFalse(env.containsKey("COVERITY_PORT"));
        assertFalse(env.containsKey("COV_USER"));
        assertFalse(env.containsKey("COVERITY_PASSPHRASE"));

        assertTrue(env.containsKey("TEST_HOST"));
        assertEquals(env.get("TEST_HOST"), "localhost");

        assertTrue(env.containsKey("TEST_PORT"));
        assertEquals(env.get("TEST_PORT"), "8080");

        assertTrue(env.containsKey("TEST_USER"));
        assertEquals(env.get("TEST_USER"), "TestUser");

        assertTrue(env.containsKey("TEST_PASSWORD"));
        assertEquals(env.get("TEST_PASSWORD"), "TestPassword");
    }

    @Test
    public void maskPassword() throws IOException, InterruptedException {
        final Context context = new Context();
        final FilePath workspace = setupFilePath();
        final CIMInstance instance = new CIMInstanceBuilder()
                .withName("TestInstance").withHost("localhost").withPort(8080).withDefaultCredentialId().build();

        when(descriptor.getInstance(Matchers.anyString())).thenReturn(instance);
        CredentialUtil.setCredentialManager("TestUser", "TestPassword");

        final CoverityEnvBuildWrapper buildWrapper = new CoverityEnvBuildWrapper(toolInstallation.getName());
        buildWrapper.setCimInstance("TestInstance");

        buildWrapper.setUp(context, mock(Run.class), workspace, mock(Launcher.class), mock(TaskListener.class), new EnvVars());
        TestableConsoleLogger logger = new TestableConsoleLogger();
        OutputStream stream = buildWrapper.createLoggerDecorator(mock(Run.class)).decorateLogger(mock(AbstractBuild.class), logger.getPrintStream());

        stream.write("My password is TestPassword".getBytes());
        stream.write(0x0A);
        stream.flush();

        logger.verifyLastMessage("My password is ******\n");
    }

    @Test
    public void descriptor_doFillCoverityToolNameItems_returnsCIMInstanceNames() {
        final CIMInstance instance = new CIMInstanceBuilder().withName("TestInstance1").build();
        when(descriptor.getInstances()).thenReturn(Arrays.asList(instance));

        final CoverityEnvBuildWrapper.DescriptorImpl buildWrapperDescriptor = new CoverityEnvBuildWrapper.DescriptorImpl();
        final ListBoxModel result = buildWrapperDescriptor.doFillCimInstanceItems();

        assertEquals(1, result.size());
        assertEquals(instance.getName(), result.get(0).name);
        assertEquals(instance.getName(), result.get(0).value);
    }

    @SuppressWarnings("deprecation")
    public FilePath setupFilePath() throws IOException, InterruptedException {
        final VirtualChannel channel = mock(VirtualChannel.class);
        final Node node = mock(Node.class);
        final FilePath workspace = new FilePath(channel, "~/.jenkins/workspace/test");
        final Computer computer = mock(Computer.class);
        PowerMockito.mockStatic(ToolLocationNodeProperty.class);

        PowerMockito.when(ToolLocationNodeProperty.getToolHome(same(node), same(toolInstallation), any(TaskListener.class))).thenReturn(toolInstallation.getHome());
        when(node.getNodeProperties()).thenReturn(new DescribableList<NodeProperty<?>, NodePropertyDescriptor>(mock(Saveable.class)));
        when(computer.getChannel()).thenReturn(channel);
        when(jenkins.getComputers()).thenReturn(new Computer[]{computer});
        when(computer.getNode()).thenReturn(node);
        return workspace;
    }
}
