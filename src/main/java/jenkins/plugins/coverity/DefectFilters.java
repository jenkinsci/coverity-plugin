package jenkins.plugins.coverity;

import com.coverity.ws.v3.MergedDefectDataObj;
import hudson.Util;
import org.kohsuke.stapler.DataBoundConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Responsible for filtering the full list of defects to determine if a build should fail or not.
 * Filters are inclusive: a defect needs to pass every filter in order to be included.
 */
public class DefectFilters {

    private final List<String> classifications = Arrays.asList("Unclassified", "Bug", "Pending");
    private final List<String> actions;
    private final List<String> severities;
    private final List<String> components;
    private final List<String> checkers;
    private List<String> ignoredCheckers;
    private final Date cutOffDate;

    @DataBoundConstructor
    public DefectFilters(List<String> actions, List<String> severities, List<String> components, List<String> checkers, String cutOffDate) throws ParseException {
        this.actions = Util.fixNull(actions);
        this.severities = Util.fixNull(severities);
        this.components = Util.fixNull(components);
        this.checkers = Util.fixNull(checkers);

        cutOffDate = Util.fixEmpty(cutOffDate);
        if (cutOffDate != null) {
            this.cutOffDate = new SimpleDateFormat("yyyy-MM-dd").parse(cutOffDate);
        } else {
            this.cutOffDate = null;
        }
    }

    void invertCheckers(Set<String> allCheckers) {
        ignoredCheckers = new ArrayList<String>(allCheckers);
        ignoredCheckers.removeAll(checkers);
    }

    public List<String> getClassifications() {
        return classifications;
    }

    public List<String> getActions() {
        return actions;
    }

    public List<String> getSeverities() {
        return severities;
    }

    public List<String> getComponents() {
        return components;
    }

    public List<String> getCheckers() {
        return checkers;
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
        if (ignoredCheckers != null) return !ignoredCheckers.contains(checker);
        return checkers.contains(checker);
    }

    public String getCutOffDate() {
        if (cutOffDate == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(cutOffDate);
    }

    public boolean matches(MergedDefectDataObj defect) {
        return isActionSelected(defect.getAction()) &&
                classifications.contains(defect.getClassification()) &&
                isSeveritySelected(defect.getSeverity()) &&
                isComponentSelected(defect.getComponentName()) &&
                isCheckerSelected(defect.getChecker()) &&
                Arrays.asList("New", "Triaged", "Various").contains(defect.getStatus()) &&
                (cutOffDate == null || defect.getFirstDetected().toGregorianCalendar().getTime().after(cutOffDate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefectFilters that = (DefectFilters) o;

        if (actions != null ? !actions.equals(that.actions) : that.actions != null) return false;
        if (checkers != null ? !checkers.equals(that.checkers) : that.checkers != null) return false;
        if (classifications != null ? !classifications.equals(that.classifications) : that.classifications != null)
            return false;
        if (components != null ? !components.equals(that.components) : that.components != null) return false;
        if (cutOffDate != null ? !cutOffDate.equals(that.cutOffDate) : that.cutOffDate != null) return false;
        if (severities != null ? !severities.equals(that.severities) : that.severities != null) return false;

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
