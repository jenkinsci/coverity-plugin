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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sun.jersey.api.client.Client;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityPublisher.DescriptorImpl;
import jenkins.plugins.coverity.Utils.TestableConsoleLogger;
import jenkins.plugins.coverity.ws.WebServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, WebServiceFactory.class, Client.class})
public class CoverityViewResultsPublisherTest {
    private CIMInstance cimInstance;
    private TestableConsoleLogger consoleLogger;
    private TaskListener listener;
    private Run run;
    private FilePath workspace;
    private Launcher launcher;

    @Before
    public void setup() {
        // setup jenkins
        final Jenkins jenkins = mock(Jenkins.class);
        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);
        final DescriptorImpl globalDescriptor = mock(CoverityPublisher.DescriptorImpl.class);
        cimInstance = new CIMInstance("pipeline-instance", "test-cim-instance", 8080, "admin", "password", false, 9080);
        final List<CIMInstance> cimInstances = Arrays.asList(cimInstance);
        when(globalDescriptor.getInstances()).thenReturn(cimInstances);
        when(jenkins.getDescriptorByType(CoverityPublisher.DescriptorImpl.class)).thenReturn(globalDescriptor);

        final CoverityViewResultsDescriptor stepDescriptor = new CoverityViewResultsDescriptor();
        when(jenkins.getDescriptorOrDie(CoverityViewResultsPublisher.class)).thenReturn(stepDescriptor);

        consoleLogger = new TestableConsoleLogger();
        listener = mock(TaskListener.class);
        run = mock(Run.class);
        workspace = new FilePath(new File("src/test/java/jenkins/plugins/coverity"));
        launcher = mock(Launcher.class);
        when(listener.getLogger()).thenReturn(consoleLogger.getPrintStream());
    }

    @Test
    public void perform_unknownInstance_logsError() throws IOException, InterruptedException {
        final String instance = "my instance";
        final String projectId = "my projectId";
        final String view = "my connect view";

        final CoverityViewResultsPublisher publisher = new CoverityViewResultsPublisher(instance, view, projectId);

        publisher.perform(run, workspace, launcher, listener);

        consoleLogger.verifyMessages(getInformationMessage(instance, projectId, view),
            "[Coverity] Unable to find Coverity Connect instance: " + instance);
        verify(run).setResult(Result.FAILURE);
    }

    @Test
    public void perform_emptyProject_logsError() throws IOException, InterruptedException {
        final String instance = cimInstance.getName();
        final String projectId = "";
        final String view = "my connect view";

        final CoverityViewResultsPublisher publisher = new CoverityViewResultsPublisher(instance, view, projectId);

        publisher.perform(run, workspace, launcher, listener);

        consoleLogger.verifyMessages(getInformationMessage(instance, projectId, view),
            "[Coverity] Coverity Connect project and view are required. But was Project: '" + projectId +"' View: '" + view + "'");
        verify(run).setResult(Result.FAILURE);
    }

    @Test
    public void perform_emptyView_logsError() throws IOException, InterruptedException {
        final String instance = cimInstance.getName();
        final String projectId = "my projectId";
        final String view = null;

        final CoverityViewResultsPublisher publisher = new CoverityViewResultsPublisher(instance, view, projectId);

        publisher.perform(run, workspace, launcher, listener);

        consoleLogger.verifyMessages(getInformationMessage(instance, projectId, view),
            "[Coverity] Coverity Connect project and view are required. But was Project: '" + projectId +"' View: '" + view + "'");
        verify(run).setResult(Result.FAILURE);
    }

    public String getInformationMessage(String instance, String projectId, String view) {
        return "[Coverity] Publish Coverity View Results { "+
            "connectInstance:'" + instance + "', " +
            "projectId:'" + projectId + "', " +
            "connectView:'" + view +
            "}";
    }
}
