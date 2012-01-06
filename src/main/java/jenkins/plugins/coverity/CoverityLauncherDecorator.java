/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.*;
import hudson.model.*;
import hudson.model.Queue;
import hudson.remoting.Channel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Logger;

import com.coverity.ws.v3.CovRemoteServiceException_Exception;

/**
 * This makes sure that all commands executed (when invocation assistance is enabled) are run through cov-build
 */
@Extension
public class CoverityLauncherDecorator extends LauncherDecorator {

    private static final Logger logger = Logger.getLogger(CoverityLauncherDecorator.class.getName());

    /**
     * A ThreadLocal that is used to disable cov-build when running other Coverity tools during the build.
     */
    public static ThreadLocal<Boolean> SKIP = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Launcher decorate(Launcher launcher, Node node) {
        Executor executor = Executor.currentExecutor();
        if (executor == null) {
            return launcher;
        }

        Queue.Executable exec = executor.getCurrentExecutable();
        if (!(exec instanceof AbstractBuild)) {
            return launcher;
        }
        AbstractBuild build = (AbstractBuild) exec;

        AbstractProject project = build.getProject();

        CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
        if (publisher == null) {
            return launcher;
        }

        InvocationAssistance ii = publisher.getInvocationAssistance();
        if (ii == null) {
            return launcher;
        }
        
        // Do not run cov-build if language is "CSHARP"
        String cimInstanceName = publisher.getCimInstance();
        CIMInstance cim = publisher.getDescriptor().getInstance(publisher.getCimInstance());
        
        String streamName = publisher.getStream();
        String language = null;
        try {
            language = cim.getStream(streamName).getLanguage();
        } catch (CovRemoteServiceException_Exception e) {
        	throw new RuntimeException("Error while retrieving stream information for " + streamName, e);
        } catch (IOException e) {
        	throw new RuntimeException("Error while retrieving stream information for " + streamName, e);
        }

        FilePath temp;

        try {
            if (ii.getIntermediateDir() == null) {
                FilePath coverityDir = node.getRootPath().child("coverity");
                coverityDir.mkdirs();
                temp = coverityDir.createTempDir("temp-", null);
            } else {
                temp = new FilePath(node.getChannel(), ii.getIntermediateDir());
                temp.mkdirs();
            }

            build.addAction(new CoverityTempDir(temp, ii.getIntermediateDir() == null));
        } catch (IOException e) {
            throw new RuntimeException("Error while creating temporary directory for Coverity", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while creating temporary directory for Coverity");
        }

        if ("CSHARP".equals(language)) {
        	logger.info("Stream " + streamName + " is of type CSHARP, skipping cov-build");
        	
        	return launcher;
        }
        
        try {
            String covBuild = "cov-build";
            TaskListener listener = launcher.getListener();
            String home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
            if (home != null) {
                covBuild = new FilePath(node.getChannel(), home).child("bin").child(covBuild).getRemote();
            }

            List<String> args = new ArrayList<String>();
            args.add(covBuild);
            args.add("--dir");
            args.add(temp.getRemote());
            if (ii.getBuildArguments() != null) {
                for (String arg: Util.tokenize(ii.getBuildArguments())) {
                    args.add(arg);
                }
            }
            
            return new DecoratedLauncher(launcher, args.toArray(new String[args.size()]));
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while creating temporary directory for Coverity");
        } catch (IOException e) {
            throw new RuntimeException("Error while creating temporary directory for Coverity", e);
        }


    }

    /**
     * Returns a decorated {@link Launcher} that puts the given set of arguments as a prefix to any commands
     * that it invokes.
     */
    public class DecoratedLauncher extends Launcher {
        private final Launcher decorated;
        private final String[] prefix;

        public DecoratedLauncher(Launcher decorated, String... prefix) {
            super(decorated);
            this.decorated = decorated;
            this.prefix = prefix;
        }

            @Override
            public Proc launch(ProcStarter starter) throws IOException {
                if (!SKIP.get()) {
                    List<String> cmds = starter.cmds();
                    cmds.addAll(0, Arrays.asList(prefix));
                    starter.cmds(cmds);
                    boolean[] masks = starter.masks();
                    if (masks == null) {
                        masks = new boolean[cmds.size()];
                        starter.masks(masks);
                    } else {
                        starter.masks(prefix(masks));
                    }
                }
                return decorated.launch(starter);
            }

            @Override
            public Channel launchChannel(String[] cmd, OutputStream out, FilePath workDir, Map<String, String> envVars) throws IOException, InterruptedException {
                return decorated.launchChannel(prefix(cmd),out,workDir,envVars);
            }

            @Override
            public void kill(Map<String, String> modelEnvVars) throws IOException, InterruptedException {
                decorated.kill(modelEnvVars);
            }

            private String[] prefix(String[] args) {
                String[] newArgs = new String[args.length+prefix.length];
                System.arraycopy(prefix,0,newArgs,0,prefix.length);
                System.arraycopy(args,0,newArgs,prefix.length,args.length);
                return newArgs;
            }

            private boolean[] prefix(boolean[] args) {
                boolean[] newArgs = new boolean[args.length+prefix.length];
                System.arraycopy(args,0,newArgs,prefix.length,args.length);
                return newArgs;
            }
        }

}
