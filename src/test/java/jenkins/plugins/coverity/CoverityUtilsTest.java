/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.*;
import hudson.model.Queue;
import hudson.model.listeners.SaveableListener;
import hudson.remoting.LocalChannel;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.tools.ToolLocationNodeProperty;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.Utils.CoverityPublisherBuilder;
import jenkins.plugins.coverity.Utils.InvocationAssistanceBuilder;
import jenkins.plugins.coverity.Utils.TestUtils;
import jenkins.plugins.coverity.Utils.TestableConsoleLogger;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jenkins.class, Executor.class, ToolLocationNodeProperty.class, SaveableListener.class, Node.class})
public class CoverityUtilsTest {
    @Mock
    private Jenkins jenkins;

    @Mock
    private TaskListener listener;

    @Rule
    private TemporaryFolder tempJenkinsRoot = new TemporaryFolder();

    private CoverityPublisher.DescriptorImpl descriptor;
    private TestableConsoleLogger testableConsoleLogger = new TestableConsoleLogger();
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

        PowerMockito.mockStatic(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);
        when(jenkins.getRootDir()).thenReturn(tempJenkinsRoot.getRoot());
        descriptor = new CoverityPublisher.DescriptorImpl();
        when(jenkins.getDescriptorOrDie(CoverityPublisher.class)).thenReturn(descriptor);
        when(listener.getLogger()).thenReturn(testableConsoleLogger.getPrintStream());
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
        assertExpand("\'$ABC\'", Arrays.asList("$ABC"), environment);
        assertExpand("\'$DOUBLE_QUOTE_VAR1\'", Arrays.asList("$DOUBLE_QUOTE_VAR1"), environment);
        assertExpand("$SINGLE_QUOTE_VAR1", Arrays.asList("a b"), environment);
        assertExpand("$SINGLE_QUOTE_VAR2", Arrays.asList("a \" b", "123"), environment);
    }

    @Test
    public void expandFailureTest(){
        // If environment variables contains recursive definitions an exception is thrown.
        assertExpandFails("$WRONGVAR1");
    }

    private void assertprepareCmds(List<String> cmdsInput, String[] envVarsArray, boolean useAdvancedParser, List<String> expectedResult){
        List<String> output = CoverityUtils.prepareCmds(cmdsInput, envVarsArray, useAdvancedParser);
        assertThat(output, is(expectedResult));
    }

    @Test
    public void prepareCmdsSuccessTest(){
        assertprepareCmds(Arrays.asList("$key"), new String[]{"key=value"}, true, Arrays.asList("value"));
        assertprepareCmds(Arrays.asList("$key"), new String[]{"key=value=with=equals"}, true, Arrays.asList("value=with=equals"));
        assertprepareCmds(Arrays.asList("$key"), new String[]{"key="}, true, new ArrayList<String>());
        assertprepareCmds(Arrays.asList("$key"), new String[]{"=key=value"}, true, new ArrayList<String>());
    }

    @Test
    public void listFilesTest() throws IOException {
        Collection<File> result = CoverityUtils.listFiles(null, null, false);
        assertNotNull(result);
        assertThat(result.size(), is(0));

        File testDir = createTestDirectoryWithFiles("TestDirectory", true);
        System.out.println(testDir.getAbsolutePath());
        result = CoverityUtils.listFiles(testDir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".txt");
            }
        }, true);
        assertNotNull(result);
        assertThat(result.size(), is(2));

        FileUtils.deleteDirectory(testDir);
        assertFalse(testDir.exists());
    }

    private File createTestDirectoryWithFiles(String rootDirectory, boolean subDirectory) throws IOException {
        File rootDir = new File(rootDirectory);
        if (!rootDir.exists()) {
            if (rootDir.mkdir()) {
                assertTrue(rootDir.exists());
                File file1 = new File(rootDir, "file1.txt");
                File file2 = new File(rootDir, "file2.java");

                if (file1.createNewFile() && file2.createNewFile()) {
                    assertTrue(file1.exists());
                    assertTrue(file2.exists());
                }

                if (subDirectory) {
                    File subDir = new File(rootDir, "subFolder");
                    if (subDir.mkdir()) {
                        assertTrue(subDir.exists());
                    }

                    File subFile1 = new File(subDir, "subFile1.txt");
                    File subFile2 = new File(subDir, "subFile2.java");

                    if (subFile1.createNewFile() && subFile2.createNewFile()) {
                        assertTrue(subFile1.exists());
                        assertTrue(subFile2.exists());
                    }
                }
            }
        }

        return rootDir;
    }

    @Test
    public void getInvocationAssistance_getsInvocationFromBuild() {
        FreeStyleBuild build = TestUtils.getFreeStyleBuild();
        InvocationAssistance invocationAssistance = CoverityUtils.getInvocationAssistance(build);
        assertNull(invocationAssistance);

        CoverityPublisher[] publishers = new CoverityPublisher[1];
        CoverityPublisherBuilder publisherBuilder = new CoverityPublisherBuilder();
        publishers[0] = publisherBuilder.build();
        build = TestUtils.getFreeStyleBuild(publishers);
        invocationAssistance = CoverityUtils.getInvocationAssistance(build);
        assertNull(invocationAssistance);

        InvocationAssistanceBuilder invocationAssistanceBuilder = new InvocationAssistanceBuilder();
        InvocationAssistance expected = invocationAssistanceBuilder.build();
        publisherBuilder.withInvocationAssistance(expected);
        publishers[0] = publisherBuilder.build();
        build = TestUtils.getFreeStyleBuild(publishers);
        invocationAssistance = CoverityUtils.getInvocationAssistance(build);
        assertSame(expected, invocationAssistance);
    }


    @Test
    public void findToolInstallationForBuild_returnsNullWithNoPublisher() {
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild();
        setupCurrentExecutable(build);

        final Node node = mock(Node.class);

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNull(result);
    }

    @Test
    public void findToolInstallationForBuild_returnsNullWithNoConfiguration() {
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNull(result);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void findToolInstallationForBuild_fallsBackToGlobalHomeValue() {
        final String globalHome = "pre110-install-path";
        descriptor.setHome(globalHome);
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals("global", result.getName());
        assertEquals(globalHome, result.getHome());
    }

    @Test
    public void findToolInstallationForBuild_returnsFirstInstallation_withNoDefault() {
        final String installName = "non default Coverity Tools";
        final String installPath = "non-default-install-path";
        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(new CoverityToolInstallation(installName, installPath));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals(installName, result.getName());
        assertEquals(installPath, result.getHome());
    }

    @Test
    public void findToolInstallationForBuild_returnsDefaultInstallation() {
        final String installPath = "default-install-path";
        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(
                new CoverityToolInstallation("non default Coverity Tools", "non-default-install-path"),
                new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, installPath));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals(CoverityToolInstallation.DEFAULT_NAME, result.getName());
        assertEquals(installPath, result.getHome());
    }

    @Test
    public void findToolInstallationForBuild_returnsNodeProperty_whenItExists() {
        final URL resource =  this.getClass().getResource("/VERSION.xml");
        assertNotNull(resource);
        final String nodePropertyInstallPath = new File(resource.getPath()).getParent();
        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, "default-install-path"));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithInstallationProperty(nodePropertyInstallPath);

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals(CoverityToolInstallation.JOB_OVERRIDE_NAME, result.getName());
        assertEquals(nodePropertyInstallPath, result.getHome());
    }

    @Test
    public void findToolInstallationForBuild_returnsDefault_whenNodePropertyDoesNotExists() {
        final String nodePropertyInstallPath ="some-unknown-path";
        final String defaultToolPath = "default-install-path";
        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, defaultToolPath));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithInstallationProperty(nodePropertyInstallPath);

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals(CoverityToolInstallation.DEFAULT_NAME, result.getName());
        assertEquals(defaultToolPath, result.getHome());
    }

    @Test
    public void findToolInstallationForBuild_returnsJobConfigToolsLocationOverride() {
        final CoverityToolInstallation expectedOverrideInstall = new CoverityToolInstallation("non default Coverity Tools", "non-default-install-path");
        final InvocationAssistanceBuilder invocationBuilder = new InvocationAssistanceBuilder();
        invocationBuilder.withToolsLocationOverride(expectedOverrideInstall.getName(), null);
        final CoverityPublisherBuilder publisherBuilder = new CoverityPublisherBuilder().withInvocationAssistance(invocationBuilder.build());

        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(
                expectedOverrideInstall,
                new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, "default-install-path"));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(publisherBuilder.build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals(expectedOverrideInstall.getName(), result.getName());
        assertEquals(expectedOverrideInstall.getHome(), result.getHome());
    }

    @Test
    public void findToolInstallationForBuild_returnsJobConfigCustomizedLocationOverride() {
        final String customOverridePath = "custom job-specific override";
        final CoverityToolInstallation installation = new CoverityToolInstallation("non default Coverity Tools", "non-default-install-path");
        final InvocationAssistanceBuilder invocationBuilder = new InvocationAssistanceBuilder();
        invocationBuilder.withToolsLocationOverride(installation.getName(), customOverridePath);
        final CoverityPublisherBuilder publisherBuilder = new CoverityPublisherBuilder().withInvocationAssistance(invocationBuilder.build());

        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(
                installation,
                new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, "default-install-path"));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(publisherBuilder.build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();

        final CoverityToolInstallation result = CoverityUtils.findToolInstallationForBuild(node, environment, listener);

        assertNotNull(result);
        assertEquals(installation.getName() + "-" + CoverityToolInstallation.JOB_OVERRIDE_NAME, result.getName());
        assertEquals(customOverridePath, result.getHome());
    }
    
    @Test
    public void getCovBuild_findsDefaultToolInstallation() {
        final URL resource = this.getClass().getResource("/VERSION.xml");
        assertNotNull(resource);
        final String installPath = new File(resource.getPath()).getParent();
        assertNotNull(installPath);
        PowerMockito.mockStatic(SaveableListener.class);
        descriptor.setInstallations(
                new CoverityToolInstallation("non default Coverity Tools", "non-default-install-path"),
                new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, installPath));
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild(new CoverityPublisherBuilder().build());
        setupCurrentExecutable(build);

        final Node node = mockNodeWithPropertyBehavior();
        final LocalChannel channel = mock(LocalChannel.class);
        when(node.getChannel()).thenReturn(channel);
        
        final String result = CoverityUtils.getCovBuild(listener, node);

        FilePath expectedPath = new FilePath(new FilePath(new FilePath(new File(installPath)), "bin"), "cov-build");
        assertEquals(expectedPath.getRemote(), result);
    }

    @Test
    public void getCovBuild_returnsNullWithNoInstallations() {
        final FreeStyleBuild build = TestUtils.getFreeStyleBuild();
        setupCurrentExecutable(build);

        final String result = CoverityUtils.getCovBuild(listener, mockNodeWithPropertyBehavior());

        assertNull(result);
    }

    private void setupCurrentExecutable(Queue.Executable build) {
        PowerMockito.mockStatic(Executor.class);
        final Executor executor = mock(Executor.class);
        when(Executor.currentExecutor()).thenReturn(executor);
        when(executor.getCurrentExecutable()).thenReturn(build);
    }

    @SuppressWarnings("deprecation")
    private Node mockNodeWithPropertyBehavior() {
        return mockNodeWithProperties(new DescribableList<NodeProperty<?>, NodePropertyDescriptor>(mock(Saveable.class)));
    }

    @SuppressWarnings("deprecation")
    private Node mockNodeWithInstallationProperty(String nodePropertyInstallPath) {
        DescribableList<NodeProperty<?>, NodePropertyDescriptor> nodeProperties = new DescribableList<>(mock(Saveable.class));
        nodeProperties.add(new CoverityInstallation(nodePropertyInstallPath));

        return mockNodeWithProperties(nodeProperties);
    }

    private Node mockNodeWithProperties(DescribableList<NodeProperty<?>, NodePropertyDescriptor> nodeProperties) {
        final Node node = PowerMockito.mock(Node.class);
        when(node.getNodeProperties()).thenReturn(nodeProperties);

        PowerMockito.mockStatic(ToolLocationNodeProperty.class);
        try {
            Answer<String> echoToolHome = new Answer<String>() {
                @Override
                public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                    Object[] args = invocationOnMock.getArguments();
                    CoverityToolInstallation installation = (CoverityToolInstallation)args[1];
                    return installation.getHome();
                }
            };
            PowerMockito
                    .when(ToolLocationNodeProperty.getToolHome(same(node), any(CoverityToolInstallation.class), any(TaskListener.class)))
                    .thenAnswer(echoToolHome);
        } catch (IOException  | InterruptedException e) {
            e.printStackTrace();
        }

        return node;
    }
}
