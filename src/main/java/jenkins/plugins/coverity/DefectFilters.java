/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import com.coverity.ws.v6.*;
import hudson.Util;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.model.BuildListener;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Responsible for filtering the full list of defects to determine if a build should fail or not. Filters are inclusive:
 * a defect needs to pass every filter in order to be included.
 */
public class DefectFilters {
    private List<String> classifications;
    private List<String> actions;
    private List<String> severities;
    private List<String> components;
    private List<String> checkers;
    private List<String> ignoredCheckers;
    private Date cutOffDate;

    @DataBoundConstructor
    public DefectFilters(List<String> actions, List<String> classifications, List<String> severities, List<String> components, List<String> checkers, String cutOffDate) throws Descriptor.FormException {
        this.classifications = Util.fixNull(classifications);
        this.actions = Util.fixNull(actions);
        this.severities = Util.fixNull(severities);
        this.components = Util.fixNull(components);
        this.checkers = Util.fixNull(checkers);
        this.ignoredCheckers = new ArrayList<String>();

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

    void invertCheckers(Set<String> allCheckers, List<String> allClassifications, List<String> allActions, List<String> allSeverities, List<String> allComponents) {
        if(classifications.isEmpty() && checkers.isEmpty() && actions.isEmpty() && components.isEmpty() && severities.isEmpty()) {
            ignoredCheckers = new ArrayList<String>();
            actions = allActions;
            severities = allSeverities;
            components = allComponents;
            classifications = allClassifications;
        } else {
            ignoredCheckers = new ArrayList<String>(allCheckers);
            ignoredCheckers.removeAll(checkers);
        }
    }

    public boolean isClassificationSelected(String action) {
        return classifications.contains(action);
    }

    public boolean isActionSelected(String action) {
        return actions.contains(action);
    }

    public boolean isSeveritySelected(String severity) {
        return severities.contains(severity);
    }

    public boolean isComponentSelected(String component) {
        return components.contains(component);
    }

    public boolean isCheckerSelected(String checker) {
        if(ignoredCheckers == null) {
            return false;
        }
        return !ignoredCheckers.contains(checker);
    }

    public String getCutOffDate() {
        if(cutOffDate == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(cutOffDate);
    }

    public List<String> getClassifications(){return classifications;}

    public List<String> getActions(){return actions;}

    public List<String> getSeverities(){return severities;}

    public List<ComponentIdDataObj> getComponents(){
        List<ComponentIdDataObj> componentIdDataList = new ArrayList<ComponentIdDataObj>();
        for(String comp : components){
            ComponentIdDataObj cIdDataObj = new ComponentIdDataObj();
            cIdDataObj.setName(comp);
            componentIdDataList.add(cIdDataObj);
        }
        return componentIdDataList;
    }

    public List<CheckerSubcategoryFilterSpecDataObj> getCheckers(BuildListener listener){
        List<CheckerSubcategoryFilterSpecDataObj> checkerSubFilterSpecDataObjList = new ArrayList<CheckerSubcategoryFilterSpecDataObj>();
        for(String check : checkers){
            if(check != null){
                CheckerSubcategoryFilterSpecDataObj checkerSubFilterSpecDataObj = new CheckerSubcategoryFilterSpecDataObj();
                checkerSubFilterSpecDataObj.setCheckerName(check);
                checkerSubFilterSpecDataObjList.add(checkerSubFilterSpecDataObj);
            }
        }
        return checkerSubFilterSpecDataObjList;
    }

    public List<String> getIgnoredChecker(){return ignoredCheckers;}

    public XMLGregorianCalendar getXMLCutOffDate(){
        GregorianCalendar calender = new GregorianCalendar();
        calender.setTime(cutOffDate);
        try{
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calender);
        }catch(Exception e){

        }
        return null;
    }

    public boolean matches(MergedDefectDataObj defect, BuildListener listener) {
        /*
        boolean result = true;
        if(!isActionSelected(defect.getAction())){
            result = false;
            listener.getLogger().println(String.format("Failed to match defect action %s with actions selected: %s",defect.getAction(),defect.getAction()));
        }

        if(!isClassificationSelected(defect.getClassification())){
            result = false;
            listener.getLogger().println(String.format("Failed to match defect classifications %s with classifications selected: %s",
                defect.getClassification(),defect.getClassification()));
        }

        if(!isSeveritySelected(defect.getSeverity())){
            result = false;
            listener.getLogger().println(String.format("Failed to match defect serverity %s with severity selected: %s",
                defect.getSeverity(),defect.getSeverity()));
        }

        if(!isComponentSelected(defect.getComponentName())){
            result = false;
            listener.getLogger().println(String.format("Failed to match defect components %s with components selected: %s",
                defect.getComponentName(),defect.getComponentName()));
        }

        if(!isCheckerSelected(defect.getCheckerName())){
            result = false;
            listener.getLogger().println(String.format("Failed to match defect checkers %s with checkers selected: %s",
                defect.getCheckerName(),defect.getCheckerName()));
        }

        if(!Arrays.asList("New", "Triaged", "Various").contains(defect.getStatus())){
            result = false;
            listener.getLogger().println(String.format("Failed to match defects with 'New' 'Triaged' or 'Various' status.: %s",
                defect.getStatus()));
        }

        if(!(cutOffDate == null || defect.getFirstDetected().toGregorianCalendar().getTime().after(cutOffDate))){
            result = false;
            listener.getLogger().println(String.format("Failed at matching cutOffDate: %s",
                defect.getFirstDetected().toGregorianCalendar().getTime().after(cutOffDate)));
        }

        return result;*/
        return isActionSelected(defect.getAction()) &&
                isClassificationSelected(defect.getClassification()) &&
                isSeveritySelected(defect.getSeverity()) &&
                isComponentSelected(defect.getComponentName()) &&
                isCheckerSelected(defect.getCheckerName()) &&
                Arrays.asList("New", "Triaged", "Various").contains(defect.getStatus()) &&
                (cutOffDate == null || defect.getFirstDetected().toGregorianCalendar().getTime().after(cutOffDate));
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        DefectFilters that = (DefectFilters) o;

        if(actions != null ? !actions.equals(that.actions) : that.actions != null) return false;
        if(checkers != null ? !checkers.equals(that.checkers) : that.checkers != null) return false;
        if(classifications != null ? !classifications.equals(that.classifications) : that.classifications != null)
            return false;
        if(components != null ? !components.equals(that.components) : that.components != null) return false;
        if(cutOffDate != null ? !cutOffDate.equals(that.cutOffDate) : that.cutOffDate != null) return false;
        if(severities != null ? !severities.equals(that.severities) : that.severities != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classifications != null ? classifications.hashCode() : 0;
        result = 31 * result + (actions != null ? actions.hashCode() : 0);
        result = 31 * result + (severities != null ? severities.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (checkers != null ? checkers.hashCode() : 0);
        result = 31 * result + (cutOffDate != null ? cutOffDate.hashCode() : 0);
        return result;
    }
}
