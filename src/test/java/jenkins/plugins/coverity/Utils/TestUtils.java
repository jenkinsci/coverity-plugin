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
package jenkins.plugins.coverity.Utils;

import hudson.EnvVars;
import hudson.model.*;
import hudson.remoting.VirtualChannel;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.tasks.Publisher;
import hudson.tools.ToolLocationNodeProperty;
import hudson.util.DescribableList;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityToolInstallation;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {
    public static FreeStyleBuild getFreeStyleBuild(CoverityPublisher... publishers) {
        final FreeStyleBuild freeStyleBuild = mock(FreeStyleBuild.class);
        final FreeStyleProject freeStyleProject = mock(FreeStyleProject.class);
        final List<CoverityPublisher> coverityPublishers = publishers != null ? Arrays.asList(publishers) : Collections.<CoverityPublisher>emptyList();
        final DescribableList<Publisher, Descriptor<Publisher>> publisherList = new DescribableList<Publisher, Descriptor<Publisher>>(freeStyleProject, coverityPublishers);
        when(freeStyleProject.getPublishersList()).thenReturn(publisherList);
        when(freeStyleBuild.getProject()).thenReturn(freeStyleProject);

        try {
            when(freeStyleBuild.getEnvironment(any(TaskListener.class))).thenReturn(new EnvVars());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return freeStyleBuild;
    }

    public static CoverityToolInstallation getTestInstallation() {
        final URL resource =  TestUtils.class.getResource("/VERSION.xml");
        assertNotNull(resource);
        File versionFile = new File(resource.getPath());
        return new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, versionFile.getParentFile().getPath());
    }

    /**
     * Sets up an executor on the node for the build and installation, this satisfies the common checks made by the
     * Coverity plugin (through manual checks or before running publisher)
     * Requires the test class to have the {@link org.powermock.core.classloader.annotations.PrepareForTest} annotation
     * for the classes {@link Executor} and {@link ToolLocationNodeProperty}
     */
    @SuppressWarnings("deprecation")
    public static void setupCurrentExecutorWithNode(String nodeName, FreeStyleBuild build, CoverityToolInstallation installation) throws IOException, InterruptedException {
        PowerMockito.mockStatic(Executor.class);
        final Executor executor = mock(Executor.class);
        final Node node = mock(Node.class);
        final Computer computer = mock(Computer.class);
        final VirtualChannel channel = mock(VirtualChannel.class);

        when(node.getDisplayName()).thenReturn(nodeName);
        when(node.getNodeProperties()).thenReturn(new DescribableList<NodeProperty<?>, NodePropertyDescriptor>(mock(Saveable.class)));
        when(computer.getNode()).thenReturn(node);
        when(computer.getChannel()).thenReturn(channel);
        when(Executor.currentExecutor()).thenReturn(executor);
        when(executor.getOwner()).thenReturn(computer);
        when(executor.getCurrentExecutable()).thenReturn(build);

        if (installation != null) {
            PowerMockito.mockStatic(ToolLocationNodeProperty.class);
            PowerMockito.when(ToolLocationNodeProperty.getToolHome(same(node), same(installation), any(TaskListener.class))).thenReturn(installation.getHome());
        }
    }
}
