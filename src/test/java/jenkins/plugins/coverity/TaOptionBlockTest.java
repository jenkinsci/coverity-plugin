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

import com.thoughtworks.xstream.XStream;
import hudson.util.XStream2;
import jenkins.plugins.coverity.Utils.TaOptionBlockBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TaOptionBlockTest {

    @Test
    public void getTaOptionBlock_forPre190Build_returnsTaStripPaths() {
        String oldTaOptionBlockXml = "<jenkins.plugins.coverity.TaOptionBlock plugin=\"coverity@1.8.1\">\n" +
                "        <cOptionBlock>false</cOptionBlock>\n" +
                "        <csOptionBlock>false</csOptionBlock>\n" +
                "        <csFramework>none</csFramework>\n" +
                "        <csCoverageTool>none</csCoverageTool>\n" +
                "        <cxxCoverageTool>none</cxxCoverageTool>\n" +
                "        <javaCoverageTool>cobertura</javaCoverageTool>\n" +
                "        <junitFramework>true</junitFramework>\n" +
                "        <junit4Framework>false</junit4Framework>\n" +
                "        <policyFile>$WORKSPACE\\one-hundred.json</policyFile>\n" +
                "        <covHistoryCheckbox>false</covHistoryCheckbox>\n" +
                "        <javaOptionBlock>true</javaOptionBlock>\n" +
                "        <taStripPath>TestingStripPath</taStripPath>\n" +
                "      </jenkins.plugins.coverity.TaOptionBlock>";

        XStream xstream = new XStream2();

        final TaOptionBlock taOptionBlock = (TaOptionBlock)xstream.fromXML(oldTaOptionBlockXml);
        assertNotNull(taOptionBlock);

        assertFalse(taOptionBlock.getcOptionBlock());
        assertEquals("none", taOptionBlock.getCxxCoverageTool());

        assertFalse(taOptionBlock.getCsOptionBlock());
        assertEquals("none", taOptionBlock.getCsFramework());
        assertEquals("none", taOptionBlock.getCsCoverageTool());

        assertTrue(taOptionBlock.getJavaOptionBlock());
        assertEquals("cobertura", taOptionBlock.getJavaCoverageTool());
        assertTrue(taOptionBlock.getJunitFramework());
        assertFalse(taOptionBlock.getJunit4Framework());

        assertEquals("$WORKSPACE\\one-hundred.json", taOptionBlock.getPolicyFile());
        assertFalse(taOptionBlock.getCovHistoryCheckbox());

        List<TaStripPath> stripPaths = taOptionBlock.getTaStripPaths();
        assertNotNull(stripPaths);
        assertEquals(1, stripPaths.size());
        assertEquals("TestingStripPath", stripPaths.get(0).getTaStripPath());
    }

    @Test
    public void checkTaConfigTest_SuccessfulCase() {
        TaOptionBlock taOptionBlock =
                new TaOptionBlockBuilder().
                        withJavaOptionBlock(true).
                        withJavaCoverageTool("Jacoco").
                        withJunitFramework(true).
                        withPolicyFile("TestPolicyFilePath").
                        build();

        assertEquals("Pass", taOptionBlock.checkTaConfig());
    }

    @Test
    public void checkTaConfigTest_FailedCase() {
        // CASE 1: No coverage language is selected
        TaOptionBlock taOptionBlock =
                new TaOptionBlockBuilder().
                        withPolicyFile("TestPolicyFilePath").
                        build();
        assertEquals("[Test Advisor] No Coverage language was chosen, please pick at least one \n",
                taOptionBlock.checkTaConfig());

        // CASE 2: No policy file is specified
        taOptionBlock =
                new TaOptionBlockBuilder().
                        withJavaOptionBlock(true).
                        withJavaCoverageTool("Jacoco").
                        withJunitFramework(true).
                        build();
        assertEquals("[Test Advisor] Policy file is not specified. \n",
                taOptionBlock.checkTaConfig());

        // CASE 3: No Coverage tool was chosen
        taOptionBlock =
                new TaOptionBlockBuilder().
                        withJavaOptionBlock(true).
                        withJavaCoverageTool("none").
                        withPolicyFile("TestPolicyFilePath").
                        build();
        assertEquals("[Test Advisor] No Coverage tool was chosen \n",
                taOptionBlock.checkTaConfig());

        // CASE 4: CXX option block without bullseye directory specified
        taOptionBlock =
                new TaOptionBlockBuilder().
                        withCoptionBlock(true).
                        withCxxCoverageTool("bullseye").
                        withPolicyFile("TestPolicyFilePath").
                        build();
        assertEquals("[Test Advisor] Bulls eye requires the installation directory. \n",
                taOptionBlock.checkTaConfig());
    }
}
