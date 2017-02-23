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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import hudson.util.XStream2;

public class CoverityPublisherTest {
    @Test
    public void getCimStream_fromOldPre12Configuration() {
        String oldJobXml = "<jenkins.plugins.coverity.CoverityPublisher plugin=\"coverity@1.2.0\">\n" +
            "      <cimInstance>test-cim-instance</cimInstance>\n" +
            "      <project>test-cim-project</project>\n" +
            "      <stream>test-cim-stream</stream>\n" +
            "      <defectFilters>\n" +
            "        <classifications>\n" +
            "          <string>Unclassified</string>\n" +
            "          <string>Pending</string>\n" +
            "          <string>Bug</string>\n" +
            "          <string>Untested</string>\n" +
            "        </classifications>\n" +
            "        <actions>\n" +
            "          <string>Undecided</string>\n" +
            "          <string>Fix Required</string>\n" +
            "          <string>Fix Submitted</string>\n" +
            "          <string>Modeling Required</string>\n" +
            "          <string>Ignore</string>\n" +
            "        </actions>\n" +
            "        <severities>\n" +
            "          <string>Unspecified</string>\n" +
            "          <string>Major</string>\n" +
            "          <string>Moderate</string>\n" +
            "          <string>Minor</string>\n" +
            "        </severities>\n" +
            "        <components>\n" +
            "          <string>Default.Other</string>\n" +
            "        </components>\n" +
            "        <checkers>\n" +
            "          <string>ALLOC_FREE_MISMATCH</string>\n" +
            "          <string>BAD_EQ</string>\n" +
            "          <string>BAD_EQ_TYPES</string>\n" +
            "          <string>BAD_FREE</string>\n" +
            "          <string>DEADCODE</string>\n" +
            "          <string>DELETE_ARRAY</string>\n" +
            "          <string>DELETE_VOID</string>      \n" +
            "          <string>FORWARD_NULL</string>\n" +
            "          <string>NULL_RETURNS</string>\n" +
            "          <string>OVERFLOW_BEFORE_WIDEN</string>\n" +
            "          <string>RESOURCE_LEAK</string>\n" +
            "          <string>UNINIT</string>\n" +
            "        </checkers>\n" +
            "        <ignoredCheckers/>\n" +
            "        <impacts>\n" +
            "          <string>High</string>\n" +
            "          <string>Medium</string>\n" +
            "          <string>Low</string>\n" +
            "        </impacts>\n" +
            "      </defectFilters>\n" +
            "      <invocationAssistance>\n" +
            "        <javaWarFilesNames/>\n" +
            "        <csharpAutomaticAssemblies>false</csharpAutomaticAssemblies>\n" +
            "        <csharpMsvsca>false</csharpMsvsca>\n" +
            "        <isUsingMisra>false</isUsingMisra>\n" +
            "        <isCompiledSrc>false</isCompiledSrc>\n" +
            "        <isScriptSrc>false</isScriptSrc>\n" +
            "        <isUsingPostCovBuildCmd>false</isUsingPostCovBuildCmd>\n" +
            "        <isUsingPostCovAnalyzeCmd>false</isUsingPostCovAnalyzeCmd>\n" +
            "        <useAdvancedParser>false</useAdvancedParser>\n" +
            "      </invocationAssistance>\n" +
            "      <failBuild>false</failBuild>\n" +
            "      <unstable>true</unstable>\n" +
            "      <keepIntDir>false</keepIntDir>\n" +
            "      <skipFetchingDefects>false</skipFetchingDefects>\n" +
            "      <hideChart>false</hideChart>\n" +
            "      <unstableBuild>false</unstableBuild>\n" +
            "    </jenkins.plugins.coverity.CoverityPublisher>";

        XStream xstream = new XStream2();

        final CoverityPublisher publisher = (CoverityPublisher)xstream.fromXML(oldJobXml);
        assertNotNull(publisher);

        final CIMStream stream = publisher.getCimStream();
        assertNotNull(stream);
        assertEquals("test-cim-instance", stream.getInstance());
        assertEquals("test-cim-project", stream.getProject());
        assertEquals("test-cim-stream", stream.getStream());
        final DefectFilters defectFilters = stream.getDefectFilters();
        assertNotNull(defectFilters);
        assertEquals(4, defectFilters.getClassifications().size());
        assertEquals(5, defectFilters.getActions().size());
        assertEquals(4, defectFilters.getSeverities().size());
        assertEquals(1, defectFilters.getComponents().size());
        assertEquals(12, defectFilters.getCheckersList().size());
        assertEquals(0, defectFilters.getIgnoredChecker().size());
        assertEquals(3, defectFilters.getImpacts().size());
    }

