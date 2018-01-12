/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity.ws;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import com.coverity.ws.v9.*;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import jenkins.plugins.coverity.CIMInstance;

public class TestWebServiceFactory extends WebServiceFactory {
    private int wsResponseCode = 200;
    private String responseMsg = "OK";

    @Override
    protected DefectService createDefectService(CIMInstance cimInstance) throws MalformedURLException {
        return new TestDefectService(
            new URL(getURL(cimInstance), DEFECT_SERVICE_V9_WSDL));
    }

    @Override
    protected ConfigurationService createConfigurationService(CIMInstance cimInstance) throws MalformedURLException {
        return new TestConfigurationService(
            new URL(getURL(cimInstance), CONFIGURATION_SERVICE_V9_WSDL));
    }

    @Override
    public CheckWsResponse getCheckWsResponse(CIMInstance cimInstance) {
        return new CheckWsResponse(wsResponseCode, responseMsg);
    }

    public void setWSResponseCode(int wsResponseCode, String responseMsg) {
        this.wsResponseCode = wsResponseCode;
        this.responseMsg = responseMsg;
    }

    public static class TestConfigurationService implements ConfigurationService {
        private URL url;
        private List<SnapshotIdDataObj> snapshotList;
        private List<ProjectDataObj> projects;
        private String externalVersion;
        private UserDataObj user;
        private List<RoleDataObj> roles;
        private List<GroupDataObj> groups;
        private List<ComponentMapDataObj> componentMaps;
        private List<String> checkerNames;
        private List<AttributeValueDataObj> classifications;
        private List<AttributeValueDataObj> actions;
        private List<AttributeValueDataObj> severities;

        public TestConfigurationService(URL url) {

            this.url = url;
            this.projects = new ArrayList<>();
            this.roles = new ArrayList<>();
            this.groups = new ArrayList<>();
            this.user = new UserDataObj();
            this.componentMaps = new ArrayList<>();
            this.checkerNames = new ArrayList<>();
            this.classifications = new ArrayList<>();
            this.actions = new ArrayList<>();
            this.severities = new ArrayList<>();
        }

        public URL getUrl() {
            return url;
        }

        public void setupSnapshotList(List<SnapshotIdDataObj> list) {
            this.snapshotList = list;
        }

        public void setupProjects(String projectNamePrefix, int projectCount, String streamNamePrefix, int streamCount) {
            for (int i = 0; i < projectCount; i++) {
                final ProjectDataObj projectDataObj = new ProjectDataObj();
                ProjectIdDataObj projectIdDataObj = new ProjectIdDataObj();
                projectIdDataObj.setName(projectNamePrefix + i);
                projectDataObj.setId(projectIdDataObj);
                projectDataObj.setProjectKey((long)i);

                for (int j = 0; j < streamCount; j++) {
                    StreamDataObj streamDataObj = new StreamDataObj();
                    StreamIdDataObj streamIdDataObj = new StreamIdDataObj();
                    streamIdDataObj.setName(streamNamePrefix + j);
                    streamDataObj.setId(streamIdDataObj);
                    ComponentMapIdDataObj componentMapIdDataObj = new ComponentMapIdDataObj();
                    componentMapIdDataObj.setName("Default");
                    streamDataObj.setComponentMapId(componentMapIdDataObj);
                    projectDataObj.getStreams().add(streamDataObj);
                }

                projects.add(projectDataObj);
            }
        }

        public void setupExternalVersion(String version) {
            this.externalVersion = version;
        }

        public void setupUser(String userName, Boolean isSuper, Map<String, String[]> rolePermissions) {
            setupUser(userName, isSuper, rolePermissions, true);
        }

        public void setupUser(String userName, Boolean isSuper, Map<String, String[]> rolePermissions, Boolean useGlobal) {
            user.setUsername(userName);
            user.setSuperUser(isSuper);

            for (Entry<String, String[]> rolePermission : rolePermissions.entrySet()) {
                RoleAssignmentDataObj roleAssignment = new RoleAssignmentDataObj();
                RoleIdDataObj roleId = new RoleIdDataObj();
                roleId.setName(rolePermission.getKey());
                roleAssignment.setRoleId(roleId);
                if (useGlobal)
                    roleAssignment.setType("global");
                else
                    roleAssignment.setType("component");
                user.getRoleAssignments().add(roleAssignment);

                RoleDataObj roleData = new RoleDataObj();
                roleData.setRoleId(roleId);

                for (String rolePerm : rolePermission.getValue()) {
                    PermissionDataObj permission = new PermissionDataObj();
                    permission.setPermissionValue(rolePerm);
                    roleData.getPermissionDataObjs().add(permission);
                }

                roles.add(roleData);
            }
        }

