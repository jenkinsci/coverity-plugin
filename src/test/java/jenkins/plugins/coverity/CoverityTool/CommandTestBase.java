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
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class CommandTestBase {

    protected AbstractBuild build;
    protected Launcher launcher;
    protected TaskListener listener;
    protected IMocksControl mocker;
    protected EnvVars envVars;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() throws IOException, InterruptedException {
        mocker = EasyMock.createNiceControl();
        build = mocker.createMock(AbstractBuild.class);
        launcher = mocker.createMock(Launcher.class);
        listener = mocker.createMock(TaskListener.class);
        envVars = new EnvVars();
        envVars.put("COV_IDIR", "TestDir");
    }

    @After
    public void teardown() {
        expectedException = ExpectedException.none();
        mocker.reset();
    }

    protected static void checkCommandLineArg(List<String> argList, String arg){
        assertTrue(argList.contains(arg));
        argList.remove(arg);
    }
}
