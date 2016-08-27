package jenkins.plugins.coverity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jenkins.plugins.coverity.EnvParser;
import jenkins.plugins.coverity.ParseException;
import org.junit.Before;
import org.junit.Test;

import hudson.EnvVars;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EnvParserTest{

    private HashMap<String, String> envMap;
    private EnvVars environment;
    private static final String bigEnvVar = 
            "_ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "12345678990";

    @Before
    public void setUp() {
        this.envMap = new HashMap<String, String>();
        envMap.put("ABC", "123");
        envMap.put("XYZ", "456");
        envMap.put(bigEnvVar, "78910");
        environment = new EnvVars(envMap);
    }

    /**
     * Tests for the interpolate() method.
     */
    private void assertInterpolatesTo(String input, String output) {
        try{
            assertEquals(output, EnvParser.interpolate(input, environment));
        } catch(ParseException e) {
            fail();
        }
    }

    private void assertInterpolatesToSelf(String input) {
        assertInterpolatesTo(input, input);
    }
    
    private void assertInterpolateFails(String input) {
        try{
            EnvParser.interpolate(input, environment);
            fail();
        } catch(ParseException e) {
        }
    }
    
    @Test
    public void interpolateSuccessTest(){
        // Innocuous text is unmodified
        assertInterpolatesToSelf("");
        assertInterpolatesToSelf("a");
        assertInterpolatesToSelf("ab");
        String innocuous = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#%^&*()_+{}[]|\\:\";<>,.?/";
        assertInterpolatesToSelf(innocuous);
        
        // Quoted strings expand to themselves
        assertInterpolatesToSelf("''");
        assertInterpolatesToSelf("'abc'");
        
        // Function of quotes: block env vars from expansion
        assertInterpolatesToSelf("'$ABC'");
        assertInterpolatesToSelf("'${ABC}'");
        
        // Function of "plain" env vars: they expand
        assertInterpolatesTo("$ABC", "123");
        assertInterpolatesTo("$ABC$XYZ", "123456");
        assertInterpolatesTo("$A", "");
        assertInterpolatesTo("$ABCD", "");
        assertInterpolatesTo("$" + bigEnvVar, "78910");
        assertInterpolatesTo("$" + bigEnvVar + "%", "78910%");
        
        // Function of "braced" env vars: they expand, but letters following them don't
        assertInterpolatesTo("${ABC}", "123");
        assertInterpolatesTo("${ABC}${XYZ}", "123456");
        assertInterpolatesTo("${A}ABC", "ABC");
        assertInterpolatesTo("${ABCD}", "");
        assertInterpolatesTo("${" + bigEnvVar + "}", "78910");
        assertInterpolatesTo("${" + bigEnvVar + "}%", "78910%");
    }

    @Test
    public void interpolateFailureTest() {
        assertInterpolateFails("'");
        assertInterpolateFails("$");
        assertInterpolateFails("${");
        assertInterpolateFails("${}");
        assertInterpolateFails("${{}");
        assertInterpolateFails("${a%}");
        assertInterpolateFails("$5abc");
        assertInterpolateFails("abc'def");
        assertInterpolateFails("abc${def");
        assertInterpolateFails("abc${}def");
        assertInterpolateFails("abc${{}def");
        assertInterpolateFails("abc$5abcdef");
    }

    /**
     * Tests for the tokenize() method.
     */
    private void assertTokenizesTo(String input, String... output) {
        try {
            assertArrayEquals(output, EnvParser.tokenize(input).toArray());
        } catch(ParseException e) {
            fail();
        }
    }

    private void assertTokenizeFails(String input) {
        try{
            EnvParser.tokenize(input);
            fail();
        } catch(ParseException e) {
        }
    }
    
    @Test
    public void tokenizeSuccessTests(){
        // flush initial space; generate not tokens
        assertTokenizesTo("");
        assertTokenizesTo(" ");
        assertTokenizesTo("  ");
        assertTokenizesTo("  \r\t\n ");
        
        // single tokens
        assertTokenizesTo("a", "a");
        assertTokenizesTo("ab", "ab");
        assertTokenizesTo(" ab", "ab");
        assertTokenizesTo("ab ", "ab");
        assertTokenizesTo("\tab ", "ab");
        
        // single tokens single quoted
        assertTokenizesTo("'ab'", "ab");
        assertTokenizesTo("'a b'", "a b");
        assertTokenizesTo("'a''b'", "ab");
        assertTokenizesTo(" 'a''b'\n", "ab");
        
        // single tokens double quoted
        assertTokenizesTo("\"ab\"", "ab");
        assertTokenizesTo("\"a b\"", "a b");
        assertTokenizesTo("\"a\"\"b\"", "ab");
        assertTokenizesTo(" \"a\"\"b\"\n", "ab");
        
        // singles around doubles
        assertTokenizesTo("'a\"b'", "a\"b");
        assertTokenizesTo("'a \"b'", "a \"b");
        assertTokenizesTo("'a''b\"'", "ab\"");
        
        // doubles around singles
        assertTokenizesTo("\"a'b\"", "a'b");
        assertTokenizesTo("\"a 'b\"", "a 'b");
        assertTokenizesTo("\"a\"\"b'\"", "ab'");
        
        // multiple tokens
        assertTokenizesTo("abc def", "abc", "def");
        assertTokenizesTo("ab'c' d'e'f", "abc", "def");
        assertTokenizesTo("  abc   def  ", "abc", "def");
    }

    
    @Test
    public void tokenizeFailureTests(){
        // unclosed single-quoted strings
        assertTokenizeFails("'");
        assertTokenizeFails("!@#$%^&*'12345\n6789");
        assertTokenizeFails("1234567890'");
        
        // unclosed double-quoted strings
        assertTokenizeFails("\"");
        assertTokenizeFails("!@#$%^&*\"12345\n6789");
        assertTokenizeFails("1234567890\"");
    }
}

