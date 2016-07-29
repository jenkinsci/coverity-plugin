// Copyright (c) 2016 Synopsys, Inc. All right reserved worldwide.
package jenkins.plugins.coverity;

/**
 * @author mmarks
 */
public class ParseException extends Exception {
    public ParseException(String why) {
        super(why);
    }
}
