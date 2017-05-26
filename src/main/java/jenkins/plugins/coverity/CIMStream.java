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
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.StreamDataObj;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.ws.CimCache;

public class CIMStream extends AbstractDescribableImpl<CIMStream> {
    // deprecated field removed in plugin version 1.9 (removed invocation override when multiple streams removed)
    private transient InvocationAssistance invocationAssistanceOverride;

    private final String instance;
    private final String project;
    private final String stream;

    /**
     * Defines how to filter discovered defects. Null for no filtering.
     */
    private final DefectFilters defectFilters;

    @DataBoundConstructor
    public CIMStream(String instance, String project, String stream, DefectFilters defectFilters) {
        this.instance = Util.fixEmpty(instance);
        this.project = Util.fixEmpty(project);
        this.stream = Util.fixEmpty(stream);
        this.defectFilters = defectFilters;
    }

    public String getInstance() {
        return instance;
    }

    public String getProject() {
        return project;
    }

    public String getStream() {
        return stream;
    }

    public DefectFilters getDefectFilters() {
        return defectFilters;
    }

    @Deprecated
    public InvocationAssistance getInvocationAssistanceOverride() {
        return invocationAssistanceOverride;
    }

    public String toPrettyString() {
        return instance + "/" + project + "/" + stream;
    }

    @Override
    public String toString() {
        return "CIMStream{" +
                "instance='" + instance + '\'' +
                ", project='" + project + '\'' +
                ", stream='" + stream + '\'' +
                ", defectFilters=" + defectFilters +
                '}';
    }

    public boolean isValid() {
        return instance != null && project != null && stream != null;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<CIMStream> {

        @Override
        public String getDisplayName() {
            return "";
        }

        public CoverityPublisher.DescriptorImpl getPublisherDescriptor() {
            return Jenkins.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
        }

        public List<CIMInstance> getInstances() {
            return getPublisherDescriptor().getInstances();
        }

        public CIMInstance getInstance(String name) {
            if (!StringUtils.isEmpty(name)) {
                for(CIMInstance instance : getInstances()) {
                    if(instance.getName().equals(name)) {
                        return instance;
                    }
                }
            }

            return null;
        }

        public ListBoxModel doFillInstanceItems() {
            ListBoxModel result = new ListBoxModel();
            result.add("");
            for(CIMInstance instance : getInstances()) {
                result.add(instance.getName());
            }
            return result;
        }

        public FormValidation doCheckInstance(@QueryParameter String instance) throws IOException, CovRemoteServiceException_Exception {
            CIMInstance cimInstance = getInstance(instance);

            if (cimInstance != null) {
                FormValidation checkResult = cimInstance.doCheck();

                // initialize cache for instance
                CimCache.getInstance().cacheCimInstance(cimInstance);

                // return FormValidation.ok in order to suppress any success messages, these don't need to show automatically here
                return checkResult.kind.equals(FormValidation.Kind.OK) ? FormValidation.ok() : checkResult;
            }
            return FormValidation.warning("Coverity Connect instance is required to select project and stream");
        }

        public List<String> loadProjects(@QueryParameter String instance, @QueryParameter String project) {

            List<String> projects = new ArrayList<>();

            if (!StringUtils.isEmpty(instance)) {
                CIMInstance cimInstance = getInstance(instance);
                if (cimInstance != null) {
                    for(String projectFromCim : CimCache.getInstance().getProjects(cimInstance)) {
                        projects.add(projectFromCim);
                    }
                }
            }

            if (!StringUtils.isEmpty(project) && !projects.contains(project)) {
                projects.add(project);
            }

            return projects;
        }

        public boolean checkProjectIsValid(@QueryParameter String instance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
            // allow initial empty project selection
            if (StringUtils.isEmpty(project))
                return true;

            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null){
                for (String projectName : CimCache.getInstance().getProjects(cimInstance)){
                    if (projectName.equalsIgnoreCase(project)){
                        return true;
                    }
                }
            }

            // project not found for cim instance is invalid
            return false;
        }

        public List<String> loadStreams(@QueryParameter String instance, @QueryParameter String project, @QueryParameter String stream) {

            List<String> streams = new ArrayList<>();

            if (!StringUtils.isEmpty(instance) && !StringUtils.isEmpty(project)) {
                CIMInstance cimInstance = getInstance(instance);
                if (cimInstance != null) {
                    for (String streamFromCim : CimCache.getInstance().getStreams(cimInstance, project)) {
                        streams.add(streamFromCim);
                    }
                }
            }

            if (!StringUtils.isEmpty(stream) && !streams.contains(stream)) {
                streams.add(stream);
            }

            return streams;
        }

        public boolean checkStreamIsValid(@QueryParameter String instance, @QueryParameter String project, @QueryParameter String stream) throws IOException, CovRemoteServiceException_Exception {
            // allow initial empty stream selection
            if (StringUtils.isEmpty(stream))
                return true;

            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null && !StringUtils.isEmpty(project)){
                for (String streamFromCIM : CimCache.getInstance().getStreams(cimInstance, project)){
                    if (streamFromCIM.equalsIgnoreCase(stream)){
                        return true;
                    }
                }
            }

            // stream not found for the project is invalid
            return false;
        }

