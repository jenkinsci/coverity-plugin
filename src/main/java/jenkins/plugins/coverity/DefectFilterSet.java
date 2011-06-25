package jenkins.plugins.coverity;

import com.coverity.ws.v3.MergedDefectDataObj;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 13/06/11
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class DefectFilterSet {

    private final ActionDefectFilter actionDefectFilter;
    private SeveritiesDefectFilter severitiesDefectFilter;
    private final ComponentDefectFilter componentDefectFilter;
    private final CheckerDefectFilter checkerDefectFilter;
    private final ClassificationDefectFilter classificationDefectFilter = new ClassificationDefectFilter();

    @DataBoundConstructor
    public DefectFilterSet(ActionDefectFilter actionDefectFilter, SeveritiesDefectFilter severitiesDefectFilter, ComponentDefectFilter componentDefectFilter, CheckerDefectFilter checkerDefectFilter) {
        this.actionDefectFilter = actionDefectFilter;
        this.severitiesDefectFilter = severitiesDefectFilter;
        this.componentDefectFilter = componentDefectFilter;
        this.checkerDefectFilter = checkerDefectFilter;
    }

    public ActionDefectFilter getActionDefectFilter() {
        return actionDefectFilter;
    }

    public ClassificationDefectFilter getClassificationDefectFilter() {
        return classificationDefectFilter;
    }

    public SeveritiesDefectFilter getSeveritiesDefectFilter() {
        return severitiesDefectFilter;
    }

    public ComponentDefectFilter getComponentDefectFilter() {
        return componentDefectFilter;
    }

    public CheckerDefectFilter getCheckerDefectFilter() {
        return checkerDefectFilter;
    }

    public boolean matches(MergedDefectDataObj defect) {
        return classificationDefectFilter.matches(defect) &&
                actionDefectFilter.matches(defect) &&
                severitiesDefectFilter.matches(defect) &&
                componentDefectFilter.matches(defect) &&
                checkerDefectFilter.matches(defect);
    }
}
