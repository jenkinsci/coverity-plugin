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
package jenkins.plugins.coverity.CoverityTool;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.CoverityUtils;
import jenkins.plugins.coverity.Utils.TestableConsoleLogger;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CoverityUtils.class)
public abstract class CommandTestBase {

    @Mock
    protected AbstractBuild build;

    @Mock
    protected Launcher launcher;

    @Mock
    protected TaskListener listener;
    protected EnvVars envVars;
    protected String[] expectedArguments;
    protected List<String> actualArguments;
    protected TestableConsoleLogger consoleLogger;
    private int noExecutedCommands;

    @Before
    public void setup() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);
        envVars = new EnvVars();
        envVars.put("COV_IDIR", "TestDir");

        actualArguments = new ArrayList<String>();
        expectedArguments = null;

        setUpListener();
        setUpCoverityUtils();
        noExecutedCommands = 0;
    }

    protected void setExpectedArguments(String[] args) {
        expectedArguments = args;
    }

    private void checkCommandLineArguments() {
        assertArrayEquals(expectedArguments, actualArguments.toArray());
    }

    private void setUpListener() {
        consoleLogger = new TestableConsoleLogger();
        when(listener.getLogger()).thenReturn(consoleLogger.getPrintStream());
    }

    private void setUpCoverityUtils() throws IOException, InterruptedException {
        PowerMockito.mockStatic(CoverityUtils.class);
        setCoverityUtils_runCmd();
        setCoverityUtils_evaluateEnvVars();
        setCoverityUtils_doubleQuote();
    }

    private void setCoverityUtils_runCmd() throws IOException, InterruptedException {
        Answer<Integer> runCmd = new Answer<Integer>() {
            public Integer answer(InvocationOnMock mock) throws Throwable {
                actualArguments = (ArrayList<String>)mock.getArguments()[0];
                checkCommandLineArguments();
                noExecutedCommands++;
                return 0;
            }
        };

        when(
                CoverityUtils.runCmd(
                        Matchers.anyList(),
                        Matchers.any(AbstractBuild.class),
                        Matchers.any(Launcher.class),
                        Matchers.any(TaskListener.class),
                        Matchers.same(envVars),
                        Matchers.anyBoolean())).thenAnswer(runCmd);
    }

    private void setCoverityUtils_evaluateEnvVars() {
        Answer<String> evaluateEnvVars = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return (String) invocationOnMock.getArguments()[0];
            }
        };

        when(
                CoverityUtils.evaluateEnvVars(
                        Matchers.anyString(),
                        Matchers.any(EnvVars.class),
                        Matchers.anyBoolean())).thenAnswer(evaluateEnvVars);
    }

    private void setCoverityUtils_doubleQuote() {
        Answer<String> doubleQuote = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return (String) invocationOnMock.getArguments()[0];
            }
        };

        when(
                CoverityUtils.doubleQuote(
                        Matchers.anyString(),
                        Matchers.anyBoolean())).thenAnswer(doubleQuote);
    }

    protected void setCoverityUtils_listFilesAsArray(File[] expectedFiles) {
        when(
                CoverityUtils.listFilesAsArray(
                        Matchers.any(File.class),
                        Matchers.any(FilenameFilter.class),
                        Matchers.anyBoolean())).thenReturn(expectedFiles);
    }

    protected boolean verifyNumberOfExecutedCommands(int expectedNum) {
        return expectedNum == noExecutedCommands;
    }
}
