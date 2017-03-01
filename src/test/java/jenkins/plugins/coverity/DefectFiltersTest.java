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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import hudson.model.Descriptor.FormException;

public class DefectFiltersTest {
    @Test
    public void invertChecker_maintainsComponentsAndCheckersNotSelected() throws FormException {
        DefectFilters filters = new DefectFilters(
            Arrays.asList("Undecided"),
            Arrays.asList("High"),
            Arrays.asList("Unclassified"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("Default")),
            new ArrayList<>(Arrays.asList("CHECKER1", "CHECKER2")),
            StringUtils.EMPTY);

        // verify component / checker selections
        assertTrue(filters.isComponentSelected("Default"));
        assertEquals(0, filters.getIgnoredComponents().size());

        assertTrue(filters.isCheckerSelected("CHECKER1"));
        assertTrue(filters.isCheckerSelected("CHECKER2"));
        assertEquals(0, filters.getIgnoredChecker().size());

        filters.initializeFilter(new HashSet<>(Arrays.asList("CHECKER1", "CHECKER2", "CHECKER3")),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("Default", "SecondComponent")),
            Arrays.asList("High", "Medium", "Low"));

        // verify component selections include added components
        assertTrue(filters.isComponentSelected("Default"));
        assertTrue(filters.isComponentSelected("SecondComponent"));
        assertEquals(0, filters.getIgnoredComponents().size());

        // verify checker selections include added checkers
        assertTrue(filters.isCheckerSelected("CHECKER1"));
        assertTrue(filters.isCheckerSelected("CHECKER2"));
        assertTrue(filters.isCheckerSelected("CHECKER3"));
        assertEquals(0, filters.getIgnoredChecker().size());

        filters.initializeFilter(new HashSet<>(Arrays.asList("CHECKER1", "CHECKER2", "CHECKER3", "CHECKER4")),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("DefaultComponent", "SecondComponent", "ThirdComponent")),
            Arrays.asList("High", "Medium", "Low"));

        // de-select components / checkers
        filters.getComponents().remove("SecondComponent");
        filters.getCheckersList().remove("CHECKER3");

        filters.invertCheckers(new HashSet<>(Arrays.asList("CHECKER1", "CHECKER2", "CHECKER3", "CHECKER4")),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("DefaultComponent", "SecondComponent", "ThirdComponent")));

        // verify component / checker selections include added checkers
        assertTrue(filters.isComponentSelected("DefaultComponent"));
        assertFalse(filters.isComponentSelected("SecondComponent"));
        assertTrue(filters.isComponentSelected("ThirdComponent"));
        assertEquals(1, filters.getIgnoredComponents().size());

        assertTrue(filters.isCheckerSelected("CHECKER1"));
        assertTrue(filters.isCheckerSelected("CHECKER2"));
        assertFalse(filters.isCheckerSelected("CHECKER3"));
        assertTrue(filters.isCheckerSelected("CHECKER4"));
        assertEquals(1, filters.getIgnoredChecker().size());

        // reset back to the original to ensure ignoreLists now cleared
        filters.initializeFilter(new HashSet<>(Arrays.asList("CHECKER1", "CHECKER2")),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("Default")),
            Arrays.asList("High", "Medium", "Low"));
        filters.invertCheckers(new HashSet<>(Arrays.asList("CHECKER1", "CHECKER2")),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("Default")));

        // verify component / checker selections
        assertTrue(filters.isComponentSelected("Default"));
        assertEquals(0, filters.getIgnoredComponents().size());

        assertTrue(filters.isCheckerSelected("CHECKER1"));
        assertTrue(filters.isCheckerSelected("CHECKER2"));
        assertEquals(0, filters.getIgnoredChecker().size());
    }
}
