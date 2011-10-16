package jenkins.plugins.coverity;

import com.coverity.ws.v3.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import hudson.*;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This publisher optionally invokes cov-analyze/cov-analyze-java and cov-commit-defects.
 * Afterwards the latest list of defects is queried from the webservice, filtered, and attached to the build.
 * If defects are found, the build can be flagged as failed and a mail is sent.
 */
public class CoverityPublisher extends Recorder {

    /**
     * ID of the CIM instance used
     */
    private final String cimInstance;

    /**
     * Configuration for the invocation assistance feature. Null if this should not be used.
     */
    private final InvocationAssistance invocationAssistance;

    /**
     * Name of Coverity project corresponding to this build.
     */
    private final String project;

    /**
     * Name of Coverity stream
     */
    private final String stream;

    /**
     * Should the build be marked as failed if defects are present ?
     */
    private final boolean failBuild;

    /**
     * Defines how to filter discovered defects. Null for no filtering.
     */
    private final DefectFilters defectFilters;

    private final CoverityMailSender mailSender;

    @DataBoundConstructor
    public CoverityPublisher(String cimInstance, InvocationAssistance invocationAssistance, String project, String stream, boolean failBuild, DefectFilters defectFilters, CoverityMailSender mailSender) {
        this.cimInstance = cimInstance;
        this.invocationAssistance = invocationAssistance;
        this.project = project;
        this.stream = stream;
        this.defectFilters = defectFilters;
        this.failBuild = failBuild;
        this.mailSender = mailSender;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.STEP;
    }

    public String getCimInstance() {
        return cimInstance;
    }

    public InvocationAssistance getInvocationAssistance() {
        return invocationAssistance;
    }

    public String getProject() {
        return project;
    }

    public String getStream() {
        return stream;
    }

    public boolean isFailBuild() {
        return failBuild;
    }

    public DefectFilters getDefectFilters() {
        return defectFilters;
    }