        public void setupUser(String userName, Map<String, String[]> groupRoles,  Map<String, String[]> rolePermissions) {
            setupUser(userName, groupRoles, rolePermissions, true);
        }

        public void setupUser(String userName, Map<String, String[]> groupRoles,  Map<String, String[]> rolePermissions, Boolean useGlobal) {
            setupUser(userName, false, rolePermissions, false);
            user.getRoleAssignments().clear();

            for (Entry<String, String[]> groupRole : groupRoles.entrySet()) {
                user.getGroups().add(groupRole.getKey());

                GroupDataObj group = new GroupDataObj();
                GroupIdDataObj groupId = new GroupIdDataObj();
                groupId.setName(groupRole.getKey());
                group.setName(groupId);

                for (String rolename : groupRole.getValue()) {
                    RoleAssignmentDataObj roleAssignment = new RoleAssignmentDataObj();
                    RoleIdDataObj roleId = new RoleIdDataObj();
                    roleId.setName(rolename);
                    roleAssignment.setRoleId(roleId);
                    if (useGlobal)
                        roleAssignment.setType("global");
                    else
                        roleAssignment.setType("component");
                    group.getRoleAssignments().add(roleAssignment);
                }
                groups.add(group);
            }
        }

        public void setupComponents(String... components) {
            ComponentMapDataObj componentMap = new ComponentMapDataObj();
            for (String component : components) {
                ComponentDataObj componentDataObj = new ComponentDataObj();
                ComponentIdDataObj componentIdDataObj = new ComponentIdDataObj();
                componentIdDataObj.setName(component);
                componentDataObj.setComponentId(componentIdDataObj);
                componentMap.getComponents().add(componentDataObj);
            }
            componentMaps.add(componentMap);
        }

        public void setupCheckerNames(String... checkerNames) {
            this.checkerNames = checkerNames != null ? Arrays.asList(checkerNames) : new ArrayList<String>();
        }

        public void setupDefaultAttributes() {
            classifications.addAll(
                    Arrays.asList(
                            createAttributeValue("Unclassified"),
                            createAttributeValue("Pending"),
                            createAttributeValue("False Positive"),
                            createAttributeValue("Intentional"),
                            createAttributeValue("Bug"),
                            createAttributeValue("Untested"),
                            createAttributeValue("No Test Needed"),
                            createAttributeValue("Tested Elsewhere")));
            actions.addAll(
                    Arrays.asList(
                            createAttributeValue("Undecided"),
                            createAttributeValue("Fix Required"),
                            createAttributeValue("Fix Submitted"),
                            createAttributeValue("Modeling Required"),
                            createAttributeValue("Ignore")));
            severities.addAll(
                    Arrays.asList(
                            createAttributeValue("Unspecified"),
                            createAttributeValue("Major"),
                            createAttributeValue("Moderate"),
                            createAttributeValue("Minor")));
        }

        private AttributeValueDataObj createAttributeValue(String value) {
            AttributeValueDataObj attributeValue = new AttributeValueDataObj();
            AttributeValueIdDataObj attributeValueId = new AttributeValueIdDataObj();
            attributeValueId.setName(value);
            attributeValue.setAttributeValueId(attributeValueId);
            attributeValue.setDisplayName(value);
            return attributeValue;
        }

