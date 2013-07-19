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

import com.coverity.ws.v5.CovRemoteServiceException_Exception;
import com.coverity.ws.v5.StreamDataObj;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.LauncherDecorator;
import hudson.Proc;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Executor;
import hudson.model.Node;
import hudson.model.Queue;
import hudson.model.TaskListener;
import hudson.remoting.Channel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This makes sure that all commands executed (when invocation assistance is enabled) are run through cov-build.
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
		if(executor == null) {
			return launcher;
		}

		Queue.Executable exec = executor.getCurrentExecutable();
		if(!(exec instanceof AbstractBuild)) {
			return launcher;
		}
		AbstractBuild build = (AbstractBuild) exec;

		AbstractProject project = build.getProject();

		CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
		if(publisher == null) {
			return launcher;
		}

		InvocationAssistance ii = publisher.getInvocationAssistance();
		if(ii == null) {
			return launcher;
		}

		FilePath temp;

		try {
			if(ii.getIntermediateDir() == null) {
				FilePath coverityDir = node.getRootPath().child("coverity");
				coverityDir.mkdirs();
				temp = coverityDir.createTempDir("temp-", null);
			} else {
                TaskListener listener = launcher.getListener();
                EnvVars env = build.getEnvironment(listener);

				temp = new FilePath(node.getChannel(), env.expand(ii.getIntermediateDir()));
				temp.mkdirs();
			}

			build.addAction(new CoverityTempDir(temp, ii.getIntermediateDir() == null));
		} catch(IOException e) {
			throw new RuntimeException("Error while creating temporary directory for Coverity", e);
		} catch(InterruptedException e) {
			throw new RuntimeException("Interrupted while creating temporary directory for Coverity");
		}

		// Do not run cov-build if language is "CSHARP"
		boolean onlyCS = true;
		for(CIMStream cs : publisher.getCimStreams()) {
			CIMInstance cim = publisher.getDescriptor().getInstance(cs.getInstance());
			String id = cs.getInstance() + "/" + cs.getStream();
			try {
				StreamDataObj stream = cim.getStream(cs.getStream());
				if(stream == null) {
					throw new RuntimeException("Could not find stream: " + id);
				}
				String language = stream.getLanguage();
				if(!"CSHARP".equals(language)) {
					onlyCS = false;
					break;
				}
			} catch(CovRemoteServiceException_Exception e) {
				throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
			} catch(IOException e) {
				throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
			}
		}
		//look for old-style stream format
		if(publisher.getCimInstance() != null) {
			CIMInstance cim = publisher.getDescriptor().getInstance(publisher.getCimInstance());
			String id = publisher.getCimInstance() + "/" + publisher.getStream();
			try {
				StreamDataObj stream = cim.getStream(publisher.getStream());
				if(stream == null) {
					throw new RuntimeException("Could not find stream: " + id);
				}
				String language = stream.getLanguage();
				if(!"CSHARP".equals(language)) {
					onlyCS = false;
				}
			} catch(CovRemoteServiceException_Exception e) {
				throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
			} catch(IOException e) {
				throw new RuntimeException("Error while retrieving stream information for instance/stream: " + id, e);
			}
		}
		if(onlyCS) {
			logger.info("Only streams of type CSHARP were found, skipping cov-build");

			return launcher;
		}

        EnvVars env = build.getEnvironment(listener);
		List<String> args = new ArrayList<String>();
		args.add("cov-build-placeholder");
		args.add("--dir");
		args.add(temp.getRemote());
		if(ii.getBuildArguments() != null) {
			for(String arg : Util.tokenize(env.expand(ii.getBuildArguments()))) {
				args.add(arg);
			}
		}

		String blacklistTemp = ii.getCovBuildBlacklist();
		String[] blacklist;
		if(blacklistTemp != null) {
			blacklist = blacklistTemp.split(",");
			for(int i = 0; i < blacklist.length; i++) {
				blacklist[i] = blacklist[i].trim();
		    }
		} else {
			blacklist = new String[0];
		}

		return new DecoratedLauncher(launcher, blacklist, node, args.toArray(new String[args.size()]));
	}

	/**
	 * A decorated {@link Launcher} that puts the given set of arguments as a prefix to any commands that it invokes.
	 */
	public class DecoratedLauncher extends Launcher {
		private final Launcher decorated;
		private final String[] prefix;
		private final String[] blacklist;
		private final String toolsDir;
		private final Node node;

		public DecoratedLauncher(Launcher decorated, String[] blacklist, Node node, String... prefix) {
			super(decorated);
			this.decorated = decorated;
			this.prefix = prefix;
			this.blacklist = blacklist;
			this.node = node;
			this.toolsDir = node.getRootPath().child("tools").getRemote();
		}

		private String[] getPrefix() {
			String[] tp = prefix.clone();
			tp[0] = getCovBuild();
			return tp;
		}

		@Override
		public Proc launch(ProcStarter starter) throws IOException {
			if(!SKIP.get()) {
				if(isBlacklisted(starter.cmds().get(0))) {
					logger.info(starter.cmds().get(0) + " is blacklisted, skipping cov-build");
					return decorated.launch(starter);
				}

				List<String> cmds = starter.cmds();

				//skip jdk installations
				String lastArg = cmds.get(cmds.size() - 1);
				if(lastArg.startsWith(toolsDir) && lastArg.endsWith(".sh")) {
					logger.info(lastArg + " is a tools script, skipping cov-build");
					return decorated.launch(starter);
				}

				cmds.addAll(0, Arrays.asList(getPrefix()));
				starter.cmds(cmds);
				boolean[] masks = starter.masks();
				if(masks == null) {
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
			String lastArg = cmd[cmd.length - 1];
			if(lastArg.startsWith(toolsDir) && lastArg.endsWith(".sh")) {
				logger.info(lastArg + " is a tools script, skipping cov-build");
				decorated.launchChannel(cmd, out, workDir, envVars);
			}
			return decorated.launchChannel(prefix(cmd), out, workDir, envVars);
		}

		@Override
		public void kill(Map<String, String> modelEnvVars) throws IOException, InterruptedException {
			decorated.kill(modelEnvVars);
		}

		private String[] prefix(String[] args) {
			if(isBlacklisted(args[0])) {
				return args;
			}
			String[] newArgs = new String[args.length + prefix.length];
			System.arraycopy(getPrefix(), 0, newArgs, 0, prefix.length);
			System.arraycopy(args, 0, newArgs, prefix.length, args.length);
			return newArgs;
		}

		private boolean[] prefix(boolean[] args) {
			boolean[] newArgs = new boolean[args.length + prefix.length];
			System.arraycopy(args, 0, newArgs, prefix.length, args.length);
			return newArgs;
		}

		private boolean isBlacklisted(String cmd) {
			for(String s : blacklist) {
				if(s.equals(cmd)) {
					return true;
				}
			}
			return false;
		}

		private String getCovBuild() {
			Executor executor = Executor.currentExecutor();
			Queue.Executable exec = executor.getCurrentExecutable();
			AbstractBuild build = (AbstractBuild) exec;
			AbstractProject project = build.getProject();
			CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
			InvocationAssistance ii = publisher.getInvocationAssistance();

			String covBuild = "cov-build";
			TaskListener listener = decorated.getListener();
			String home = null;
			try {
				home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
			} catch(IOException e) {
				e.printStackTrace();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(ii.getSaOverride() != null) {
				try {
					home = new CoverityInstallation(ii.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
				} catch(IOException e) {
					e.printStackTrace();
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(home != null) {
				covBuild = new FilePath(node.getChannel(), home).child("bin").child(covBuild).getRemote();
			}

			return covBuild;
		}
	}
}