        public ListBoxModel doFillClassificationDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null) {
                com.coverity.ws.v9.AttributeDefinitionIdDataObj adido = new com.coverity.ws.v9.AttributeDefinitionIdDataObj();
                adido.setName("Classification");
                com.coverity.ws.v9.AttributeDefinitionDataObj addo = instance.getConfigurationService().getAttribute(adido);
                for(com.coverity.ws.v9.AttributeValueDataObj classification : addo.getConfigurableValues()) {
                    result.add(classification.getAttributeValueId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillActionDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null) {
                com.coverity.ws.v9.AttributeDefinitionIdDataObj adido = new com.coverity.ws.v9.AttributeDefinitionIdDataObj();
                adido.setName("Action");
                com.coverity.ws.v9.AttributeDefinitionDataObj addo = instance.getConfigurationService().getAttribute(adido);
                for(com.coverity.ws.v9.AttributeValueDataObj classification : addo.getConfigurableValues()) {
                    result.add(classification.getAttributeValueId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillImpactDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            result.add("High");
            result.add("Medium");
            result.add("Low");
            return result;
        }

        public ListBoxModel doFillSeveritiesDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null) {
                com.coverity.ws.v9.AttributeDefinitionIdDataObj adido = new com.coverity.ws.v9.AttributeDefinitionIdDataObj();
                adido.setName("Severity");
                com.coverity.ws.v9.AttributeDefinitionDataObj addo = instance.getConfigurationService().getAttribute(adido);
                for(com.coverity.ws.v9.AttributeValueDataObj classification : addo.getConfigurableValues()) {
                    result.add(classification.getAttributeValueId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillComponentDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);

            if(instance != null) {
                com.coverity.ws.v9.ComponentMapFilterSpecDataObj componentMapFilterSpec = new com.coverity.ws.v9.ComponentMapFilterSpecDataObj();

                if (!StringUtils.isEmpty(streamId)) {
                    StreamDataObj stream = instance.getStream(streamId);
                    String componentMapId = stream.getComponentMapId().getName();

                    componentMapFilterSpec.setNamePattern(componentMapId);
                } else {
                    componentMapFilterSpec.setNamePattern("*");
                }

                for(com.coverity.ws.v9.ComponentMapDataObj map : instance.getConfigurationService().getComponentMaps(componentMapFilterSpec)) {
                    for(com.coverity.ws.v9.ComponentDataObj component : map.getComponents()) {
                        result.add(component.getComponentId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillCheckerDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId) throws IOException, CovRemoteServiceException_Exception {
            if(StringUtils.isEmpty(streamId)) return new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance == null) return new ListBoxModel();

            // Retrieve all checkers for a specific cim instance.
            List<String> checkers = instance.getCimInstanceCheckers();

            ListBoxModel result = new ListBoxModel();
            for (String checker : checkers) {
                result.add(checker);
            }

            return result;
        }
    }
}
