/*******************************************************************************
 * Copyright (c) 2014 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.EnvVars;
import hudson.model.Queue;
import hudson.remoting.VirtualChannel;
import hudson.util.ArgumentListBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CoverityUtils {

	public static String evaluateEnvVars(String input, AbstractBuild build, TaskListener listener)throws RuntimeException{

		try{
			return build.getEnvironment(listener).expand(input);
		}catch(Exception e){
			throw new RuntimeException("Error trying to evaluate environment variable: " + input);
		}
	}

    public static void checkDir(VirtualChannel channel, String home) throws Exception {
		FilePath homePath = new FilePath(channel, home);
        if(!homePath.exists()){
            throw new Exception("Directory: " + home + " doesn't exist.");
        }
    }

	/**
	 * getCovBuild
	 *
	 * Retrieves the location of cov-build executable/sh from the system and returns the string of the
	 * path
	 * @return  string of cov-build's path
	 */
	public static String getCovBuild(TaskListener listener, Node node) {
		Executor executor = Executor.currentExecutor();
		Queue.Executable exec = executor.getCurrentExecutable();
		AbstractBuild build = (AbstractBuild) exec;
		AbstractProject project = build.getProject();
		CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
		InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();

        if(listener == null){
            try{
                throw new Exception("Listener used by getCovBuild() is null.");
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

		String covBuild = "cov-build";
		String home = null;
		try {
			home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
		} catch(Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            listener.getLogger().println("[Error] " + pw.toString());
			e.printStackTrace();
		}
		if(invocationAssistance != null){
			if(invocationAssistance.getSaOverride() != null) {
				try {
					home = new CoverityInstallation(invocationAssistance.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
					CoverityUtils.checkDir(node.getChannel(), home);
				} catch(IOException e) {
					e.printStackTrace();
				} catch(InterruptedException e) {
					e.printStackTrace();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(home != null) {
			covBuild = new FilePath(node.getChannel(), home).child("bin").child(covBuild).getRemote();
		}

		return covBuild;
	}

    /**
     * Calls intepolate() in order to evaluate environment variables. In the case that a substitution took place, it
     * tokenize the result and it calls itself on each token in case further evaluations are needed.
     *
     * In the case of a recursive definition (ex: VAR1=$VAR2, VAR2=$VAR1) an exception is thrown.
     */
    public static List<String> expand(String input, EnvVars environment) throws ParseException {
        /**
         * Interpolates environment
         */
        List<String> output = new ArrayList<>();
        String interpolated = EnvParser.interpolateRecursively(input, 1, environment);
        output.addAll(EnvParser.tokenize(interpolated));
        return output;
    }

    /**
     * Evaluates environment variables on a command represented by a list of tokens.
     */
    public static List<String> evaluateEnvVars(List<String> input, EnvVars environment){
        List<String> output = new ArrayList<String>();
        try {
            for(String arg : input){
                output.addAll(expand(arg, environment));
            }
        } catch(ParseException e){
            throw new RuntimeException(e.getMessage());
        }
        return output;
    }

	/**
	 * Gets the stacktrace from an exception, so that this exception can be handled.
	 */
	public static String getStackTrace(Exception e){
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter( writer );
		e.printStackTrace(printWriter);
		printWriter.flush();
		String stackTrace = writer.toString();
		try {
			writer.close();
			printWriter.close();
		} catch (IOException e1) {
		}
		return stackTrace;
	}

	public static void handleException(String message, AbstractBuild<?, ?> build, BuildListener listener, Exception exception){
		listener.getLogger().println(message);
		listener.getLogger().println("Stacktrace: \n" + CoverityUtils.getStackTrace(exception));
		build.setResult(Result.FAILURE);
	}

    /**
     * Prepares command according with the specified parsing mechanism. If "useAdvancedParser" is set to true, the plugin
     * will evaluate environment variables with its custom mechanism. If not, environment variable substitution will be
     * handled by Jenkins in the standard way.
     */
    public static List<String> prepareCmds(List<String> input, String[] envVarsArray, boolean useAdvancedParser){
        EnvVars envVars = new EnvVars(arrayToMap(envVarsArray));
        if(useAdvancedParser){
            return CoverityUtils.evaluateEnvVars(input, envVars);
        } else {
            return input;
        }
    }

    /**
     * Jenkins API ProcStarter.envs() returns an array of environment variables where each element is a string "key=value".
     * However the constructor for EnvVars accepts only arrays of the format [key1, value1, key2, value2]. Because of
     * this, we need to transform that array into a map and use a constructor that accepts that map.
     */
    public static Map<String, String> arrayToMap(String[] input){
        Map<String, String> result = new HashMap<String, String>();
        for(int i=0; i<input.length; i++){
            int index = input[i].indexOf('=');
            // If an element on the array doesn't have
            if(index <= 0){
                throw new RuntimeException("Array of environment variables contains a malformed element: " + input[i]);
            }
            String key = input[i].substring(0, index);
            String value = input[i].substring(index + 1);
            result.put(key, value);
        }

        return result;
    }

    /**
     * Returns the InvocationAssistance for a given build.
     */
    public static InvocationAssistance getInvocationAssistance(AbstractBuild<?, ?> build){
        AbstractProject project = build.getProject();
        CoverityPublisher publisher = (CoverityPublisher) project.getPublishersList().get(CoverityPublisher.class);
        return publisher.getInvocationAssistance();
    }

    /**
     * Returns the InvocationAssistance on the current thread. This can be used when an "AbstractBuild" object is not
     * available, for example while decorating the launcher.
     */
    public static InvocationAssistance getInvocationAssistance(){
        Executor executor = Executor.currentExecutor();
        Queue.Executable exec = executor.getCurrentExecutable();
        AbstractBuild build = (AbstractBuild) exec;
        return getInvocationAssistance(build);
    }

    /**
     * Collects environment variables from an array and an EnvVars object and returns an updated EnvVars object.
     * This is useful for updating the environment variables on a ProcStarter with the variables from the listener.
     */
    public static String[] addEnvVars(String[] envVarsArray, EnvVars envVars){
        // All variables are stored on a map, the ones from ProcStarter will take precedence.
        EnvVars resultMap = new EnvVars(envVars);
        resultMap.putAll(arrayToMap(envVarsArray));

        String[] r = new String[resultMap.size()];
        int idx=0;

        for (Map.Entry<String,String> e : resultMap.entrySet()) {
            r[idx++] = e.getKey() + '=' + e.getValue();
        }
        return r;
    }

    public static int runCmd(List<String> cmd, AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener,
                             EnvVars envVars, boolean useAdvancedParser) throws IOException, InterruptedException {
        /**
         * Get environment variables from a launcher, add custom environment environment variables if needed,
         * then call join() to starts the launcher process.
         */
        String[] launcherEnvVars = launcher.launch().envs();
        launcherEnvVars = CoverityUtils.addEnvVars(launcherEnvVars, envVars);
        cmd = CoverityUtils.prepareCmds(cmd, launcherEnvVars, useAdvancedParser);
        int result = launcher.
                launch().
                cmds(new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]))).
                pwd(build.getWorkspace()).
                stdout(listener).
                stderr(listener.getLogger()).
                envs(launcherEnvVars).
                join();
        return result;
    }
}