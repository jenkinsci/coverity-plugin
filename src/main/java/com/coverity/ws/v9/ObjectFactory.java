
package com.coverity.ws.v9;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.coverity.ws.v9 package. 
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

    private final static QName _CreateTriageStore_QNAME = new QName("http://ws.coverity.com/v9", "createTriageStore");
    private final static QName _NotifyResponse_QNAME = new QName("http://ws.coverity.com/v9", "notifyResponse");
    private final static QName _CreateStreamResponse_QNAME = new QName("http://ws.coverity.com/v9", "createStreamResponse");
    private final static QName _CoverityFault_QNAME = new QName("http://ws.coverity.com/v9", "CoverityFault");
    private final static QName _UpdateRoleResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateRoleResponse");
    private final static QName _GetServerTime_QNAME = new QName("http://ws.coverity.com/v9", "getServerTime");
    private final static QName _GetLoggingConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getLoggingConfigurationResponse");
    private final static QName _GetBackupConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getBackupConfigurationResponse");
    private final static QName _GetTypeNamesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getTypeNamesResponse");
    private final static QName _GetSystemConfigResponse_QNAME = new QName("http://ws.coverity.com/v9", "getSystemConfigResponse");
    private final static QName _GetAllRolesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getAllRolesResponse");
    private final static QName _CopyStream_QNAME = new QName("http://ws.coverity.com/v9", "copyStream");
    private final static QName _GetGroups_QNAME = new QName("http://ws.coverity.com/v9", "getGroups");
    private final static QName _GetCategoryNames_QNAME = new QName("http://ws.coverity.com/v9", "getCategoryNames");
    private final static QName _UpdateStream_QNAME = new QName("http://ws.coverity.com/v9", "updateStream");
    private final static QName _GetProjects_QNAME = new QName("http://ws.coverity.com/v9", "getProjects");
    private final static QName _GetTriageStores_QNAME = new QName("http://ws.coverity.com/v9", "getTriageStores");
    private final static QName _GetGroup_QNAME = new QName("http://ws.coverity.com/v9", "getGroup");
    private final static QName _SetSnapshotPurgeDetails_QNAME = new QName("http://ws.coverity.com/v9", "setSnapshotPurgeDetails");
    private final static QName _GetSnapshotInformationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getSnapshotInformationResponse");
    private final static QName _GetUserResponse_QNAME = new QName("http://ws.coverity.com/v9", "getUserResponse");
    private final static QName _GetCommitStateResponse_QNAME = new QName("http://ws.coverity.com/v9", "getCommitStateResponse");
    private final static QName _UpdateTriageStore_QNAME = new QName("http://ws.coverity.com/v9", "updateTriageStore");
    private final static QName _GetCheckerNames_QNAME = new QName("http://ws.coverity.com/v9", "getCheckerNames");
    private final static QName _CreateGroup_QNAME = new QName("http://ws.coverity.com/v9", "createGroup");
    private final static QName _UpdateAttributeResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateAttributeResponse");
    private final static QName _CreateAttribute_QNAME = new QName("http://ws.coverity.com/v9", "createAttribute");
    private final static QName _CreateUserResponse_QNAME = new QName("http://ws.coverity.com/v9", "createUserResponse");
    private final static QName _GetCommitState_QNAME = new QName("http://ws.coverity.com/v9", "getCommitState");
    private final static QName _GetAttribute_QNAME = new QName("http://ws.coverity.com/v9", "getAttribute");
    private final static QName _CreateTriageStoreResponse_QNAME = new QName("http://ws.coverity.com/v9", "createTriageStoreResponse");
    private final static QName _UpdateSignInConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "updateSignInConfiguration");
    private final static QName _SetBackupConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "setBackupConfigurationResponse");
    private final static QName _UpdateSignInConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateSignInConfigurationResponse");
    private final static QName _CreateStreamInProject_QNAME = new QName("http://ws.coverity.com/v9", "createStreamInProject");
    private final static QName _GetTypeNames_QNAME = new QName("http://ws.coverity.com/v9", "getTypeNames");
    private final static QName _UpdateGroupResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateGroupResponse");
    private final static QName _DeleteTriageStore_QNAME = new QName("http://ws.coverity.com/v9", "deleteTriageStore");
    private final static QName _DeleteSnapshotResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteSnapshotResponse");
    private final static QName _GetSignInConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getSignInConfigurationResponse");
    private final static QName _DeleteAttributeResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteAttributeResponse");
    private final static QName _CreateRoleResponse_QNAME = new QName("http://ws.coverity.com/v9", "createRoleResponse");
    private final static QName _ExecuteNotificationResponse_QNAME = new QName("http://ws.coverity.com/v9", "executeNotificationResponse");
    private final static QName _UpdateUserResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateUserResponse");
    private final static QName _GetGroupResponse_QNAME = new QName("http://ws.coverity.com/v9", "getGroupResponse");
    private final static QName _UpdateAttribute_QNAME = new QName("http://ws.coverity.com/v9", "updateAttribute");
    private final static QName _GetDeleteSnapshotJobInfoResponse_QNAME = new QName("http://ws.coverity.com/v9", "getDeleteSnapshotJobInfoResponse");
    private final static QName _DeleteComponentMapResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteComponentMapResponse");
    private final static QName _GetLicenseConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "getLicenseConfiguration");
    private final static QName _GetLicenseState_QNAME = new QName("http://ws.coverity.com/v9", "getLicenseState");
    private final static QName _SetArchitectureAnalysisConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "setArchitectureAnalysisConfigurationResponse");
    private final static QName _ExecuteNotification_QNAME = new QName("http://ws.coverity.com/v9", "executeNotification");
    private final static QName _SetLoggingConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "setLoggingConfiguration");
    private final static QName _GetStreamsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getStreamsResponse");
    private final static QName _GetAllPermissionsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getAllPermissionsResponse");
    private final static QName _DeleteRole_QNAME = new QName("http://ws.coverity.com/v9", "deleteRole");
    private final static QName _GetAttributeResponse_QNAME = new QName("http://ws.coverity.com/v9", "getAttributeResponse");
    private final static QName _CreateProject_QNAME = new QName("http://ws.coverity.com/v9", "createProject");
    private final static QName _GetUser_QNAME = new QName("http://ws.coverity.com/v9", "getUser");
    private final static QName _DeleteSnapshot_QNAME = new QName("http://ws.coverity.com/v9", "deleteSnapshot");
    private final static QName _CreateProjectResponse_QNAME = new QName("http://ws.coverity.com/v9", "createProjectResponse");
    private final static QName _GetLicenseStateResponse_QNAME = new QName("http://ws.coverity.com/v9", "getLicenseStateResponse");
    private final static QName _DeleteGroup_QNAME = new QName("http://ws.coverity.com/v9", "deleteGroup");
    private final static QName _CreateLdapConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "createLdapConfiguration");
    private final static QName _DeleteStream_QNAME = new QName("http://ws.coverity.com/v9", "deleteStream");
    private final static QName _GetArchitectureAnalysisConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "getArchitectureAnalysisConfiguration");
    private final static QName _CreateAttributeResponse_QNAME = new QName("http://ws.coverity.com/v9", "createAttributeResponse");
    private final static QName _DeleteUserResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteUserResponse");
    private final static QName _SetLoggingConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "setLoggingConfigurationResponse");
    private final static QName _GetDeveloperStreamsProjectsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getDeveloperStreamsProjectsResponse");
    private final static QName _GetComponentResponse_QNAME = new QName("http://ws.coverity.com/v9", "getComponentResponse");
    private final static QName _GetVersion_QNAME = new QName("http://ws.coverity.com/v9", "getVersion");
    private final static QName _GetAllLdapConfigurations_QNAME = new QName("http://ws.coverity.com/v9", "getAllLdapConfigurations");
    private final static QName _GetRole_QNAME = new QName("http://ws.coverity.com/v9", "getRole");
    private final static QName _GetLdapServerDomains_QNAME = new QName("http://ws.coverity.com/v9", "getLdapServerDomains");
    private final static QName _GetLdapServerDomainsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getLdapServerDomainsResponse");
    private final static QName _SetAcceptingNewCommits_QNAME = new QName("http://ws.coverity.com/v9", "setAcceptingNewCommits");
    private final static QName _UpdateProjectResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateProjectResponse");
    private final static QName _GetDefectStatusesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getDefectStatusesResponse");
    private final static QName _SetSnapshotPurgeDetailsResponse_QNAME = new QName("http://ws.coverity.com/v9", "setSnapshotPurgeDetailsResponse");
    private final static QName _DeleteGroupResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteGroupResponse");
    private final static QName _GetDefectStatuses_QNAME = new QName("http://ws.coverity.com/v9", "getDefectStatuses");
    private final static QName _GetUsersResponse_QNAME = new QName("http://ws.coverity.com/v9", "getUsersResponse");
    private final static QName _UpdateComponentMap_QNAME = new QName("http://ws.coverity.com/v9", "updateComponentMap");
    private final static QName _GetMessageOfTheDay_QNAME = new QName("http://ws.coverity.com/v9", "getMessageOfTheDay");
    private final static QName _MergeTriageStoresResponse_QNAME = new QName("http://ws.coverity.com/v9", "mergeTriageStoresResponse");
    private final static QName _DeleteUser_QNAME = new QName("http://ws.coverity.com/v9", "deleteUser");
    private final static QName _GetSnapshotsForStream_QNAME = new QName("http://ws.coverity.com/v9", "getSnapshotsForStream");
    private final static QName _GetSnapshotsForStreamResponse_QNAME = new QName("http://ws.coverity.com/v9", "getSnapshotsForStreamResponse");
    private final static QName _GetLicenseConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getLicenseConfigurationResponse");
    private final static QName _DeleteLdapConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteLdapConfigurationResponse");
    private final static QName _SetMessageOfTheDayResponse_QNAME = new QName("http://ws.coverity.com/v9", "setMessageOfTheDayResponse");
    private final static QName _UpdateTriageStoreResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateTriageStoreResponse");
    private final static QName _GetSnapshotPurgeDetailsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getSnapshotPurgeDetailsResponse");
    private final static QName _UpdateUser_QNAME = new QName("http://ws.coverity.com/v9", "updateUser");
    private final static QName _GetAttributes_QNAME = new QName("http://ws.coverity.com/v9", "getAttributes");
    private final static QName _DeleteStreamResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteStreamResponse");
    private final static QName _GetBackupConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "getBackupConfiguration");
    private final static QName _CreateComponentMapResponse_QNAME = new QName("http://ws.coverity.com/v9", "createComponentMapResponse");
    private final static QName _CopyStreamResponse_QNAME = new QName("http://ws.coverity.com/v9", "copyStreamResponse");
    private final static QName _CreateUser_QNAME = new QName("http://ws.coverity.com/v9", "createUser");
    private final static QName _GetLoggingConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "getLoggingConfiguration");
    private final static QName _GetLastUpdateTimesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getLastUpdateTimesResponse");
    private final static QName _GetSnapshotPurgeDetails_QNAME = new QName("http://ws.coverity.com/v9", "getSnapshotPurgeDetails");
    private final static QName _DeleteAttribute_QNAME = new QName("http://ws.coverity.com/v9", "deleteAttribute");
    private final static QName _GetGroupsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getGroupsResponse");
    private final static QName _SetArchitectureAnalysisConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "setArchitectureAnalysisConfiguration");
    private final static QName _UpdateRole_QNAME = new QName("http://ws.coverity.com/v9", "updateRole");
    private final static QName _GetSnapshotInformation_QNAME = new QName("http://ws.coverity.com/v9", "getSnapshotInformation");
    private final static QName _DeleteRoleResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteRoleResponse");
    private final static QName _GetAllRoles_QNAME = new QName("http://ws.coverity.com/v9", "getAllRoles");
    private final static QName _UpdateLdapConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateLdapConfigurationResponse");
    private final static QName _DeleteComponentMap_QNAME = new QName("http://ws.coverity.com/v9", "deleteComponentMap");
    private final static QName _DeleteProjectResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteProjectResponse");
    private final static QName _GetDeveloperStreamsProjects_QNAME = new QName("http://ws.coverity.com/v9", "getDeveloperStreamsProjects");
    private final static QName _CreateComponentMap_QNAME = new QName("http://ws.coverity.com/v9", "createComponentMap");
    private final static QName _ImportLicense_QNAME = new QName("http://ws.coverity.com/v9", "importLicense");
    private final static QName _GetLastUpdateTimes_QNAME = new QName("http://ws.coverity.com/v9", "getLastUpdateTimes");
    private final static QName _GetProjectsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getProjectsResponse");
    private final static QName _CreateStream_QNAME = new QName("http://ws.coverity.com/v9", "createStream");
    private final static QName _DeleteLdapConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "deleteLdapConfiguration");
    private final static QName _GetVersionResponse_QNAME = new QName("http://ws.coverity.com/v9", "getVersionResponse");
    private final static QName _DeleteTriageStoreResponse_QNAME = new QName("http://ws.coverity.com/v9", "deleteTriageStoreResponse");
    private final static QName _GetComponentMapsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getComponentMapsResponse");
    private final static QName _GetCategoryNamesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getCategoryNamesResponse");
    private final static QName _GetDeleteSnapshotJobInfo_QNAME = new QName("http://ws.coverity.com/v9", "getDeleteSnapshotJobInfo");
    private final static QName _UpdateSnapshotInfo_QNAME = new QName("http://ws.coverity.com/v9", "updateSnapshotInfo");
    private final static QName _GetSystemConfig_QNAME = new QName("http://ws.coverity.com/v9", "getSystemConfig");
    private final static QName _UpdateSnapshotInfoResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateSnapshotInfoResponse");
    private final static QName _GetAllPermissions_QNAME = new QName("http://ws.coverity.com/v9", "getAllPermissions");
    private final static QName _SetMessageOfTheDay_QNAME = new QName("http://ws.coverity.com/v9", "setMessageOfTheDay");
    private final static QName _GetComponentMaps_QNAME = new QName("http://ws.coverity.com/v9", "getComponentMaps");
    private final static QName _GetUsers_QNAME = new QName("http://ws.coverity.com/v9", "getUsers");
    private final static QName _GetComponent_QNAME = new QName("http://ws.coverity.com/v9", "getComponent");
    private final static QName _CreateGroupResponse_QNAME = new QName("http://ws.coverity.com/v9", "createGroupResponse");
    private final static QName _SetSkeletonizationConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "setSkeletonizationConfigurationResponse");
    private final static QName _GetServerTimeResponse_QNAME = new QName("http://ws.coverity.com/v9", "getServerTimeResponse");
    private final static QName _CreateLdapConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "createLdapConfigurationResponse");
    private final static QName _GetAttributesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getAttributesResponse");
    private final static QName _GetMessageOfTheDayResponse_QNAME = new QName("http://ws.coverity.com/v9", "getMessageOfTheDayResponse");
    private final static QName _UpdateLdapConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "updateLdapConfiguration");
    private final static QName _SetAcceptingNewCommitsResponse_QNAME = new QName("http://ws.coverity.com/v9", "setAcceptingNewCommitsResponse");
    private final static QName _CreateStreamInProjectResponse_QNAME = new QName("http://ws.coverity.com/v9", "createStreamInProjectResponse");
    private final static QName _UpdateGroup_QNAME = new QName("http://ws.coverity.com/v9", "updateGroup");
    private final static QName _UpdateStreamResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateStreamResponse");
    private final static QName _GetArchitectureAnalysisConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getArchitectureAnalysisConfigurationResponse");
    private final static QName _GetTriageStoresResponse_QNAME = new QName("http://ws.coverity.com/v9", "getTriageStoresResponse");
    private final static QName _SetBackupConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "setBackupConfiguration");
    private final static QName _ImportLicenseResponse_QNAME = new QName("http://ws.coverity.com/v9", "importLicenseResponse");
    private final static QName _GetAllLdapConfigurationsResponse_QNAME = new QName("http://ws.coverity.com/v9", "getAllLdapConfigurationsResponse");
    private final static QName _MergeTriageStores_QNAME = new QName("http://ws.coverity.com/v9", "mergeTriageStores");
    private final static QName _SetSkeletonizationConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "setSkeletonizationConfiguration");
    private final static QName _CreateRole_QNAME = new QName("http://ws.coverity.com/v9", "createRole");
    private final static QName _GetSignInConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "getSignInConfiguration");
    private final static QName _GetSkeletonizationConfiguration_QNAME = new QName("http://ws.coverity.com/v9", "getSkeletonizationConfiguration");
    private final static QName _GetCheckerNamesResponse_QNAME = new QName("http://ws.coverity.com/v9", "getCheckerNamesResponse");
    private final static QName _Notify_QNAME = new QName("http://ws.coverity.com/v9", "notify");
    private final static QName _GetRoleResponse_QNAME = new QName("http://ws.coverity.com/v9", "getRoleResponse");
    private final static QName _GetStreams_QNAME = new QName("http://ws.coverity.com/v9", "getStreams");
    private final static QName _UpdateProject_QNAME = new QName("http://ws.coverity.com/v9", "updateProject");
    private final static QName _DeleteProject_QNAME = new QName("http://ws.coverity.com/v9", "deleteProject");
    private final static QName _GetSkeletonizationConfigurationResponse_QNAME = new QName("http://ws.coverity.com/v9", "getSkeletonizationConfigurationResponse");
    private final static QName _UpdateComponentMapResponse_QNAME = new QName("http://ws.coverity.com/v9", "updateComponentMapResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.coverity.ws.v9
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateTriageStoreResponse }
     * 
     */
    public CreateTriageStoreResponse createCreateTriageStoreResponse() {
        return new CreateTriageStoreResponse();
    }

    /**
     * Create an instance of {@link UpdateSignInConfiguration }
     * 
     */
    public UpdateSignInConfiguration createUpdateSignInConfiguration() {
        return new UpdateSignInConfiguration();
    }

    /**
     * Create an instance of {@link UpdateSignInConfigurationResponse }
     * 
     */
    public UpdateSignInConfigurationResponse createUpdateSignInConfigurationResponse() {
        return new UpdateSignInConfigurationResponse();
    }

    /**
     * Create an instance of {@link SetBackupConfigurationResponse }
     * 
     */
    public SetBackupConfigurationResponse createSetBackupConfigurationResponse() {
        return new SetBackupConfigurationResponse();
    }

    /**
     * Create an instance of {@link CreateStreamInProject }
     * 
     */
    public CreateStreamInProject createCreateStreamInProject() {
        return new CreateStreamInProject();
    }

    /**
     * Create an instance of {@link GetCommitState }
     * 
     */
    public GetCommitState createGetCommitState() {
        return new GetCommitState();
    }

    /**
     * Create an instance of {@link CreateUserResponse }
     * 
     */
    public CreateUserResponse createCreateUserResponse() {
        return new CreateUserResponse();
    }

    /**
     * Create an instance of {@link GetAttribute }
     * 
     */
    public GetAttribute createGetAttribute() {
        return new GetAttribute();
    }

    /**
     * Create an instance of {@link UpdateTriageStore }
     * 
     */
    public UpdateTriageStore createUpdateTriageStore() {
        return new UpdateTriageStore();
    }

    /**
     * Create an instance of {@link CreateGroup }
     * 
     */
    public CreateGroup createCreateGroup() {
        return new CreateGroup();
    }

    /**
     * Create an instance of {@link GetCheckerNames }
     * 
     */
    public GetCheckerNames createGetCheckerNames() {
        return new GetCheckerNames();
    }

    /**
     * Create an instance of {@link UpdateAttributeResponse }
     * 
     */
    public UpdateAttributeResponse createUpdateAttributeResponse() {
        return new UpdateAttributeResponse();
    }

    /**
     * Create an instance of {@link CreateAttribute }
     * 
     */
    public CreateAttribute createCreateAttribute() {
        return new CreateAttribute();
    }

    /**
     * Create an instance of {@link SetSnapshotPurgeDetails }
     * 
     */
    public SetSnapshotPurgeDetails createSetSnapshotPurgeDetails() {
        return new SetSnapshotPurgeDetails();
    }

    /**
     * Create an instance of {@link GetGroup }
     * 
     */
    public GetGroup createGetGroup() {
        return new GetGroup();
    }

    /**
     * Create an instance of {@link GetSnapshotInformationResponse }
     * 
     */
    public GetSnapshotInformationResponse createGetSnapshotInformationResponse() {
        return new GetSnapshotInformationResponse();
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link GetCommitStateResponse }
     * 
     */
    public GetCommitStateResponse createGetCommitStateResponse() {
        return new GetCommitStateResponse();
    }

    /**
     * Create an instance of {@link GetCategoryNames }
     * 
     */
    public GetCategoryNames createGetCategoryNames() {
        return new GetCategoryNames();
    }

    /**
     * Create an instance of {@link GetGroups }
     * 
     */
    public GetGroups createGetGroups() {
        return new GetGroups();
    }

    /**
     * Create an instance of {@link GetTriageStores }
     * 
     */
    public GetTriageStores createGetTriageStores() {
        return new GetTriageStores();
    }

    /**
     * Create an instance of {@link UpdateStream }
     * 
     */
    public UpdateStream createUpdateStream() {
        return new UpdateStream();
    }

    /**
     * Create an instance of {@link GetProjects }
     * 
     */
    public GetProjects createGetProjects() {
        return new GetProjects();
    }

    /**
     * Create an instance of {@link CopyStream }
     * 
     */
    public CopyStream createCopyStream() {
        return new CopyStream();
    }

    /**
     * Create an instance of {@link GetServerTime }
     * 
     */
    public GetServerTime createGetServerTime() {
        return new GetServerTime();
    }

    /**
     * Create an instance of {@link GetBackupConfigurationResponse }
     * 
     */
    public GetBackupConfigurationResponse createGetBackupConfigurationResponse() {
        return new GetBackupConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetLoggingConfigurationResponse }
     * 
     */
    public GetLoggingConfigurationResponse createGetLoggingConfigurationResponse() {
        return new GetLoggingConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetSystemConfigResponse }
     * 
     */
    public GetSystemConfigResponse createGetSystemConfigResponse() {
        return new GetSystemConfigResponse();
    }

    /**
     * Create an instance of {@link GetTypeNamesResponse }
     * 
     */
    public GetTypeNamesResponse createGetTypeNamesResponse() {
        return new GetTypeNamesResponse();
    }

    /**
     * Create an instance of {@link GetAllRolesResponse }
     * 
     */
    public GetAllRolesResponse createGetAllRolesResponse() {
        return new GetAllRolesResponse();
    }

    /**
     * Create an instance of {@link NotifyResponse }
     * 
     */
    public NotifyResponse createNotifyResponse() {
        return new NotifyResponse();
    }

    /**
     * Create an instance of {@link CreateTriageStore }
     * 
     */
    public CreateTriageStore createCreateTriageStore() {
        return new CreateTriageStore();
    }

    /**
     * Create an instance of {@link UpdateRoleResponse }
     * 
     */
    public UpdateRoleResponse createUpdateRoleResponse() {
        return new UpdateRoleResponse();
    }

    /**
     * Create an instance of {@link CovRemoteServiceException }
     * 
     */
    public CovRemoteServiceException createCovRemoteServiceException() {
        return new CovRemoteServiceException();
    }

    /**
     * Create an instance of {@link CreateStreamResponse }
     * 
     */
    public CreateStreamResponse createCreateStreamResponse() {
        return new CreateStreamResponse();
    }

    /**
     * Create an instance of {@link GetDefectStatuses }
     * 
     */
    public GetDefectStatuses createGetDefectStatuses() {
        return new GetDefectStatuses();
    }

    /**
     * Create an instance of {@link UpdateComponentMap }
     * 
     */
    public UpdateComponentMap createUpdateComponentMap() {
        return new UpdateComponentMap();
    }

    /**
     * Create an instance of {@link GetUsersResponse }
     * 
     */
    public GetUsersResponse createGetUsersResponse() {
        return new GetUsersResponse();
    }

    /**
     * Create an instance of {@link MergeTriageStoresResponse }
     * 
     */
    public MergeTriageStoresResponse createMergeTriageStoresResponse() {
        return new MergeTriageStoresResponse();
    }

    /**
     * Create an instance of {@link GetMessageOfTheDay }
     * 
     */
    public GetMessageOfTheDay createGetMessageOfTheDay() {
        return new GetMessageOfTheDay();
    }

    /**
     * Create an instance of {@link UpdateProjectResponse }
     * 
     */
    public UpdateProjectResponse createUpdateProjectResponse() {
        return new UpdateProjectResponse();
    }

    /**
     * Create an instance of {@link SetAcceptingNewCommits }
     * 
     */
    public SetAcceptingNewCommits createSetAcceptingNewCommits() {
        return new SetAcceptingNewCommits();
    }

    /**
     * Create an instance of {@link GetLdapServerDomainsResponse }
     * 
     */
    public GetLdapServerDomainsResponse createGetLdapServerDomainsResponse() {
        return new GetLdapServerDomainsResponse();
    }

    /**
     * Create an instance of {@link SetSnapshotPurgeDetailsResponse }
     * 
     */
    public SetSnapshotPurgeDetailsResponse createSetSnapshotPurgeDetailsResponse() {
        return new SetSnapshotPurgeDetailsResponse();
    }

    /**
     * Create an instance of {@link GetDefectStatusesResponse }
     * 
     */
    public GetDefectStatusesResponse createGetDefectStatusesResponse() {
        return new GetDefectStatusesResponse();
    }

    /**
     * Create an instance of {@link DeleteGroupResponse }
     * 
     */
    public DeleteGroupResponse createDeleteGroupResponse() {
        return new DeleteGroupResponse();
    }

    /**
     * Create an instance of {@link SetLoggingConfigurationResponse }
     * 
     */
    public SetLoggingConfigurationResponse createSetLoggingConfigurationResponse() {
        return new SetLoggingConfigurationResponse();
    }

    /**
     * Create an instance of {@link DeleteUserResponse }
     * 
     */
    public DeleteUserResponse createDeleteUserResponse() {
        return new DeleteUserResponse();
    }

    /**
     * Create an instance of {@link GetDeveloperStreamsProjectsResponse }
     * 
     */
    public GetDeveloperStreamsProjectsResponse createGetDeveloperStreamsProjectsResponse() {
        return new GetDeveloperStreamsProjectsResponse();
    }

    /**
     * Create an instance of {@link CreateAttributeResponse }
     * 
     */
    public CreateAttributeResponse createCreateAttributeResponse() {
        return new CreateAttributeResponse();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link GetAllLdapConfigurations }
     * 
     */
    public GetAllLdapConfigurations createGetAllLdapConfigurations() {
        return new GetAllLdapConfigurations();
    }

    /**
     * Create an instance of {@link GetComponentResponse }
     * 
     */
    public GetComponentResponse createGetComponentResponse() {
        return new GetComponentResponse();
    }

    /**
     * Create an instance of {@link GetRole }
     * 
     */
    public GetRole createGetRole() {
        return new GetRole();
    }

    /**
     * Create an instance of {@link GetLdapServerDomains }
     * 
     */
    public GetLdapServerDomains createGetLdapServerDomains() {
        return new GetLdapServerDomains();
    }

    /**
     * Create an instance of {@link DeleteGroup }
     * 
     */
    public DeleteGroup createDeleteGroup() {
        return new DeleteGroup();
    }

    /**
     * Create an instance of {@link CreateLdapConfiguration }
     * 
     */
    public CreateLdapConfiguration createCreateLdapConfiguration() {
        return new CreateLdapConfiguration();
    }

    /**
     * Create an instance of {@link DeleteStream }
     * 
     */
    public DeleteStream createDeleteStream() {
        return new DeleteStream();
    }

    /**
     * Create an instance of {@link GetArchitectureAnalysisConfiguration }
     * 
     */
    public GetArchitectureAnalysisConfiguration createGetArchitectureAnalysisConfiguration() {
        return new GetArchitectureAnalysisConfiguration();
    }

    /**
     * Create an instance of {@link GetAttributeResponse }
     * 
     */
    public GetAttributeResponse createGetAttributeResponse() {
        return new GetAttributeResponse();
    }

    /**
     * Create an instance of {@link DeleteRole }
     * 
     */
    public DeleteRole createDeleteRole() {
        return new DeleteRole();
    }

    /**
     * Create an instance of {@link GetUser }
     * 
     */
    public GetUser createGetUser() {
        return new GetUser();
    }

    /**
     * Create an instance of {@link DeleteSnapshot }
     * 
     */
    public DeleteSnapshot createDeleteSnapshot() {
        return new DeleteSnapshot();
    }

    /**
     * Create an instance of {@link CreateProjectResponse }
     * 
     */
    public CreateProjectResponse createCreateProjectResponse() {
        return new CreateProjectResponse();
    }

    /**
     * Create an instance of {@link GetLicenseStateResponse }
     * 
     */
    public GetLicenseStateResponse createGetLicenseStateResponse() {
        return new GetLicenseStateResponse();
    }

    /**
     * Create an instance of {@link CreateProject }
     * 
     */
    public CreateProject createCreateProject() {
        return new CreateProject();
    }

    /**
     * Create an instance of {@link SetArchitectureAnalysisConfigurationResponse }
     * 
     */
    public SetArchitectureAnalysisConfigurationResponse createSetArchitectureAnalysisConfigurationResponse() {
        return new SetArchitectureAnalysisConfigurationResponse();
    }

    /**
     * Create an instance of {@link ExecuteNotification }
     * 
     */
    public ExecuteNotification createExecuteNotification() {
        return new ExecuteNotification();
    }

    /**
     * Create an instance of {@link SetLoggingConfiguration }
     * 
     */
    public SetLoggingConfiguration createSetLoggingConfiguration() {
        return new SetLoggingConfiguration();
    }

    /**
     * Create an instance of {@link GetStreamsResponse }
     * 
     */
    public GetStreamsResponse createGetStreamsResponse() {
        return new GetStreamsResponse();
    }

    /**
     * Create an instance of {@link GetAllPermissionsResponse }
     * 
     */
    public GetAllPermissionsResponse createGetAllPermissionsResponse() {
        return new GetAllPermissionsResponse();
    }

    /**
     * Create an instance of {@link UpdateUserResponse }
     * 
     */
    public UpdateUserResponse createUpdateUserResponse() {
        return new UpdateUserResponse();
    }

    /**
     * Create an instance of {@link GetGroupResponse }
     * 
     */
    public GetGroupResponse createGetGroupResponse() {
        return new GetGroupResponse();
    }

    /**
     * Create an instance of {@link UpdateAttribute }
     * 
     */
    public UpdateAttribute createUpdateAttribute() {
        return new UpdateAttribute();
    }

    /**
     * Create an instance of {@link GetDeleteSnapshotJobInfoResponse }
     * 
     */
    public GetDeleteSnapshotJobInfoResponse createGetDeleteSnapshotJobInfoResponse() {
        return new GetDeleteSnapshotJobInfoResponse();
    }

    /**
     * Create an instance of {@link DeleteComponentMapResponse }
     * 
     */
    public DeleteComponentMapResponse createDeleteComponentMapResponse() {
        return new DeleteComponentMapResponse();
    }

    /**
     * Create an instance of {@link GetLicenseConfiguration }
     * 
     */
    public GetLicenseConfiguration createGetLicenseConfiguration() {
        return new GetLicenseConfiguration();
    }

    /**
     * Create an instance of {@link GetLicenseState }
     * 
     */
    public GetLicenseState createGetLicenseState() {
        return new GetLicenseState();
    }

    /**
     * Create an instance of {@link UpdateGroupResponse }
     * 
     */
    public UpdateGroupResponse createUpdateGroupResponse() {
        return new UpdateGroupResponse();
    }

    /**
     * Create an instance of {@link DeleteTriageStore }
     * 
     */
    public DeleteTriageStore createDeleteTriageStore() {
        return new DeleteTriageStore();
    }

    /**
     * Create an instance of {@link GetTypeNames }
     * 
     */
    public GetTypeNames createGetTypeNames() {
        return new GetTypeNames();
    }

    /**
     * Create an instance of {@link GetSignInConfigurationResponse }
     * 
     */
    public GetSignInConfigurationResponse createGetSignInConfigurationResponse() {
        return new GetSignInConfigurationResponse();
    }

    /**
     * Create an instance of {@link DeleteAttributeResponse }
     * 
     */
    public DeleteAttributeResponse createDeleteAttributeResponse() {
        return new DeleteAttributeResponse();
    }

    /**
     * Create an instance of {@link CreateRoleResponse }
     * 
     */
    public CreateRoleResponse createCreateRoleResponse() {
        return new CreateRoleResponse();
    }

    /**
     * Create an instance of {@link DeleteSnapshotResponse }
     * 
     */
    public DeleteSnapshotResponse createDeleteSnapshotResponse() {
        return new DeleteSnapshotResponse();
    }

    /**
     * Create an instance of {@link ExecuteNotificationResponse }
     * 
     */
    public ExecuteNotificationResponse createExecuteNotificationResponse() {
        return new ExecuteNotificationResponse();
    }

    /**
     * Create an instance of {@link GetComponentMapsResponse }
     * 
     */
    public GetComponentMapsResponse createGetComponentMapsResponse() {
        return new GetComponentMapsResponse();
    }

    /**
     * Create an instance of {@link GetCategoryNamesResponse }
     * 
     */
    public GetCategoryNamesResponse createGetCategoryNamesResponse() {
        return new GetCategoryNamesResponse();
    }

    /**
     * Create an instance of {@link UpdateSnapshotInfo }
     * 
     */
    public UpdateSnapshotInfo createUpdateSnapshotInfo() {
        return new UpdateSnapshotInfo();
    }

    /**
     * Create an instance of {@link GetSystemConfig }
     * 
     */
    public GetSystemConfig createGetSystemConfig() {
        return new GetSystemConfig();
    }

    /**
     * Create an instance of {@link GetDeleteSnapshotJobInfo }
     * 
     */
    public GetDeleteSnapshotJobInfo createGetDeleteSnapshotJobInfo() {
        return new GetDeleteSnapshotJobInfo();
    }

    /**
     * Create an instance of {@link SetMessageOfTheDay }
     * 
     */
    public SetMessageOfTheDay createSetMessageOfTheDay() {
        return new SetMessageOfTheDay();
    }

    /**
     * Create an instance of {@link UpdateSnapshotInfoResponse }
     * 
     */
    public UpdateSnapshotInfoResponse createUpdateSnapshotInfoResponse() {
        return new UpdateSnapshotInfoResponse();
    }

    /**
     * Create an instance of {@link GetAllPermissions }
     * 
     */
    public GetAllPermissions createGetAllPermissions() {
        return new GetAllPermissions();
    }

    /**
     * Create an instance of {@link GetProjectsResponse }
     * 
     */
    public GetProjectsResponse createGetProjectsResponse() {
        return new GetProjectsResponse();
    }

    /**
     * Create an instance of {@link DeleteLdapConfiguration }
     * 
     */
    public DeleteLdapConfiguration createDeleteLdapConfiguration() {
        return new DeleteLdapConfiguration();
    }

    /**
     * Create an instance of {@link CreateStream }
     * 
     */
    public CreateStream createCreateStream() {
        return new CreateStream();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link DeleteTriageStoreResponse }
     * 
     */
    public DeleteTriageStoreResponse createDeleteTriageStoreResponse() {
        return new DeleteTriageStoreResponse();
    }

    /**
     * Create an instance of {@link ImportLicense }
     * 
     */
    public ImportLicense createImportLicense() {
        return new ImportLicense();
    }

    /**
     * Create an instance of {@link CreateComponentMap }
     * 
     */
    public CreateComponentMap createCreateComponentMap() {
        return new CreateComponentMap();
    }

    /**
     * Create an instance of {@link GetDeveloperStreamsProjects }
     * 
     */
    public GetDeveloperStreamsProjects createGetDeveloperStreamsProjects() {
        return new GetDeveloperStreamsProjects();
    }

    /**
     * Create an instance of {@link GetLastUpdateTimes }
     * 
     */
    public GetLastUpdateTimes createGetLastUpdateTimes() {
        return new GetLastUpdateTimes();
    }

    /**
     * Create an instance of {@link UpdateLdapConfigurationResponse }
     * 
     */
    public UpdateLdapConfigurationResponse createUpdateLdapConfigurationResponse() {
        return new UpdateLdapConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetAllRoles }
     * 
     */
    public GetAllRoles createGetAllRoles() {
        return new GetAllRoles();
    }

    /**
     * Create an instance of {@link DeleteProjectResponse }
     * 
     */
    public DeleteProjectResponse createDeleteProjectResponse() {
        return new DeleteProjectResponse();
    }

    /**
     * Create an instance of {@link DeleteComponentMap }
     * 
     */
    public DeleteComponentMap createDeleteComponentMap() {
        return new DeleteComponentMap();
    }

    /**
     * Create an instance of {@link DeleteRoleResponse }
     * 
     */
    public DeleteRoleResponse createDeleteRoleResponse() {
        return new DeleteRoleResponse();
    }

    /**
     * Create an instance of {@link GetSnapshotInformation }
     * 
     */
    public GetSnapshotInformation createGetSnapshotInformation() {
        return new GetSnapshotInformation();
    }

    /**
     * Create an instance of {@link DeleteAttribute }
     * 
     */
    public DeleteAttribute createDeleteAttribute() {
        return new DeleteAttribute();
    }

    /**
     * Create an instance of {@link GetSnapshotPurgeDetails }
     * 
     */
    public GetSnapshotPurgeDetails createGetSnapshotPurgeDetails() {
        return new GetSnapshotPurgeDetails();
    }

    /**
     * Create an instance of {@link SetArchitectureAnalysisConfiguration }
     * 
     */
    public SetArchitectureAnalysisConfiguration createSetArchitectureAnalysisConfiguration() {
        return new SetArchitectureAnalysisConfiguration();
    }

    /**
     * Create an instance of {@link GetGroupsResponse }
     * 
     */
    public GetGroupsResponse createGetGroupsResponse() {
        return new GetGroupsResponse();
    }

    /**
     * Create an instance of {@link UpdateRole }
     * 
     */
    public UpdateRole createUpdateRole() {
        return new UpdateRole();
    }

    /**
     * Create an instance of {@link CreateComponentMapResponse }
     * 
     */
    public CreateComponentMapResponse createCreateComponentMapResponse() {
        return new CreateComponentMapResponse();
    }

    /**
     * Create an instance of {@link GetBackupConfiguration }
     * 
     */
    public GetBackupConfiguration createGetBackupConfiguration() {
        return new GetBackupConfiguration();
    }

    /**
     * Create an instance of {@link CreateUser }
     * 
     */
    public CreateUser createCreateUser() {
        return new CreateUser();
    }

    /**
     * Create an instance of {@link CopyStreamResponse }
     * 
     */
    public CopyStreamResponse createCopyStreamResponse() {
        return new CopyStreamResponse();
    }

    /**
     * Create an instance of {@link GetLastUpdateTimesResponse }
     * 
     */
    public GetLastUpdateTimesResponse createGetLastUpdateTimesResponse() {
        return new GetLastUpdateTimesResponse();
    }

    /**
     * Create an instance of {@link GetLoggingConfiguration }
     * 
     */
    public GetLoggingConfiguration createGetLoggingConfiguration() {
        return new GetLoggingConfiguration();
    }

    /**
     * Create an instance of {@link SetMessageOfTheDayResponse }
     * 
     */
    public SetMessageOfTheDayResponse createSetMessageOfTheDayResponse() {
        return new SetMessageOfTheDayResponse();
    }

    /**
     * Create an instance of {@link DeleteLdapConfigurationResponse }
     * 
     */
    public DeleteLdapConfigurationResponse createDeleteLdapConfigurationResponse() {
        return new DeleteLdapConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetLicenseConfigurationResponse }
     * 
     */
    public GetLicenseConfigurationResponse createGetLicenseConfigurationResponse() {
        return new GetLicenseConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetSnapshotsForStreamResponse }
     * 
     */
    public GetSnapshotsForStreamResponse createGetSnapshotsForStreamResponse() {
        return new GetSnapshotsForStreamResponse();
    }

    /**
     * Create an instance of {@link GetSnapshotsForStream }
     * 
     */
    public GetSnapshotsForStream createGetSnapshotsForStream() {
        return new GetSnapshotsForStream();
    }

    /**
     * Create an instance of {@link DeleteUser }
     * 
     */
    public DeleteUser createDeleteUser() {
        return new DeleteUser();
    }

    /**
     * Create an instance of {@link UpdateTriageStoreResponse }
     * 
     */
    public UpdateTriageStoreResponse createUpdateTriageStoreResponse() {
        return new UpdateTriageStoreResponse();
    }

    /**
     * Create an instance of {@link DeleteStreamResponse }
     * 
     */
    public DeleteStreamResponse createDeleteStreamResponse() {
        return new DeleteStreamResponse();
    }

    /**
     * Create an instance of {@link GetAttributes }
     * 
     */
    public GetAttributes createGetAttributes() {
        return new GetAttributes();
    }

    /**
     * Create an instance of {@link UpdateUser }
     * 
     */
    public UpdateUser createUpdateUser() {
        return new UpdateUser();
    }

    /**
     * Create an instance of {@link GetSnapshotPurgeDetailsResponse }
     * 
     */
    public GetSnapshotPurgeDetailsResponse createGetSnapshotPurgeDetailsResponse() {
        return new GetSnapshotPurgeDetailsResponse();
    }

    /**
     * Create an instance of {@link GetStreams }
     * 
     */
    public GetStreams createGetStreams() {
        return new GetStreams();
    }

    /**
     * Create an instance of {@link GetRoleResponse }
     * 
     */
    public GetRoleResponse createGetRoleResponse() {
        return new GetRoleResponse();
    }

    /**
     * Create an instance of {@link UpdateProject }
     * 
     */
    public UpdateProject createUpdateProject() {
        return new UpdateProject();
    }

    /**
     * Create an instance of {@link UpdateComponentMapResponse }
     * 
     */
    public UpdateComponentMapResponse createUpdateComponentMapResponse() {
        return new UpdateComponentMapResponse();
    }

    /**
     * Create an instance of {@link GetSkeletonizationConfigurationResponse }
     * 
     */
    public GetSkeletonizationConfigurationResponse createGetSkeletonizationConfigurationResponse() {
        return new GetSkeletonizationConfigurationResponse();
    }

    /**
     * Create an instance of {@link DeleteProject }
     * 
     */
    public DeleteProject createDeleteProject() {
        return new DeleteProject();
    }

    /**
     * Create an instance of {@link SetSkeletonizationConfiguration }
     * 
     */
    public SetSkeletonizationConfiguration createSetSkeletonizationConfiguration() {
        return new SetSkeletonizationConfiguration();
    }

    /**
     * Create an instance of {@link MergeTriageStores }
     * 
     */
    public MergeTriageStores createMergeTriageStores() {
        return new MergeTriageStores();
    }

    /**
     * Create an instance of {@link GetAllLdapConfigurationsResponse }
     * 
     */
    public GetAllLdapConfigurationsResponse createGetAllLdapConfigurationsResponse() {
        return new GetAllLdapConfigurationsResponse();
    }

    /**
     * Create an instance of {@link ImportLicenseResponse }
     * 
     */
    public ImportLicenseResponse createImportLicenseResponse() {
        return new ImportLicenseResponse();
    }

    /**
     * Create an instance of {@link GetCheckerNamesResponse }
     * 
     */
    public GetCheckerNamesResponse createGetCheckerNamesResponse() {
        return new GetCheckerNamesResponse();
    }

    /**
     * Create an instance of {@link GetSkeletonizationConfiguration }
     * 
     */
    public GetSkeletonizationConfiguration createGetSkeletonizationConfiguration() {
        return new GetSkeletonizationConfiguration();
    }

    /**
     * Create an instance of {@link GetSignInConfiguration }
     * 
     */
    public GetSignInConfiguration createGetSignInConfiguration() {
        return new GetSignInConfiguration();
    }

    /**
     * Create an instance of {@link CreateRole }
     * 
     */
    public CreateRole createCreateRole() {
        return new CreateRole();
    }

    /**
     * Create an instance of {@link Notify }
     * 
     */
    public Notify createNotify() {
        return new Notify();
    }

    /**
     * Create an instance of {@link SetAcceptingNewCommitsResponse }
     * 
     */
    public SetAcceptingNewCommitsResponse createSetAcceptingNewCommitsResponse() {
        return new SetAcceptingNewCommitsResponse();
    }

    /**
     * Create an instance of {@link UpdateStreamResponse }
     * 
     */
    public UpdateStreamResponse createUpdateStreamResponse() {
        return new UpdateStreamResponse();
    }

    /**
     * Create an instance of {@link UpdateGroup }
     * 
     */
    public UpdateGroup createUpdateGroup() {
        return new UpdateGroup();
    }

    /**
     * Create an instance of {@link CreateStreamInProjectResponse }
     * 
     */
    public CreateStreamInProjectResponse createCreateStreamInProjectResponse() {
        return new CreateStreamInProjectResponse();
    }

    /**
     * Create an instance of {@link SetBackupConfiguration }
     * 
     */
    public SetBackupConfiguration createSetBackupConfiguration() {
        return new SetBackupConfiguration();
    }

    /**
     * Create an instance of {@link GetTriageStoresResponse }
     * 
     */
    public GetTriageStoresResponse createGetTriageStoresResponse() {
        return new GetTriageStoresResponse();
    }

    /**
     * Create an instance of {@link GetArchitectureAnalysisConfigurationResponse }
     * 
     */
    public GetArchitectureAnalysisConfigurationResponse createGetArchitectureAnalysisConfigurationResponse() {
        return new GetArchitectureAnalysisConfigurationResponse();
    }

    /**
     * Create an instance of {@link UpdateLdapConfiguration }
     * 
     */
    public UpdateLdapConfiguration createUpdateLdapConfiguration() {
        return new UpdateLdapConfiguration();
    }

    /**
     * Create an instance of {@link CreateLdapConfigurationResponse }
     * 
     */
    public CreateLdapConfigurationResponse createCreateLdapConfigurationResponse() {
        return new CreateLdapConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetServerTimeResponse }
     * 
     */
    public GetServerTimeResponse createGetServerTimeResponse() {
        return new GetServerTimeResponse();
    }

    /**
     * Create an instance of {@link GetMessageOfTheDayResponse }
     * 
     */
    public GetMessageOfTheDayResponse createGetMessageOfTheDayResponse() {
        return new GetMessageOfTheDayResponse();
    }

    /**
     * Create an instance of {@link GetAttributesResponse }
     * 
     */
    public GetAttributesResponse createGetAttributesResponse() {
        return new GetAttributesResponse();
    }

    /**
     * Create an instance of {@link CreateGroupResponse }
     * 
     */
    public CreateGroupResponse createCreateGroupResponse() {
        return new CreateGroupResponse();
    }

    /**
     * Create an instance of {@link GetComponent }
     * 
     */
    public GetComponent createGetComponent() {
        return new GetComponent();
    }

    /**
     * Create an instance of {@link SetSkeletonizationConfigurationResponse }
     * 
     */
    public SetSkeletonizationConfigurationResponse createSetSkeletonizationConfigurationResponse() {
        return new SetSkeletonizationConfigurationResponse();
    }

    /**
     * Create an instance of {@link GetComponentMaps }
     * 
     */
    public GetComponentMaps createGetComponentMaps() {
        return new GetComponentMaps();
    }

    /**
     * Create an instance of {@link GetUsers }
     * 
     */
    public GetUsers createGetUsers() {
        return new GetUsers();
    }

    /**
     * Create an instance of {@link AttributeValueDataObj }
     * 
     */
    public AttributeValueDataObj createAttributeValueDataObj() {
        return new AttributeValueDataObj();
    }

    /**
     * Create an instance of {@link TriageStoreFilterSpecDataObj }
     * 
     */
    public TriageStoreFilterSpecDataObj createTriageStoreFilterSpecDataObj() {
        return new TriageStoreFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link AttributeDefinitionDataObj }
     * 
     */
    public AttributeDefinitionDataObj createAttributeDefinitionDataObj() {
        return new AttributeDefinitionDataObj();
    }

    /**
     * Create an instance of {@link LicenseSpecDataObj }
     * 
     */
    public LicenseSpecDataObj createLicenseSpecDataObj() {
        return new LicenseSpecDataObj();
    }

    /**
     * Create an instance of {@link SnapshotFilterSpecDataObj }
     * 
     */
    public SnapshotFilterSpecDataObj createSnapshotFilterSpecDataObj() {
        return new SnapshotFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link FeatureUpdateTimeDataObj }
     * 
     */
    public FeatureUpdateTimeDataObj createFeatureUpdateTimeDataObj() {
        return new FeatureUpdateTimeDataObj();
    }

    /**
     * Create an instance of {@link RoleIdDataObj }
     * 
     */
    public RoleIdDataObj createRoleIdDataObj() {
        return new RoleIdDataObj();
    }

    /**
     * Create an instance of {@link TriageStoreSpecDataObj }
     * 
     */
    public TriageStoreSpecDataObj createTriageStoreSpecDataObj() {
        return new TriageStoreSpecDataObj();
    }

    /**
     * Create an instance of {@link UserSpecDataObj }
     * 
     */
    public UserSpecDataObj createUserSpecDataObj() {
        return new UserSpecDataObj();
    }

    /**
     * Create an instance of {@link LocalizedValueDataObj }
     * 
     */
    public LocalizedValueDataObj createLocalizedValueDataObj() {
        return new LocalizedValueDataObj();
    }

    /**
     * Create an instance of {@link ProjectFilterSpecDataObj }
     * 
     */
    public ProjectFilterSpecDataObj createProjectFilterSpecDataObj() {
        return new ProjectFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link LdapConfigurationSpecDataObj }
     * 
     */
    public LdapConfigurationSpecDataObj createLdapConfigurationSpecDataObj() {
        return new LdapConfigurationSpecDataObj();
    }

    /**
     * Create an instance of {@link TriageStoreDataObj }
     * 
     */
    public TriageStoreDataObj createTriageStoreDataObj() {
        return new TriageStoreDataObj();
    }

    /**
     * Create an instance of {@link GroupIdDataObj }
     * 
     */
    public GroupIdDataObj createGroupIdDataObj() {
        return new GroupIdDataObj();
    }

    /**
     * Create an instance of {@link StreamSpecDataObj }
     * 
     */
    public StreamSpecDataObj createStreamSpecDataObj() {
        return new StreamSpecDataObj();
    }

    /**
     * Create an instance of {@link UserFilterSpecDataObj }
     * 
     */
    public UserFilterSpecDataObj createUserFilterSpecDataObj() {
        return new UserFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link LicenseDataObj }
     * 
     */
    public LicenseDataObj createLicenseDataObj() {
        return new LicenseDataObj();
    }

    /**
     * Create an instance of {@link RoleDataObj }
     * 
     */
    public RoleDataObj createRoleDataObj() {
        return new RoleDataObj();
    }

    /**
     * Create an instance of {@link AttributeValueIdDataObj }
     * 
     */
    public AttributeValueIdDataObj createAttributeValueIdDataObj() {
        return new AttributeValueIdDataObj();
    }

    /**
     * Create an instance of {@link GroupDataObj }
     * 
     */
    public GroupDataObj createGroupDataObj() {
        return new GroupDataObj();
    }

    /**
     * Create an instance of {@link LoggingConfigurationDataObj }
     * 
     */
    public LoggingConfigurationDataObj createLoggingConfigurationDataObj() {
        return new LoggingConfigurationDataObj();
    }

    /**
     * Create an instance of {@link GroupSpecDataObj }
     * 
     */
    public GroupSpecDataObj createGroupSpecDataObj() {
        return new GroupSpecDataObj();
    }

    /**
     * Create an instance of {@link UserDataObj }
     * 
     */
    public UserDataObj createUserDataObj() {
        return new UserDataObj();
    }

    /**
     * Create an instance of {@link AttributeDefinitionIdDataObj }
     * 
     */
    public AttributeDefinitionIdDataObj createAttributeDefinitionIdDataObj() {
        return new AttributeDefinitionIdDataObj();
    }

    /**
     * Create an instance of {@link TriageStoreIdDataObj }
     * 
     */
    public TriageStoreIdDataObj createTriageStoreIdDataObj() {
        return new TriageStoreIdDataObj();
    }

    /**
     * Create an instance of {@link ConfigurationDataObj }
     * 
     */
    public ConfigurationDataObj createConfigurationDataObj() {
        return new ConfigurationDataObj();
    }

    /**
     * Create an instance of {@link AttributeDefinitionSpecDataObj }
     * 
     */
    public AttributeDefinitionSpecDataObj createAttributeDefinitionSpecDataObj() {
        return new AttributeDefinitionSpecDataObj();
    }

    /**
     * Create an instance of {@link StreamIdDataObj }
     * 
     */
    public StreamIdDataObj createStreamIdDataObj() {
        return new StreamIdDataObj();
    }

    /**
     * Create an instance of {@link ComponentMapFilterSpecDataObj }
     * 
     */
    public ComponentMapFilterSpecDataObj createComponentMapFilterSpecDataObj() {
        return new ComponentMapFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link SkeletonizationConfigurationDataObj }
     * 
     */
    public SkeletonizationConfigurationDataObj createSkeletonizationConfigurationDataObj() {
        return new SkeletonizationConfigurationDataObj();
    }

    /**
     * Create an instance of {@link CommitStateDataObj }
     * 
     */
    public CommitStateDataObj createCommitStateDataObj() {
        return new CommitStateDataObj();
    }

    /**
     * Create an instance of {@link ProjectSpecDataObj }
     * 
     */
    public ProjectSpecDataObj createProjectSpecDataObj() {
        return new ProjectSpecDataObj();
    }

    /**
     * Create an instance of {@link BackupConfigurationDataObj }
     * 
     */
    public BackupConfigurationDataObj createBackupConfigurationDataObj() {
        return new BackupConfigurationDataObj();
    }

    /**
     * Create an instance of {@link ComponentDataObj }
     * 
     */
    public ComponentDataObj createComponentDataObj() {
        return new ComponentDataObj();
    }

    /**
     * Create an instance of {@link LdapConfigurationDataObj }
     * 
     */
    public LdapConfigurationDataObj createLdapConfigurationDataObj() {
        return new LdapConfigurationDataObj();
    }

    /**
     * Create an instance of {@link UsersPageDataObj }
     * 
     */
    public UsersPageDataObj createUsersPageDataObj() {
        return new UsersPageDataObj();
    }

    /**
     * Create an instance of {@link RoleAssignmentDataObj }
     * 
     */
    public RoleAssignmentDataObj createRoleAssignmentDataObj() {
        return new RoleAssignmentDataObj();
    }

    /**
     * Create an instance of {@link ProjectDataObj }
     * 
     */
    public ProjectDataObj createProjectDataObj() {
        return new ProjectDataObj();
    }

    /**
     * Create an instance of {@link SnapshotInfoDataObj }
     * 
     */
    public SnapshotInfoDataObj createSnapshotInfoDataObj() {
        return new SnapshotInfoDataObj();
    }

    /**
     * Create an instance of {@link PermissionDataObj }
     * 
     */
    public PermissionDataObj createPermissionDataObj() {
        return new PermissionDataObj();
    }

    /**
     * Create an instance of {@link GroupsPageDataObj }
     * 
     */
    public GroupsPageDataObj createGroupsPageDataObj() {
        return new GroupsPageDataObj();
    }

    /**
     * Create an instance of {@link ServerDomainIdDataObj }
     * 
     */
    public ServerDomainIdDataObj createServerDomainIdDataObj() {
        return new ServerDomainIdDataObj();
    }

    /**
     * Create an instance of {@link AttributeValueSpecDataObj }
     * 
     */
    public AttributeValueSpecDataObj createAttributeValueSpecDataObj() {
        return new AttributeValueSpecDataObj();
    }

    /**
     * Create an instance of {@link ComponentMapIdDataObj }
     * 
     */
    public ComponentMapIdDataObj createComponentMapIdDataObj() {
        return new ComponentMapIdDataObj();
    }

    /**
     * Create an instance of {@link RoleSpecDataObj }
     * 
     */
    public RoleSpecDataObj createRoleSpecDataObj() {
        return new RoleSpecDataObj();
    }

    /**
     * Create an instance of {@link ComponentPathRuleDataObj }
     * 
     */
    public ComponentPathRuleDataObj createComponentPathRuleDataObj() {
        return new ComponentPathRuleDataObj();
    }

    /**
     * Create an instance of {@link ComponentMapSpecDataObj }
     * 
     */
    public ComponentMapSpecDataObj createComponentMapSpecDataObj() {
        return new ComponentMapSpecDataObj();
    }

    /**
     * Create an instance of {@link ComponentDefectRuleDataObj }
     * 
     */
    public ComponentDefectRuleDataObj createComponentDefectRuleDataObj() {
        return new ComponentDefectRuleDataObj();
    }

    /**
     * Create an instance of {@link DeleteSnapshotJobInfoDataObj }
     * 
     */
    public DeleteSnapshotJobInfoDataObj createDeleteSnapshotJobInfoDataObj() {
        return new DeleteSnapshotJobInfoDataObj();
    }

    /**
     * Create an instance of {@link AttributeValueChangeSpecDataObj }
     * 
     */
    public AttributeValueChangeSpecDataObj createAttributeValueChangeSpecDataObj() {
        return new AttributeValueChangeSpecDataObj();
    }

    /**
     * Create an instance of {@link ProjectIdDataObj }
     * 
     */
    public ProjectIdDataObj createProjectIdDataObj() {
        return new ProjectIdDataObj();
    }

    /**
     * Create an instance of {@link GroupFilterSpecDataObj }
     * 
     */
    public GroupFilterSpecDataObj createGroupFilterSpecDataObj() {
        return new GroupFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link StreamDataObj }
     * 
     */
    public StreamDataObj createStreamDataObj() {
        return new StreamDataObj();
    }

    /**
     * Create an instance of {@link SignInSettingsDataObj }
     * 
     */
    public SignInSettingsDataObj createSignInSettingsDataObj() {
        return new SignInSettingsDataObj();
    }

    /**
     * Create an instance of {@link StreamFilterSpecDataObj }
     * 
     */
    public StreamFilterSpecDataObj createStreamFilterSpecDataObj() {
        return new StreamFilterSpecDataObj();
    }

    /**
     * Create an instance of {@link ComponentMapDataObj }
     * 
     */
    public ComponentMapDataObj createComponentMapDataObj() {
        return new ComponentMapDataObj();
    }

    /**
     * Create an instance of {@link SnapshotIdDataObj }
     * 
     */
    public SnapshotIdDataObj createSnapshotIdDataObj() {
        return new SnapshotIdDataObj();
    }

    /**
     * Create an instance of {@link PageSpecDataObj }
     * 
     */
    public PageSpecDataObj createPageSpecDataObj() {
        return new PageSpecDataObj();
    }

    /**
     * Create an instance of {@link LicenseStateDataObj }
     * 
     */
    public LicenseStateDataObj createLicenseStateDataObj() {
        return new LicenseStateDataObj();
    }

    /**
     * Create an instance of {@link SnapshotPurgeDetailsObj }
     * 
     */
    public SnapshotPurgeDetailsObj createSnapshotPurgeDetailsObj() {
        return new SnapshotPurgeDetailsObj();
    }

    /**
     * Create an instance of {@link ComponentIdDataObj }
     * 
     */
    public ComponentIdDataObj createComponentIdDataObj() {
        return new ComponentIdDataObj();
    }

    /**
     * Create an instance of {@link VersionDataObj }
     * 
     */
    public VersionDataObj createVersionDataObj() {
        return new VersionDataObj();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTriageStore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createTriageStore")
    public JAXBElement<CreateTriageStore> createCreateTriageStore(CreateTriageStore value) {
        return new JAXBElement<CreateTriageStore>(_CreateTriageStore_QNAME, CreateTriageStore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "notifyResponse")
    public JAXBElement<NotifyResponse> createNotifyResponse(NotifyResponse value) {
        return new JAXBElement<NotifyResponse>(_NotifyResponse_QNAME, NotifyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStreamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createStreamResponse")
    public JAXBElement<CreateStreamResponse> createCreateStreamResponse(CreateStreamResponse value) {
        return new JAXBElement<CreateStreamResponse>(_CreateStreamResponse_QNAME, CreateStreamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CovRemoteServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "CoverityFault")
    public JAXBElement<CovRemoteServiceException> createCoverityFault(CovRemoteServiceException value) {
        return new JAXBElement<CovRemoteServiceException>(_CoverityFault_QNAME, CovRemoteServiceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateRoleResponse")
    public JAXBElement<UpdateRoleResponse> createUpdateRoleResponse(UpdateRoleResponse value) {
        return new JAXBElement<UpdateRoleResponse>(_UpdateRoleResponse_QNAME, UpdateRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServerTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getServerTime")
    public JAXBElement<GetServerTime> createGetServerTime(GetServerTime value) {
        return new JAXBElement<GetServerTime>(_GetServerTime_QNAME, GetServerTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLoggingConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLoggingConfigurationResponse")
    public JAXBElement<GetLoggingConfigurationResponse> createGetLoggingConfigurationResponse(GetLoggingConfigurationResponse value) {
        return new JAXBElement<GetLoggingConfigurationResponse>(_GetLoggingConfigurationResponse_QNAME, GetLoggingConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBackupConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getBackupConfigurationResponse")
    public JAXBElement<GetBackupConfigurationResponse> createGetBackupConfigurationResponse(GetBackupConfigurationResponse value) {
        return new JAXBElement<GetBackupConfigurationResponse>(_GetBackupConfigurationResponse_QNAME, GetBackupConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTypeNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getTypeNamesResponse")
    public JAXBElement<GetTypeNamesResponse> createGetTypeNamesResponse(GetTypeNamesResponse value) {
        return new JAXBElement<GetTypeNamesResponse>(_GetTypeNamesResponse_QNAME, GetTypeNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSystemConfigResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSystemConfigResponse")
    public JAXBElement<GetSystemConfigResponse> createGetSystemConfigResponse(GetSystemConfigResponse value) {
        return new JAXBElement<GetSystemConfigResponse>(_GetSystemConfigResponse_QNAME, GetSystemConfigResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllRolesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAllRolesResponse")
    public JAXBElement<GetAllRolesResponse> createGetAllRolesResponse(GetAllRolesResponse value) {
        return new JAXBElement<GetAllRolesResponse>(_GetAllRolesResponse_QNAME, GetAllRolesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyStream }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "copyStream")
    public JAXBElement<CopyStream> createCopyStream(CopyStream value) {
        return new JAXBElement<CopyStream>(_CopyStream_QNAME, CopyStream.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGroups }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getGroups")
    public JAXBElement<GetGroups> createGetGroups(GetGroups value) {
        return new JAXBElement<GetGroups>(_GetGroups_QNAME, GetGroups.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCategoryNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getCategoryNames")
    public JAXBElement<GetCategoryNames> createGetCategoryNames(GetCategoryNames value) {
        return new JAXBElement<GetCategoryNames>(_GetCategoryNames_QNAME, GetCategoryNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateStream }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateStream")
    public JAXBElement<UpdateStream> createUpdateStream(UpdateStream value) {
        return new JAXBElement<UpdateStream>(_UpdateStream_QNAME, UpdateStream.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getProjects")
    public JAXBElement<GetProjects> createGetProjects(GetProjects value) {
        return new JAXBElement<GetProjects>(_GetProjects_QNAME, GetProjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTriageStores }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getTriageStores")
    public JAXBElement<GetTriageStores> createGetTriageStores(GetTriageStores value) {
        return new JAXBElement<GetTriageStores>(_GetTriageStores_QNAME, GetTriageStores.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getGroup")
    public JAXBElement<GetGroup> createGetGroup(GetGroup value) {
        return new JAXBElement<GetGroup>(_GetGroup_QNAME, GetGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSnapshotPurgeDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setSnapshotPurgeDetails")
    public JAXBElement<SetSnapshotPurgeDetails> createSetSnapshotPurgeDetails(SetSnapshotPurgeDetails value) {
        return new JAXBElement<SetSnapshotPurgeDetails>(_SetSnapshotPurgeDetails_QNAME, SetSnapshotPurgeDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSnapshotInformationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSnapshotInformationResponse")
    public JAXBElement<GetSnapshotInformationResponse> createGetSnapshotInformationResponse(GetSnapshotInformationResponse value) {
        return new JAXBElement<GetSnapshotInformationResponse>(_GetSnapshotInformationResponse_QNAME, GetSnapshotInformationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getUserResponse")
    public JAXBElement<GetUserResponse> createGetUserResponse(GetUserResponse value) {
        return new JAXBElement<GetUserResponse>(_GetUserResponse_QNAME, GetUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCommitStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getCommitStateResponse")
    public JAXBElement<GetCommitStateResponse> createGetCommitStateResponse(GetCommitStateResponse value) {
        return new JAXBElement<GetCommitStateResponse>(_GetCommitStateResponse_QNAME, GetCommitStateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTriageStore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateTriageStore")
    public JAXBElement<UpdateTriageStore> createUpdateTriageStore(UpdateTriageStore value) {
        return new JAXBElement<UpdateTriageStore>(_UpdateTriageStore_QNAME, UpdateTriageStore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckerNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getCheckerNames")
    public JAXBElement<GetCheckerNames> createGetCheckerNames(GetCheckerNames value) {
        return new JAXBElement<GetCheckerNames>(_GetCheckerNames_QNAME, GetCheckerNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createGroup")
    public JAXBElement<CreateGroup> createCreateGroup(CreateGroup value) {
        return new JAXBElement<CreateGroup>(_CreateGroup_QNAME, CreateGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateAttributeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateAttributeResponse")
    public JAXBElement<UpdateAttributeResponse> createUpdateAttributeResponse(UpdateAttributeResponse value) {
        return new JAXBElement<UpdateAttributeResponse>(_UpdateAttributeResponse_QNAME, UpdateAttributeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createAttribute")
    public JAXBElement<CreateAttribute> createCreateAttribute(CreateAttribute value) {
        return new JAXBElement<CreateAttribute>(_CreateAttribute_QNAME, CreateAttribute.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createUserResponse")
    public JAXBElement<CreateUserResponse> createCreateUserResponse(CreateUserResponse value) {
        return new JAXBElement<CreateUserResponse>(_CreateUserResponse_QNAME, CreateUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCommitState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getCommitState")
    public JAXBElement<GetCommitState> createGetCommitState(GetCommitState value) {
        return new JAXBElement<GetCommitState>(_GetCommitState_QNAME, GetCommitState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAttribute")
    public JAXBElement<GetAttribute> createGetAttribute(GetAttribute value) {
        return new JAXBElement<GetAttribute>(_GetAttribute_QNAME, GetAttribute.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTriageStoreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createTriageStoreResponse")
    public JAXBElement<CreateTriageStoreResponse> createCreateTriageStoreResponse(CreateTriageStoreResponse value) {
        return new JAXBElement<CreateTriageStoreResponse>(_CreateTriageStoreResponse_QNAME, CreateTriageStoreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSignInConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateSignInConfiguration")
    public JAXBElement<UpdateSignInConfiguration> createUpdateSignInConfiguration(UpdateSignInConfiguration value) {
        return new JAXBElement<UpdateSignInConfiguration>(_UpdateSignInConfiguration_QNAME, UpdateSignInConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetBackupConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setBackupConfigurationResponse")
    public JAXBElement<SetBackupConfigurationResponse> createSetBackupConfigurationResponse(SetBackupConfigurationResponse value) {
        return new JAXBElement<SetBackupConfigurationResponse>(_SetBackupConfigurationResponse_QNAME, SetBackupConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSignInConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateSignInConfigurationResponse")
    public JAXBElement<UpdateSignInConfigurationResponse> createUpdateSignInConfigurationResponse(UpdateSignInConfigurationResponse value) {
        return new JAXBElement<UpdateSignInConfigurationResponse>(_UpdateSignInConfigurationResponse_QNAME, UpdateSignInConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStreamInProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createStreamInProject")
    public JAXBElement<CreateStreamInProject> createCreateStreamInProject(CreateStreamInProject value) {
        return new JAXBElement<CreateStreamInProject>(_CreateStreamInProject_QNAME, CreateStreamInProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTypeNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getTypeNames")
    public JAXBElement<GetTypeNames> createGetTypeNames(GetTypeNames value) {
        return new JAXBElement<GetTypeNames>(_GetTypeNames_QNAME, GetTypeNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateGroupResponse")
    public JAXBElement<UpdateGroupResponse> createUpdateGroupResponse(UpdateGroupResponse value) {
        return new JAXBElement<UpdateGroupResponse>(_UpdateGroupResponse_QNAME, UpdateGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTriageStore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteTriageStore")
    public JAXBElement<DeleteTriageStore> createDeleteTriageStore(DeleteTriageStore value) {
        return new JAXBElement<DeleteTriageStore>(_DeleteTriageStore_QNAME, DeleteTriageStore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSnapshotResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteSnapshotResponse")
    public JAXBElement<DeleteSnapshotResponse> createDeleteSnapshotResponse(DeleteSnapshotResponse value) {
        return new JAXBElement<DeleteSnapshotResponse>(_DeleteSnapshotResponse_QNAME, DeleteSnapshotResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSignInConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSignInConfigurationResponse")
    public JAXBElement<GetSignInConfigurationResponse> createGetSignInConfigurationResponse(GetSignInConfigurationResponse value) {
        return new JAXBElement<GetSignInConfigurationResponse>(_GetSignInConfigurationResponse_QNAME, GetSignInConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAttributeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteAttributeResponse")
    public JAXBElement<DeleteAttributeResponse> createDeleteAttributeResponse(DeleteAttributeResponse value) {
        return new JAXBElement<DeleteAttributeResponse>(_DeleteAttributeResponse_QNAME, DeleteAttributeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createRoleResponse")
    public JAXBElement<CreateRoleResponse> createCreateRoleResponse(CreateRoleResponse value) {
        return new JAXBElement<CreateRoleResponse>(_CreateRoleResponse_QNAME, CreateRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteNotificationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "executeNotificationResponse")
    public JAXBElement<ExecuteNotificationResponse> createExecuteNotificationResponse(ExecuteNotificationResponse value) {
        return new JAXBElement<ExecuteNotificationResponse>(_ExecuteNotificationResponse_QNAME, ExecuteNotificationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateUserResponse")
    public JAXBElement<UpdateUserResponse> createUpdateUserResponse(UpdateUserResponse value) {
        return new JAXBElement<UpdateUserResponse>(_UpdateUserResponse_QNAME, UpdateUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getGroupResponse")
    public JAXBElement<GetGroupResponse> createGetGroupResponse(GetGroupResponse value) {
        return new JAXBElement<GetGroupResponse>(_GetGroupResponse_QNAME, GetGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateAttribute")
    public JAXBElement<UpdateAttribute> createUpdateAttribute(UpdateAttribute value) {
        return new JAXBElement<UpdateAttribute>(_UpdateAttribute_QNAME, UpdateAttribute.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeleteSnapshotJobInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getDeleteSnapshotJobInfoResponse")
    public JAXBElement<GetDeleteSnapshotJobInfoResponse> createGetDeleteSnapshotJobInfoResponse(GetDeleteSnapshotJobInfoResponse value) {
        return new JAXBElement<GetDeleteSnapshotJobInfoResponse>(_GetDeleteSnapshotJobInfoResponse_QNAME, GetDeleteSnapshotJobInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteComponentMapResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteComponentMapResponse")
    public JAXBElement<DeleteComponentMapResponse> createDeleteComponentMapResponse(DeleteComponentMapResponse value) {
        return new JAXBElement<DeleteComponentMapResponse>(_DeleteComponentMapResponse_QNAME, DeleteComponentMapResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLicenseConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLicenseConfiguration")
    public JAXBElement<GetLicenseConfiguration> createGetLicenseConfiguration(GetLicenseConfiguration value) {
        return new JAXBElement<GetLicenseConfiguration>(_GetLicenseConfiguration_QNAME, GetLicenseConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLicenseState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLicenseState")
    public JAXBElement<GetLicenseState> createGetLicenseState(GetLicenseState value) {
        return new JAXBElement<GetLicenseState>(_GetLicenseState_QNAME, GetLicenseState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetArchitectureAnalysisConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setArchitectureAnalysisConfigurationResponse")
    public JAXBElement<SetArchitectureAnalysisConfigurationResponse> createSetArchitectureAnalysisConfigurationResponse(SetArchitectureAnalysisConfigurationResponse value) {
        return new JAXBElement<SetArchitectureAnalysisConfigurationResponse>(_SetArchitectureAnalysisConfigurationResponse_QNAME, SetArchitectureAnalysisConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteNotification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "executeNotification")
    public JAXBElement<ExecuteNotification> createExecuteNotification(ExecuteNotification value) {
        return new JAXBElement<ExecuteNotification>(_ExecuteNotification_QNAME, ExecuteNotification.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetLoggingConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setLoggingConfiguration")
    public JAXBElement<SetLoggingConfiguration> createSetLoggingConfiguration(SetLoggingConfiguration value) {
        return new JAXBElement<SetLoggingConfiguration>(_SetLoggingConfiguration_QNAME, SetLoggingConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getStreamsResponse")
    public JAXBElement<GetStreamsResponse> createGetStreamsResponse(GetStreamsResponse value) {
        return new JAXBElement<GetStreamsResponse>(_GetStreamsResponse_QNAME, GetStreamsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllPermissionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAllPermissionsResponse")
    public JAXBElement<GetAllPermissionsResponse> createGetAllPermissionsResponse(GetAllPermissionsResponse value) {
        return new JAXBElement<GetAllPermissionsResponse>(_GetAllPermissionsResponse_QNAME, GetAllPermissionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteRole")
    public JAXBElement<DeleteRole> createDeleteRole(DeleteRole value) {
        return new JAXBElement<DeleteRole>(_DeleteRole_QNAME, DeleteRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttributeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAttributeResponse")
    public JAXBElement<GetAttributeResponse> createGetAttributeResponse(GetAttributeResponse value) {
        return new JAXBElement<GetAttributeResponse>(_GetAttributeResponse_QNAME, GetAttributeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createProject")
    public JAXBElement<CreateProject> createCreateProject(CreateProject value) {
        return new JAXBElement<CreateProject>(_CreateProject_QNAME, CreateProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getUser")
    public JAXBElement<GetUser> createGetUser(GetUser value) {
        return new JAXBElement<GetUser>(_GetUser_QNAME, GetUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSnapshot }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteSnapshot")
    public JAXBElement<DeleteSnapshot> createDeleteSnapshot(DeleteSnapshot value) {
        return new JAXBElement<DeleteSnapshot>(_DeleteSnapshot_QNAME, DeleteSnapshot.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createProjectResponse")
    public JAXBElement<CreateProjectResponse> createCreateProjectResponse(CreateProjectResponse value) {
        return new JAXBElement<CreateProjectResponse>(_CreateProjectResponse_QNAME, CreateProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLicenseStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLicenseStateResponse")
    public JAXBElement<GetLicenseStateResponse> createGetLicenseStateResponse(GetLicenseStateResponse value) {
        return new JAXBElement<GetLicenseStateResponse>(_GetLicenseStateResponse_QNAME, GetLicenseStateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteGroup")
    public JAXBElement<DeleteGroup> createDeleteGroup(DeleteGroup value) {
        return new JAXBElement<DeleteGroup>(_DeleteGroup_QNAME, DeleteGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateLdapConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createLdapConfiguration")
    public JAXBElement<CreateLdapConfiguration> createCreateLdapConfiguration(CreateLdapConfiguration value) {
        return new JAXBElement<CreateLdapConfiguration>(_CreateLdapConfiguration_QNAME, CreateLdapConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteStream }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteStream")
    public JAXBElement<DeleteStream> createDeleteStream(DeleteStream value) {
        return new JAXBElement<DeleteStream>(_DeleteStream_QNAME, DeleteStream.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetArchitectureAnalysisConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getArchitectureAnalysisConfiguration")
    public JAXBElement<GetArchitectureAnalysisConfiguration> createGetArchitectureAnalysisConfiguration(GetArchitectureAnalysisConfiguration value) {
        return new JAXBElement<GetArchitectureAnalysisConfiguration>(_GetArchitectureAnalysisConfiguration_QNAME, GetArchitectureAnalysisConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAttributeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createAttributeResponse")
    public JAXBElement<CreateAttributeResponse> createCreateAttributeResponse(CreateAttributeResponse value) {
        return new JAXBElement<CreateAttributeResponse>(_CreateAttributeResponse_QNAME, CreateAttributeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteUserResponse")
    public JAXBElement<DeleteUserResponse> createDeleteUserResponse(DeleteUserResponse value) {
        return new JAXBElement<DeleteUserResponse>(_DeleteUserResponse_QNAME, DeleteUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetLoggingConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setLoggingConfigurationResponse")
    public JAXBElement<SetLoggingConfigurationResponse> createSetLoggingConfigurationResponse(SetLoggingConfigurationResponse value) {
        return new JAXBElement<SetLoggingConfigurationResponse>(_SetLoggingConfigurationResponse_QNAME, SetLoggingConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeveloperStreamsProjectsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getDeveloperStreamsProjectsResponse")
    public JAXBElement<GetDeveloperStreamsProjectsResponse> createGetDeveloperStreamsProjectsResponse(GetDeveloperStreamsProjectsResponse value) {
        return new JAXBElement<GetDeveloperStreamsProjectsResponse>(_GetDeveloperStreamsProjectsResponse_QNAME, GetDeveloperStreamsProjectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getComponentResponse")
    public JAXBElement<GetComponentResponse> createGetComponentResponse(GetComponentResponse value) {
        return new JAXBElement<GetComponentResponse>(_GetComponentResponse_QNAME, GetComponentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getVersion")
    public JAXBElement<GetVersion> createGetVersion(GetVersion value) {
        return new JAXBElement<GetVersion>(_GetVersion_QNAME, GetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllLdapConfigurations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAllLdapConfigurations")
    public JAXBElement<GetAllLdapConfigurations> createGetAllLdapConfigurations(GetAllLdapConfigurations value) {
        return new JAXBElement<GetAllLdapConfigurations>(_GetAllLdapConfigurations_QNAME, GetAllLdapConfigurations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getRole")
    public JAXBElement<GetRole> createGetRole(GetRole value) {
        return new JAXBElement<GetRole>(_GetRole_QNAME, GetRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLdapServerDomains }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLdapServerDomains")
    public JAXBElement<GetLdapServerDomains> createGetLdapServerDomains(GetLdapServerDomains value) {
        return new JAXBElement<GetLdapServerDomains>(_GetLdapServerDomains_QNAME, GetLdapServerDomains.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLdapServerDomainsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLdapServerDomainsResponse")
    public JAXBElement<GetLdapServerDomainsResponse> createGetLdapServerDomainsResponse(GetLdapServerDomainsResponse value) {
        return new JAXBElement<GetLdapServerDomainsResponse>(_GetLdapServerDomainsResponse_QNAME, GetLdapServerDomainsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetAcceptingNewCommits }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setAcceptingNewCommits")
    public JAXBElement<SetAcceptingNewCommits> createSetAcceptingNewCommits(SetAcceptingNewCommits value) {
        return new JAXBElement<SetAcceptingNewCommits>(_SetAcceptingNewCommits_QNAME, SetAcceptingNewCommits.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateProjectResponse")
    public JAXBElement<UpdateProjectResponse> createUpdateProjectResponse(UpdateProjectResponse value) {
        return new JAXBElement<UpdateProjectResponse>(_UpdateProjectResponse_QNAME, UpdateProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDefectStatusesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getDefectStatusesResponse")
    public JAXBElement<GetDefectStatusesResponse> createGetDefectStatusesResponse(GetDefectStatusesResponse value) {
        return new JAXBElement<GetDefectStatusesResponse>(_GetDefectStatusesResponse_QNAME, GetDefectStatusesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSnapshotPurgeDetailsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setSnapshotPurgeDetailsResponse")
    public JAXBElement<SetSnapshotPurgeDetailsResponse> createSetSnapshotPurgeDetailsResponse(SetSnapshotPurgeDetailsResponse value) {
        return new JAXBElement<SetSnapshotPurgeDetailsResponse>(_SetSnapshotPurgeDetailsResponse_QNAME, SetSnapshotPurgeDetailsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteGroupResponse")
    public JAXBElement<DeleteGroupResponse> createDeleteGroupResponse(DeleteGroupResponse value) {
        return new JAXBElement<DeleteGroupResponse>(_DeleteGroupResponse_QNAME, DeleteGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDefectStatuses }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getDefectStatuses")
    public JAXBElement<GetDefectStatuses> createGetDefectStatuses(GetDefectStatuses value) {
        return new JAXBElement<GetDefectStatuses>(_GetDefectStatuses_QNAME, GetDefectStatuses.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getUsersResponse")
    public JAXBElement<GetUsersResponse> createGetUsersResponse(GetUsersResponse value) {
        return new JAXBElement<GetUsersResponse>(_GetUsersResponse_QNAME, GetUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateComponentMap }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateComponentMap")
    public JAXBElement<UpdateComponentMap> createUpdateComponentMap(UpdateComponentMap value) {
        return new JAXBElement<UpdateComponentMap>(_UpdateComponentMap_QNAME, UpdateComponentMap.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageOfTheDay }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getMessageOfTheDay")
    public JAXBElement<GetMessageOfTheDay> createGetMessageOfTheDay(GetMessageOfTheDay value) {
        return new JAXBElement<GetMessageOfTheDay>(_GetMessageOfTheDay_QNAME, GetMessageOfTheDay.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeTriageStoresResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "mergeTriageStoresResponse")
    public JAXBElement<MergeTriageStoresResponse> createMergeTriageStoresResponse(MergeTriageStoresResponse value) {
        return new JAXBElement<MergeTriageStoresResponse>(_MergeTriageStoresResponse_QNAME, MergeTriageStoresResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteUser")
    public JAXBElement<DeleteUser> createDeleteUser(DeleteUser value) {
        return new JAXBElement<DeleteUser>(_DeleteUser_QNAME, DeleteUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSnapshotsForStream }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSnapshotsForStream")
    public JAXBElement<GetSnapshotsForStream> createGetSnapshotsForStream(GetSnapshotsForStream value) {
        return new JAXBElement<GetSnapshotsForStream>(_GetSnapshotsForStream_QNAME, GetSnapshotsForStream.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSnapshotsForStreamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSnapshotsForStreamResponse")
    public JAXBElement<GetSnapshotsForStreamResponse> createGetSnapshotsForStreamResponse(GetSnapshotsForStreamResponse value) {
        return new JAXBElement<GetSnapshotsForStreamResponse>(_GetSnapshotsForStreamResponse_QNAME, GetSnapshotsForStreamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLicenseConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLicenseConfigurationResponse")
    public JAXBElement<GetLicenseConfigurationResponse> createGetLicenseConfigurationResponse(GetLicenseConfigurationResponse value) {
        return new JAXBElement<GetLicenseConfigurationResponse>(_GetLicenseConfigurationResponse_QNAME, GetLicenseConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteLdapConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteLdapConfigurationResponse")
    public JAXBElement<DeleteLdapConfigurationResponse> createDeleteLdapConfigurationResponse(DeleteLdapConfigurationResponse value) {
        return new JAXBElement<DeleteLdapConfigurationResponse>(_DeleteLdapConfigurationResponse_QNAME, DeleteLdapConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMessageOfTheDayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setMessageOfTheDayResponse")
    public JAXBElement<SetMessageOfTheDayResponse> createSetMessageOfTheDayResponse(SetMessageOfTheDayResponse value) {
        return new JAXBElement<SetMessageOfTheDayResponse>(_SetMessageOfTheDayResponse_QNAME, SetMessageOfTheDayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTriageStoreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateTriageStoreResponse")
    public JAXBElement<UpdateTriageStoreResponse> createUpdateTriageStoreResponse(UpdateTriageStoreResponse value) {
        return new JAXBElement<UpdateTriageStoreResponse>(_UpdateTriageStoreResponse_QNAME, UpdateTriageStoreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSnapshotPurgeDetailsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSnapshotPurgeDetailsResponse")
    public JAXBElement<GetSnapshotPurgeDetailsResponse> createGetSnapshotPurgeDetailsResponse(GetSnapshotPurgeDetailsResponse value) {
        return new JAXBElement<GetSnapshotPurgeDetailsResponse>(_GetSnapshotPurgeDetailsResponse_QNAME, GetSnapshotPurgeDetailsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateUser")
    public JAXBElement<UpdateUser> createUpdateUser(UpdateUser value) {
        return new JAXBElement<UpdateUser>(_UpdateUser_QNAME, UpdateUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttributes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAttributes")
    public JAXBElement<GetAttributes> createGetAttributes(GetAttributes value) {
        return new JAXBElement<GetAttributes>(_GetAttributes_QNAME, GetAttributes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteStreamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteStreamResponse")
    public JAXBElement<DeleteStreamResponse> createDeleteStreamResponse(DeleteStreamResponse value) {
        return new JAXBElement<DeleteStreamResponse>(_DeleteStreamResponse_QNAME, DeleteStreamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBackupConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getBackupConfiguration")
    public JAXBElement<GetBackupConfiguration> createGetBackupConfiguration(GetBackupConfiguration value) {
        return new JAXBElement<GetBackupConfiguration>(_GetBackupConfiguration_QNAME, GetBackupConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateComponentMapResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createComponentMapResponse")
    public JAXBElement<CreateComponentMapResponse> createCreateComponentMapResponse(CreateComponentMapResponse value) {
        return new JAXBElement<CreateComponentMapResponse>(_CreateComponentMapResponse_QNAME, CreateComponentMapResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyStreamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "copyStreamResponse")
    public JAXBElement<CopyStreamResponse> createCopyStreamResponse(CopyStreamResponse value) {
        return new JAXBElement<CopyStreamResponse>(_CopyStreamResponse_QNAME, CopyStreamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createUser")
    public JAXBElement<CreateUser> createCreateUser(CreateUser value) {
        return new JAXBElement<CreateUser>(_CreateUser_QNAME, CreateUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLoggingConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLoggingConfiguration")
    public JAXBElement<GetLoggingConfiguration> createGetLoggingConfiguration(GetLoggingConfiguration value) {
        return new JAXBElement<GetLoggingConfiguration>(_GetLoggingConfiguration_QNAME, GetLoggingConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLastUpdateTimesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLastUpdateTimesResponse")
    public JAXBElement<GetLastUpdateTimesResponse> createGetLastUpdateTimesResponse(GetLastUpdateTimesResponse value) {
        return new JAXBElement<GetLastUpdateTimesResponse>(_GetLastUpdateTimesResponse_QNAME, GetLastUpdateTimesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSnapshotPurgeDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSnapshotPurgeDetails")
    public JAXBElement<GetSnapshotPurgeDetails> createGetSnapshotPurgeDetails(GetSnapshotPurgeDetails value) {
        return new JAXBElement<GetSnapshotPurgeDetails>(_GetSnapshotPurgeDetails_QNAME, GetSnapshotPurgeDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteAttribute")
    public JAXBElement<DeleteAttribute> createDeleteAttribute(DeleteAttribute value) {
        return new JAXBElement<DeleteAttribute>(_DeleteAttribute_QNAME, DeleteAttribute.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetGroupsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getGroupsResponse")
    public JAXBElement<GetGroupsResponse> createGetGroupsResponse(GetGroupsResponse value) {
        return new JAXBElement<GetGroupsResponse>(_GetGroupsResponse_QNAME, GetGroupsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetArchitectureAnalysisConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setArchitectureAnalysisConfiguration")
    public JAXBElement<SetArchitectureAnalysisConfiguration> createSetArchitectureAnalysisConfiguration(SetArchitectureAnalysisConfiguration value) {
        return new JAXBElement<SetArchitectureAnalysisConfiguration>(_SetArchitectureAnalysisConfiguration_QNAME, SetArchitectureAnalysisConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateRole")
    public JAXBElement<UpdateRole> createUpdateRole(UpdateRole value) {
        return new JAXBElement<UpdateRole>(_UpdateRole_QNAME, UpdateRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSnapshotInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSnapshotInformation")
    public JAXBElement<GetSnapshotInformation> createGetSnapshotInformation(GetSnapshotInformation value) {
        return new JAXBElement<GetSnapshotInformation>(_GetSnapshotInformation_QNAME, GetSnapshotInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteRoleResponse")
    public JAXBElement<DeleteRoleResponse> createDeleteRoleResponse(DeleteRoleResponse value) {
        return new JAXBElement<DeleteRoleResponse>(_DeleteRoleResponse_QNAME, DeleteRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllRoles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAllRoles")
    public JAXBElement<GetAllRoles> createGetAllRoles(GetAllRoles value) {
        return new JAXBElement<GetAllRoles>(_GetAllRoles_QNAME, GetAllRoles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateLdapConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateLdapConfigurationResponse")
    public JAXBElement<UpdateLdapConfigurationResponse> createUpdateLdapConfigurationResponse(UpdateLdapConfigurationResponse value) {
        return new JAXBElement<UpdateLdapConfigurationResponse>(_UpdateLdapConfigurationResponse_QNAME, UpdateLdapConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteComponentMap }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteComponentMap")
    public JAXBElement<DeleteComponentMap> createDeleteComponentMap(DeleteComponentMap value) {
        return new JAXBElement<DeleteComponentMap>(_DeleteComponentMap_QNAME, DeleteComponentMap.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteProjectResponse")
    public JAXBElement<DeleteProjectResponse> createDeleteProjectResponse(DeleteProjectResponse value) {
        return new JAXBElement<DeleteProjectResponse>(_DeleteProjectResponse_QNAME, DeleteProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeveloperStreamsProjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getDeveloperStreamsProjects")
    public JAXBElement<GetDeveloperStreamsProjects> createGetDeveloperStreamsProjects(GetDeveloperStreamsProjects value) {
        return new JAXBElement<GetDeveloperStreamsProjects>(_GetDeveloperStreamsProjects_QNAME, GetDeveloperStreamsProjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateComponentMap }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createComponentMap")
    public JAXBElement<CreateComponentMap> createCreateComponentMap(CreateComponentMap value) {
        return new JAXBElement<CreateComponentMap>(_CreateComponentMap_QNAME, CreateComponentMap.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportLicense }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "importLicense")
    public JAXBElement<ImportLicense> createImportLicense(ImportLicense value) {
        return new JAXBElement<ImportLicense>(_ImportLicense_QNAME, ImportLicense.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLastUpdateTimes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getLastUpdateTimes")
    public JAXBElement<GetLastUpdateTimes> createGetLastUpdateTimes(GetLastUpdateTimes value) {
        return new JAXBElement<GetLastUpdateTimes>(_GetLastUpdateTimes_QNAME, GetLastUpdateTimes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProjectsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getProjectsResponse")
    public JAXBElement<GetProjectsResponse> createGetProjectsResponse(GetProjectsResponse value) {
        return new JAXBElement<GetProjectsResponse>(_GetProjectsResponse_QNAME, GetProjectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStream }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createStream")
    public JAXBElement<CreateStream> createCreateStream(CreateStream value) {
        return new JAXBElement<CreateStream>(_CreateStream_QNAME, CreateStream.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteLdapConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteLdapConfiguration")
    public JAXBElement<DeleteLdapConfiguration> createDeleteLdapConfiguration(DeleteLdapConfiguration value) {
        return new JAXBElement<DeleteLdapConfiguration>(_DeleteLdapConfiguration_QNAME, DeleteLdapConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getVersionResponse")
    public JAXBElement<GetVersionResponse> createGetVersionResponse(GetVersionResponse value) {
        return new JAXBElement<GetVersionResponse>(_GetVersionResponse_QNAME, GetVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTriageStoreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteTriageStoreResponse")
    public JAXBElement<DeleteTriageStoreResponse> createDeleteTriageStoreResponse(DeleteTriageStoreResponse value) {
        return new JAXBElement<DeleteTriageStoreResponse>(_DeleteTriageStoreResponse_QNAME, DeleteTriageStoreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentMapsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getComponentMapsResponse")
    public JAXBElement<GetComponentMapsResponse> createGetComponentMapsResponse(GetComponentMapsResponse value) {
        return new JAXBElement<GetComponentMapsResponse>(_GetComponentMapsResponse_QNAME, GetComponentMapsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCategoryNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getCategoryNamesResponse")
    public JAXBElement<GetCategoryNamesResponse> createGetCategoryNamesResponse(GetCategoryNamesResponse value) {
        return new JAXBElement<GetCategoryNamesResponse>(_GetCategoryNamesResponse_QNAME, GetCategoryNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeleteSnapshotJobInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getDeleteSnapshotJobInfo")
    public JAXBElement<GetDeleteSnapshotJobInfo> createGetDeleteSnapshotJobInfo(GetDeleteSnapshotJobInfo value) {
        return new JAXBElement<GetDeleteSnapshotJobInfo>(_GetDeleteSnapshotJobInfo_QNAME, GetDeleteSnapshotJobInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSnapshotInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateSnapshotInfo")
    public JAXBElement<UpdateSnapshotInfo> createUpdateSnapshotInfo(UpdateSnapshotInfo value) {
        return new JAXBElement<UpdateSnapshotInfo>(_UpdateSnapshotInfo_QNAME, UpdateSnapshotInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSystemConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSystemConfig")
    public JAXBElement<GetSystemConfig> createGetSystemConfig(GetSystemConfig value) {
        return new JAXBElement<GetSystemConfig>(_GetSystemConfig_QNAME, GetSystemConfig.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSnapshotInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateSnapshotInfoResponse")
    public JAXBElement<UpdateSnapshotInfoResponse> createUpdateSnapshotInfoResponse(UpdateSnapshotInfoResponse value) {
        return new JAXBElement<UpdateSnapshotInfoResponse>(_UpdateSnapshotInfoResponse_QNAME, UpdateSnapshotInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllPermissions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAllPermissions")
    public JAXBElement<GetAllPermissions> createGetAllPermissions(GetAllPermissions value) {
        return new JAXBElement<GetAllPermissions>(_GetAllPermissions_QNAME, GetAllPermissions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMessageOfTheDay }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setMessageOfTheDay")
    public JAXBElement<SetMessageOfTheDay> createSetMessageOfTheDay(SetMessageOfTheDay value) {
        return new JAXBElement<SetMessageOfTheDay>(_SetMessageOfTheDay_QNAME, SetMessageOfTheDay.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentMaps }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getComponentMaps")
    public JAXBElement<GetComponentMaps> createGetComponentMaps(GetComponentMaps value) {
        return new JAXBElement<GetComponentMaps>(_GetComponentMaps_QNAME, GetComponentMaps.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getUsers")
    public JAXBElement<GetUsers> createGetUsers(GetUsers value) {
        return new JAXBElement<GetUsers>(_GetUsers_QNAME, GetUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getComponent")
    public JAXBElement<GetComponent> createGetComponent(GetComponent value) {
        return new JAXBElement<GetComponent>(_GetComponent_QNAME, GetComponent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateGroupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createGroupResponse")
    public JAXBElement<CreateGroupResponse> createCreateGroupResponse(CreateGroupResponse value) {
        return new JAXBElement<CreateGroupResponse>(_CreateGroupResponse_QNAME, CreateGroupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSkeletonizationConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setSkeletonizationConfigurationResponse")
    public JAXBElement<SetSkeletonizationConfigurationResponse> createSetSkeletonizationConfigurationResponse(SetSkeletonizationConfigurationResponse value) {
        return new JAXBElement<SetSkeletonizationConfigurationResponse>(_SetSkeletonizationConfigurationResponse_QNAME, SetSkeletonizationConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServerTimeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getServerTimeResponse")
    public JAXBElement<GetServerTimeResponse> createGetServerTimeResponse(GetServerTimeResponse value) {
        return new JAXBElement<GetServerTimeResponse>(_GetServerTimeResponse_QNAME, GetServerTimeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateLdapConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createLdapConfigurationResponse")
    public JAXBElement<CreateLdapConfigurationResponse> createCreateLdapConfigurationResponse(CreateLdapConfigurationResponse value) {
        return new JAXBElement<CreateLdapConfigurationResponse>(_CreateLdapConfigurationResponse_QNAME, CreateLdapConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttributesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAttributesResponse")
    public JAXBElement<GetAttributesResponse> createGetAttributesResponse(GetAttributesResponse value) {
        return new JAXBElement<GetAttributesResponse>(_GetAttributesResponse_QNAME, GetAttributesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageOfTheDayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getMessageOfTheDayResponse")
    public JAXBElement<GetMessageOfTheDayResponse> createGetMessageOfTheDayResponse(GetMessageOfTheDayResponse value) {
        return new JAXBElement<GetMessageOfTheDayResponse>(_GetMessageOfTheDayResponse_QNAME, GetMessageOfTheDayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateLdapConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateLdapConfiguration")
    public JAXBElement<UpdateLdapConfiguration> createUpdateLdapConfiguration(UpdateLdapConfiguration value) {
        return new JAXBElement<UpdateLdapConfiguration>(_UpdateLdapConfiguration_QNAME, UpdateLdapConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetAcceptingNewCommitsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setAcceptingNewCommitsResponse")
    public JAXBElement<SetAcceptingNewCommitsResponse> createSetAcceptingNewCommitsResponse(SetAcceptingNewCommitsResponse value) {
        return new JAXBElement<SetAcceptingNewCommitsResponse>(_SetAcceptingNewCommitsResponse_QNAME, SetAcceptingNewCommitsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStreamInProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createStreamInProjectResponse")
    public JAXBElement<CreateStreamInProjectResponse> createCreateStreamInProjectResponse(CreateStreamInProjectResponse value) {
        return new JAXBElement<CreateStreamInProjectResponse>(_CreateStreamInProjectResponse_QNAME, CreateStreamInProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateGroup")
    public JAXBElement<UpdateGroup> createUpdateGroup(UpdateGroup value) {
        return new JAXBElement<UpdateGroup>(_UpdateGroup_QNAME, UpdateGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateStreamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateStreamResponse")
    public JAXBElement<UpdateStreamResponse> createUpdateStreamResponse(UpdateStreamResponse value) {
        return new JAXBElement<UpdateStreamResponse>(_UpdateStreamResponse_QNAME, UpdateStreamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetArchitectureAnalysisConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getArchitectureAnalysisConfigurationResponse")
    public JAXBElement<GetArchitectureAnalysisConfigurationResponse> createGetArchitectureAnalysisConfigurationResponse(GetArchitectureAnalysisConfigurationResponse value) {
        return new JAXBElement<GetArchitectureAnalysisConfigurationResponse>(_GetArchitectureAnalysisConfigurationResponse_QNAME, GetArchitectureAnalysisConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTriageStoresResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getTriageStoresResponse")
    public JAXBElement<GetTriageStoresResponse> createGetTriageStoresResponse(GetTriageStoresResponse value) {
        return new JAXBElement<GetTriageStoresResponse>(_GetTriageStoresResponse_QNAME, GetTriageStoresResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetBackupConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setBackupConfiguration")
    public JAXBElement<SetBackupConfiguration> createSetBackupConfiguration(SetBackupConfiguration value) {
        return new JAXBElement<SetBackupConfiguration>(_SetBackupConfiguration_QNAME, SetBackupConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportLicenseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "importLicenseResponse")
    public JAXBElement<ImportLicenseResponse> createImportLicenseResponse(ImportLicenseResponse value) {
        return new JAXBElement<ImportLicenseResponse>(_ImportLicenseResponse_QNAME, ImportLicenseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllLdapConfigurationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getAllLdapConfigurationsResponse")
    public JAXBElement<GetAllLdapConfigurationsResponse> createGetAllLdapConfigurationsResponse(GetAllLdapConfigurationsResponse value) {
        return new JAXBElement<GetAllLdapConfigurationsResponse>(_GetAllLdapConfigurationsResponse_QNAME, GetAllLdapConfigurationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeTriageStores }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "mergeTriageStores")
    public JAXBElement<MergeTriageStores> createMergeTriageStores(MergeTriageStores value) {
        return new JAXBElement<MergeTriageStores>(_MergeTriageStores_QNAME, MergeTriageStores.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSkeletonizationConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "setSkeletonizationConfiguration")
    public JAXBElement<SetSkeletonizationConfiguration> createSetSkeletonizationConfiguration(SetSkeletonizationConfiguration value) {
        return new JAXBElement<SetSkeletonizationConfiguration>(_SetSkeletonizationConfiguration_QNAME, SetSkeletonizationConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "createRole")
    public JAXBElement<CreateRole> createCreateRole(CreateRole value) {
        return new JAXBElement<CreateRole>(_CreateRole_QNAME, CreateRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSignInConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSignInConfiguration")
    public JAXBElement<GetSignInConfiguration> createGetSignInConfiguration(GetSignInConfiguration value) {
        return new JAXBElement<GetSignInConfiguration>(_GetSignInConfiguration_QNAME, GetSignInConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSkeletonizationConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSkeletonizationConfiguration")
    public JAXBElement<GetSkeletonizationConfiguration> createGetSkeletonizationConfiguration(GetSkeletonizationConfiguration value) {
        return new JAXBElement<GetSkeletonizationConfiguration>(_GetSkeletonizationConfiguration_QNAME, GetSkeletonizationConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCheckerNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getCheckerNamesResponse")
    public JAXBElement<GetCheckerNamesResponse> createGetCheckerNamesResponse(GetCheckerNamesResponse value) {
        return new JAXBElement<GetCheckerNamesResponse>(_GetCheckerNamesResponse_QNAME, GetCheckerNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Notify }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "notify")
    public JAXBElement<Notify> createNotify(Notify value) {
        return new JAXBElement<Notify>(_Notify_QNAME, Notify.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getRoleResponse")
    public JAXBElement<GetRoleResponse> createGetRoleResponse(GetRoleResponse value) {
        return new JAXBElement<GetRoleResponse>(_GetRoleResponse_QNAME, GetRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getStreams")
    public JAXBElement<GetStreams> createGetStreams(GetStreams value) {
        return new JAXBElement<GetStreams>(_GetStreams_QNAME, GetStreams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateProject")
    public JAXBElement<UpdateProject> createUpdateProject(UpdateProject value) {
        return new JAXBElement<UpdateProject>(_UpdateProject_QNAME, UpdateProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "deleteProject")
    public JAXBElement<DeleteProject> createDeleteProject(DeleteProject value) {
        return new JAXBElement<DeleteProject>(_DeleteProject_QNAME, DeleteProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSkeletonizationConfigurationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "getSkeletonizationConfigurationResponse")
    public JAXBElement<GetSkeletonizationConfigurationResponse> createGetSkeletonizationConfigurationResponse(GetSkeletonizationConfigurationResponse value) {
        return new JAXBElement<GetSkeletonizationConfigurationResponse>(_GetSkeletonizationConfigurationResponse_QNAME, GetSkeletonizationConfigurationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateComponentMapResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.coverity.com/v9", name = "updateComponentMapResponse")
    public JAXBElement<UpdateComponentMapResponse> createUpdateComponentMapResponse(UpdateComponentMapResponse value) {
        return new JAXBElement<UpdateComponentMapResponse>(_UpdateComponentMapResponse_QNAME, UpdateComponentMapResponse.class, null, value);
    }

}
