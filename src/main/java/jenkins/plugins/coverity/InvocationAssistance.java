package jenkins.plugins.coverity;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 19/06/11
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class InvocationAssistance {

    private String buildArguments;
    private String analyzeArguments;
    private String commitArguments;

    @DataBoundConstructor
    public InvocationAssistance(String buildArguments, String analyzeArguments, String commitArguments) {

        this.buildArguments = buildArguments;
        this.analyzeArguments = analyzeArguments;
        this.commitArguments = commitArguments;
    }
}
