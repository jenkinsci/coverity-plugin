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
import hudson.model.*;
import hudson.EnvVars;
import hudson.remoting.VirtualChannel;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CoverityUtils {


    static EnvVars envVars;
    
	
	public static String evaluateEnvVars(String input, AbstractBuild build, TaskListener listener)throws RuntimeException{
		
		try{
			envVars = build.getEnvironment(listener);
			return envVars.expand(input);
		}catch(Exception e){
			throw new RuntimeException("Error trying to evaluate environment variable: " + input);
		}
	}

	public static List<String> evaluateEnvVars(List<String> input, AbstractBuild build, TaskListener listener)throws RuntimeException{
		List<String> output = new ArrayList<String>();
		
		try{
			envVars = build.getEnvironment(listener);	
			
			for(String cmd : input){
                /**
                 * Fix bug 78168
                 * After evaluating an environment variable, we need to check if more than one options where specified
                 * on it. In order to do so, we use the command split(" ").
                 */
                cmd = envVars.expand(cmd).trim().replaceAll(" +", " ");
                Collections.addAll(output, cmd.split(" "));
			}
		}catch(Exception e){
			throw new RuntimeException("Error trying to evaluate Environment variables in: " + input.toString() );
		}

		return output;

	}

	public static String evaluateEnvVars(String input, EnvVars environment)throws RuntimeException{

		try{
			return environment.expand(input);
		}catch(Exception e){
			throw new RuntimeException("Error trying to evaluate environment variable: " + input);
		}
	}

    public static List<String> evaluateEnvVars(List<String> input, EnvVars environment)throws RuntimeException{
		List<String> output = new ArrayList<String>();
        String inputString = StringUtils.join(input, " ");
        boolean parsingWasNeeded = false;
		try{
            /**
             * Fix 78168 and 79244.
             * Environment variables are now parsed properly even with quotes and spaces. This method will use self
             * recursion until no further parsing is needed.
             */
            for(String cmd : input){
                for(String key : environment.keySet()){
                    if(cmd.equals("$" + key) || cmd.equals("${" + key + "}")){
                        cmd = environment.get(key);
                        parsingWasNeeded = true;
                        break;
                    }
                }
                /**
                 * In the case of a escaped cmd, Commandline.translateCommandline(cmd) would remove the escaping. Hence,
                 * in that case the plugin should use the original cmd. However, there are cases on which an env variable
                 * contains a command as value, then this command would require further parsing.
                 */
                String expandedCmds[] = Commandline.translateCommandline(cmd);
                if(expandedCmds.length > 1){
                    output.addAll(Arrays.asList(expandedCmds));
                    parsingWasNeeded = true;
                } else {
                    output.add(cmd);
                }
            }

            /**
             * The plugin keeps parsing the given command until parsing the command doesn't change the command anymore.
             * That condition is determined by the boolean parsingWasNeeded which checks if a new environment variable
             * was found of if new tokens were found.
             */
            if(parsingWasNeeded){
                return evaluateEnvVars(output, environment);
            } else {
                return input;
            }
		}catch(Exception e){
			throw new RuntimeException("Error trying to evaluate Environment variables in: " + input.toString() );
		}
	}


	public static void setEnvVars(EnvVars environment){
		envVars = environment;
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

}