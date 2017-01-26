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
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.StreamDataObj;
import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class CIMStream extends AbstractDescribableImpl<CIMStream> {
    private static final Logger logger = Logger.getLogger(CIMStream.class.getName());
    private final String instance;
    private final String project;
    private final String stream;
    private final String id;

    /**
     * Defines how to filter discovered defects. Null for no filtering.
     */
    private final DefectFilters defectFilters;

    private final InvocationAssistance invocationAssistanceOverride;

    @DataBoundConstructor
    public CIMStream(String instance, String project, String stream, DefectFilters defectFilters, String id, InvocationAssistance invocationAssistanceOverride) {
        this.instance = Util.fixEmpty(instance);
        this.project = Util.fixEmpty(project);
        this.stream = Util.fixEmpty(stream);
        this.id = Util.fixEmpty(id);
        this.defectFilters = defectFilters;
        this.invocationAssistanceOverride = invocationAssistanceOverride;
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

    public String getId() {
        return id;
    }

    public DefectFilters getDefectFilters() {
        return defectFilters;
    }

    public InvocationAssistance getInvocationAssistanceOverride() {
        return invocationAssistanceOverride;
    }

    public String toPrettyString() {
        return instance + "/" + project + "/" + stream;
    }

    public String getDomain() {
        CIMInstance ci = ((DescriptorImpl) getDescriptor()).getInstance(instance);
        try {
            StreamDataObj str = ci.getStream(stream);
            return str.getLanguage();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        } catch(CovRemoteServiceException_Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "CIMStream{" +
                "instance='" + instance + '\'' +
                ", project='" + project + '\'' +
                ", stream='" + stream + '\'' +
                ", id='" + id + '\'' +
                ", defectFilters=" + defectFilters +
                ", invocationAssistanceOverride=" + invocationAssistanceOverride +
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

        public String getRandomID() {
            Random r = new Random();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 16; i++) {
                sb.append("" + r.nextInt(10));
            }
            return sb.toString();
        }

        public String getRandomID(Object o) {
            if(o != null) {
                return "" + o.hashCode();
            }
            return getRandomID();
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

                // return FormValidation.ok in order to suppress any success messages, these don't need to show automatically here
                return checkResult.kind.equals(FormValidation.Kind.OK) ? FormValidation.ok() : checkResult;
            }
            return FormValidation.warning("Coverity Connect instance is required to select project and stream");
        }

        public ListBoxModel doFillProjectItems(@QueryParameter String instance, @QueryParameter String project, @QueryParameter String stream) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            boolean containCurrentProject = false;
            CIMInstance cimInstance = getInstance(instance);
            if(cimInstance != null) {
                for(ProjectDataObj projectFromCIM : cimInstance.getProjects()) {
                    // don't add projects for which there are no valid streams
                    ListBoxModel streams = doFillStreamItems(instance, projectFromCIM.getId().getName(), stream);
                    if(!streams.isEmpty()) {
                        result.add(projectFromCIM.getId().getName());
                        if (!StringUtils.isEmpty(project) && projectFromCIM.getId().getName().equalsIgnoreCase(project)){
                            containCurrentProject = true;
                        }
                    }
                }
            }
            if (!containCurrentProject && !StringUtils.isEmpty(project)){
                result.add(project);
            }
            return result;
        }

        public FormValidation doCheckProject(@QueryParameter String instance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
            // allow initial empty project selection
            if (StringUtils.isEmpty(project))
                return FormValidation.ok();

            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null){
                for (ProjectDataObj projectFromCIM : cimInstance.getProjects()){
                    if (projectFromCIM.getId().getName().equalsIgnoreCase(project)){
                        return FormValidation.ok();
                    }
                }
            }
            return FormValidation.error("Project [ " + project + " ] is not found");
        }

        public ListBoxModel doFillStreamItems(@QueryParameter String instance, @QueryParameter String project, @QueryParameter String stream) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            boolean containCurrentStream = false;
            if (StringUtils.isEmpty(project)) return result;
            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null) {
                for (StreamDataObj streamFromCIM : cimInstance.getStaticStreams(project)) {
                    result.add(streamFromCIM.getId().getName());
                    if (!StringUtils.isEmpty(stream) && streamFromCIM.getId().getName().equalsIgnoreCase(stream)) {
                        containCurrentStream = true;
                    }
                }
            }
            if (!containCurrentStream && !StringUtils.isEmpty(stream)) {
                result.add(stream);
            }
            return result;
        }

        public FormValidation doCheckStream(@QueryParameter String instance, @QueryParameter String project, @QueryParameter String stream) throws IOException, CovRemoteServiceException_Exception {
            // allow initial empty stream selection
            if (StringUtils.isEmpty(stream))
                return FormValidation.ok();

            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null && !StringUtils.isEmpty(project)){
                for (StreamDataObj streamFromCIM : cimInstance.getStaticStreams(project)){
                    if (streamFromCIM.getId().getName().equalsIgnoreCase(stream)){
                        return FormValidation.ok();
                    }
                }
            }
            return FormValidation.error("Stream [ " + stream + " ] is not found");
        }

        public ListBoxModel doFillClassificationDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception, com.coverity.ws.v9.CovRemoteServiceException_Exception {
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

        public ListBoxModel doFillActionDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception, com.coverity.ws.v9.CovRemoteServiceException_Exception {
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

        public ListBoxModel doFillSeveritiesDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception, com.coverity.ws.v9.CovRemoteServiceException_Exception {
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

        public ListBoxModel doFillComponentDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId) throws IOException, CovRemoteServiceException_Exception, com.coverity.ws.v9.CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null && !StringUtils.isEmpty(streamId)) {
                StreamDataObj stream = instance.getStream(streamId);
                String componentMapId = stream.getComponentMapId().getName();

                com.coverity.ws.v9.ComponentMapFilterSpecDataObj componentMapFilterSpec = new com.coverity.ws.v9.ComponentMapFilterSpecDataObj();
                componentMapFilterSpec.setNamePattern(componentMapId);
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

            try {
                // Retrieve all defects for a specific cim instance.
                String cs = instance.getCimInstanceCheckers();
                return getPublisherDescriptor().split(cs);
            } catch(Exception e) {
                return new ListBoxModel();
            }
        }
    }
}
