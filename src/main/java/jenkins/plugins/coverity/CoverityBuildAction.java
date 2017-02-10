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

import com.coverity.ws.v9.CovRemoteServiceException_Exception;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Captures Coverity information for a single build, including a snapshot of cim instance, project and stream, and a
 * filtered list of defects. This shows a link on the left side of each build page, which goes to a list of defects from
 * that build.
 */
public class CoverityBuildAction implements Action {
    public static final String BUILD_ACTION_IDENTIFIER = "coverity_defects";

    // deprecated defectIds field
    private transient List<Long> defectIds;

    private final AbstractBuild build;
    private final String projectId;
    private final String streamId;
    private final String cimInstance;
    private final List<CoverityDefect> defects;
    private String uniqueId;

    public CoverityBuildAction(AbstractBuild build, String projectId, String streamId, String cimInstance, List<CoverityDefect> defects, int buildCommitNumber) {
        this.build = build;
        this.projectId = projectId;
        this.streamId = streamId;
        this.cimInstance = cimInstance;
        // modify the build identifier builds where more than one stream was committed
        this.uniqueId = buildCommitNumber == 0 ? BUILD_ACTION_IDENTIFIER : BUILD_ACTION_IDENTIFIER + buildCommitNumber;
        this.defects = defects;
    }

    /**
     * Implement readResolve to update the de-serialized object in the case transient data was found. Transient fields
     * will be read during de-serialization and readResolve allow updating the BuildAction object after being created.
     */
    protected Object readResolve() {
        if(uniqueId == null) {
            this.uniqueId = BUILD_ACTION_IDENTIFIER;
        }

        return this;
    }

    /**
     * The owning build
     */
    public AbstractBuild getBuild() {
        return build;
    }

    /**
     * The data for the defects that were captured for this build. This will perform a call to the web service.
     */
    public List<CoverityDefect> getDefects() {

        // use defectIds field if specified to support older builds (pre-1.9.0 plugin)
        if (defectIds != null && !defectIds.isEmpty()) {
            List<CoverityDefect> covDefects = new ArrayList<>();
            for(Long defectId : defectIds) {
                covDefects.add(new CoverityDefect(defectId, "---", "View in Coverity Connect", StringUtils.EMPTY));
            }
            return covDefects;
        }  else {
            return defects != null ? defects : new ArrayList<CoverityDefect>();
        }
    }

    /**
     * Returns the URL to the page for this defect in the CIM instance.
     */
    public String getURL(CoverityDefect defect) throws IOException, CovRemoteServiceException_Exception {
        CIMInstance instance = Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class).getInstance(cimInstance);
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

    public String getGraphDisplayName() {
        return "Coverity Defects (" + streamId + ")";
    }

    public String getUrlName() {
        return uniqueId;
    }
}
