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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.NotImplementedException;

import com.coverity.ws.v9.ComponentIdDataObj;
import com.coverity.ws.v9.ComponentMetricsDataObj;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.DefectChangeDataObj;
import com.coverity.ws.v9.DefectDetectionHistoryDataObj;
import com.coverity.ws.v9.DefectInstanceIdDataObj;
import com.coverity.ws.v9.DefectService;
import com.coverity.ws.v9.DefectStateSpecDataObj;
import com.coverity.ws.v9.FileContentsDataObj;
import com.coverity.ws.v9.FileIdDataObj;
import com.coverity.ws.v9.MergedDefectFilterSpecDataObj;
import com.coverity.ws.v9.MergedDefectIdDataObj;
import com.coverity.ws.v9.MergedDefectsPageDataObj;
import com.coverity.ws.v9.PageSpecDataObj;
import com.coverity.ws.v9.ProjectIdDataObj;
import com.coverity.ws.v9.ProjectMetricsDataObj;
import com.coverity.ws.v9.ProjectScopeDefectFilterSpecDataObj;
import com.coverity.ws.v9.ProjectTrendRecordFilterSpecDataObj;
import com.coverity.ws.v9.PropertySpecDataObj;
import com.coverity.ws.v9.SnapshotScopeDefectFilterSpecDataObj;
import com.coverity.ws.v9.SnapshotScopeSpecDataObj;
import com.coverity.ws.v9.StreamDefectDataObj;
import com.coverity.ws.v9.StreamDefectFilterSpecDataObj;
import com.coverity.ws.v9.StreamDefectIdDataObj;
import com.coverity.ws.v9.StreamIdDataObj;
import com.coverity.ws.v9.TriageHistoryDataObj;
import com.coverity.ws.v9.TriageStoreIdDataObj;

import jenkins.plugins.coverity.CIMInstance;

public class TestWebServiceFactory extends WebServiceFactory {
    public TestWebServiceFactory(Map<CIMInstance, DefectService> defectServiceMap) {
        super(defectServiceMap);
    }

    @Override
    protected DefectService createDefectService(CIMInstance cimInstance) throws MalformedURLException {
        return new TestDefectService(
            new URL(getURL(cimInstance), DEFECT_SERVICE_V9_WSDL));
    }

    public class TestDefectService implements DefectService {
        private URL url;

        public TestDefectService(URL url) {
            this.url = url;
        }

        @Override
        public void updateDefectInstanceProperties(DefectInstanceIdDataObj defectInstanceId, List<PropertySpecDataObj> properties) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateStreamDefects(List<StreamDefectIdDataObj> streamDefectIds, DefectStateSpecDataObj defectStateSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<TriageHistoryDataObj> getTriageHistory(MergedDefectIdDataObj mergedDefectIdDataObj, List<TriageStoreIdDataObj> triageStoreIds) throws CovRemoteServiceException_Exception {
            return null;
        }

        @Override
        public List<StreamDefectDataObj> getStreamDefects(List<MergedDefectIdDataObj> mergedDefectIdDataObjs, StreamDefectFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public MergedDefectsPageDataObj getMergedDefectsForStreams(List<StreamIdDataObj> streamIds, MergedDefectFilterSpecDataObj filterSpec, PageSpecDataObj pageSpec, SnapshotScopeSpecDataObj snapshotScope) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<DefectChangeDataObj> getMergedDefectHistory(MergedDefectIdDataObj mergedDefectIdDataObj, List<StreamIdDataObj> streamIds) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateTriageForCIDsInTriageStore(TriageStoreIdDataObj triageStore, List<MergedDefectIdDataObj> mergedDefectIdDataObjs, DefectStateSpecDataObj defectState) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<ProjectMetricsDataObj> getTrendRecordsForProject(ProjectIdDataObj projectId, ProjectTrendRecordFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<ComponentMetricsDataObj> getComponentMetricsForProject(ProjectIdDataObj projectId, List<ComponentIdDataObj> componentIds) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createMergedDefect(String mergeKey, XMLGregorianCalendar dateOriginated, String externalPreventVersion, String internalPreventVersion, String checkerName, String domainName) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public FileContentsDataObj getFileContents(StreamIdDataObj streamId, FileIdDataObj fileId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public MergedDefectsPageDataObj getMergedDefectsForSnapshotScope(ProjectIdDataObj projectId, SnapshotScopeDefectFilterSpecDataObj filterSpec, PageSpecDataObj pageSpec, SnapshotScopeSpecDataObj snapshotScope) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<DefectDetectionHistoryDataObj> getMergedDefectDetectionHistory(MergedDefectIdDataObj mergedDefectIdDataObj, List<StreamIdDataObj> streamIds) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public MergedDefectsPageDataObj getMergedDefectsForProjectScope(ProjectIdDataObj projectId, ProjectScopeDefectFilterSpecDataObj filterSpec, PageSpecDataObj pageSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }
    }
}
