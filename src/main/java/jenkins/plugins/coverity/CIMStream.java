package jenkins.plugins.coverity;

import com.coverity.ws.v6.AttributeDefinitionDataObj;
import com.coverity.ws.v6.AttributeDefinitionIdDataObj;
import com.coverity.ws.v6.AttributeValueDataObj;
import com.coverity.ws.v6.ComponentDataObj;
import com.coverity.ws.v6.ComponentMapDataObj;
import com.coverity.ws.v6.ComponentMapFilterSpecDataObj;
import com.coverity.ws.v6.CovRemoteServiceException_Exception;
import com.coverity.ws.v6.ProjectDataObj;
import com.coverity.ws.v6.StreamDataObj;
import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.ListBoxModel;
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
    private final String language;

    /**
     * Defines how to filter discovered defects. Null for no filtering.
     */
    private final DefectFilters defectFilters;

    private final InvocationAssistance invocationAssistanceOverride;

    @DataBoundConstructor
    public CIMStream(String instance, String project, String stream, DefectFilters defectFilters, String id, String language, InvocationAssistance invocationAssistanceOverride) {
        this.instance = Util.fixEmpty(instance);
        this.project = Util.fixEmpty(project);
        this.stream = Util.fixEmpty(stream);
        this.id = Util.fixEmpty(id);
        this.language = Util.fixEmpty(language);
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

    public String getLanguage() {
        return language;
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
                ", language='" + language + '\'' +
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
            return Hudson.getInstance().getDescriptorByType(CoverityPublisher.DescriptorImpl.class);
        }

        public List<CIMInstance> getInstances() {
            return getPublisherDescriptor().getInstances();
        }

        public CIMInstance getInstance(String name) {
            for(CIMInstance instance : getInstances()) {
                if(instance.getName().equals(name)) {
                    return instance;
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

        public ListBoxModel doFillProjectItems(@QueryParameter String instance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if(!StringUtils.isEmpty(instance)) {
                for(ProjectDataObj project : getInstance(instance).getProjects()) {
                    // don't add projects for which there are no valid streams
                    ListBoxModel streams = doFillStreamItems(instance, project.getId().getName());
                    if(!streams.isEmpty()) {
                        result.add(project.getId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillStreamItems(@QueryParameter String instance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if(StringUtils.isEmpty(project)) return result;
            CIMInstance cimInstance = getInstance(instance);
            if(cimInstance != null) {
                for(StreamDataObj stream : cimInstance.getStaticStreams(project)) {
                    if("MIXED".equals(stream.getLanguage()) || "JAVA".equals(stream.getLanguage()) || "CXX".equals(stream.getLanguage()) || "CSHARP".equals(stream.getLanguage())) {
                        result.add(stream.getId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillLanguageItems(@QueryParameter String instance, @QueryParameter String stream) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if(StringUtils.isEmpty(stream)) return result;
            CIMInstance cimInstance = getInstance(instance);
            if(cimInstance != null) {
                StreamDataObj str = cimInstance.getStream(stream);
                if("MIXED".equals(str.getLanguage())) {
                    result.add("ALL");
                    result.add("JAVA");
                    result.add("CXX");
                    result.add("CSHARP");
                } else {
                    result.add(str.getLanguage());
                }
            }
            return result;
        }

        public ListBoxModel doFillClassificationDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null) {
                AttributeDefinitionIdDataObj adido = new AttributeDefinitionIdDataObj();
                adido.setName("Classification");
                AttributeDefinitionDataObj addo = getInstance(cimInstance).getConfigurationService().getAttribute(adido);
                for(AttributeValueDataObj classification : addo.getConfigurableValues()) {
                    result.add(classification.getAttributeValueId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillActionDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null) {
                AttributeDefinitionIdDataObj adido = new AttributeDefinitionIdDataObj();
                adido.setName("Action");
                AttributeDefinitionDataObj addo = getInstance(cimInstance).getConfigurationService().getAttribute(adido);
                for(AttributeValueDataObj classification : addo.getConfigurableValues()) {
                    result.add(classification.getAttributeValueId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillSeveritiesDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null) {
                AttributeDefinitionIdDataObj adido = new AttributeDefinitionIdDataObj();
                adido.setName("Severity");
                AttributeDefinitionDataObj addo = getInstance(cimInstance).getConfigurationService().getAttribute(adido);
                for(AttributeValueDataObj classification : addo.getConfigurableValues()) {
                    result.add(classification.getAttributeValueId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillComponentDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance != null && !StringUtils.isEmpty(streamId)) {
                StreamDataObj stream = instance.getStream(streamId);
                String componentMapId = stream.getComponentMapId().getName();

                ComponentMapFilterSpecDataObj componentMapFilterSpec = new ComponentMapFilterSpecDataObj();
                componentMapFilterSpec.setNamePattern(componentMapId);
                for(ComponentMapDataObj map : instance.getConfigurationService().getComponentMaps(componentMapFilterSpec)) {
                    for(ComponentDataObj component : map.getComponents()) {
                        result.add(component.getComponentId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillCheckerDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId, @QueryParameter(value = "../language") String language) throws IOException, CovRemoteServiceException_Exception {
            if(StringUtils.isEmpty(streamId)) return new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if(instance == null) return new ListBoxModel();

            StreamDataObj stream = instance.getStream(streamId);
            String type = stream.getLanguage();

            if("MIXED".equals(type)) {
                type = language;
            }

            try {
                String cs = getPublisherDescriptor().getCheckers(type);
                return getPublisherDescriptor().split(cs);
            } catch(Exception e) {
                return new ListBoxModel();
            }
        }
    }
}
