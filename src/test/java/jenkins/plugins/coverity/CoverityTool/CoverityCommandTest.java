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
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.CoverityTempDir;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CoverityCommandTest {

    private final static String command = "cov-command";
    private AbstractBuild build;
    private Launcher launcher;
    private TaskListener listener;
    private CoverityPublisher publisher;
    private EnvVars envVars;

    @Before
    public void setup() {
        build = mock(AbstractBuild.class);
        launcher = mock(Launcher.class);
        listener = mock(TaskListener.class);
        publisher = new CoverityPublisherBuilder().build();
        envVars = new EnvVars();
    }

    @Test
    public void addIntermediateDirTest_WithEnvironmentVar() {
        envVars.put("COV_IDIR", "TestDir");
        final List<String> expectedArguments = Arrays.asList("cov-command", "--dir", "TestDir");

        TestableCoverityCommand coverityCommand =
                new TestableCoverityCommand(command, build, launcher, listener, publisher, StringUtils.EMPTY, envVars);

        List<String> args = coverityCommand.getCommands();
        assertEquals(3, args.size());
        assertEquals(expectedArguments, args);
    }

    @Test
    public void addIntermediateDirTest_WithCoverityTempDir() {
        CoverityTempDir tempDir = new CoverityTempDir(new FilePath(new File("TestIntermediateDir")), false);
        when(build.getAction(CoverityTempDir.class)).thenReturn(tempDir);

        final List<String> expectedArguments = Arrays.asList("cov-command", "--dir", "TestIntermediateDir");

        TestableCoverityCommand coverityCommand =
                new TestableCoverityCommand(command, build, launcher, listener, publisher, StringUtils.EMPTY, envVars);

        List<String> args = coverityCommand.getCommands();
        assertEquals(3, args.size());
        assertEquals(expectedArguments, args);
    }

    @Test
    public void addIntermediateDirTest_Default() {
        final List<String> expectedArguments = Arrays.asList("cov-command", "--dir", "$COV_IDIR");

        TestableCoverityCommand coverityCommand =
                new TestableCoverityCommand(command, build, launcher, listener, publisher, StringUtils.EMPTY, envVars);

        List<String> args = coverityCommand.getCommands();
        assertEquals(3, args.size());
        assertEquals(expectedArguments, args);
    }

    public static class TestableCoverityCommand extends CoverityCommand{

        public TestableCoverityCommand(@Nonnull String command, AbstractBuild<?, ?> build,
                                       Launcher launcher, TaskListener listener, CoverityPublisher publisher,
                                       String home, EnvVars envVars) {
            super(command, build, launcher, listener, publisher, home, envVars);
        }

        @Override
        protected void prepareCommand() {
            // no op
        }

        @Override
        protected boolean canExecute() {
            return true;
        }

        public List<String> getCommands() {
            return commandLine;
        }
    }
}
