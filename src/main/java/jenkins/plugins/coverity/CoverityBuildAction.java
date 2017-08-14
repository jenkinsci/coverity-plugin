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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep.LastBuildAction;

/**
 * Captures Coverity information for a single build, including a snapshot of cim instance, project and stream, and a
 * filtered list of defects. This shows a link on the left side of each build page, which goes to a list of defects from
 * that build.
 */
public class CoverityBuildAction implements LastBuildAction {
    public static final String BUILD_ACTION_IDENTIFIER = "coverity_defects";

    // deprecated defectIds field
    private transient List<Long> defectIds;

    private final Run<?, ?>  build;
    private final String projectId;
    private final String streamId;
    private final String cimInstance;
    private final List<CoverityDefect> defects;

    public CoverityBuildAction(Run<?, ?> build, String projectId, String streamId, String cimInstance, List<CoverityDefect> defects) {
        this.build = build;
        this.projectId = projectId;
        this.streamId = streamId;
        this.cimInstance = cimInstance;
        this.defects = defects;
    }

    /**
     * The owning build
     */
    public AbstractBuild getBuild() {
        return (AbstractBuild)build;
    }

    /**
     * The data for the defects that were captured for this build.
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
        return BUILD_ACTION_IDENTIFIER;
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        if (build != null) {
            final Job<?, ?> parent = this.build.getParent();
            if (parent instanceof AbstractProject) {
                AbstractProject project = (AbstractProject)parent;
                final CoverityPublisher coverityPublisher = (CoverityPublisher)project.getPublishersList().get(CoverityPublisher.class);
                // project builds with Coverity Publisher can hide the chart
                if (coverityPublisher != null &&
                    coverityPublisher.getHideChart())
                    return Collections.emptyList();
            }

            return Collections.singleton(new CoverityProjectAction(build.getParent()));
        }

        return Collections.emptyList();
    }
}
