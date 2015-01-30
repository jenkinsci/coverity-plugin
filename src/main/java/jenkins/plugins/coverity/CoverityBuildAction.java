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

import com.coverity.ws.v6.CovRemoteServiceException_Exception;
import com.coverity.ws.v6.MergedDefectDataObj;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Hudson;

import java.io.IOException;
import java.util.List;

/**
 * Captures Coverity information for a single build, including a snapshot of cim instance, project and stream, and a
 * filtered list of defects. This shows a link on the left side of each build page, which goes to a list of defects from
 * that build.
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

    /**
     * The owning build
     */
    public AbstractBuild getBuild() {
        return build;
    }

    /**
     * The IDs defects that were captured for this build
     */
    public List<Long> getDefectIds() {
        return defectIds;
    }

    /**
     * The data for the defects that were captured for this build. This will perform a call to the web service.
     */
    public List<MergedDefectDataObj> getDefects() throws IOException, CovRemoteServiceException_Exception {
        CIMInstance cim = Hudson.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class).getInstance(cimInstance);
        return cim.getDefects(streamId, defectIds);
    }

    /**
     * Returns the URL to the page for this defect in the CIM instance.
     */
    public String getURL(MergedDefectDataObj defect) throws IOException, CovRemoteServiceException_Exception {
        CIMInstance instance = Hudson.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class).getInstance(cimInstance);
        String header = "http";

        if(instance.isUseSSL()){
            header = "https";
        }

        return String.format(header + "://%s:%d/sourcebrowser.htm?projectId=%s&mergedDefectId=%d",
                instance.getHost(), instance.getPort(), instance.getProjectKey(projectId), defect.getCid());
    }

    public String getIconFileName() {
        return "/plugin/coverity/icons/coverity-logo-400px.png";
    }

    public String getDisplayName() {
        return "Coverity Defects ";
    }

    public String getUrlName() {
        return "coverity_" + getId();
    }

    /**
     * ID of the CIM project used for ths build
     *
     * @return
     */
    public String getProjectId() {
        return projectId;
    }

    public String getUrl() {
        return build.getUrl() + getUrlName();
    }

    public String getId() {
        return cimInstance + "_" + projectId + "_" + streamId;
    }
}
