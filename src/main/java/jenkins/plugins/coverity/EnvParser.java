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

import java.util.ArrayList;
import java.util.List;

import hudson.EnvVars;

import static jenkins.plugins.coverity.EnvParser.State.NORMAL;

/**
 * Parser for replacing environment variables on a given string and split it into tokens.
 * Environment variables, single quotes and double quotes are handled in a similar way to bash.
 * Specifically, input strings are first subject to variable expansion (by interpolate()), then
 * are divided into tokens (by tokenize()).
 */
public class EnvParser {

    public enum State {
        NORMAL,
        SINGLE_QUOTE,
        DOUBLE_QUOTE,
        SPACE
    }

    /*
     * Parse the given string, returning a list of tokens.  The language accepted is
     *   commandLine ::= whiteSpace* token whiteSpace+ commandLine
     *      | {}
     *   token ::= tokenPiece+
     *   tokenPiece ::= text+ | singleQuote | doubleQuote
     *   singleQuote ::= '\'' singleCharacter* '\''
     *   singleCharacter ::= any character except '\''
     *   doubleQuote ::= '"' doubleCharacter* '"'
     *   doubleCharacter ::= any character except '"'
     *   whiteSpace ::= [ \n\t\r]
     *   text ::= any character except '\'' and '"'
     */
    public static List<String> tokenize(String input) throws ParseException {
        List<String> result = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();
        // Start in SPACE state 
        State state = State.SPACE;
        Scanner scanner = new Scanner(input);
        
        // While there are characters to get
        while(scanner.get()) {
            char c = scanner.got();
            // execute the state transition, with the side effect of collecting token characters sometimes.
            switch (state){
            case NORMAL: 
                if(c == '\"'){
                    state = State.DOUBLE_QUOTE;
                } else if(c == '\''){
                    state = State.SINGLE_QUOTE;
                } else if(c == ' ' || c == '\n' || c == '\r' || c == '\t'){
                    state = State.SPACE;
                    // spaces terminate tokens, so buffer the token and reset the token builder
                    result.add(tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                } else {
                    tokenBuilder.append(c);
                }
                break;
            case SINGLE_QUOTE:
                if(c == '\''){
                    state = State.NORMAL;
                } else {
                    tokenBuilder.append(c);
                }
                break;
            case DOUBLE_QUOTE:
                if(c == '\"'){
                    state = State.NORMAL;
                } else {
                    tokenBuilder.append(c);
                }
                break;
            case SPACE:
                if(c == '\"'){
                    state = State.DOUBLE_QUOTE;
                    tokenBuilder = new StringBuilder();
                } else if(c == '\''){
                    state = State.SINGLE_QUOTE;
                    tokenBuilder = new StringBuilder();
                } else if(c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                } else {
                    state = NORMAL;
                    tokenBuilder = new StringBuilder();
                    tokenBuilder.append(c);
                }
                break;
            }
        }

        // Handle exhaustion of characters.
        switch(state) {
        case NORMAL:
            // exxhaustion terminates the existing token, if there is one.
            String token = tokenBuilder.toString();
            if(!token.isEmpty()) {
                result.add(token);
            }
            break;
        case SINGLE_QUOTE:
            // Illegal input
            throw new ParseException("Command line parsing failed: Unclosed double-quoted string.");
            // Illegal input
        case DOUBLE_QUOTE:
            throw new ParseException("Command line parsing failed: Unclosed single-quoted string.");
        case SPACE:
        }

        return result;
    }

    public static List<String> tokenizeWithRuntimeException(String input) throws RuntimeException {
        try {
            return tokenize(input);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /*
     * This class provides a string with a movable cursor.
     */
    private static class Scanner {
        private String input;
        private int index;
        private char next;

        public Scanner(String input) {
            this.input = input;
            index = 0;
        }
        
        // If get returns true, there's another character available
        public boolean get() {
            if(index < input.length()) {
                next = input.charAt(index++);
                return true;
            }
            return false;
        }
        
        // Back up the cursor
        public void unget(){
            --index;
        }
        
        // return the next character
        public char got() {
            return next;
        }

        // do a get and require it to succeed.
        public char hunt(char sought) throws ParseException {
            if(!get()) {
                throw new ParseException("Parsing failed while seeking '" + sought + "'.");
            }
            return got();
        }
    }
    
    // The first char of an env var can't be a digit
    private static boolean isFirstEnvVarChar(char c){
        return c == '_'
            || c >= 'a' && c <= 'z'
            || c >= 'A' && c <= 'Z';
    }

    private static boolean isEnvVarChar(char c) {
        return isFirstEnvVarChar(c) || c >= '0' && c <= '9';
    }

    /*
     * This method expands environment variables in the input string.  The language
     * accepted is:
     *    commandLine ::= text* commandLine
     *      | envVar commandLine
     *      | quote commandLine
     *      | {}
     *    envVar ::= '$' '{' varName '}'
     *      | '$' varName
     *    varName ::= first subsequent*
     *    first ::= [_A-Za-z]
     *    subsequent ::= [0-9] | first
     *    text ::= any character except '$' and '\''
     *    quote ::= '\'' nonQuote* '\''
     *    nonQuote ::= any character except '\''
     *    
     *    Each envVar is replaced by the value found by looking up the associated varName in
     *    the given environment variable map, or by the empty string if the map does not contain
     *    an entry for varName.
     */
    public static String interpolate(String input, EnvVars environment) throws ParseException {
        StringBuilder builder = new StringBuilder();
        State state = NORMAL;
        Scanner scanner = new Scanner(input);
        while(scanner.get()) {
            char c = scanner.got();
            switch (state){
                case NORMAL:
                    if(c == '$'){
                        // Starting an env var name
                        if(!scanner.get()) {
                            throw makeInterpolationException("Missing environment variable.");
                        }
                        c = scanner.got();
                        StringBuilder envVarNameBuilder = new StringBuilder();
                        if(c == '{') {
                            // In this branch the nv var is terminated by a } char
                            c = scanner.hunt('}');
                            if(isFirstEnvVarChar(c)) {
                                envVarNameBuilder.append(c);
                            } else {
                                throw makeInterpolationException("Invalid first environment variable character: '"
                                    + c + "'.");
                            }
                            while((c = scanner.hunt('}')) != '}'){
                                if(isEnvVarChar(c)) {
                                    envVarNameBuilder.append(c);
                                } else {
                                    throw makeInterpolationException("Invalid environment variable character: '"
                                        + c + "'.");
                                }
                            }
                        } else {
                            // here the env var name is terminated by a non-env-var char or end of input.
                            if(isFirstEnvVarChar(c)) {
                                envVarNameBuilder.append(c);
                            } else {
                                throw makeInterpolationException("Invalid first environment variable character: '"
                                        + c + "'.");
                            }
                            while(scanner.get()) {
                                c = scanner.got();
                                if(isEnvVarChar(c)) {
                                    envVarNameBuilder.append(c);
                                } else {
                                    // Unconsume the last char so it can be used in the next trip around the loop
                                    scanner.unget();
                                    break;
                                }
                            }
                        }
                        if(envVarNameBuilder.length() == 0){
                            throw makeInterpolationException("Empty environment variable name");
                        }
                        // Substitute the value of the env var
                        String envValue = environment.get(envVarNameBuilder.toString());
                        if(envValue != null) {
                            builder.append(envValue);
                        }
                    } else if(c == '\''){
                        state = State.SINGLE_QUOTE;
                        builder.append(c);
                    } else {
                        builder.append(c);
                    }
                    break;
                case SINGLE_QUOTE:
                    if(c == '\'') {
                        state = NORMAL;
                    }
                    builder.append(c);
                    break;
            }
        }

        if(!state.equals(NORMAL)){
            throw makeInterpolationException("Unterminated single-quoted string.");
        }

        return builder.toString();
    }

    private static ParseException makeInterpolationException(String reason) {
        return new ParseException("Error expanding environment variables: " + reason);
    }

    public static String interpolateRecursively(String input, int depth, EnvVars environment) throws ParseException {
        if(depth > 20){
            throw new ParseException("Recursive environment variable referenced in \"" + input + "\"");
        }

        String interpolated = EnvParser.interpolate(input, environment);
        if(interpolated.equals(input)){
            return input;
        } else {
            return interpolateRecursively(interpolated, depth + 1, environment);
        }
    }
}
