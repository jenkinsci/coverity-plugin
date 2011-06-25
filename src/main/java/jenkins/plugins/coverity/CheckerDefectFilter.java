package jenkins.plugins.coverity;

import com.coverity.ws.v3.MergedDefectDataObj;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 13/06/11
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class CheckerDefectFilter implements IDefectFilter {

    private final Set<String> selected;

    @DataBoundConstructor
    public CheckerDefectFilter(Set<String> selected) {
        this.selected = selected;
    }

    public boolean isSelected(String value) {
        return selected.contains(value);
    }

    public boolean matches(MergedDefectDataObj defect) {
        return selected.contains(defect.getChecker());
    }
}