    public CoverityMailSender getMailSender() {
        return mailSender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoverityPublisher that = (CoverityPublisher) o;

        if (!cimInstance.equals(that.cimInstance)) return false;
        if (defectFilters != null ? !defectFilters.equals(that.defectFilters) : that.defectFilters != null)
            return false;
        if (invocationAssistance != null ? !invocationAssistance.equals(that.invocationAssistance) : that.invocationAssistance != null)
            return false;
        if (!project.equals(that.project)) return false;
        if (!stream.equals(that.stream)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cimInstance.hashCode();
        result = 31 * result + (invocationAssistance != null ? invocationAssistance.hashCode() : 0);
        result = 31 * result + project.hashCode();
        result = 31 * result + stream.hashCode();
        result = 31 * result + (defectFilters != null ? defectFilters.hashCode() : 0);
        return result;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new CoverityProjectAction(project);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        if (build.getResult().isWorseOrEqualTo(Result.FAILURE)) return true;

        CIMInstance cim = getDescriptor().getInstance(cimInstance);

        CoverityTempDir temp = null;
        String language = null;
        try {
            language = cim.getStream(stream).getLanguage();
        } catch (CovRemoteServiceException_Exception e) {
            e.printStackTrace(listener.error("Error while retrieving stream information for " + stream));
            return false;
        }
        if (invocationAssistance != null) {
            try {
                String covAnalyze = "JAVA".equals(language) ? "cov-analyze-java" : "cov-analyze";
                String covCommitDefects = "cov-commit-defects";

                Node node = Executor.currentExecutor().getOwner().getNode();
                String home = getDescriptor().getHome(node, build.getEnvironment(listener));
                if (home != null) {
                    covAnalyze = new FilePath(launcher.getChannel(), home).child("bin").child(covAnalyze).getRemote();
                    covCommitDefects = new FilePath(launcher.getChannel(), home).child("bin").child(covCommitDefects).getRemote();
                }

                CoverityLauncherDecorator.SKIP.set(true);
                temp = build.getAction(CoverityTempDir.class);

                List<String> cmd = new ArrayList<String>();
                cmd.add(covAnalyze);
                cmd.add("--dir");
                cmd.add(temp.tempDir.getRemote());
                if (invocationAssistance.getAnalyzeArguments() != null) {
                    for (String arg : Util.tokenize(invocationAssistance.getAnalyzeArguments())) {
                        cmd.add(arg);
                    }
                }

                int result = launcher.
                        launch().
                        cmds(new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]))).
                        stdout(listener).
                        join();
                if (result != 0) {
                    listener.getLogger().println("[Coverity] " + covAnalyze + " returned " + result + ", aborting...");
                    build.setResult(Result.FAILURE);
                    return false;
                }

                cmd = new ArrayList<String>();
                cmd.add(covCommitDefects);
                cmd.add("--dir");
                cmd.add(temp.tempDir.getRemote());
                cmd.add("--host");
                cmd.add(cim.getHost());
                cmd.add("--port");
                cmd.add(Integer.toString(cim.getPort()));
                cmd.add("--stream");
                cmd.add(stream);
                cmd.add("--user");
                cmd.add(cim.getUser());

                if (invocationAssistance.getCommitArguments() != null) {
                    for (String arg : Util.tokenize(invocationAssistance.getCommitArguments())) {
                        cmd.add(arg);
                    }
                }

                ArgumentListBuilder args = new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]));

                result = launcher.
                        launch().
                        cmds(args).
                        envs(Collections.singletonMap("COVERITY_PASSPHRASE", cim.getPassword())).
                        stdout(listener).
                        stderr(listener.getLogger()).
                        join();

                if (result != 0) {
                    listener.getLogger().println("[Coverity] cov-commit-defects returned " + result + ", aborting...");

                    build.setResult(Result.FAILURE);
                    return false;
                }
            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
                if (temp != null) temp.tempDir.deleteRecursive();
            }
        }

        Pattern snapshotPattern = Pattern.compile(".*New snapshot ID (\\d*) added.");
        BufferedReader reader = new BufferedReader(build.getLogReader());
        String line = null;
        long snapshotId = -1;
        try {
            while ((line = reader.readLine()) != null && snapshotId < 0) {
                Matcher m = snapshotPattern.matcher(line);
                if (m.matches()) {
                    snapshotId = Long.parseLong(m.group(1));
                }
            }
        } finally {
            reader.close();
        }

        if (snapshotId == -1) {
            listener.getLogger().println("[Coverity] No snapshot ID found in build log");
            build.setResult(Result.FAILURE);
            return false;
        }

        listener.getLogger().println("[Coverity] Found snapshot ID " + snapshotId);


        try {
            listener.getLogger().println("[Coverity] Fetching defects for stream " + stream);
            StreamIdDataObj streamId = new StreamIdDataObj();
            streamId.setName(stream);
            streamId.setType("STATIC");
            SnapshotIdDataObj snapshot = new SnapshotIdDataObj();
            snapshot.setId(snapshotId);
            List<Long> cids = cim.getDefectService().getCIDsForSnapshot(snapshot);
            listener.getLogger().println("[Coverity] Found " + cids.size() + " defects");

            List<MergedDefectDataObj> defects = cim.getDefects(stream, cids);

            Set<String> checkers = new HashSet<String>();
            for (MergedDefectDataObj defect : defects) {
                checkers.add(defect.getChecker());
            }
            getDescriptor().updateCheckers(language, checkers);

            List<Long> matchingDefects = new ArrayList<Long>();

            XStream xs = new XStream2();

            if (defectFilters != null) {
                listener.getLogger().println("matching defects against filters: " + xs.toXML(defectFilters));
            }
            for (MergedDefectDataObj defect : defects) {
                if (defectFilters == null) {
                    matchingDefects.add(defect.getCid());
                } else {
                    listener.getLogger().println("checking defect: " + xs.toXML(defect));
                    boolean match = defectFilters.matches(defect);
                    listener.getLogger().println("defect match:" + match);
                    if (match) {
                        matchingDefects.add(defect.getCid());
                    }
                }
            }

            if (!matchingDefects.isEmpty()) {
                listener.getLogger().println("[Coverity] Found " + matchingDefects.size() + " defects matching all filters: " + matchingDefects);
                if (failBuild) {
                    if (build.getResult().isBetterThan(Result.FAILURE)) {
                        build.setResult(Result.FAILURE);
                    }
                }
            } else {
                listener.getLogger().println("[Coverity] No defects matched all filters.");
            }

            CoverityBuildAction action = new CoverityBuildAction(build, project, stream, cimInstance, matchingDefects);
            build.addAction(action);

            if (!matchingDefects.isEmpty() && mailSender != null) {
                mailSender.execute(action, listener);
            }

            String rootUrl = Hudson.getInstance().getRootUrl();
            if (rootUrl != null) {
                listener.getLogger().println("Coverity details: " + Hudson.getInstance().getRootUrl() + build.getUrl() + action.getUrlName());
            }

        } catch (CovRemoteServiceException_Exception e) {
            e.printStackTrace(listener.error("[Coverity] An error occurred while fetching defects"));
            build.setResult(Result.FAILURE);
            return false;
        }
        return true;
    }

    public String getLanguage() throws IOException, CovRemoteServiceException_Exception {
        return getDescriptor().getInstance(cimInstance).getStream(stream).getLanguage();
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        private List<CIMInstance> instances = new ArrayList<CIMInstance>();
        private String home;

        private String javaCheckers;
        private String cxxCheckers;

        public DescriptorImpl() {
            super(CoverityPublisher.class);
            load();

            setJavaCheckers(javaCheckers);
            setCxxCheckers(cxxCheckers);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);

            home = Util.fixEmpty(home);

            save();

            return true;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getJavaCheckers() {
            return javaCheckers;
        }

        public void setJavaCheckers(String javaCheckers) {
            this.javaCheckers = Util.fixEmpty(javaCheckers);
            try {
                if (this.javaCheckers == null)
                    this.javaCheckers = IOUtils.toString(getClass().getResourceAsStream("java-checkers.txt"));
            } catch (IOException e) {
            }
        }

        public String getCxxCheckers() {
            return cxxCheckers;
        }

        public void setCxxCheckers(String cxxCheckers) {
            this.cxxCheckers = Util.fixEmpty(cxxCheckers);
            try {
                if (this.cxxCheckers == null)
                    this.cxxCheckers = IOUtils.toString(getClass().getResourceAsStream("cxx-checkers.txt"));
            } catch (IOException e) {
            }
        }

        public String getHome(Node node, EnvVars environment) {
            CoverityInstallation install = node.getNodeProperties().get(CoverityInstallation.class);
            if (install != null) {
                return install.forEnvironment(environment).getHome();
            } else if (home != null) {
                return new CoverityInstallation(home).forEnvironment(environment).getHome();
            } else {
                return null;
            }
        }

        public void setInstances(List<CIMInstance> instances) {
            this.instances = instances;
        }

        public List<CIMInstance> getInstances() {
            return instances;
        }

        public CIMInstance getInstance(String name) {
            for (CIMInstance instance : instances) {
                if (instance.getName().equals(name)) {
                    return instance;
                }
            }
            return null;
        }


        @Override
        public String getDisplayName() {
            return "Coverity";
        }

        public FormValidation doCheckInstance(@QueryParameter String host, @QueryParameter int port, @QueryParameter boolean useSSL, @QueryParameter String user, @QueryParameter String password) throws IOException {
            return new CIMInstance("", host, port, user, password, useSSL).doCheck();
        }

        public FormValidation doCheckCutOffDate(@QueryParameter String value) throws FormException {
            try {
                if (!StringUtils.isEmpty(value)) new SimpleDateFormat("yyyy-MM-dd").parse(value);
                return FormValidation.ok();
            } catch (ParseException e) {
                return FormValidation.error("yyyy-MM-dd expected");
            }
        }

        public ListBoxModel doFillCimInstanceItems() {
            ListBoxModel result = new ListBoxModel();
            result.add("");
            for (CIMInstance instance : instances) {
                result.add(instance.getName());
            }
            return result;
        }

        public ListBoxModel doFillProjectItems(@QueryParameter String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if (!StringUtils.isEmpty(cimInstance)) {
                for (ProjectDataObj project : getInstance(cimInstance).getProjects()) {
                    // don't add projects for which there are no valid streams
                    ListBoxModel streams = doFillStreamItems(cimInstance, project.getId().getName());
                    if (!streams.isEmpty()) {
                        result.add(project.getId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillStreamItems(@QueryParameter String cimInstance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if (StringUtils.isEmpty(project)) return result;
            CIMInstance instance = getInstance(cimInstance);
            if (instance != null) {
                for (StreamDataObj stream : instance.getStaticStreams(project)) {
                    if ("JAVA".equals(stream.getLanguage()) || "CXX".equals(stream.getLanguage())) {
                        result.add(stream.getId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillActionDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if (instance != null) {
                for (String action : getInstance(cimInstance).getConfigurationService().getActions()) {
                    result.add(action);
                }
            }
            return result;
        }

        public ListBoxModel doFillSeveritiesDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if (instance != null) {
                for (String action : getInstance(cimInstance).getConfigurationService().getSeverities()) {
                    result.add(action);
                }
            }
            return result;
        }

        public ListBoxModel doFillComponentDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if (instance != null && !StringUtils.isEmpty(streamId)) {
                StreamDataObj stream = instance.getStream(streamId);
                String componentMapId = stream.getComponentMapId().getName();

                ComponentMapFilterSpecDataObj componentMapFilterSpec = new ComponentMapFilterSpecDataObj();
                componentMapFilterSpec.setNamePattern(componentMapId);
                for (ComponentMapDataObj map : instance.getConfigurationService().getComponentMaps(componentMapFilterSpec)) {
                    for (ComponentDataObj component : map.getComponents()) {
                        result.add(component.getComponentId().getName());
                    }
                }
            }
            return result;
        }

        public ListBoxModel doFillCheckerDefectFilterItems(@QueryParameter(value = "../cimInstance") String cimInstance, @QueryParameter(value = "../stream") String streamId) throws IOException, CovRemoteServiceException_Exception {
            if (StringUtils.isEmpty(streamId)) return new ListBoxModel();
            CIMInstance instance = getInstance(cimInstance);
            if (instance == null) return new ListBoxModel();

            StreamDataObj stream = instance.getStream(streamId);
            String type = stream.getLanguage();
            if ("CXX".equals(type)) {
                return split(cxxCheckers);
            } else if ("JAVA".equals(type)) {
                return split(javaCheckers);
            } else {
                return new ListBoxModel();
            }
        }

        private ListBoxModel split(String string) {
            ListBoxModel result = new ListBoxModel();
            for (String s: string.split("[\r\n]")) {
                s = Util.fixEmptyAndTrim(s);
                if (s != null) {
                    result.add(s);
                }
            }
            return result;
        }
        private Set<String> split2(String string) {
            Set<String> result = new TreeSet<String>();
            for (String s: string.split("[\r\n]")) {
                s = Util.fixEmptyAndTrim(s);
                if (s != null) {
                    result.add(s);
                }
            }
            return result;
        }

        public void doDefectFiltersConfig(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
            req.setAttribute("descriptor", this);

            JSONObject json = req.getSubmittedForm().getJSONObject(getJsonSafeClassName());
            if (json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                if (StringUtils.isEmpty(publisher.getCimInstance()) ||StringUtils.isEmpty(publisher.getStream()) ||StringUtils.isEmpty(publisher.getProject())) {

                } else
                try {
                    String language = publisher.getLanguage();
                    Set<String> allCheckers = split2(getCheckers(language));
                    publisher.getDefectFilters().invertCheckers(
                            allCheckers,
                            toStrings(doFillActionDefectFilterItems(publisher.getCimInstance())),
                            toStrings(doFillSeveritiesDefectFilterItems(publisher.getCimInstance())),
                            toStrings(doFillComponentDefectFilterItems(publisher.getCimInstance(), publisher.getStream()))
                            );
                } catch (CovRemoteServiceException_Exception e) {
                    throw new IOException(e);
                }
                req.setAttribute("instance", publisher);
            }
            rsp.forward(this, "defectFilters", req);
        }

        public FormValidation doCheckDate(@QueryParameter String date) {
            try {
                if (!StringUtils.isEmpty(date.trim())) {
                    new SimpleDateFormat("yyyy-MM-dd").parse(date);
                }
                return FormValidation.ok();
            } catch (ParseException e) {
                return FormValidation.error("Date in yyyy-mm-dd format expected");
            }
        }

        public String getCheckers(String language) {
            if ("CXX".equals(language)) return cxxCheckers;
            if ("JAVA".equals(language)) return javaCheckers;
            throw new IllegalArgumentException("Unknown language: " + language);
        }

        public void setCheckers(String language, Set<String> checkers) {
            if ("CXX".equals(language)) {
                cxxCheckers = join(checkers);
            } else if ("JAVA".equals(language)) {
                javaCheckers = join(checkers);
            } else {
                throw new IllegalArgumentException(language);
            }
        }

        public void updateCheckers(String language, Set<String> checkers) {
            String oldCheckers = getCheckers(language);

            Set<String> newCheckers = new TreeSet<String>();
            Set<String> c = new TreeSet<String>();
            for (ListBoxModel.Option s: split(oldCheckers)) c.add(s.name);
            for (String s: checkers) {
                if (c.add(s)) {
                    newCheckers.add(s);
                }
            }
            setCheckers(language, c);

            save();
        }

        private String join(Collection<String> c) {
            StringBuffer result =new StringBuffer();
            for (String s: c) result.append(s).append("\n");
            return result.toString();
        }

        @Override
        public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            String cutOffDate = Util.fixEmpty(req.getParameter("cutOffDate"));
            try {
                if (cutOffDate != null) new SimpleDateFormat("yyyy-MM-dd").parse(cutOffDate);
            } catch (ParseException e) {
                throw new Descriptor.FormException("Could not parse date '" + cutOffDate + "', yyyy-MM-dd expected", "cutOffDate");
            }
            CoverityPublisher publisher = (CoverityPublisher) super.newInstance(req, formData);

            try {
                String language = publisher.getLanguage();
                Set<String> allCheckers = split2(getCheckers(language));
                publisher.getDefectFilters().invertCheckers(
                        allCheckers,
                        toStrings(doFillActionDefectFilterItems(publisher.getCimInstance())),
                        toStrings(doFillSeveritiesDefectFilterItems(publisher.getCimInstance())),
                        toStrings(doFillComponentDefectFilterItems(publisher.getCimInstance(), publisher.getStream()))
                        );

                return publisher;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static List<String> toStrings(ListBoxModel list) {
            List<String> result = new ArrayList<String>();
            for (ListBoxModel.Option option:list) result.add(option.name);
            return result;
        }
    }


}
