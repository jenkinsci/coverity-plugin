package jenkins.plugins.coverity;

import hudson.EnvVars;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CoverityUtilsTest {
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
        envMap.put("AtoZ", "$ABC$XYZ");
        envMap.put("VAR1", "$VAR2");
        envMap.put("VAR2", "$VAR3");
        envMap.put("VAR3", "$AtoZ");
        envMap.put("SPACE_VAR1", "a b");
        envMap.put(bigEnvVar, "78910");
        envMap.put("WRONGVAR1", "$WRONGVAR2");
        envMap.put("WRONGVAR2", "$WRONGVAR1");
        envMap.put("SINGLE_QUOTE_VAR1", "\'a b\'");
        envMap.put("SINGLE_QUOTE_VAR2", "\'a \" b\' \"$ABC\"");
        envMap.put("DOUBLE_QUOTE_VAR1", "\"a b\"");
        envMap.put("DOUBLE_QUOTE_VAR2", "\"$SPACE_VAR1\"");
        environment = new EnvVars(envMap);
    }

    private void assertExpand(String input, List<String> expectedResult, EnvVars environment){
        List<String> output = new ArrayList<String>();
        try {
            output.addAll(CoverityUtils.expand(input, environment));
        } catch(ParseException e){
            fail();
        }
        assertThat(output, is(expectedResult));
    }

    private void assertExpandFails(String input){
        try{
            CoverityUtils.expand(input, environment);
            fail();
        } catch(ParseException e) {
        }
    }

    @Test
    public void expandSuccessTest(){
        //Expands a single environment varaible.
        assertExpand("$ABC", Arrays.asList("123"), environment);
        assertExpand("$" + bigEnvVar, Arrays.asList("78910"), environment);

        //Expands concatenated environemnt varaibles.
        assertExpand("$ABC$XYZ", Arrays.asList("123456"), environment);
        assertExpand("${ABC}${XYZ}", Arrays.asList("123456"), environment);
        assertExpand("$ABC-$XYZ", Arrays.asList("123-456"), environment);

        //Expands environment variables using recursion.
        assertExpand("$VAR1", Arrays.asList("123456"), environment);
        assertExpand("echo $VAR1", Arrays.asList("echo", "123456"), environment);
        assertExpand("\"echo $VAR1\"", Arrays.asList("echo 123456"), environment);

        //Expands environment variables containing spaces
        assertExpand("$SPACE_VAR1", Arrays.asList("a", "b"), environment);

        //Expands environment variables with double quotes
        assertExpand("\"$SPACE_VAR1\"", Arrays.asList("a b"), environment);
        assertExpand("\"$DOUBLE_QUOTE_VAR1\"", Arrays.asList("a", "b"), environment);
        assertExpand("$DOUBLE_QUOTE_VAR1", Arrays.asList("a b"), environment);
        assertExpand("$DOUBLE_QUOTE_VAR2", Arrays.asList("a b"), environment);
        assertExpand("\"\"$ABC\"\"", Arrays.asList("123"), environment);

        //Expands environment variables with single quotes
        assertExpand("\'$ABC\'", Arrays.asList("\'$ABC\'"), environment);
        assertExpand("\'$DOUBLE_QUOTE_VAR1\'", Arrays.asList("\'$DOUBLE_QUOTE_VAR1\'"), environment);
        assertExpand("$SINGLE_QUOTE_VAR1", Arrays.asList("a b"), environment);
        assertExpand("$SINGLE_QUOTE_VAR2", Arrays.asList("a \" b", "123"), environment);
    }

    @Test
    public void expandFailureTest(){
        // If environment variables contains recursive definitions an exception is thrown.
        assertExpandFails("$WRONGVAR1");
    }
}