        @Override
        public void updateAttribute(AttributeDefinitionIdDataObj attributeDefinitionId, AttributeDefinitionSpecDataObj attributeDefinitionSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteAttribute(AttributeDefinitionIdDataObj attributeDefinitionId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createComponentMap(ComponentMapSpecDataObj componentMapSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateComponentMap(ComponentMapIdDataObj componentMapId, ComponentMapSpecDataObj componentMapSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteComponentMap(ComponentMapIdDataObj componentMapId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateGroup(GroupIdDataObj groupId, GroupSpecDataObj groupSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteGroup(GroupIdDataObj groupId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createProject(ProjectSpecDataObj projectSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateProject(ProjectIdDataObj projectId, ProjectSpecDataObj projectSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteProject(ProjectIdDataObj projectId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createRole(RoleSpecDataObj roleSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateRole(RoleIdDataObj roleId, RoleSpecDataObj roleSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteRole(RoleIdDataObj roleId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createStream(StreamSpecDataObj streamSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateStream(StreamIdDataObj streamId, StreamSpecDataObj streamSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteStream(StreamIdDataObj streamId, boolean onlyIfEmpty) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createTriageStore(TriageStoreSpecDataObj triageStoreSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateTriageStore(TriageStoreIdDataObj triageStoreId, TriageStoreSpecDataObj triageStoreSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteTriageStore(TriageStoreIdDataObj triageStoreId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createUser(UserSpecDataObj userSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateUser(String username, UserSpecDataObj userSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void deleteUser(String username) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public UserDataObj getUser(String username) throws CovRemoteServiceException_Exception {
            Boolean canAccessWS = user.isSuperUser();
            rolesloop:
            for (RoleDataObj role : roles) {
                for (PermissionDataObj permission : role.getPermissionDataObjs()) {
                    if (permission.getPermissionValue().equals("invokeWS")) {
                        canAccessWS = true;
                        break rolesloop;
                    }
                }
            }

            if (!canAccessWS) {
                SOAPFault fault = mock(SOAPFault.class);
                when(fault.getFaultString()).thenReturn("User " + username + " Doesn't have permissions to perform {invokeWS}");
                throw new SOAPFaultException(fault);
            }

            return user;
        }

        @Override
        public List<StreamDataObj> getStreams(StreamFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            List<StreamDataObj> matchingStreams = new ArrayList<>();

            for (ProjectDataObj project : projects) {
                for (StreamDataObj stream : project.getStreams()) {
                    if (stream.getId().getName().equals(filterSpec.getNamePattern()))
                        matchingStreams.add(stream);
                    else if (StringUtils.isEmpty(filterSpec.getNamePattern()))
                        matchingStreams.add(stream);
                }
            }

            return matchingStreams;
        }

        @Override
        public void mergeTriageStores(List<TriageStoreIdDataObj> srcTriageStoreIds, TriageStoreIdDataObj triageStoreId, boolean deleteSourceStores, boolean assignStreamsToTargetStore) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<PermissionDataObj> getAllPermissions() {
            throw new NotImplementedException();
        }

        @Override
        public List<LdapConfigurationDataObj> getAllLdapConfigurations() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<DeleteSnapshotJobInfoDataObj> getDeleteSnapshotJobInfo(List<SnapshotIdDataObj> snapshotId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<SnapshotIdDataObj> getSnapshotsForStream(StreamIdDataObj streamId, SnapshotFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            return snapshotList;
        }

        @Override
        public void deleteLdapConfiguration(ServerDomainIdDataObj serverDomainIdDataObj) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<FeatureUpdateTimeDataObj> getLastUpdateTimes() {
            throw new NotImplementedException();
        }

        @Override
        public void setAcceptingNewCommits(boolean acceptNewCommits) {
            throw new NotImplementedException();
        }

        @Override
        public void deleteSnapshot(List<SnapshotIdDataObj> snapshotId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void executeNotification(String viewname) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public StreamDataObj copyStream(ProjectIdDataObj projectId, StreamIdDataObj sourceStreamId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createStreamInProject(ProjectIdDataObj projectId, StreamSpecDataObj streamSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void setMessageOfTheDay(String message) {
            throw new NotImplementedException();
        }

        @Override
        public String getMessageOfTheDay() {
            throw new NotImplementedException();
        }

        @Override
        public List<ProjectDataObj> getProjects(ProjectFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            if (!StringUtils.isEmpty(filterSpec.getNamePattern()))
            {
                List<ProjectDataObj> matchingProjects = new ArrayList<>();
                for (ProjectDataObj project : projects) {
                    if (project.getId().getName().equals(filterSpec.getNamePattern()))
                        matchingProjects.add(project);
                }
                return matchingProjects;
            }

            return projects;
        }

        @Override
        public List<ComponentMapDataObj> getComponentMaps(ComponentMapFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            return componentMaps;
        }

        @Override
        public List<TriageStoreDataObj> getTriageStores(TriageStoreFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<LocalizedValueDataObj> getCategoryNames() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<String> getDefectStatuses() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<SnapshotInfoDataObj> getSnapshotInformation(List<SnapshotIdDataObj> snapshotIds) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateSnapshotInfo(SnapshotIdDataObj snapshotId, SnapshotInfoDataObj snapshotData) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public CommitStateDataObj getCommitState() {
            throw new NotImplementedException();
        }

        @Override
        public List<ServerDomainIdDataObj> getLdapServerDomains() {
            throw new NotImplementedException();
        }

        @Override
        public XMLGregorianCalendar getServerTime() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public ConfigurationDataObj getSystemConfig() {
            throw new NotImplementedException();
        }

        @Override
        public LicenseStateDataObj getLicenseState() {
            throw new NotImplementedException();
        }

        @Override
        public SnapshotPurgeDetailsObj getSnapshotPurgeDetails() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createLdapConfiguration(LdapConfigurationSpecDataObj ldapConfigurationSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateLdapConfiguration(ServerDomainIdDataObj serverDomainIdDataObj, LdapConfigurationSpecDataObj ldapConfigurationSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void setSnapshotPurgeDetails(SnapshotPurgeDetailsObj purgeDetailsSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void setBackupConfiguration(BackupConfigurationDataObj backupConfigurationDataObj) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<ProjectDataObj> getDeveloperStreamsProjects(ProjectFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public SignInSettingsDataObj getSignInConfiguration() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public LoggingConfigurationDataObj getLoggingConfiguration() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void setLoggingConfiguration(LoggingConfigurationDataObj loggingConfigurationDataObj) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void setSkeletonizationConfiguration(SkeletonizationConfigurationDataObj skeletonizationConfigurationDataObj) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void importLicense(LicenseSpecDataObj licenseSpecDataObj) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public BackupConfigurationDataObj getBackupConfiguration() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public SkeletonizationConfigurationDataObj getSkeletonizationConfiguration() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public LicenseDataObj getLicenseConfiguration() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public String getArchitectureAnalysisConfiguration() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void setArchitectureAnalysisConfiguration(String architectureAnalysisConfiguration) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void updateSignInConfiguration(SignInSettingsDataObj signInSettingsDataObj) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<String> getCheckerNames() throws CovRemoteServiceException_Exception {
            return checkerNames;
        }

        @Override
        public VersionDataObj getVersion() throws CovRemoteServiceException_Exception {
            VersionDataObj version = new VersionDataObj();
            version.setExternalVersion(this.externalVersion);
            return version;
        }

        @Override
        public void createGroup(GroupSpecDataObj groupSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public void createAttribute(AttributeDefinitionSpecDataObj attributeDefinitionSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<LocalizedValueDataObj> getTypeNames() throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public AttributeDefinitionDataObj getAttribute(AttributeDefinitionIdDataObj attributeDefinitionId) throws CovRemoteServiceException_Exception {
            final AttributeDefinitionDataObj attributeDefinitionDataObj = new AttributeDefinitionDataObj();
            attributeDefinitionDataObj.setAttributeDefinitionId(attributeDefinitionId);

            if (attributeDefinitionId.getName().equals("Classification")) {
                attributeDefinitionDataObj.getConfigurableValues().addAll(classifications);
            } else if (attributeDefinitionId.getName().equals("Action")) {
                attributeDefinitionDataObj.getConfigurableValues().addAll(actions);
            } else if (attributeDefinitionId.getName().equals("Severity")) {
                attributeDefinitionDataObj.getConfigurableValues().addAll(severities);
            }

            return attributeDefinitionDataObj;
        }

        @Override
        public ComponentDataObj getComponent(ComponentIdDataObj componentId) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public GroupsPageDataObj getGroups(GroupFilterSpecDataObj filterSpec, PageSpecDataObj pageSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public UsersPageDataObj getUsers(UserFilterSpecDataObj filterSpec, PageSpecDataObj pageSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public RoleDataObj getRole(RoleIdDataObj roleId) throws CovRemoteServiceException_Exception {
            for (RoleDataObj role : roles) {
                if (role.getRoleId().getName().equals(roleId.getName()))
                    return role;
            }

            return null;
        }

        @Override
        public GroupDataObj getGroup(GroupIdDataObj groupId) throws CovRemoteServiceException_Exception {
            for (GroupDataObj group : groups) {
                if (group.getName().getName().equals(groupId.getName()))
                    return group;
            }

            return null;
        }

        @Override
        public List<RoleDataObj> getAllRoles() {
            throw new NotImplementedException();
        }

        @Override
        public List<String> notify(List<String> usernames, String subject, String message) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public List<AttributeDefinitionDataObj> getAttributes() {
            throw new NotImplementedException();
        }
    }

    public static class TestDefectService implements DefectService {
        private URL url;
        private List<MergedDefectIdDataObj> mergedDefectIds = new ArrayList<>();
        private List<MergedDefectDataObj> mergedDefects = new ArrayList<>();

        public TestDefectService(URL url) {
            this.url = url;
        }

        public void setupMergedDefects(int defectCount) throws ParseException, DatatypeConfigurationException {
            for (long i = 0; i < defectCount; i++) {
                MergedDefectIdDataObj idDataObj = new MergedDefectIdDataObj();
                idDataObj.setCid(i);
                idDataObj.setMergeKey("MK_" + i);
                mergedDefectIds.add(idDataObj);

                MergedDefectDataObj defectDataObj = new MergedDefectDataObj();
                defectDataObj.setCid(i);
                defectDataObj.setMergeKey("MK_" + i);
                defectDataObj.setCheckerName("TEST_CHECKER");
                defectDataObj.setFilePathname("/defect/file/test." + i + ".java");
                defectDataObj.setFunctionDisplayName("defect_function_" + i + "()");

                // set default attribute values for filtering
                defectDataObj.getDefectStateAttributeValues().add(newAttribute("Action", "Undecided"));
                defectDataObj.getDefectStateAttributeValues().add(newAttribute("Classification", "Unclassified"));
                defectDataObj.getDefectStateAttributeValues().add(newAttribute("Severity", "Unspecified"));
                defectDataObj.setDisplayImpact("Low");
                defectDataObj.setComponentName("Default.Other");


                GregorianCalendar calender = new GregorianCalendar();
                calender.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2017-02-01"));
                defectDataObj.setFirstDetected(DatatypeFactory.newInstance().newXMLGregorianCalendar(calender));

                mergedDefects.add(defectDataObj);
            }
        }

        private DefectStateAttributeValueDataObj newAttribute(String name, String value){
            DefectStateAttributeValueDataObj attributeValueDataObj = new DefectStateAttributeValueDataObj();

            AttributeDefinitionIdDataObj attributeDefinitionId = new AttributeDefinitionIdDataObj();
            attributeDefinitionId.setName(name);
            attributeValueDataObj.setAttributeDefinitionId(attributeDefinitionId);

            AttributeValueIdDataObj attributeValueId = new AttributeValueIdDataObj();
            attributeValueId.setName(value);
            attributeValueDataObj.setAttributeValueId(attributeValueId);

            return attributeValueDataObj;
        }

        public URL getUrl() {
            return url;
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
            throw new NotImplementedException();
        }

        @Override
        public List<StreamDefectDataObj> getStreamDefects(List<MergedDefectIdDataObj> mergedDefectIdDataObjs, StreamDefectFilterSpecDataObj filterSpec) throws CovRemoteServiceException_Exception {
            throw new NotImplementedException();
        }

        @Override
        public MergedDefectsPageDataObj getMergedDefectsForStreams(List<StreamIdDataObj> streamIds, MergedDefectFilterSpecDataObj filterSpec, PageSpecDataObj pageSpec, SnapshotScopeSpecDataObj snapshotScope) throws CovRemoteServiceException_Exception {
            MergedDefectsPageDataObj mergedDefectsPageDataObj = new MergedDefectsPageDataObj();

            final int totalRecords = mergedDefects.size();
            mergedDefectsPageDataObj.setTotalNumberOfRecords(totalRecords);

            int toIndex = pageSpec.getStartIndex() + pageSpec.getPageSize();
            if (toIndex > mergedDefects.size())
                toIndex = mergedDefects.size();

            List<MergedDefectIdDataObj> defectIds = mergedDefectIds.subList(pageSpec.getStartIndex(), toIndex);
            mergedDefectsPageDataObj.getMergedDefectIds().addAll(defectIds);

            List<MergedDefectDataObj> defects = mergedDefects.subList(pageSpec.getStartIndex(), toIndex);
            mergedDefectsPageDataObj.getMergedDefects().addAll(defects);

            return mergedDefectsPageDataObj;
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
