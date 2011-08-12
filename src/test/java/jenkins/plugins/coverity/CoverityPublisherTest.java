package jenkins.plugins.coverity;

import hudson.FilePath;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
import hudson.model.Result;
import hudson.tasks.BatchFile;
import hudson.util.IOUtils;
import org.jvnet.hudson.test.HudsonTestCase;

import java.util.Arrays;

public class CoverityPublisherTest extends HudsonTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Hudson.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class).setInstances(Arrays.asList(new CIMInstance("cimInstance", "localhost", 18080, "admin", "password", false)));
    }

    /**
     * this test assumes a project has been set up in CIM, with name unit-tests and Java as a language.
     * @throws Exception
     */
    public void testBasic() throws Exception {
        FreeStyleProject project = createFreeStyleProject();

        FilePath ws = new FilePath(project.getRootDir()).child("workspace");
        ws.deleteRecursive();

        String source = IOUtils.toString(getClass().getResourceAsStream("GoodSource.java.txt"));
        ws.child("Source1.java").write(source.replaceAll("CLASSNAME", "Source1"), "UTF-8");

        project.getBuildersList().add(new BatchFile("javac *.java"));

        project.getPublishersList().add(
                new CoverityPublisher(
                        "cimInstance",
                        new InvocationAssistance("", "", "", null),
                        "unit-tests",
                        "unit-tests",
                        true,
                        new DefectFilters(
                                Arrays.<String>asList("Undecided"),
                                Arrays.<String>asList("Unspecified"),
                                Arrays.<String>asList("Default.Other"),
                                Arrays.<String>asList("FORWARD_NULL"),
                                null),
                        new CoverityMailSender(null)
                )
        );

        buildAndAssertSuccess(project);

        source = IOUtils.toString(getClass().getResourceAsStream("BadSource.java.txt"));
        ws.child("Source2.java").write(source.replaceAll("CLASSNAME", "Source2"), "UTF-8");

        assertBuildStatus(Result.FAILURE, project.scheduleBuild2(0).get());

        System.out.println("test complete");

    }

}