    @Test
    public void getCimStream_fromOldPre190Configuration_withMultipleStreams() {
        String oldJobXml = "<jenkins.plugins.coverity.CoverityPublisher plugin=\"coverity@1.8.1\">\n" +
            "      <cimStreams>\n" +
            "        <jenkins.plugins.coverity.CIMStream>\n" +
            "          <instance>first-cim-instance</instance>\n" +
            "          <project>first-cim-project</project>\n" +
            "          <stream>first-cim-stream</stream>\n" +
            "          <id>1955941757</id>\n" +
            "          <defectFilters>\n" +
            "            <classifications>\n" +
            "              <string>Unclassified</string>\n" +
            "              <string>Pending</string>\n" +
            "              <string>Bug</string>\n" +
            "              <string>Untested</string>\n" +
            "            </classifications>\n" +
            "            <actions>\n" +
            "              <string>Undecided</string>\n" +
            "              <string>Fix Required</string>\n" +
            "              <string>Fix Submitted</string>\n" +
            "              <string>Modeling Required</string>\n" +
            "              <string>Ignore</string>\n" +
            "            </actions>\n" +
            "            <severities>\n" +
            "              <string>Unspecified</string>\n" +
            "              <string>Major</string>\n" +
            "              <string>Moderate</string>\n" +
            "              <string>Minor</string>\n" +
            "            </severities>\n" +
            "            <components>\n" +
            "              <string>Default.Other</string>\n" +
            "            </components>\n" +
            "            <checkers class=\"empty-list\"/>\n" +
            "            <ignoredCheckers/>\n" +
            "            <impacts class=\"empty-list\"/>\n" +
            "          </defectFilters>\n" +
            "          <invocationAssistanceOverride>\n" +
            "            <buildArguments>--return-emit-failures</buildArguments>\n" +
            "            <analyzeArguments>-j 4 --all</analyzeArguments>\n" +
            "            <javaWarFilesNames/>\n" +
            "            <csharpAutomaticAssemblies>false</csharpAutomaticAssemblies>\n" +
            "            <csharpMsvsca>false</csharpMsvsca>\n" +
            "            <isUsingMisra>false</isUsingMisra>\n" +
            "            <isScriptSrc>false</isScriptSrc>\n" +
            "            <isUsingPostCovBuildCmd>false</isUsingPostCovBuildCmd>\n" +
            "            <isUsingPostCovAnalyzeCmd>false</isUsingPostCovAnalyzeCmd>\n" +
            "            <useAdvancedParser>false</useAdvancedParser>\n" +
            "          </invocationAssistanceOverride>" +
            "        </jenkins.plugins.coverity.CIMStream>\n" +
            "        <jenkins.plugins.coverity.CIMStream>\n" +
            "          <instance>test-cim-instance</instance>\n" +
            "          <project>another projectt</project>\n" +
            "          <stream>another stream</stream>\n" +
            "          <id>0123456789</id>\n" +
            "          <defectFilters>\n" +
            "            <classifications>\n" +
            "              <string>Unclassified</string>\n" +
            "            </classifications>\n" +
            "            <actions>\n" +
            "              <string>Undecided</string>\n" +
            "            </actions>\n" +
            "            <severities>\n" +
            "              <string>Unspecified</string>\n" +
            "            </severities>\n" +
            "            <components>\n" +
            "              <string>Default.Other</string>\n" +
            "            </components>\n" +
            "            <checkers>\n" +
            "              <string>DEADCODE</string>\n" +
            "              <string>FORWARD_NULL</string>\n" +
            "              <string>RESOURCE_LEAK</string>\n" +
            "            </checkers>\n" +
            "            <ignoredCheckers/>\n" +
            "            <impacts>\n" +
            "              <string>High</string>\n" +
            "              <string>Medium</string>\n" +
            "              <string>Low</string>\n" +
            "            </impacts>\n" +
            "          </defectFilters>\n" +
            "        </jenkins.plugins.coverity.CIMStream>\n" +
            "      </cimStreams>\n" +
            "      <invocationAssistance>\n" +
            "        <javaWarFilesNames/>\n" +
            "        <csharpAutomaticAssemblies>false</csharpAutomaticAssemblies>\n" +
            "        <csharpMsvsca>false</csharpMsvsca>\n" +
            "        <isUsingMisra>false</isUsingMisra>\n" +
            "        <isCompiledSrc>false</isCompiledSrc>\n" +
            "        <isScriptSrc>true</isScriptSrc>\n" +
            "        <isUsingPostCovBuildCmd>false</isUsingPostCovBuildCmd>\n" +
            "        <isUsingPostCovAnalyzeCmd>false</isUsingPostCovAnalyzeCmd>\n" +
            "        <useAdvancedParser>false</useAdvancedParser>\n" +
            "      </invocationAssistance>\n" +
            "      <failBuild>false</failBuild>\n" +
            "      <unstable>false</unstable>\n" +
            "      <keepIntDir>false</keepIntDir>\n" +
            "      <skipFetchingDefects>false</skipFetchingDefects>\n" +
            "      <hideChart>false</hideChart>\n" +
            "      <unstableBuild>false</unstableBuild>\n" +
            "    </jenkins.plugins.coverity.CoverityPublisher>";

        XStream xstream = new XStream2();

        final CoverityPublisher publisher = (CoverityPublisher)xstream.fromXML(oldJobXml);
        assertNotNull(publisher);

        final CIMStream stream = publisher.getCimStream();
        assertNotNull(stream);
        assertEquals("first-cim-instance", stream.getInstance());
        assertEquals("first-cim-project", stream.getProject());
        assertEquals("first-cim-stream", stream.getStream());
        final DefectFilters defectFilters = stream.getDefectFilters();
        assertNotNull(defectFilters);
        assertEquals(4, defectFilters.getClassifications().size());
        assertEquals(5, defectFilters.getActions().size());
        assertEquals(4, defectFilters.getSeverities().size());
        assertEquals(1, defectFilters.getComponents().size());
        assertNull( defectFilters.getCheckersList());
        assertEquals(0, defectFilters.getIgnoredChecker().size());
        assertNull(defectFilters.getImpacts());

        // verify invocation assistance overrides
        final InvocationAssistance invocationAssistance = publisher.getInvocationAssistance();
        assertNotNull(invocationAssistance);
        assertEquals("--return-emit-failures", invocationAssistance.getBuildArguments());
        assertEquals("-j 4 --all", invocationAssistance.getAnalyzeArguments());
    }
}
