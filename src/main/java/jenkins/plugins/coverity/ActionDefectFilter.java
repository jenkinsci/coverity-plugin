package jenkins.plugins.coverity;

import com.coverity.ws.v3.MergedDefectDataObj;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 13/06/11
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class ActionDefectFilter implements IDefectFilter {

    private static final List<String> OPTIONS = Arrays.asList("Undecided", "Fix", "Fix Submitted", "Ignore");

    private boolean undecided;
    private boolean fix;
    private boolean fixSubmitted;
    private boolean ignore;

    @DataBoundConstructor
    public ActionDefectFilter(boolean undecided, boolean fix, boolean fixSubmitted, boolean ignore) {
        this.undecided = undecided;
        this.fix = fix;
        this.fixSubmitted = fixSubmitted;
        this.ignore = ignore;
    }

    public boolean matches(MergedDefectDataObj defect) {
        String action = defect.getAction();
        return fix && action.equals("Fix") || undecided && action.equals("Undecided") || fixSubmitted && action.equals("FixSubmitted") || ignore && action.equals("Ignore");
    }

    public boolean isUndecided() {
        return undecided;
    }

    public boolean isFix() {
        return fix;
    }

    public boolean isFixSubmitted() {
        return fixSubmitted;
    }

    public boolean isIgnore() {
        return ignore;
    }
}
