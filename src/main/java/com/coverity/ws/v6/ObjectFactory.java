
package com.coverity.ws.v6;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.coverity.ws.v6 package. 
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

    private final static QName _GetCheckerSubcategoriesForProjectResponse_QNAME = new QName("http://ws.coverity.com/v6", "getCheckerSubcategoriesForProjectResponse");
    private final static QName _UpdateTriageForCIDsInTriageStore_QNAME = new QName("http://ws.coverity.com/v6", "updateTriageForCIDsInTriageStore");
    private final static QName _GetFileContents_QNAME = new QName("http://ws.coverity.com/v6", "getFileContents");
    private final static QName _GetCheckerSubcategoriesForStreamsResponse_QNAME = new QName("http://ws.coverity.com/v6", "getCheckerSubcategoriesForStreamsResponse");
    private final static QName _GetComponentMetricsForProject_QNAME = new QName("http://ws.coverity.com/v6", "getComponentMetricsForProject");
    private final static QName _GetMergedDefectsForStreamsResponse_QNAME = new QName("http://ws.coverity.com/v6", "getMergedDefectsForStreamsResponse");
    private final static QName _UpdateDefectInstanceProperties_QNAME = new QName("http://ws.coverity.com/v6", "updateDefectInstanceProperties");
    private final static QName _UpdateDefectInstancePropertiesResponse_QNAME = new QName("http://ws.coverity.com/v6", "updateDefectInstancePropertiesResponse");
    private final static QName _GetMergedDefectHistoryResponse_QNAME = new QName("http://ws.coverity.com/v6", "getMergedDefectHistoryResponse");
    private final static QName _UpdateStreamDefects_QNAME = new QName("http://ws.coverity.com/v6", "updateStreamDefects");
    private final static QName _GetCheckerSubcategoriesForProject_QNAME = new QName("http://ws.coverity.com/v6", "getCheckerSubcategoriesForProject");
    private final static QName _CoverityFault_QNAME = new QName("http://ws.coverity.com/v6", "CoverityFault");
    private final static QName _CreateMergedDefectResponse_QNAME = new QName("http://ws.coverity.com/v6", "createMergedDefectResponse");
    private final static QName _GetTrendRecordsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v6", "getTrendRecordsForProjectResponse");
    private final static QName _GetComponentMetricsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v6", "getComponentMetricsForProjectResponse");
    private final static QName _GetCIDForMergeKey_QNAME = new QName("http://ws.coverity.com/v6", "getCIDForMergeKey");
    private final static QName _GetStreamDefectsResponse_QNAME = new QName("http://ws.coverity.com/v6", "getStreamDefectsResponse");
    private final static QName _GetStreamDefects_QNAME = new QName("http://ws.coverity.com/v6", "getStreamDefects");
    private final static QName _GetMergedDefectsForProject_QNAME = new QName("http://ws.coverity.com/v6", "getMergedDefectsForProject");
    private final static QName _UpdateStreamDefectsResponse_QNAME = new QName("http://ws.coverity.com/v6", "updateStreamDefectsResponse");
    private final static QName _GetCIDsForProject_QNAME = new QName("http://ws.coverity.com/v6", "getCIDsForProject");
    private final static QName _GetCIDsForStreams_QNAME = new QName("http://ws.coverity.com/v6", "getCIDsForStreams");
    private final static QName _UpdateTriageForCIDsInTriageStoreResponse_QNAME = new QName("http://ws.coverity.com/v6", "updateTriageForCIDsInTriageStoreResponse");
    private final static QName _GetFileContentsResponse_QNAME = new QName("http://ws.coverity.com/v6", "getFileContentsResponse");
    private final static QName _GetCIDForDMCIDResponse_QNAME = new QName("http://ws.coverity.com/v6", "getCIDForDMCIDResponse");
    private final static QName _GetCIDsForStreamsResponse_QNAME = new QName("http://ws.coverity.com/v6", "getCIDsForStreamsResponse");
    private final static QName _GetTrendRecordsForProject_QNAME = new QName("http://ws.coverity.com/v6", "getTrendRecordsForProject");
    private final static QName _GetCIDsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v6", "getCIDsForProjectResponse");
    private final static QName _GetStreamFunctionsInternalResponse_QNAME = new QName("http://ws.coverity.com/v6", "getStreamFunctionsInternalResponse");
    private final static QName _GetMergedDefectsForProjectResponse_QNAME = new QName("http://ws.coverity.com/v6", "getMergedDefectsForProjectResponse");
    private final static QName _GetStreamFunctionsInternal_QNAME = new QName("http://ws.coverity.com/v6", "getStreamFunctionsInternal");
    private final static QName _GetMergedDefectHistory_QNAME = new QName("http://ws.coverity.com/v6", "getMergedDefectHistory");
    private final static QName _GetCheckerSubcategoriesForStreams_QNAME = new QName("http://ws.coverity.com/v6", "getCheckerSubcategoriesForStreams");
    private final static QName _GetMergedDefectsForStreams_QNAME = new QName("http://ws.coverity.com/v6", "getMergedDefectsForStreams");
    private final static QName _GetCIDForMergeKeyResponse_QNAME = new QName("http://ws.coverity.com/v6", "getCIDForMergeKeyResponse");
    private final static QName _CreateMergedDefect_QNAME = new QName("http://ws.coverity.com/v6", "createMergedDefect");
    private final static QName _GetCIDForDMCID_QNAME = new QName("http://ws.coverity.com/v6", "getCIDForDMCID");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.coverity.ws.v6
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMergedDefectsForProjectResponse }
     * 
     */
    public GetMergedDefectsForProjectResponse createGetMergedDefectsForProjectResponse() {
        return new GetMergedDefectsForProjectResponse();
    }

    /**
     * Create an instance of {@link CreateMergedDefect }
     * 
     */
    public CreateMergedDefect createCreateMergedDefect() {
        return new CreateMergedDefect();
    }

    /**
     * Create an instance of {@link GetTrendRecordsForProject }
     * 
     */
    public GetTrendRecordsForProject createGetTrendRecordsForProject() {
        return new GetTrendRecordsForProject();
    }

    /**
     * Create an instance of {@link GetTrendRecordsForProjectResponse }
     * 
     */
    public GetTrendRecordsForProjectResponse createGetTrendRecordsForProjectResponse() {
        return new GetTrendRecordsForProjectResponse();
    }

    /**
     * Create an instance of {@link FunctionInfoDataObj }
     * 
     */
    public FunctionInfoDataObj createFunctionInfoDataObj() {
        return new FunctionInfoDataObj();
    }

    /**
     * Create an instance of {@link UpdateDefectInstanceProperties }
     * 
     */
    public UpdateDefectInstanceProperties createUpdateDefectInstanceProperties() {
        return new UpdateDefectInstanceProperties();
    }

    /**
     * Create an instance of {@link ComponentMetricsDataObj }
     * 
     */
    public ComponentMetricsDataObj createComponentMetricsDataObj() {
        return new ComponentMetricsDataObj();
    }

    /**
     * Create an instance of {@link StreamFunctionDataObj }
     * 
     */
    public StreamFunctionDataObj createStreamFunctionDataObj() {
        return new StreamFunctionDataObj();
    }

    /**
     * Create an instance of {@link FileContentsDataObj }
     * 
     */
    public FileContentsDataObj createFileContentsDataObj() {
        return new FileContentsDataObj();
    }

    /**
     * Create an instance of {@link StreamDefectFilterSpecDataObj }
     * 
     */
    public StreamDefectFilterSpecDataObj createStreamDefectFilterSpecDataObj() {
        return new StreamDefectFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link GetCheckerSubcategoriesForStreamsResponse }
     * 
     */
    public GetCheckerSubcategoriesForStreamsResponse createGetCheckerSubcategoriesForStreamsResponse() {
        return new GetCheckerSubcategoriesForStreamsResponse();
    }

    /**
     * Create an instance of {@link GetCheckerSubcategoriesForStreams }
     * 
     */
    public GetCheckerSubcategoriesForStreams createGetCheckerSubcategoriesForStreams() {
        return new GetCheckerSubcategoriesForStreams();
    }

    /**
     * Create an instance of {@link GetMergedDefectHistoryResponse }
     * 
     */
    public GetMergedDefectHistoryResponse createGetMergedDefectHistoryResponse() {
        return new GetMergedDefectHistoryResponse();
    }

    /**
     * Create an instance of {@link CreateMergedDefectResponse }
     * 
     */
    public CreateMergedDefectResponse createCreateMergedDefectResponse() {
        return new CreateMergedDefectResponse();
    }

    /**
     * Create an instance of {@link GetFileContents }
     * 
     */
    public GetFileContents createGetFileContents() {
        return new GetFileContents();
    }

    /**
     * Create an instance of {@link UpdateTriageForCIDsInTriageStoreResponse }
     * 
     */
    public UpdateTriageForCIDsInTriageStoreResponse createUpdateTriageForCIDsInTriageStoreResponse() {
        return new UpdateTriageForCIDsInTriageStoreResponse();
    }

    /**
     * Create an instance of {@link GetMergedDefectsForProject }
     * 
     */
    public GetMergedDefectsForProject createGetMergedDefectsForProject() {
        return new GetMergedDefectsForProject();
    }

    /**
     * Create an instance of {@link StreamSnapshotFilterSpecDataObj }
     * 
     */
    public StreamSnapshotFilterSpecDataObj createStreamSnapshotFilterSpecDataObj() {
        return new StreamSnapshotFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link DefectInstanceIdDataObj }
     * 
     */
    public DefectInstanceIdDataObj createDefectInstanceIdDataObj() {
        return new DefectInstanceIdDataObj();
    }

    /**
     * Create an instance of {@link PageSpecDataObj }
     * 
     */
    public PageSpecDataObj createPageSpecDataObj() {
        return new PageSpecDataObj();
    }

    /**
     * Create an instance of {@link ComponentIdDataObj }
     * 
     */
    public ComponentIdDataObj createComponentIdDataObj() {
        return new ComponentIdDataObj();
    }

    /**
     * Create an instance of {@link GetCIDForDMCID }
     * 
     */
    public GetCIDForDMCID createGetCIDForDMCID() {
        return new GetCIDForDMCID();
    }

    /**
     * Create an instance of {@link StreamFunctionPageDataObj }
     * 
     */
    public StreamFunctionPageDataObj createStreamFunctionPageDataObj() {
        return new StreamFunctionPageDataObj();
    }

    /**
     * Create an instance of {@link AttributeDefinitionValueFilterMapDataObj }
     * 
     */
    public AttributeDefinitionValueFilterMapDataObj createAttributeDefinitionValueFilterMapDataObj() {
        return new AttributeDefinitionValueFilterMapDataObj();
    }

    /**
     * Create an instance of {@link GetCIDsForStreamsResponse }
     * 
     */
    public GetCIDsForStreamsResponse createGetCIDsForStreamsResponse() {
        return new GetCIDsForStreamsResponse();
    }

    /**
     * Create an instance of {@link CheckerSubcategoryFilterSpecDataObj }
     * 
     */
    public CheckerSubcategoryFilterSpecDataObj createCheckerSubcategoryFilterSpecDataObj() {
        return new CheckerSubcategoryFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link GetStreamFunctionsInternal }
     * 
     */
    public GetStreamFunctionsInternal createGetStreamFunctionsInternal() {
        return new GetStreamFunctionsInternal();
    }

    /**
     * Create an instance of {@link DefectStateDataObj }
     * 
     */
    public DefectStateDataObj createDefectStateDataObj() {
        return new DefectStateDataObj();
    }

    /**
     * Create an instance of {@link FileIdDataObj }
     * 
     */
    public FileIdDataObj createFileIdDataObj() {
        return new FileIdDataObj();
    }

    /**
     * Create an instance of {@link GetCIDForDMCIDResponse }
     * 
     */
    public GetCIDForDMCIDResponse createGetCIDForDMCIDResponse() {
        return new GetCIDForDMCIDResponse();
    }

    /**
     * Create an instance of {@link FieldChangeDataObj }
     * 
     */
    public FieldChangeDataObj createFieldChangeDataObj() {
        return new FieldChangeDataObj();
    }

    /**
     * Create an instance of {@link DefectInstanceDataObj }
     * 
     */
    public DefectInstanceDataObj createDefectInstanceDataObj() {
        return new DefectInstanceDataObj();
    }

    /**
     * Create an instance of {@link GetCIDForMergeKey }
     * 
     */
    public GetCIDForMergeKey createGetCIDForMergeKey() {
        return new GetCIDForMergeKey();
    }

    /**
     * Create an instance of {@link GetMergedDefectsForStreamsResponse }
     * 
     */
    public GetMergedDefectsForStreamsResponse createGetMergedDefectsForStreamsResponse() {
        return new GetMergedDefectsForStreamsResponse();
    }

    /**
     * Create an instance of {@link GetCheckerSubcategoriesForProjectResponse }
     * 
     */
    public GetCheckerSubcategoriesForProjectResponse createGetCheckerSubcategoriesForProjectResponse() {
        return new GetCheckerSubcategoriesForProjectResponse();
    }

    /**
     * Create an instance of {@link UpdateStreamDefectsResponse }
     * 
     */
    public UpdateStreamDefectsResponse createUpdateStreamDefectsResponse() {
        return new UpdateStreamDefectsResponse();
    }

    /**
     * Create an instance of {@link PropertyDataObj }
     * 
     */
    public PropertyDataObj createPropertyDataObj() {
        return new PropertyDataObj();
    }

    /**
     * Create an instance of {@link StreamDefectIdDataObj }
     * 
     */
    public StreamDefectIdDataObj createStreamDefectIdDataObj() {
        return new StreamDefectIdDataObj();
    }

    /**
     * Create an instance of {@link UpdateTriageForCIDsInTriageStore }
     * 
     */
    public UpdateTriageForCIDsInTriageStore createUpdateTriageForCIDsInTriageStore() {
        return new UpdateTriageForCIDsInTriageStore();
    }

    /**
     * Create an instance of {@link GetCIDsForProjectResponse }
     * 
     */
    public GetCIDsForProjectResponse createGetCIDsForProjectResponse() {
        return new GetCIDsForProjectResponse();
    }

    /**
     * Create an instance of {@link MergedDefectsPageDataObj }
     * 
     */
    public MergedDefectsPageDataObj createMergedDefectsPageDataObj() {
        return new MergedDefectsPageDataObj();
    }

    /**
     * Create an instance of {@link GetComponentMetricsForProjectResponse }
     * 
     */
    public GetComponentMetricsForProjectResponse createGetComponentMetricsForProjectResponse() {
        return new GetComponentMetricsForProjectResponse();
    }

    /**
     * Create an instance of {@link GetStreamDefects }
     * 
     */
    public GetStreamDefects createGetStreamDefects() {
        return new GetStreamDefects();
    }

    /**
     * Create an instance of {@link SnapshotIdDataObj }
     * 
     */
    public SnapshotIdDataObj createSnapshotIdDataObj() {
        return new SnapshotIdDataObj();
    }

    /**
     * Create an instance of {@link DefectStateSpecDataObj }
     * 
     */
    public DefectStateSpecDataObj createDefectStateSpecDataObj() {
        return new DefectStateSpecDataObj();
    }

    /**
     * Create an instance of {@link EventDataObj }
     * 
     */
    public EventDataObj createEventDataObj() {
        return new EventDataObj();
    }

    /**
     * Create an instance of {@link GetCIDsForStreams }
     * 
     */
    public GetCIDsForStreams createGetCIDsForStreams() {
        return new GetCIDsForStreams();
    }

    /**
     * Create an instance of {@link GetCIDsForProject }
     * 
     */
    public GetCIDsForProject createGetCIDsForProject() {
        return new GetCIDsForProject();
    }

    /**
     * Create an instance of {@link PropertySpecDataObj }
     * 
     */
    public PropertySpecDataObj createPropertySpecDataObj() {
        return new PropertySpecDataObj();
    }

    /**
     * Create an instance of {@link GetMergedDefectHistory }
     * 
     */
    public GetMergedDefectHistory createGetMergedDefectHistory() {
        return new GetMergedDefectHistory();
    }

    /**
     * Create an instance of {@link GetMergedDefectsForStreams }
     * 
     */
    public GetMergedDefectsForStreams createGetMergedDefectsForStreams() {
        return new GetMergedDefectsForStreams();
    }

    /**
     * Create an instance of {@link GetCIDForMergeKeyResponse }
     * 
     */
    public GetCIDForMergeKeyResponse createGetCIDForMergeKeyResponse() {
        return new GetCIDForMergeKeyResponse();
    }

    /**
     * Create an instance of {@link GetStreamDefectsResponse }
     * 
     */
    public GetStreamDefectsResponse createGetStreamDefectsResponse() {
        return new GetStreamDefectsResponse();
    }

    /**
     * Create an instance of {@link GetCheckerSubcategoriesForProject }
     * 
     */
    public GetCheckerSubcategoriesForProject createGetCheckerSubcategoriesForProject() {
        return new GetCheckerSubcategoriesForProject();
    }

    /**
     * Create an instance of {@link MergedDefectDataObj }
     * 
     */
    public MergedDefectDataObj createMergedDefectDataObj() {
        return new MergedDefectDataObj();
    }

    /**
     * Create an instance of {@link UpdateStreamDefects }
     * 
     */
    public UpdateStreamDefects createUpdateStreamDefects() {
        return new UpdateStreamDefects();
    }

    /**
     * Create an instance of {@link GetComponentMetricsForProject }
     * 
     */
    public GetComponentMetricsForProject createGetComponentMetricsForProject() {
        return new GetComponentMetricsForProject();
    }

    /**
     * Create an instance of {@link GetStreamFunctionsInternalResponse }
     * 
     */
    public GetStreamFunctionsInternalResponse createGetStreamFunctionsInternalResponse() {
        return new GetStreamFunctionsInternalResponse();
    }

    /**
     * Create an instance of {@link StreamIdDataObj }
     * 
     */
    public StreamIdDataObj createStreamIdDataObj() {
        return new StreamIdDataObj();
    }

    /**
     * Create an instance of {@link DefectChangeDataObj }
     * 
     */
    public DefectChangeDataObj createDefectChangeDataObj() {
        return new DefectChangeDataObj();
    }

    /**
     * Create an instance of {@link GetFileContentsResponse }
     * 
     */
    public GetFileContentsResponse createGetFileContentsResponse() {
        return new GetFileContentsResponse();
    }

    /**
     * Create an instance of {@link ProjectTrendRecordFilterSpecDataObj }
     * 
     */
    public ProjectTrendRecordFilterSpecDataObj createProjectTrendRecordFilterSpecDataObj() {
        return new ProjectTrendRecordFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link CheckerSubcategoryIdDataObj }
     * 
     */
    public CheckerSubcategoryIdDataObj createCheckerSubcategoryIdDataObj() {
        return new CheckerSubcategoryIdDataObj();
    }

    /**
     * Create an instance of {@link StreamDefectDataObj }
     * 
     */
    public StreamDefectDataObj createStreamDefectDataObj() {
        return new StreamDefectDataObj();
    }

    /**
     * Create an instance of {@link ProjectIdDataObj }
     * 
     */
    public ProjectIdDataObj createProjectIdDataObj() {
        return new ProjectIdDataObj();
    }

    /**
     * Create an instance of {@link AttributeValueIdDataObj }
     * 
     */
    public AttributeValueIdDataObj createAttributeValueIdDataObj() {
        return new AttributeValueIdDataObj();
    }

    /**
     * Create an instance of {@link MergedDefectFilterSpecDataObj }
     * 
     */
    public MergedDefectFilterSpecDataObj createMergedDefectFilterSpecDataObj() {
        return new MergedDefectFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link DefectStateCustomAttributeValueDataObj }
     * 
     */
    public DefectStateCustomAttributeValueDataObj createDefectStateCustomAttributeValueDataObj() {
        return new DefectStateCustomAttributeValueDataObj();
    }

    /**
     * Create an instance of {@link ProjectMetricsDataObj }
     * 
     */
    public ProjectMetricsDataObj createProjectMetricsDataObj() {
        return new ProjectMetricsDataObj();
    }

    /**
     * Create an instance of {@link CovRemoteServiceException }
     * 
     */
    public CovRemoteServiceException createCovRemoteServiceException() {
        return new CovRemoteServiceException();
    }

    /**
     * Create an instance of {@link TriageStoreIdDataObj }
     * 
     */
    public TriageStoreIdDataObj createTriageStoreIdDataObj() {
        return new TriageStoreIdDataObj();
    }

    /**
     * Create an instance of {@link AttributeDefinitionIdDataObj }
     * 
     */
    public AttributeDefinitionIdDataObj createAttributeDefinitionIdDataObj() {
        return new AttributeDefinitionIdDataObj();
    }

    /**
     * Create an instance of {@link UpdateDefectInstancePropertiesResponse }
     * 
     */
    public UpdateDefectInstancePropertiesResponse createUpdateDefectInstancePropertiesResponse() {
        return new UpdateDefectInstancePropertiesResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckerSubcategoriesForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCheckerSubcategoriesForProjectResponse")
    public JAXBElement<GetCheckerSubcategoriesForProjectResponse> createGetCheckerSubcategoriesForProjectResponse(GetCheckerSubcategoriesForProjectResponse value) {
        return new JAXBElement<GetCheckerSubcategoriesForProjectResponse>(_GetCheckerSubcategoriesForProjectResponse_QNAME, GetCheckerSubcategoriesForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTriageForCIDsInTriageStore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "updateTriageForCIDsInTriageStore")
    public JAXBElement<UpdateTriageForCIDsInTriageStore> createUpdateTriageForCIDsInTriageStore(UpdateTriageForCIDsInTriageStore value) {
        return new JAXBElement<UpdateTriageForCIDsInTriageStore>(_UpdateTriageForCIDsInTriageStore_QNAME, UpdateTriageForCIDsInTriageStore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileContents }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getFileContents")
    public JAXBElement<GetFileContents> createGetFileContents(GetFileContents value) {
        return new JAXBElement<GetFileContents>(_GetFileContents_QNAME, GetFileContents.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckerSubcategoriesForStreamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCheckerSubcategoriesForStreamsResponse")
    public JAXBElement<GetCheckerSubcategoriesForStreamsResponse> createGetCheckerSubcategoriesForStreamsResponse(GetCheckerSubcategoriesForStreamsResponse value) {
        return new JAXBElement<GetCheckerSubcategoriesForStreamsResponse>(_GetCheckerSubcategoriesForStreamsResponse_QNAME, GetCheckerSubcategoriesForStreamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentMetricsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getComponentMetricsForProject")
    public JAXBElement<GetComponentMetricsForProject> createGetComponentMetricsForProject(GetComponentMetricsForProject value) {
        return new JAXBElement<GetComponentMetricsForProject>(_GetComponentMetricsForProject_QNAME, GetComponentMetricsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMergedDefectsForStreamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getMergedDefectsForStreamsResponse")
    public JAXBElement<GetMergedDefectsForStreamsResponse> createGetMergedDefectsForStreamsResponse(GetMergedDefectsForStreamsResponse value) {
        return new JAXBElement<GetMergedDefectsForStreamsResponse>(_GetMergedDefectsForStreamsResponse_QNAME, GetMergedDefectsForStreamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateDefectInstanceProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "updateDefectInstanceProperties")
    public JAXBElement<UpdateDefectInstanceProperties> createUpdateDefectInstanceProperties(UpdateDefectInstanceProperties value) {
        return new JAXBElement<UpdateDefectInstanceProperties>(_UpdateDefectInstanceProperties_QNAME, UpdateDefectInstanceProperties.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateDefectInstancePropertiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "updateDefectInstancePropertiesResponse")
    public JAXBElement<UpdateDefectInstancePropertiesResponse> createUpdateDefectInstancePropertiesResponse(UpdateDefectInstancePropertiesResponse value) {
        return new JAXBElement<UpdateDefectInstancePropertiesResponse>(_UpdateDefectInstancePropertiesResponse_QNAME, UpdateDefectInstancePropertiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMergedDefectHistoryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getMergedDefectHistoryResponse")
    public JAXBElement<GetMergedDefectHistoryResponse> createGetMergedDefectHistoryResponse(GetMergedDefectHistoryResponse value) {
        return new JAXBElement<GetMergedDefectHistoryResponse>(_GetMergedDefectHistoryResponse_QNAME, GetMergedDefectHistoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateStreamDefects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "updateStreamDefects")
    public JAXBElement<UpdateStreamDefects> createUpdateStreamDefects(UpdateStreamDefects value) {
        return new JAXBElement<UpdateStreamDefects>(_UpdateStreamDefects_QNAME, UpdateStreamDefects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckerSubcategoriesForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCheckerSubcategoriesForProject")
    public JAXBElement<GetCheckerSubcategoriesForProject> createGetCheckerSubcategoriesForProject(GetCheckerSubcategoriesForProject value) {
        return new JAXBElement<GetCheckerSubcategoriesForProject>(_GetCheckerSubcategoriesForProject_QNAME, GetCheckerSubcategoriesForProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CovRemoteServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "CoverityFault")
    public JAXBElement<CovRemoteServiceException> createCoverityFault(CovRemoteServiceException value) {
        return new JAXBElement<CovRemoteServiceException>(_CoverityFault_QNAME, CovRemoteServiceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateMergedDefectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "createMergedDefectResponse")
    public JAXBElement<CreateMergedDefectResponse> createCreateMergedDefectResponse(CreateMergedDefectResponse value) {
        return new JAXBElement<CreateMergedDefectResponse>(_CreateMergedDefectResponse_QNAME, CreateMergedDefectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTrendRecordsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getTrendRecordsForProjectResponse")
    public JAXBElement<GetTrendRecordsForProjectResponse> createGetTrendRecordsForProjectResponse(GetTrendRecordsForProjectResponse value) {
        return new JAXBElement<GetTrendRecordsForProjectResponse>(_GetTrendRecordsForProjectResponse_QNAME, GetTrendRecordsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentMetricsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getComponentMetricsForProjectResponse")
    public JAXBElement<GetComponentMetricsForProjectResponse> createGetComponentMetricsForProjectResponse(GetComponentMetricsForProjectResponse value) {
        return new JAXBElement<GetComponentMetricsForProjectResponse>(_GetComponentMetricsForProjectResponse_QNAME, GetComponentMetricsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDForMergeKey }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDForMergeKey")
    public JAXBElement<GetCIDForMergeKey> createGetCIDForMergeKey(GetCIDForMergeKey value) {
        return new JAXBElement<GetCIDForMergeKey>(_GetCIDForMergeKey_QNAME, GetCIDForMergeKey.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamDefectsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getStreamDefectsResponse")
    public JAXBElement<GetStreamDefectsResponse> createGetStreamDefectsResponse(GetStreamDefectsResponse value) {
        return new JAXBElement<GetStreamDefectsResponse>(_GetStreamDefectsResponse_QNAME, GetStreamDefectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamDefects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getStreamDefects")
    public JAXBElement<GetStreamDefects> createGetStreamDefects(GetStreamDefects value) {
        return new JAXBElement<GetStreamDefects>(_GetStreamDefects_QNAME, GetStreamDefects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMergedDefectsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getMergedDefectsForProject")
    public JAXBElement<GetMergedDefectsForProject> createGetMergedDefectsForProject(GetMergedDefectsForProject value) {
        return new JAXBElement<GetMergedDefectsForProject>(_GetMergedDefectsForProject_QNAME, GetMergedDefectsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateStreamDefectsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "updateStreamDefectsResponse")
    public JAXBElement<UpdateStreamDefectsResponse> createUpdateStreamDefectsResponse(UpdateStreamDefectsResponse value) {
        return new JAXBElement<UpdateStreamDefectsResponse>(_UpdateStreamDefectsResponse_QNAME, UpdateStreamDefectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDsForProject")
    public JAXBElement<GetCIDsForProject> createGetCIDsForProject(GetCIDsForProject value) {
        return new JAXBElement<GetCIDsForProject>(_GetCIDsForProject_QNAME, GetCIDsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDsForStreams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDsForStreams")
    public JAXBElement<GetCIDsForStreams> createGetCIDsForStreams(GetCIDsForStreams value) {
        return new JAXBElement<GetCIDsForStreams>(_GetCIDsForStreams_QNAME, GetCIDsForStreams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTriageForCIDsInTriageStoreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "updateTriageForCIDsInTriageStoreResponse")
    public JAXBElement<UpdateTriageForCIDsInTriageStoreResponse> createUpdateTriageForCIDsInTriageStoreResponse(UpdateTriageForCIDsInTriageStoreResponse value) {
        return new JAXBElement<UpdateTriageForCIDsInTriageStoreResponse>(_UpdateTriageForCIDsInTriageStoreResponse_QNAME, UpdateTriageForCIDsInTriageStoreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileContentsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getFileContentsResponse")
    public JAXBElement<GetFileContentsResponse> createGetFileContentsResponse(GetFileContentsResponse value) {
        return new JAXBElement<GetFileContentsResponse>(_GetFileContentsResponse_QNAME, GetFileContentsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDForDMCIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDForDMCIDResponse")
    public JAXBElement<GetCIDForDMCIDResponse> createGetCIDForDMCIDResponse(GetCIDForDMCIDResponse value) {
        return new JAXBElement<GetCIDForDMCIDResponse>(_GetCIDForDMCIDResponse_QNAME, GetCIDForDMCIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDsForStreamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDsForStreamsResponse")
    public JAXBElement<GetCIDsForStreamsResponse> createGetCIDsForStreamsResponse(GetCIDsForStreamsResponse value) {
        return new JAXBElement<GetCIDsForStreamsResponse>(_GetCIDsForStreamsResponse_QNAME, GetCIDsForStreamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTrendRecordsForProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getTrendRecordsForProject")
    public JAXBElement<GetTrendRecordsForProject> createGetTrendRecordsForProject(GetTrendRecordsForProject value) {
        return new JAXBElement<GetTrendRecordsForProject>(_GetTrendRecordsForProject_QNAME, GetTrendRecordsForProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDsForProjectResponse")
    public JAXBElement<GetCIDsForProjectResponse> createGetCIDsForProjectResponse(GetCIDsForProjectResponse value) {
        return new JAXBElement<GetCIDsForProjectResponse>(_GetCIDsForProjectResponse_QNAME, GetCIDsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamFunctionsInternalResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getStreamFunctionsInternalResponse")
    public JAXBElement<GetStreamFunctionsInternalResponse> createGetStreamFunctionsInternalResponse(GetStreamFunctionsInternalResponse value) {
        return new JAXBElement<GetStreamFunctionsInternalResponse>(_GetStreamFunctionsInternalResponse_QNAME, GetStreamFunctionsInternalResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMergedDefectsForProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getMergedDefectsForProjectResponse")
    public JAXBElement<GetMergedDefectsForProjectResponse> createGetMergedDefectsForProjectResponse(GetMergedDefectsForProjectResponse value) {
        return new JAXBElement<GetMergedDefectsForProjectResponse>(_GetMergedDefectsForProjectResponse_QNAME, GetMergedDefectsForProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamFunctionsInternal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getStreamFunctionsInternal")
    public JAXBElement<GetStreamFunctionsInternal> createGetStreamFunctionsInternal(GetStreamFunctionsInternal value) {
        return new JAXBElement<GetStreamFunctionsInternal>(_GetStreamFunctionsInternal_QNAME, GetStreamFunctionsInternal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMergedDefectHistory }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getMergedDefectHistory")
    public JAXBElement<GetMergedDefectHistory> createGetMergedDefectHistory(GetMergedDefectHistory value) {
        return new JAXBElement<GetMergedDefectHistory>(_GetMergedDefectHistory_QNAME, GetMergedDefectHistory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckerSubcategoriesForStreams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCheckerSubcategoriesForStreams")
    public JAXBElement<GetCheckerSubcategoriesForStreams> createGetCheckerSubcategoriesForStreams(GetCheckerSubcategoriesForStreams value) {
        return new JAXBElement<GetCheckerSubcategoriesForStreams>(_GetCheckerSubcategoriesForStreams_QNAME, GetCheckerSubcategoriesForStreams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMergedDefectsForStreams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getMergedDefectsForStreams")
    public JAXBElement<GetMergedDefectsForStreams> createGetMergedDefectsForStreams(GetMergedDefectsForStreams value) {
        return new JAXBElement<GetMergedDefectsForStreams>(_GetMergedDefectsForStreams_QNAME, GetMergedDefectsForStreams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDForMergeKeyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDForMergeKeyResponse")
    public JAXBElement<GetCIDForMergeKeyResponse> createGetCIDForMergeKeyResponse(GetCIDForMergeKeyResponse value) {
        return new JAXBElement<GetCIDForMergeKeyResponse>(_GetCIDForMergeKeyResponse_QNAME, GetCIDForMergeKeyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateMergedDefect }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "createMergedDefect")
    public JAXBElement<CreateMergedDefect> createCreateMergedDefect(CreateMergedDefect value) {
        return new JAXBElement<CreateMergedDefect>(_CreateMergedDefect_QNAME, CreateMergedDefect.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCIDForDMCID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v6", name = "getCIDForDMCID")
    public JAXBElement<GetCIDForDMCID> createGetCIDForDMCID(GetCIDForDMCID value) {
        return new JAXBElement<GetCIDForDMCID>(_GetCIDForDMCID_QNAME, GetCIDForDMCID.class, null, value);
    }

}
