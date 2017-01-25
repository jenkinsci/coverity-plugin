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
package jenkins.plugins.coverity.ws;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.MergedDefectDataObj;
import com.coverity.ws.v9.MergedDefectFilterSpecDataObj;
import com.coverity.ws.v9.MergedDefectsPageDataObj;
import com.coverity.ws.v9.PageSpecDataObj;
import com.coverity.ws.v9.SnapshotScopeSpecDataObj;
import com.coverity.ws.v9.StreamIdDataObj;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Hudson;
import hudson.model.Result;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.CIMStream;
import jenkins.plugins.coverity.CoverityBuildAction;
import jenkins.plugins.coverity.CoverityDefect;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.DefectFilters;

/**
 * Class responsible for reading defects from Coverity after the commit process has been completed. The defects
 * will be added as a {@link CoverityBuildAction} to the build.
 */
public class DefectReader {
    private AbstractBuild<?, ?> build;
    private Launcher launcher;
    private BuildListener listener;
    private CoverityPublisher publisher;

    public DefectReader(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) {
        this.build = build;
        this.launcher = launcher;
        this.listener = listener;
        this.publisher = publisher;
    }

    public boolean getLatestDefectsForBuild()
    {
        if (publisher.isSkipFetchingDefects()) {
            // never get any defects when configured to skip fetching defects
            return false;
        }

        List<CIMStream> cimStreams = publisher.getCimStreams();
        for(int i = 0; i < cimStreams.size(); i++) {
            CIMStream cimStream = cimStreams.get(i);
            CIMInstance cimInstance = publisher.getDescriptor().getInstance(cimStream.getInstance());

            listener.getLogger().println("[Coverity] Fetching defects for stream " + cimStream.getStream());

            List<MergedDefectDataObj> defects = null;

            try {
                defects = getDefectsForSnapshot(cimInstance, cimStream);

                List<CoverityDefect> matchingDefects = new ArrayList<>();

                // Loop through all defects create defect objects
                for(MergedDefectDataObj defect : defects) {
                    matchingDefects.add(new CoverityDefect(defect.getCid(), defect.getCheckerName(), defect.getFunctionDisplayName(), defect.getFilePathname()));
                }

                if(!matchingDefects.isEmpty()) {
                    listener.getLogger().println("[Coverity] Found " + matchingDefects.size() + " defects matching all filters");
                    if(publisher.isFailBuild()) {
                        if(build.getResult().isBetterThan(Result.FAILURE)) {
                            build.setResult(Result.FAILURE);
                            return false;
                        }
                    }

                    // if the user wants to mark the build as unstable when defects are found, then we
                    // notify the publisher to do so.
                    if(publisher.isUnstable()){
                        publisher.setUnstableBuild(true);

                    }

                } else {
                    listener.getLogger().println("[Coverity] No defects matched all filters.");
                }

                CoverityBuildAction action = new CoverityBuildAction(build, cimStream.getProject(), cimStream.getStream(), cimStream.getInstance(), matchingDefects);
                build.addAction(action);

                String rootUrl = Jenkins.getInstance().getRootUrl();
                if(rootUrl != null) {
                    listener.getLogger().println("Coverity details: " + rootUrl + build.getUrl() + action.getUrlName());
                }
            } catch (IOException e) {
                e.printStackTrace(listener.error("[Coverity] An error occurred while fetching defects"));
                build.setResult(Result.FAILURE);
                return false;
            } catch (CovRemoteServiceException_Exception e) {
                e.printStackTrace(listener.error("[Coverity] An error occurred while fetching defects"));
                build.setResult(Result.FAILURE);
                return false;
            }
        }

        return true;
    }

    private List<MergedDefectDataObj> getDefectsForSnapshot(CIMInstance cim, CIMStream cimStream) throws IOException, CovRemoteServiceException_Exception {

        List<MergedDefectDataObj> mergeList = new ArrayList<MergedDefectDataObj>();

        DefectService ds = cim.getDefectService();

        StreamIdDataObj streamId = new StreamIdDataObj();
        streamId.setName(cimStream.getStream());
        List<StreamIdDataObj> streamIds = new ArrayList<StreamIdDataObj>();
        streamIds.add(streamId);

        DefectFilters defectFilters = cimStream.getDefectFilters();
        MergedDefectFilterSpecDataObj filter = defectFilters != null ?  defectFilters.ToFilterSpecDataObj() : new MergedDefectFilterSpecDataObj();

        PageSpecDataObj pageSpec = new PageSpecDataObj();

        SnapshotScopeSpecDataObj snapshotScope = new SnapshotScopeSpecDataObj();
        snapshotScope.setShowSelector("last()");

        // The loop will pull up to the maximum amount of defect, doing per page size
        int pageSize = 1000; // Size of page to be pulled
        int defectSize = 3000; // Maximum amount of defect to pull
        for(int pageStart = 0; pageStart < defectSize; pageStart += pageSize){
            pageSpec.setPageSize(pageSize);
            pageSpec.setStartIndex(pageStart);
            pageSpec.setSortAscending(true);
            MergedDefectsPageDataObj mergedDefectsForStreams = ds.getMergedDefectsForStreams(streamIds, filter, pageSpec, snapshotScope);
            defectSize = mergedDefectsForStreams.getTotalNumberOfRecords();
            mergeList.addAll(mergedDefectsForStreams.getMergedDefects());
        }
        return mergeList;
    }
}
