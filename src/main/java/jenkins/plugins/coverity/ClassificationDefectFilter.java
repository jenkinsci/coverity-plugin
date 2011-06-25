package jenkins.plugins.coverity;

import com.coverity.ws.v3.MergedDefectDataObj;
import com.ctc.wstx.util.StringVector;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 13/06/11
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class ClassificationDefectFilter implements IDefectFilter {

    private static final List<String> OPTIONS = Arrays.asList(
            "Unclassified",
            "Pending",
            "False Positive",
            "Intentional",
            "Bug",
            "Various"
    );
    private static final List<String> classifications = Arrays.asList("Unclassified", "Bug", "Pending");

    public boolean matches(MergedDefectDataObj defect) {
        return classifications.contains(defect.getClassification());
    }
}
