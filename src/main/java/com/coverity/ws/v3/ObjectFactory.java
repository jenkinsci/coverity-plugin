package com.coverity.ws.v3;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.coverity.ws.v3 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetMergedDefectsForStreamsResponse_QNAME = new QName("http://ws.coverity.com/v3", "getMergedDefectsForStreamsResponse");
    private final static QName _UpdateDefectInstanceProperties_QNAME = new QName("http://ws.coverity.com/v3", "updateDefectInstanceProperties");
    private final static QName _UpdateDefectInstancePropertiesResponse_QNAME = new QName("http://ws.coverity.com/v3", "updateDefectInstancePropertiesResponse");
    private final static QName _GetComponentMetricsForProject_QNAME = new QName("http://ws.coverity.com/v3", "getComponentMetricsForProject");
    private final static QName _GetCIDsForSnapshot_QNAME = new QName("http://ws.coverity.com/v3", "getCIDsForSnapshot");
    private final static QName _GetCIDsForSnapshotResponse_QNAME = new QName("http://ws.coverity.com/v3", "getCIDsForSnapshotResponse");
    private final static QName _GetComponentMetricsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v3", "getComponentMetricsForProjectResponse");
    private final static QName _GetTrendRecordsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v3", "getTrendRecordsForProjectResponse");
    private final static QName _CoverityFault_QNAME = new QName("http://ws.coverity.com/v3", "CoverityFault");
    private final static QName _GetMergedDefectHistoryResponse_QNAME = new QName("http://ws.coverity.com/v3", "getMergedDefectHistoryResponse");
    private final static QName _UpdateStreamDefects_QNAME = new QName("http://ws.coverity.com/v3", "updateStreamDefects");
    private final static QName _GetCIDsForProject_QNAME = new QName("http://ws.coverity.com/v3", "getCIDsForProject");
    private final static QName _UpdateStreamDefectsResponse_QNAME = new QName("http://ws.coverity.com/v3", "updateStreamDefectsResponse");
    private final static QName _GetStreamDefects_QNAME = new QName("http://ws.coverity.com/v3", "getStreamDefects");
    private final static QName _GetMergedDefectsForProject_QNAME = new QName("http://ws.coverity.com/v3", "getMergedDefectsForProject");
    private final static QName _GetStreamDefectsResponse_QNAME = new QName("http://ws.coverity.com/v3", "getStreamDefectsResponse");
    private final static QName _GetCIDsForStreams_QNAME = new QName("http://ws.coverity.com/v3", "getCIDsForStreams");
    private final static QName _GetCIDForDMCIDResponse_QNAME = new QName("http://ws.coverity.com/v3", "getCIDForDMCIDResponse");
    private final static QName _GetTrendRecordsForProject_QNAME = new QName("http://ws.coverity.com/v3", "getTrendRecordsForProject");
    private final static QName _GetCIDsForStreamsResponse_QNAME = new QName("http://ws.coverity.com/v3", "getCIDsForStreamsResponse");
    private final static QName _GetCIDsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v3", "getCIDsForProjectResponse");
    private final static QName _GetCIDForDMCID_QNAME = new QName("http://ws.coverity.com/v3", "getCIDForDMCID");
    private final static QName _GetMergedDefectsForStreams_QNAME = new QName("http://ws.coverity.com/v3", "getMergedDefectsForStreams");
    private final static QName _GetMergedDefectHistory_QNAME = new QName("http://ws.coverity.com/v3", "getMergedDefectHistory");
    private final static QName _GetMergedDefectsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v3", "getMergedDefectsForProjectResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.coverity.ws.v3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PropertyDataObj }
     * 
     */
    public PropertyDataObj createPropertyDataObj() {
        return new PropertyDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDsForSnapshot }
     * 
     */
    public GetCIDsForSnapshot createGetCIDsForSnapshot() {
        return new GetCIDsForSnapshot();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.ComponentIdDataObj }
     * 
     */
    public ComponentIdDataObj createComponentIdDataObj() {
        return new ComponentIdDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetMergedDefectsForProjectResponse }
     * 
     */
    public GetMergedDefectsForProjectResponse createGetMergedDefectsForProjectResponse() {
        return new GetMergedDefectsForProjectResponse();
    }

    /**
     * Create an instance of {@link UpdateStreamDefects }
     * 
     */
    public UpdateStreamDefects createUpdateStreamDefects() {
        return new UpdateStreamDefects();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetTrendRecordsForProject }
     * 
     */
    public GetTrendRecordsForProject createGetTrendRecordsForProject() {
        return new GetTrendRecordsForProject();
    }

    /**
     * Create an instance of {@link StreamIdDataObj }
     * 
     */
    public StreamIdDataObj createStreamIdDataObj() {
        return new StreamIdDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetTrendRecordsForProjectResponse }
     * 
     */
    public GetTrendRecordsForProjectResponse createGetTrendRecordsForProjectResponse() {
        return new GetTrendRecordsForProjectResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDForDMCID }
     * 
     */
    public GetCIDForDMCID createGetCIDForDMCID() {
        return new GetCIDForDMCID();
    }

    /**
     * Create an instance of {@link StreamDefectFilterSpecDataObj }
     * 
     */
    public StreamDefectFilterSpecDataObj createStreamDefectFilterSpecDataObj() {
        return new StreamDefectFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link StreamDefectIdDataObj }
     * 
     */
    public StreamDefectIdDataObj createStreamDefectIdDataObj() {
        return new StreamDefectIdDataObj();
    }

    /**
     * Create an instance of {@link SnapshotIdDataObj }
     * 
     */
    public SnapshotIdDataObj createSnapshotIdDataObj() {
        return new SnapshotIdDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetComponentMetricsForProject }
     * 
     */
    public GetComponentMetricsForProject createGetComponentMetricsForProject() {
        return new GetComponentMetricsForProject();
    }

    /**
     * Create an instance of {@link PageSpecDataObj }
     * 
     */
    public PageSpecDataObj createPageSpecDataObj() {
        return new PageSpecDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDsForProjectResponse }
     * 
     */
    public GetCIDsForProjectResponse createGetCIDsForProjectResponse() {
        return new GetCIDsForProjectResponse();
    }

    /**
     * Create an instance of {@link ProjectTrendRecordFilterSpecDataObj }
     * 
     */
    public ProjectTrendRecordFilterSpecDataObj createProjectTrendRecordFilterSpecDataObj() {
        return new ProjectTrendRecordFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link UpdateDefectInstancePropertiesResponse }
     * 
     */
    public UpdateDefectInstancePropertiesResponse createUpdateDefectInstancePropertiesResponse() {
        return new UpdateDefectInstancePropertiesResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetMergedDefectHistoryResponse }
     * 
     */
    public GetMergedDefectHistoryResponse createGetMergedDefectHistoryResponse() {
        return new GetMergedDefectHistoryResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.DefectInstanceDataObj }
     * 
     */
    public DefectInstanceDataObj createDefectInstanceDataObj() {
        return new DefectInstanceDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDsForStreams }
     * 
     */
    public GetCIDsForStreams createGetCIDsForStreams() {
        return new GetCIDsForStreams();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.DefectInstanceIdDataObj }
     * 
     */
    public DefectInstanceIdDataObj createDefectInstanceIdDataObj() {
        return new DefectInstanceIdDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.MergedDefectsPageDataObj }
     * 
     */
    public MergedDefectsPageDataObj createMergedDefectsPageDataObj() {
        return new MergedDefectsPageDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.DefectStateDataObj }
     * 
     */
    public DefectStateDataObj createDefectStateDataObj() {
        return new DefectStateDataObj();
    }

    /**
     * Create an instance of {@link PropertySpecDataObj }
     * 
     */
    public PropertySpecDataObj createPropertySpecDataObj() {
        return new PropertySpecDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetStreamDefects }
     * 
     */
    public GetStreamDefects createGetStreamDefects() {
        return new GetStreamDefects();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetMergedDefectHistory }
     * 
     */
    public GetMergedDefectHistory createGetMergedDefectHistory() {
        return new GetMergedDefectHistory();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.MergedDefectDataObj }
     * 
     */
    public MergedDefectDataObj createMergedDefectDataObj() {
        return new MergedDefectDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.FunctionInfoDataObj }
     * 
     */
    public FunctionInfoDataObj createFunctionInfoDataObj() {
        return new FunctionInfoDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDsForStreamsResponse }
     * 
     */
    public GetCIDsForStreamsResponse createGetCIDsForStreamsResponse() {
        return new GetCIDsForStreamsResponse();
    }

    /**
     * Create an instance of {@link ProjectIdDataObj }
     * 
     */
    public ProjectIdDataObj createProjectIdDataObj() {
        return new ProjectIdDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetComponentMetricsForProjectResponse }
     * 
     */
    public GetComponentMetricsForProjectResponse createGetComponentMetricsForProjectResponse() {
        return new GetComponentMetricsForProjectResponse();
    }

    /**
     * Create an instance of {@link StreamDefectDataObj }
     * 
     */
    public StreamDefectDataObj createStreamDefectDataObj() {
        return new StreamDefectDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.CheckerFilterSpecDataObj }
     * 
     */
    public CheckerFilterSpecDataObj createCheckerFilterSpecDataObj() {
        return new CheckerFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.DefectStateSpecDataObj }
     * 
     */
    public DefectStateSpecDataObj createDefectStateSpecDataObj() {
        return new DefectStateSpecDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.CovRemoteServiceException }
     * 
     */
    public CovRemoteServiceException createCovRemoteServiceException() {
        return new CovRemoteServiceException();
    }

    /**
     * Create an instance of {@link ProjectMetricsDataObj }
     * 
     */
    public ProjectMetricsDataObj createProjectMetricsDataObj() {
        return new ProjectMetricsDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDsForProject }
     * 
     */
    public GetCIDsForProject createGetCIDsForProject() {
        return new GetCIDsForProject();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.DefectChangeDataObj }
     * 
     */
    public DefectChangeDataObj createDefectChangeDataObj() {
        return new DefectChangeDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.FieldChangeDataObj }
     * 
     */
    public FieldChangeDataObj createFieldChangeDataObj() {
        return new FieldChangeDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.EventDataObj }
     * 
     */
    public EventDataObj createEventDataObj() {
        return new EventDataObj();
    }

    /**
     * Create an instance of {@link UpdateStreamDefectsResponse }
     * 
     */
    public UpdateStreamDefectsResponse createUpdateStreamDefectsResponse() {
        return new UpdateStreamDefectsResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDsForSnapshotResponse }
     * 
     */
    public GetCIDsForSnapshotResponse createGetCIDsForSnapshotResponse() {
        return new GetCIDsForSnapshotResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetMergedDefectsForStreamsResponse }
     * 
     */
    public GetMergedDefectsForStreamsResponse createGetMergedDefectsForStreamsResponse() {
        return new GetMergedDefectsForStreamsResponse();
    }

    /**
     * Create an instance of {@link UpdateDefectInstanceProperties }
     * 
     */
    public UpdateDefectInstanceProperties createUpdateDefectInstanceProperties() {
        return new UpdateDefectInstanceProperties();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetStreamDefectsResponse }
     * 
     */
    public GetStreamDefectsResponse createGetStreamDefectsResponse() {
        return new GetStreamDefectsResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.MergedDefectFilterSpecDataObj }
     * 
     */
    public MergedDefectFilterSpecDataObj createMergedDefectFilterSpecDataObj() {
        return new MergedDefectFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.ComponentMetricsDataObj }
     * 
     */
    public ComponentMetricsDataObj createComponentMetricsDataObj() {
        return new ComponentMetricsDataObj();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetMergedDefectsForStreams }
     * 
     */
    public GetMergedDefectsForStreams createGetMergedDefectsForStreams() {
        return new GetMergedDefectsForStreams();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetCIDForDMCIDResponse }
     * 
     */
    public GetCIDForDMCIDResponse createGetCIDForDMCIDResponse() {
        return new GetCIDForDMCIDResponse();
    }

    /**
     * Create an instance of {@link com.coverity.ws.v3.GetMergedDefectsForProject }
     * 
     */
    public GetMergedDefectsForProject createGetMergedDefectsForProject() {
        return new GetMergedDefectsForProject();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetMergedDefectsForStreamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getMergedDefectsForStreamsResponse")
    public JAXBElement<GetMergedDefectsForStreamsResponse> createGetMergedDefectsForStreamsResponse(GetMergedDefectsForStreamsResponse value) {
        return new JAXBElement<GetMergedDefectsForStreamsResponse>(_GetMergedDefectsForStreamsResponse_QNAME, GetMergedDefectsForStreamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link UpdateDefectInstanceProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "updateDefectInstanceProperties")
    public JAXBElement<UpdateDefectInstanceProperties> createUpdateDefectInstanceProperties(UpdateDefectInstanceProperties value) {
        return new JAXBElement<UpdateDefectInstanceProperties>(_UpdateDefectInstanceProperties_QNAME, UpdateDefectInstanceProperties.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link UpdateDefectInstancePropertiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "updateDefectInstancePropertiesResponse")
    public JAXBElement<UpdateDefectInstancePropertiesResponse> createUpdateDefectInstancePropertiesResponse(UpdateDefectInstancePropertiesResponse value) {
        return new JAXBElement<UpdateDefectInstancePropertiesResponse>(_UpdateDefectInstancePropertiesResponse_QNAME, UpdateDefectInstancePropertiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetComponentMetricsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getComponentMetricsForProject")
    public JAXBElement<GetComponentMetricsForProject> createGetComponentMetricsForProject(GetComponentMetricsForProject value) {
        return new JAXBElement<GetComponentMetricsForProject>(_GetComponentMetricsForProject_QNAME, GetComponentMetricsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDsForSnapshot }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDsForSnapshot")
    public JAXBElement<GetCIDsForSnapshot> createGetCIDsForSnapshot(GetCIDsForSnapshot value) {
        return new JAXBElement<GetCIDsForSnapshot>(_GetCIDsForSnapshot_QNAME, GetCIDsForSnapshot.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDsForSnapshotResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDsForSnapshotResponse")
    public JAXBElement<GetCIDsForSnapshotResponse> createGetCIDsForSnapshotResponse(GetCIDsForSnapshotResponse value) {
        return new JAXBElement<GetCIDsForSnapshotResponse>(_GetCIDsForSnapshotResponse_QNAME, GetCIDsForSnapshotResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetComponentMetricsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getComponentMetricsForProjectResponse")
    public JAXBElement<GetComponentMetricsForProjectResponse> createGetComponentMetricsForProjectResponse(GetComponentMetricsForProjectResponse value) {
        return new JAXBElement<GetComponentMetricsForProjectResponse>(_GetComponentMetricsForProjectResponse_QNAME, GetComponentMetricsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetTrendRecordsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getTrendRecordsForProjectResponse")
    public JAXBElement<GetTrendRecordsForProjectResponse> createGetTrendRecordsForProjectResponse(GetTrendRecordsForProjectResponse value) {
        return new JAXBElement<GetTrendRecordsForProjectResponse>(_GetTrendRecordsForProjectResponse_QNAME, GetTrendRecordsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.CovRemoteServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "CoverityFault")
    public JAXBElement<CovRemoteServiceException> createCoverityFault(CovRemoteServiceException value) {
        return new JAXBElement<CovRemoteServiceException>(_CoverityFault_QNAME, CovRemoteServiceException.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetMergedDefectHistoryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getMergedDefectHistoryResponse")
    public JAXBElement<GetMergedDefectHistoryResponse> createGetMergedDefectHistoryResponse(GetMergedDefectHistoryResponse value) {
        return new JAXBElement<GetMergedDefectHistoryResponse>(_GetMergedDefectHistoryResponse_QNAME, GetMergedDefectHistoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link UpdateStreamDefects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "updateStreamDefects")
    public JAXBElement<UpdateStreamDefects> createUpdateStreamDefects(UpdateStreamDefects value) {
        return new JAXBElement<UpdateStreamDefects>(_UpdateStreamDefects_QNAME, UpdateStreamDefects.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDsForProject")
    public JAXBElement<GetCIDsForProject> createGetCIDsForProject(GetCIDsForProject value) {
        return new JAXBElement<GetCIDsForProject>(_GetCIDsForProject_QNAME, GetCIDsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link UpdateStreamDefectsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "updateStreamDefectsResponse")
    public JAXBElement<UpdateStreamDefectsResponse> createUpdateStreamDefectsResponse(UpdateStreamDefectsResponse value) {
        return new JAXBElement<UpdateStreamDefectsResponse>(_UpdateStreamDefectsResponse_QNAME, UpdateStreamDefectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetStreamDefects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getStreamDefects")
    public JAXBElement<GetStreamDefects> createGetStreamDefects(GetStreamDefects value) {
        return new JAXBElement<GetStreamDefects>(_GetStreamDefects_QNAME, GetStreamDefects.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetMergedDefectsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getMergedDefectsForProject")
    public JAXBElement<GetMergedDefectsForProject> createGetMergedDefectsForProject(GetMergedDefectsForProject value) {
        return new JAXBElement<GetMergedDefectsForProject>(_GetMergedDefectsForProject_QNAME, GetMergedDefectsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetStreamDefectsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getStreamDefectsResponse")
    public JAXBElement<GetStreamDefectsResponse> createGetStreamDefectsResponse(GetStreamDefectsResponse value) {
        return new JAXBElement<GetStreamDefectsResponse>(_GetStreamDefectsResponse_QNAME, GetStreamDefectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDsForStreams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDsForStreams")
    public JAXBElement<GetCIDsForStreams> createGetCIDsForStreams(GetCIDsForStreams value) {
        return new JAXBElement<GetCIDsForStreams>(_GetCIDsForStreams_QNAME, GetCIDsForStreams.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDForDMCIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDForDMCIDResponse")
    public JAXBElement<GetCIDForDMCIDResponse> createGetCIDForDMCIDResponse(GetCIDForDMCIDResponse value) {
        return new JAXBElement<GetCIDForDMCIDResponse>(_GetCIDForDMCIDResponse_QNAME, GetCIDForDMCIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetTrendRecordsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getTrendRecordsForProject")
    public JAXBElement<GetTrendRecordsForProject> createGetTrendRecordsForProject(GetTrendRecordsForProject value) {
        return new JAXBElement<GetTrendRecordsForProject>(_GetTrendRecordsForProject_QNAME, GetTrendRecordsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDsForStreamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDsForStreamsResponse")
    public JAXBElement<GetCIDsForStreamsResponse> createGetCIDsForStreamsResponse(GetCIDsForStreamsResponse value) {
        return new JAXBElement<GetCIDsForStreamsResponse>(_GetCIDsForStreamsResponse_QNAME, GetCIDsForStreamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDsForProjectResponse")
    public JAXBElement<GetCIDsForProjectResponse> createGetCIDsForProjectResponse(GetCIDsForProjectResponse value) {
        return new JAXBElement<GetCIDsForProjectResponse>(_GetCIDsForProjectResponse_QNAME, GetCIDsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetCIDForDMCID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getCIDForDMCID")
    public JAXBElement<GetCIDForDMCID> createGetCIDForDMCID(GetCIDForDMCID value) {
        return new JAXBElement<GetCIDForDMCID>(_GetCIDForDMCID_QNAME, GetCIDForDMCID.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetMergedDefectsForStreams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getMergedDefectsForStreams")
    public JAXBElement<GetMergedDefectsForStreams> createGetMergedDefectsForStreams(GetMergedDefectsForStreams value) {
        return new JAXBElement<GetMergedDefectsForStreams>(_GetMergedDefectsForStreams_QNAME, GetMergedDefectsForStreams.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetMergedDefectHistory }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getMergedDefectHistory")
    public JAXBElement<GetMergedDefectHistory> createGetMergedDefectHistory(GetMergedDefectHistory value) {
        return new JAXBElement<GetMergedDefectHistory>(_GetMergedDefectHistory_QNAME, GetMergedDefectHistory.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.coverity.ws.v3.GetMergedDefectsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v3", name = "getMergedDefectsForProjectResponse")
    public JAXBElement<GetMergedDefectsForProjectResponse> createGetMergedDefectsForProjectResponse(GetMergedDefectsForProjectResponse value) {
        return new JAXBElement<GetMergedDefectsForProjectResponse>(_GetMergedDefectsForProjectResponse_QNAME, GetMergedDefectsForProjectResponse.class, null, value);
    }

}
