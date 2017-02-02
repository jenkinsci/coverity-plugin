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
import jenkins.plugins.coverity.ws.CimCache;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        public FormValidation doCheckInstance(@QueryParameter String instance, @QueryParameter String id) throws IOException, CovRemoteServiceException_Exception {
            CIMInstance cimInstance = getInstance(instance);

            if (cimInstance != null) {
                FormValidation checkResult = cimInstance.doCheck();

                // initialize cache for instance
                CimCache.getInstance().cacheInstance(cimInstance);

                if (id != null) {
                    Map<String, String> cims = new HashMap<>();
                    cims.put(id, instance);
                }

                // return FormValidation.ok in order to suppress any success messages, these don't need to show automatically here
                return checkResult.kind.equals(FormValidation.Kind.OK) ? FormValidation.ok() : checkResult;
            }
            return FormValidation.warning("Coverity Connect instance is required to select project and stream");
        }

//        public ListBoxModel doFillProjectItems(@QueryParameter String instance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
//            ListBoxModel result = new ListBoxModel();
//            CIMInstance cimInstance = getInstance(instance);
//            if(cimInstance != null) {
//                for (String projectName : CimCache.getInstance().getProjects(cimInstance)) {
//                    result.add(projectName);
//                }
//            }
//            // add project if it is not contained (instead of dropping value and clearing item)
//            if (!StringUtils.isEmpty(project) && !result.contains(project)){
//                result.add(project);
//            }
//            return result;
//        }

        public FormValidation doCheckProject(@QueryParameter String instance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
            // allow initial empty project selection
            if (StringUtils.isEmpty(project))
                return FormValidation.ok();

            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null){
                for (String projectName : CimCache.getInstance().getProjects(cimInstance)){
                    if (projectName.equalsIgnoreCase(project)){
                        return FormValidation.ok();
                    }
                }
            }
            return FormValidation.error("Project [ " + project + " ] is not found");
        }

//        public ListBoxModel doFillStreamItems(@QueryParameter String project, @QueryParameter String instance) throws IOException, CovRemoteServiceException_Exception {
//            ListBoxModel result = new ListBoxModel();
//
//            for (String stream : getStreams(instance, project, null)) {
//                result.add(stream);
//            }
//
//            return result;
//        }

        private List<String> getStreams(String instance, String project, String stream) throws IOException, CovRemoteServiceException_Exception {
            List<String> result = new ArrayList<>();
            if (StringUtils.isEmpty(project)) return result;
            CIMInstance cimInstance = getInstance(instance);
            if (cimInstance != null) {
                for (String streamName : CimCache.getInstance().getStreams(cimInstance, project)) {
                    result.add(streamName);
                }
            }
            // add stream if it is not contained (instead of dropping value and clearing item)
            if (!StringUtils.isEmpty(stream) && !result.contains(stream)) {
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
