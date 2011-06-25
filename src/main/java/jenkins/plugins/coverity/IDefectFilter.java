package jenkins.plugins.coverity;

import com.coverity.ws.v3.MergedDefectDataObj;
import com.coverity.ws.v3.MergedDefectsPageDataObj;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 13/06/11
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */
public interface IDefectFilter {

    public boolean matches(MergedDefectDataObj defect);

}
