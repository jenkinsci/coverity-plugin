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
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Executor;
import hudson.model.Queue;
import hudson.model.TaskListener;
import hudson.EnvVars;

import java.util.ArrayList;
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
		
		try{
				
			for(String cmd : input){
				output.add(environment.expand(cmd));
			}
		}catch(Exception e){
			throw new RuntimeException("Error trying to evaluate Environment variables in: " + input.toString() );
		}

		return output;

	}


	public static void setEnvVars(EnvVars environment){
		envVars = environment;
	}
}