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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import hudson.model.Descriptor.FormException;

public class DefectFiltersTest {

    private final List<String> allClassifications = new ArrayList<>(Arrays.asList("Unclassified", "Pending", "False Positive", "Intentional", "Bug", "Untested", "No Test Needed", "Tested Elsewhere"));
    private final List<String> allActions = new ArrayList<>(Arrays.asList("Undecided", "Fix Required", "Fix Submitted", "Modeling Required", "Ignore"));
    private final List<String> allSeverities = new ArrayList<>(Arrays.asList("Unspecified", "Major", "Moderate", "Minor"));
    private final List<String> allImpacts = new ArrayList<>(Arrays.asList("High", "Medium", "Low"));
    private final List<String> components = new ArrayList<>(Arrays.asList("Default"));
    private final List<String> checkers = new ArrayList<>(Arrays.asList("CHECKER1", "CHECKER2"));

    @Test
    public void initializeFilter_setsExpectedAttributes() throws FormException {
        DefectFilters filters = new DefectFilters();

        filters.initializeFilter(checkers,
            allClassifications,
            allActions,
            allSeverities,
            components,
            allImpacts);

        assertEquals(allActions, filters.getActions());
        for (String action : allActions)
            assertTrue(filters.isActionSelected(action));

        assertEquals(allSeverities, filters.getSeverities());
        for (String severities : allSeverities)
            assertTrue(filters.isSeveritySelected(severities));

        assertEquals(allImpacts, filters.getImpacts());
        for(String impact : new ArrayList<>(allImpacts))
            assertTrue(filters.isImpactsSelected(impact));

        // only "outstanding" classifications selected by default
        List<String> expectedClassifications = Arrays.asList("Unclassified", "Pending", "Bug", "Untested");
        assertEquals(expectedClassifications, filters.getClassifications());
        for (String classification : expectedClassifications)
            assertTrue(filters.isClassificationSelected(classification));

        assertEquals(components, filters.getComponents());
        for (String component : components)
            assertTrue(filters.isComponentSelected(component));
        assertEquals(new ArrayList<String>(), filters.getIgnoredComponents());

        assertEquals(checkers, filters.getCheckersList());
        for (String checker : checkers)
            assertTrue(filters.isCheckerSelected(checker));
        assertEquals(new ArrayList<String>(), filters.getIgnoredChecker());
    }

    @Test
    public void initializeFilter_invertChecker_maintainsComponentsAndCheckersNotSelected() throws FormException {
        DefectFilters filters = new DefectFilters();
        filters.setComponents(components);
        filters.setCheckers(checkers);

        // verify component / checker selections
        assertTrue(filters.isComponentSelected("Default"));
        assertEquals(0, filters.getIgnoredComponents().size());

        assertTrue(filters.isCheckerSelected("CHECKER1"));
        assertTrue(filters.isCheckerSelected("CHECKER2"));
        assertEquals(0, filters.getIgnoredChecker().size());

        filters.initializeFilter(Arrays.asList("CHECKER1", "CHECKER2", "CHECKER3"),
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

        filters.initializeFilter(Arrays.asList("CHECKER1", "CHECKER2", "CHECKER3", "CHECKER4"),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("DefaultComponent", "SecondComponent", "ThirdComponent")),
            Arrays.asList("High", "Medium", "Low"));

        // de-select components / checkers
        filters.getComponents().remove("SecondComponent");
        filters.getCheckersList().remove("CHECKER3");

        filters.invertCheckers(Arrays.asList("CHECKER1", "CHECKER2", "CHECKER3", "CHECKER4"));
        filters.invertComponents(Arrays.asList("DefaultComponent", "SecondComponent", "ThirdComponent"));

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
        filters.initializeFilter(Arrays.asList("CHECKER1", "CHECKER2"),
            Arrays.asList("Unclassified"),
            Arrays.asList("Undecided"),
            Arrays.asList("Major"),
            new ArrayList<>(Arrays.asList("Default")),
            Arrays.asList("High", "Medium", "Low"));
        filters.invertCheckers(Arrays.asList("CHECKER1", "CHECKER2"));
        filters.invertComponents(Arrays.asList("Default"));

        // verify component / checker selections
        assertTrue(filters.isComponentSelected("Default"));
        assertEquals(0, filters.getIgnoredComponents().size());

        assertTrue(filters.isCheckerSelected("CHECKER1"));
        assertTrue(filters.isCheckerSelected("CHECKER2"));
        assertEquals(0, filters.getIgnoredChecker().size());
    }

    @Test
    public void defectCutOffDates() throws FormException, java.text.ParseException, DatatypeConfigurationException {
        String cutOffDate = null;
        DefectFilters filters = new DefectFilters();
        filters.setCutOffDate(cutOffDate);

        assertNull(filters.getCutOffDate());
        assertNull(filters.getXMLCutOffDate());

        cutOffDate = "unparsable-date-format";
        filters = new DefectFilters();
        filters.setCutOffDate(cutOffDate);

        assertNull(filters.getCutOffDate());
        assertNull(filters.getXMLCutOffDate());

        cutOffDate = "2010-01-31";
        filters = new DefectFilters();
        filters.setCutOffDate(cutOffDate);

        assertEquals(cutOffDate, filters.getCutOffDate());

        GregorianCalendar calender = new GregorianCalendar();
        calender.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(cutOffDate));
        XMLGregorianCalendar expectedXmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calender);
        assertEquals(expectedXmlDate, filters.getXMLCutOffDate());
    }

    @Test
    public void isComponentSelected_withNoIgnoredComponents() throws FormException {
        DefectFilters filters = new DefectFilters();
        assertTrue(filters.isComponentSelected("Default"));
    }

    @Test
    public void isCheckerSelected_withNoIgnoredCheckers() throws FormException {
        DefectFilters filters = new DefectFilters();
        assertTrue(filters.isCheckerSelected("CHECKER"));
    }
}
