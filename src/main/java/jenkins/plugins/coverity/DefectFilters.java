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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.kohsuke.stapler.DataBoundConstructor;

import com.coverity.ws.v9.ComponentIdDataObj;
import com.coverity.ws.v9.MergedDefectFilterSpecDataObj;

import hudson.Util;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Responsible for filtering the full list of defects to determine if a build should fail or not. Filters are inclusive:
 * a defect needs to pass every filter in order to be included.
 */
public class DefectFilters {
    private List<String> classifications;
    private List<String> actions;
    private List<String> severities;
    private List<String> components;
    private List<String> ignoredComponents;
    private List<String> checkers;
    private List<String> ignoredCheckers;
    private Date cutOffDate;
    private List<String> impacts;

    @DataBoundConstructor
    public DefectFilters() {
        this.ignoredComponents = new ArrayList<String>();
        this.ignoredCheckers = new ArrayList<String>();
    }

    @DataBoundSetter
    public void setCutOffDate(String cutOffDate) throws Descriptor.FormException {
        cutOffDate = Util.fixEmpty(cutOffDate);
        if(cutOffDate != null) {
            try {
                this.cutOffDate = new SimpleDateFormat("yyyy-MM-dd").parse(cutOffDate);
            } catch(ParseException e) {
                this.cutOffDate = null;
            }
        } else {
            this.cutOffDate = null;
        }
    }

    public String getCutOffDate() {
        if(cutOffDate == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(cutOffDate);
    }

    @DataBoundSetter
    public void setClassifications(List<String> classifications){
        this.classifications = Util.fixNull(classifications);
    }

    public List<String> getClassifications(){return classifications;}

    @DataBoundSetter
    public void setActions(List<String> actions){
        this.actions = Util.fixNull(actions);
    }

    public List<String> getActions(){return actions;}

    @DataBoundSetter
    public void setSeverities(List<String> severities){
        this.severities = Util.fixNull(severities);
    }

    public List<String> getSeverities(){return severities;}

    @DataBoundSetter
    public void setImpacts(List<String> impacts){
        this.impacts = Util.fixNull(impacts);
    }

    public List<String> getImpacts(){return impacts;}

    @DataBoundSetter
    public void setComponents(List<String> components){
        this.components = Util.fixNull(components);
    }

    public List<String> getComponents(){
        return components;
    }

    public List<String> getIgnoredComponents() {
        return ignoredComponents;
    }

    public List<String> getIgnoredChecker(){return ignoredCheckers;}

    @DataBoundSetter
    public void setCheckers(List<String> checkers){
        this.checkers = Util.fixNull(checkers);
    }

    public List<String> getCheckersList(){
        return checkers;
    }

    /**
     * Initializes the default filter selection values when given the attribute values from the active Coverity connect.
     */
    void initializeFilter(List<String> allCheckers, List<String> allClassifications, List<String> allActions, List<String> allSeverities, List<String> allComponents, List<String> allImpacts) {
        // initialize new values by enabling all defaults
        ignoredCheckers = new ArrayList<String>();
        checkers = new ArrayList<>(allCheckers);
        actions = allActions;
        severities = allSeverities;
        ignoredComponents = new ArrayList<String>();
        components = allComponents;
        impacts = allImpacts;

        // remove the "Intentional", "False Positive", "No Test Needed", "Tested Elsewhere" classifications to match default outstanding filters
        allClassifications.removeAll(Arrays.asList("Intentional", "False Positive", "No Test Needed", "Tested Elsewhere"));
        classifications = allClassifications;
    }

    /**
     * Inverts the check selection in order to persist the list of ignored checkers. This is necessary to allow new
     * checkers to be enabled by default when added to Coverity connect (via commits).
     */
    public void invertCheckers(List<String> allCheckers) {
        ignoredCheckers = new ArrayList<>(allCheckers);
        if (checkers != null){
            ignoredCheckers.removeAll(checkers);
        }
    }

    /**
     * Inverts the component selection in order to persist the list of ignored components. This is necessary to allow new
     * component mapss configured on the stream in Coverity connect.
     */
    public void invertComponents(List<String> allComponents) {
        ignoredComponents = new ArrayList<>(allComponents);
        if (components != null){
            ignoredComponents.removeAll(components);
        }
    }

    /**
     * We want to do a check on the defect configuration so that no null values are going into the build.
     * There has been issues on upgrade where configurations are changed, and not updates thus causing builds to
     * fail because some configuaritons are null.
     * @return true if defect configurations are not null
     */
    public boolean checkConfig(){
        if(this.getCheckersList() == null || this.getClassifications() == null ||
                this.getActions() == null || this.getSeverities() == null || this.getComponents() == null){
            return false;
        }
        return true;
    }

    public boolean isClassificationSelected(String action) {
        return classifications != null && classifications.contains(action);
    }

    public boolean isActionSelected(String action) {
        return actions != null && actions.contains(action);
    }

    public boolean isImpactsSelected(String impact){
        if (impacts == null)
            return false;

        Map<String, String> mapWithJapaneseTranslations;
        mapWithJapaneseTranslations = new HashMap<String, String>();
        mapWithJapaneseTranslations.put("High", "高");
        mapWithJapaneseTranslations.put("Medium", "中");
        mapWithJapaneseTranslations.put("Low", "低");
        List<String> initialImpacts = new ArrayList<String>(this.impacts);
        if(initialImpacts != null && !initialImpacts.isEmpty()){
            for(String specificImpact : initialImpacts){
                if(mapWithJapaneseTranslations.containsKey(specificImpact)){
                    this.impacts.add(mapWithJapaneseTranslations.get(specificImpact));
                }
            }
        }
        return impacts.contains(impact);
    }

    public boolean isSeveritySelected(String severity) {
        return severities != null && severities.contains(severity);
    }

    public boolean isComponentSelected(String component) {
        return ignoredComponents != null && !ignoredComponents.contains(component);
    }

    public boolean isCheckerSelected(String checker) {
        return ignoredCheckers != null && !ignoredCheckers.contains(checker);
    }

    public XMLGregorianCalendar getXMLCutOffDate(){
        if (cutOffDate != null) {
            GregorianCalendar calender = new GregorianCalendar();
            calender.setTime(cutOffDate);
            try{
                return DatatypeFactory.newInstance().newXMLGregorianCalendar(calender);
            }catch(Exception e){

            }
        }

        return null;
    }

    public MergedDefectFilterSpecDataObj ToFilterSpecDataObj(){
        MergedDefectFilterSpecDataObj filterSpecDataObj = new MergedDefectFilterSpecDataObj();
        filterSpecDataObj.getActionNameList().addAll(actions);
        filterSpecDataObj.getClassificationNameList().addAll(classifications);
        filterSpecDataObj.getSeverityNameList().addAll(severities);
        filterSpecDataObj.getImpactList().addAll(impacts);
        if (components != null){
            for (String component : components) {
                ComponentIdDataObj componentIdDataObj = new ComponentIdDataObj();
                componentIdDataObj.setName(component);
                filterSpecDataObj.getComponentIdList().add(componentIdDataObj);
            }
        }

        if (checkers != null){
            filterSpecDataObj.getCheckerList().addAll(checkers);
        }
        XMLGregorianCalendar xmlCutOffDate = getXMLCutOffDate();
        if (xmlCutOffDate != null) {
            filterSpecDataObj.setFirstDetectedStartDate(xmlCutOffDate);
        }
        return filterSpecDataObj;
    }
}
