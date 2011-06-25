package jenkins.plugins.coverity;

import com.coverity.ws.v3.*;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.kohsuke.stapler.*;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 4/06/11
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class CIMInstance implements Describable<CIMInstance> {

    private final String name;
    private final String host;
    private final int port;
    private final String user;
    private final String password;

    private transient AdministrationServiceService administrationServiceService;
    private transient DefectServiceService defectServiceService;
    private transient ConfigurationServiceService configurationServiceService;
    private List<ProjectDataObj[]> projects;

    @DataBoundConstructor
    public CIMInstance(String name, String host, int port, String user, String password) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public URL getURL() throws MalformedURLException {
        return new URL("http", host, port, "/");
    }

    public DefectService getDefectService() throws IOException {
        synchronized (this) {
            if (defectServiceService == null) {
            // Create a Web Services port to the server
                defectServiceService = new DefectServiceService(
                    new URL(getURL(), "/ws/v3/defectservice?wsdl"),
                    new QName("http://ws.coverity.com/v3", "DefectServiceService"));
            }
        }

        DefectService defectService = defectServiceService.getDefectServicePort();

        // Attach an authentication handler to it
        BindingProvider bindingProvider = (BindingProvider)defectService;
        bindingProvider.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(user, password)));

        return defectService;
    }

    public ConfigurationService getConfigurationService() throws IOException {
        synchronized (this) {
            if (configurationServiceService == null) {
            // Create a Web Services port to the server
                configurationServiceService = new ConfigurationServiceService(
                    new URL(getURL(), "/ws/v3/configurationservice?wsdl"),
                    new QName("http://ws.coverity.com/v3", "ConfigurationServiceService"));
            }
        }

        ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();

        // Attach an authentication handler to it
        BindingProvider bindingProvider = (BindingProvider)configurationService;
        bindingProvider.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(user, password)));

        return configurationService;
    }

    public AdministrationService getAdministrationService() throws IOException {
        synchronized (this) {
            if (administrationServiceService == null) {
            // Create a Web Services port to the server
                administrationServiceService = new AdministrationServiceService(
                    new URL(getURL(), "/ws/v3/administrationservice?wsdl"),
                    new QName("http://ws.coverity.com/v3", "AdministrationServiceService"));
            }
        }

        AdministrationService administrationService = administrationServiceService.getAdministrationServicePort();

        // Attach an authentication handler to it
        BindingProvider bindingProvider = (BindingProvider) administrationService;
        bindingProvider.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(user, password)));

        return administrationService;
    }

    public List<MergedDefectDataObj> getDefects(String streamId, List<Long> defectIds) throws IOException, CovRemoteServiceException_Exception {
        MergedDefectFilterSpecDataObj filterSpec1 = new MergedDefectFilterSpecDataObj();
        StreamIdDataObj stream = new StreamIdDataObj();
        stream.setName(streamId);
        stream.setType("STATIC");
        filterSpec1.getStreamIdIncludeList().add(stream);
        PageSpecDataObj pageSpec = new PageSpecDataObj();
        pageSpec.setPageSize(2500);

        List<MergedDefectDataObj> result =new ArrayList<MergedDefectDataObj>();
        int defectCount = 0;
        MergedDefectsPageDataObj defects = null;
        do {
            pageSpec.setStartIndex(defectCount);
            defects = getDefectService().getMergedDefectsForStreams(Arrays.asList(stream), filterSpec1, pageSpec);
            for (MergedDefectDataObj defect: defects.getMergedDefects()) {
                if (defectIds.contains(defect.getCid())) {
                    result.add(defect);
                }
            }
            defectCount += defects.getMergedDefects().size();
        } while (defectCount < defects.getTotalNumberOfRecords());

        return result;
    }

    public ProjectDataObj getProject(String projectId) throws IOException, CovRemoteServiceException_Exception {
        ProjectFilterSpecDataObj filterSpec = new ProjectFilterSpecDataObj();
        filterSpec.setNamePattern(projectId);
        List<ProjectDataObj> projects = getConfigurationService().getProjects(filterSpec);
        if (projects.size() == 0) {
            return null;
        } else {
            return projects.get(0);
        }
    }

    private transient Map<String,Long> projectKeys;

    public Long getProjectKey(String projectId) throws IOException, CovRemoteServiceException_Exception {
        if (projectKeys == null) {
            projectKeys = new ConcurrentHashMap<String, Long> ();
        }

        Long result = projectKeys.get(projectId);
        if (result == null) {
            result = getProject(projectId).getProjectKey();
            projectKeys.put(projectId, result);
        }
        return result;
    }

    public List<ProjectDataObj> getProjects() throws IOException, CovRemoteServiceException_Exception {
        return getConfigurationService().getProjects(new ProjectFilterSpecDataObj());
    }

    public List<StreamDataObj> getStaticStreams(String projectId) throws IOException, CovRemoteServiceException_Exception {
        /*
            to get a list of all static streams for one project,
            - get a list of streams for the project
            - get a list of all static streams
            - compute the intersection
            there should be a better way, but I can't see it.

            you can't filter on project, and StreamDataObj does not have project or type information
         */


        ProjectDataObj project = getProject(projectId);
        Set<String> projectStreams = new HashSet<String>();
        for (StreamDataObj stream: project.getStreams()) {
            projectStreams.add(stream.getId().getName());
        }
        List<StreamDataObj> result = new ArrayList<StreamDataObj>();
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.getTypeList().add("STATIC");

        for (StreamDataObj stream: getConfigurationService().getStreams(filter)) {
            if (projectStreams.contains(stream.getId().getName())) {
                result.add(stream);
            }
        }

        return result;
    }

    public StreamDataObj getStream(String streamId) throws IOException, CovRemoteServiceException_Exception {
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.getTypeList().add("STATIC");
        filter.setNamePattern(streamId);

        List<StreamDataObj> streams = getConfigurationService().getStreams(filter);
        if (streams.isEmpty()) {
            return null;
        } else {
            return streams.get(0);
        }
    }

    public Descriptor<CIMInstance> getDescriptor() {
        return Hudson.getInstance().getDescriptor(CIMInstance.class);
    }

    public static final DescriptorImpl DESCRIPTOR = Hudson.getInstance().getDescriptorByType(DescriptorImpl.class);

    @Extension
    public static class DescriptorImpl extends Descriptor<CIMInstance> {

        private List<CIMInstance> instances = new ArrayList<CIMInstance>();

        public DescriptorImpl() {
            super(CIMInstance.class);

            load();
        }

        public String getDisplayName() {
            return "Coverity CIM Instances";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);

            save();

            return true;
        }

        public void setInstances(List<CIMInstance> instances) {
            this.instances = instances;
        }

        public List<CIMInstance> getInstances() {
            return instances;
        }

        public CIMInstance getInstance(String name) {
            for (CIMInstance instance: instances) {
                if (instance.getName().equals(name)) {
                    return instance;
                }
            }
            throw new IllegalArgumentException("Unknown instance " + name);
        }

        public FormValidation doCheck(@QueryParameter String host, @QueryParameter int port, @QueryParameter String user, @QueryParameter String password) throws IOException {
            CIMInstance instance = new CIMInstance("", host, port, user, password);

            try {
                int responseCode = getURLResponseCode(new URL(instance.getURL(), "/ws/v3/administrationservice?wsdl"));
                if (responseCode != 200) {
                    if (getURLResponseCode(new URL(instance.getURL(), "/ws/v2/administrationservice?wsdl")) == 200) {
                        return FormValidation.error("Detected Coverity web services v2, but v3 is required");
                    } else if (getURLResponseCode(new URL(instance.getURL(), "/ws/v1/administrationservice?wsdl")) == 200) {
                        return FormValidation.error("Detected Coverity web services v1, but v3 is required");
                    }
                }
                instance.getAdministrationService().getServerTime();
                return FormValidation.ok("Successfully connected to server.");
            } catch (UnknownHostException e) {
                return FormValidation.error("Host name unknown");
            } catch (ConnectException e) {
                return FormValidation.error("Connection refused");
            } catch (Exception e) {
                return error(e);
            }
        }

        private FormValidation error(Throwable t) {
            if (Hudson.getInstance().hasPermission(Hudson.ADMINISTER)) {
                return FormValidation.error(t, t.getMessage());
            } else {
                return FormValidation.error(t.getMessage());
            }
        }

        private int getURLResponseCode(URL url) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
            return conn.getResponseCode();

        }

    }



}
