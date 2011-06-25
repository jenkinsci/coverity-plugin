package jenkins.plugins.coverity;

import com.coverity.ws.v3.CovRemoteServiceException_Exception;
import com.coverity.ws.v3.MergedDefectDataObj;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Hudson;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 13/06/11
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class CoverityBuildAction implements Action {
    private final AbstractBuild build;
    private final List<Long> defectIds;
    private final String projectId;
    private final String streamId;
    private final String cimInstance;
    private String url;


    public CoverityBuildAction(AbstractBuild build, String projectId, String streamId, String cimInstance, List<Long> matchingDefects) {
        this.build = build;
        this.projectId = projectId;
        this.streamId = streamId;
        this.cimInstance = cimInstance;
        this.defectIds = matchingDefects;
    }

    public AbstractBuild getBuild() {
        return build;
    }

    public List<Long> getDefectIds() {
        return defectIds;
    }

    public List<MergedDefectDataObj> getDefects() throws IOException, CovRemoteServiceException_Exception {
        CIMInstance cim = Hudson.getInstance().getDescriptorByType(CIMInstance.DescriptorImpl.class).getInstance(cimInstance);
        return cim.getDefects(streamId, defectIds);
    }

    public String getURL(MergedDefectDataObj defect) throws IOException, CovRemoteServiceException_Exception {
        CIMInstance instance = CIMInstance.DESCRIPTOR.getInstance(cimInstance);
        return String.format("http://%s:%d/sourcebrowser.htm?projectId=%s#mergedDefectId=%d",
                instance.getHost(), instance.getPort(), instance.getProjectKey(projectId), defect.getCid());
    }

    public String getIconFileName() {
        return "star.gif"; //TODO
    }

    public String getDisplayName() {
        return "Coverity Defects";
    }

    public String getUrlName() {
        return "coverity";
    }

    public String getProjectId() {
        return projectId;
    }

    public String getUrl() {
        return build.getUrl() + getUrlName();
    }
}
