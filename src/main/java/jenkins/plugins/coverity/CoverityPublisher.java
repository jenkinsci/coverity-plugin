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

import com.coverity.ws.v9.StreamDataObj;
import com.coverity.ws.v9.StreamFilterSpecDataObj;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.plugins.coverity.CoverityTool.CoverityToolHandler;
import jenkins.plugins.coverity.ws.CimCache;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.xml.ws.WebServiceException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * This publisher optionally invokes cov-analyze/cov-analyze-java and cov-commit-defects. Afterwards the latest list of
 * defects is queried from the webservice, filtered, and attached to the build. If defects are found, the build can be
 * flagged as failed and a mail is sent.
 */
public class CoverityPublisher extends Recorder {

    private static final Logger logger = Logger.getLogger(CoverityPublisher.class.getName());

    // deprecated fields which were removed in plugin version 1.2
    private transient String cimInstance;
    private transient String project;
    private transient String stream;
    private transient DefectFilters defectFilters;

    /**
     * List of CIM streams configured
     */
    private List<CIMStream> cimStreams;
    /**
     * Configuration for the invocation assistance feature. Null if this should not be used.
     */
    private final InvocationAssistance invocationAssistance;
    /**
     * Should the build be marked as failed if defects are present ?
     */
    private final boolean failBuild;

    /**
     * Should the build be marked as unstable if defects are present ?
     */
    private final boolean unstable;

    /**
     * Should the intermediate directory be preserved after each build?
     */
    private final boolean keepIntDir;
    /**
     * Should defects be fetched after each build? Enabling this prevents the build from being failed due to defects.
     */
    private final boolean skipFetchingDefects;
    /**
     * Hide the chart to make page loads faster
     */
    private final boolean hideChart;

    private final TaOptionBlock taOptionBlock;

    private final ScmOptionBlock scmOptionBlock;

    /**
     * Internal variable to notify the Publisher that the build should be marked as unstable
     * since we cannot set the build as unstable within the tool handler
     */
    private boolean unstableBuild;

    @DataBoundConstructor
    public CoverityPublisher(List<CIMStream> cimStreams,
                             InvocationAssistance invocationAssistance,
                             boolean failBuild,
                             boolean unstable,
                             boolean keepIntDir,
                             boolean skipFetchingDefects,
                             boolean hideChart,
                             TaOptionBlock taOptionBlock,
                             ScmOptionBlock scmOptionBlock) {
        this.cimStreams = cimStreams;
        this.invocationAssistance = invocationAssistance;
        this.failBuild = failBuild;
        this.unstable = unstable;
        this.keepIntDir = keepIntDir;
        this.skipFetchingDefects = skipFetchingDefects;
        this.hideChart = hideChart;
        this.taOptionBlock = taOptionBlock;
        this.scmOptionBlock = scmOptionBlock;
        this.unstableBuild = false;
    }

    /**
     * Implement readResolve to update the de-serialized object in the case transient data was found. Transient fields
     * will be read during de-serialization and readResolve allow updating the Publisher object after being created.
     */
    protected Object readResolve() {
        // Check for old data values cimInstance, project, stream, defectFilters being set
        if(cimInstance != null || project != null || stream != null || defectFilters != null) {
            logger.info("Old data format detected. Converting to new format.");
            convertTransientDataFields();
        }

        return this;
    }

    /**
     * Converts the old data values cimInstance, project, stream, defectFilters to a {@link CIMStream} object
     * and adds to the configured streams. Then trims any configured streams which are not valid (when a stream
     * is missing a instance, project or stream name it is not valid).
     */
    private void convertTransientDataFields() {
        CIMStream newcs = new CIMStream(cimInstance, project, stream, defectFilters, null, null);

        cimInstance = null;
        project = null;
        stream = null;
        defectFilters = null;

        if(cimStreams == null) {
            this.cimStreams = new ArrayList<CIMStream>();
        }
        cimStreams.add(newcs);
        trimInvalidStreams();
    }

    /**
     * Trims any configured streams which are not valid. A stream which is missing a instance, project
     * or stream name is not valid. Duplicate steams are also not valid.
     */
    private void trimInvalidStreams() {
        Iterator<CIMStream> i = getCimStreams().iterator();
        while(i.hasNext()) {
            CIMStream cs = i.next();
            if(!cs.isValid()) {
                i.remove();
                continue;
            }
            if(cs.getInstance().equals("null") && cs.getProject().equals("null") && cs.getStream().equals("null")) {
                i.remove();
                continue;
            }
        }

        //remove duplicates
        Set<CIMStream> temp = new LinkedHashSet<CIMStream>();
        temp.addAll(cimStreams);
        cimStreams.clear();
        cimStreams.addAll(temp);
    }

    public String getCimInstance() {
        return cimInstance;
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

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.STEP;
    }

    public InvocationAssistance getInvocationAssistance() {
        return invocationAssistance;
    }

    public boolean isFailBuild() {
        return failBuild;
    }

    public boolean isKeepIntDir() {
        return keepIntDir;
    }

    public boolean isSkipFetchingDefects() {
        return skipFetchingDefects;
    }

    public boolean isHideChart() {
        return hideChart;
    }

    public boolean isUnstable(){
        return unstable;
    }
    
    public boolean isUnstableBuild(){
            return unstableBuild;
    }

    public void setUnstableBuild(boolean unstable){
        unstableBuild = unstable;
    }

    public TaOptionBlock getTaOptionBlock(){return taOptionBlock;}

    public ScmOptionBlock getScmOptionBlock(){return scmOptionBlock;}

    public List<CIMStream> getCimStreams() {
        if(cimStreams == null) {
            return new ArrayList<CIMStream>();
        }
        return cimStreams;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return hideChart ? super.getProjectAction(project) : new CoverityProjectAction(project);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        // set initial state for unstable build to false
        this.unstableBuild = false;

        if(build.getResult().isWorseOrEqualTo(Result.FAILURE)) return true;

        try{
            CoverityVersion version = CheckConfig.checkNode(this, build, launcher, listener).getVersion();

            if(version == null){
                throw new Exception("Coverity Version is null. Please verify the version file under your Coverity Analysis installation.");
            }

            CoverityToolHandler cth = new CoverityToolHandler(version);
            cth.perform(build, launcher, listener, this);
            
            if(isUnstableBuild()){
                build.setResult(Result.UNSTABLE);
            }

            // Delete intermediate directory unless user checked to preserve the intermediate directory option.
            // Deletion of the intermediate directory will occurr regardless of the result of the build job.
            deleteIntermediateDirectory(listener, build.getAction(CoverityTempDir.class));

            return true;
        } catch(com.coverity.ws.v9.CovRemoteServiceException_Exception e){
            CoverityUtils.handleException("Cov Remote Service Error: \n" + e.getMessage(), build, listener, e);
            return false;
        } catch (Exception e) {
            CoverityUtils.handleException("Exception message: \n" + e.getMessage(), build, listener, e);
            return false;
        }
    }

    public StreamDataObj getStream(String streamId, CIMInstance cimInstance) throws IOException, com.coverity.ws.v9.CovRemoteServiceException_Exception {
        StreamFilterSpecDataObj filter = new StreamFilterSpecDataObj();
        filter.setNamePattern(streamId);

        List<StreamDataObj> streams = cimInstance.getConfigurationService().getStreams(filter);
        if(streams.isEmpty()) {
            return null;
        } else {
            return streams.get(0);
        }
    }

    public void deleteIntermediateDirectory(BuildListener listener, CoverityTempDir temp) {
        if (temp != null) {
            try{
                if(!isKeepIntDir() || temp.isDef()) {
                    listener.getLogger().println("[Coverity] deleting intermediate directory: " + temp.getTempDir());
                    temp.getTempDir().deleteRecursive();
                    listener.getLogger().println("[Coverity] deleting intermediate directory  \"" + temp.getTempDir() + "\" was successful");
                } else {
                    listener.getLogger().println("[Coverity] preserving intermediate directory: " + temp.getTempDir());
                }
            } catch (InterruptedException e) {
                listener.getLogger().println("[Coverity] Interrupted Exception occurred during deletion of intermediate directory: " + temp.getTempDir());
            } catch (IOException e) {
                listener.getLogger().println("[Coverity] IOException Exception occurred during deletion of intermediate directory: " + temp.getTempDir());
            }
        }
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        private List<CIMInstance> instances = new ArrayList<CIMInstance>();
        private String home;
        private SSLConfigurations sslConfigurations;

        public DescriptorImpl() {
            super(CoverityPublisher.class);
            load();
        }

        public static List<String> toStrings(ListBoxModel list) {
            List<String> result = new ArrayList<String>();
            for(ListBoxModel.Option option : list) result.add(option.name);
            return result;
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

        public void setSslConfigurations(SSLConfigurations sslConfigurations) {
            this.sslConfigurations = sslConfigurations;
        }

        public SSLConfigurations getSslConfigurations() {
            /**
             * Fix Bug:85629
             * If SSL were not configured that resulted on a null pointer exception that marked the build as a failure.
             * In the case SSL is not configured, by default SSL configurations would be set up to not trust self-signed
             * certificates and no CA file would be present.
             */
            if(this.sslConfigurations != null){
                return this.sslConfigurations;
            } else {
                return new SSLConfigurations(false, null);
            }
        }

        public String getHome(Node node, EnvVars environment) {
            CoverityInstallation install = node.getNodeProperties().get(CoverityInstallation.class);
            if(install != null) {
                return install.forEnvironment(environment).getHome();
            } else if(home != null) {
                return new CoverityInstallation(home).forEnvironment(environment).getHome();
            } else {
                return null;
            }
        }

        public List<CIMInstance> getInstances() {
            return instances;
        }

        public void setInstances(List<CIMInstance> instances) {
            this.instances = instances;
        }

        public CIMInstance getInstance(String name) {
            for(CIMInstance instance : instances) {
                if(instance.getName().equals(name)) {
                    return instance;
                }
            }
            return null;
        }

        @Override
        public String getDisplayName() {
            return "Coverity";
        }

        public FormValidation doCheckInstance(@QueryParameter String host, @QueryParameter int port, @QueryParameter String user, @QueryParameter String password, @QueryParameter boolean useSSL, @QueryParameter int dataPort) throws IOException {
            return new CIMInstance("", host, port, user, password, useSSL, dataPort).doCheck();
        }

        public FormValidation doCheckAnalysisLocation(@QueryParameter String home) throws IOException {
            File analysisDir = new File(home);
            File analysisVersionXml = new File(home, "VERSION.xml");
            if(analysisDir.exists()){
                if(analysisVersionXml.isFile()){
                    try {
                        // check the version file value and validate it is greater than minimum version
                        CoverityVersion version = CheckConfig.getVersion(new FilePath(analysisDir), null);

                        if(version.compareTo(CoverityVersion.MINIMUM_SUPPORTED_VERSION) < 0) {
                            return FormValidation.error("\"Coverity Static Analysis\" version " + version.toString() + " detected. " +
                                "The minimum supported version is " + CoverityVersion.MINIMUM_SUPPORTED_VERSION.getEffectiveVersion().toString());
                        }

                    } catch (InterruptedException e) {
                        return FormValidation.error("Unable to verify the \"Coverity Static Analysis\" directory version.");
                    }

                    return FormValidation.ok("Analysis installation directory has been verified.");
                } else{
                    return FormValidation.error("The specified \"Coverity Static Analysis\" directory doesn't contain a VERSION.xml file.");
                }
            } else{
                return FormValidation.error("The specified \"Coverity Static Analysis\" directory doesn't exists.");
            }
        }

        public FormValidation doCheckCutOffDate(@QueryParameter String value) throws FormException {
            try {
                if(!StringUtils.isEmpty(value)) new SimpleDateFormat("yyyy-MM-dd").parse(value);
                return FormValidation.ok();
            } catch(ParseException e) {
                return FormValidation.error("yyyy-MM-dd expected");
            }
        }

        public FormValidation doCheckDate(@QueryParameter String date) {
            try {
                if(!StringUtils.isEmpty(date.trim())) {
                    new SimpleDateFormat("yyyy-MM-dd").parse(date);
                }
                return FormValidation.ok();
            } catch(ParseException e) {
                return FormValidation.error("Date in yyyy-mm-dd format expected");
            }
        }

        @Override
        public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            logger.info(formData.toString());

            String cutOffDate = Util.fixEmpty(req.getParameter("cutOffDate"));
            try {
                if(cutOffDate != null) new SimpleDateFormat("yyyy-MM-dd").parse(cutOffDate);
            } catch(ParseException e) {
                throw new Descriptor.FormException("Could not parse date '" + cutOffDate + "', yyyy-MM-dd expected", "cutOffDate");
            }
            CoverityPublisher publisher = (CoverityPublisher) super.newInstance(req, formData);

            for(CIMStream current : publisher.getCimStreams()) {
                CIMStream.DescriptorImpl currentDescriptor = ((CIMStream.DescriptorImpl) current.getDescriptor());

                String cimInstance = current.getInstance();

                try {
                    if(current.isValid()) {
                        Set<String> allCheckers = new HashSet<>(getInstance(current.getInstance()).getCimInstanceCheckers());
                        DefectFilters defectFilters = current.getDefectFilters();
                        if(defectFilters != null) {
                            defectFilters.invertCheckers(
                                    allCheckers,
                                    toStrings(currentDescriptor.doFillClassificationDefectFilterItems(cimInstance)),
                                    toStrings(currentDescriptor.doFillActionDefectFilterItems(cimInstance)),
                                    toStrings(currentDescriptor.doFillSeveritiesDefectFilterItems(cimInstance)),
                                    toStrings(currentDescriptor.doFillComponentDefectFilterItems(cimInstance, current.getStream()))
                            );
                        }
                    }
                } catch (CovRemoteServiceException_Exception | WebServiceException e) {
                    throw new Descriptor.FormException(
                        "There was an exception from the configured Coverity Connect server (instance: " + cimInstance + "). Please verify the Coverity Connect instance configuration is valid.",
                        e,
                        "defectFilters");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return publisher;
        }

        private JSONObject getJSONClassObject(JSONObject o, String targetClass) {
            //try old-style json format
            JSONObject jsonA = o.getJSONObject(getJsonSafeClassName());
            if(jsonA == null || jsonA.toString().equals("null")) {
                //new style json format
                JSON jsonB = (JSON) o.get("publisher");
                if(jsonB.isArray()) {
                    JSONArray arr = (JSONArray) jsonB;
                    for(Object i : arr) {
                        JSONObject ji = (JSONObject) i;
                        if(targetClass.equals(ji.get("stapler-class"))) {
                            return ji;
                        }
                    }
                } else {
                    return (JSONObject) jsonB;
                }
            } else {
                return jsonA;
            }

            return null;
        }

        public void doCheckConfig(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
            JSONObject json = getJSONClassObject(req.getSubmittedForm(), getId());

            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);

                CheckConfig ccs = new CheckConfig(publisher, null, null, null);
                ccs.check();

                req.setAttribute("descriptor", ccs.getDescriptor());
                req.setAttribute("instance", ccs);

                rsp.forward(ccs.getDescriptor(), "checkConfig", req);
            }
        }

        public void doDefectFiltersConfig(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException, CovRemoteServiceException_Exception {
            logger.info(req.getSubmittedForm().toString());

            JSONObject json = getJSONClassObject(req.getSubmittedForm(), getId());

            CIMStream current = null;
            CIMStream.DescriptorImpl currentDescriptor = null;
            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                String id = req.getParameterMap().get("id")[0];
                for(CIMStream cs : publisher.getCimStreams()) {
                    if(id.equals(cs.getId())) {
                        current = cs;
                        break;
                    }
                }

                if (current != null)
                    currentDescriptor = ((CIMStream.DescriptorImpl) current.getDescriptor());

                if (currentDescriptor != null) {
                    if (StringUtils.isEmpty(current.getInstance())) {
                        //do nothing
                    } else if (StringUtils.isEmpty(current.getStream()) || StringUtils.isEmpty(current.getProject())) {
                        //initialize 'new' defectFilters item with default values selected
                        DefectFilters defectFilters = current.getDefectFilters();
                        if (defectFilters != null) {
                            Set<String> allCheckers = new HashSet<>(getInstance(current.getInstance()).getCimInstanceCheckers());
                            try {
                                current.getDefectFilters().invertCheckers(
                                    allCheckers,
                                    toStrings(currentDescriptor.doFillClassificationDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillActionDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillSeveritiesDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillComponentDefectFilterItems(current.getInstance(), current.getStream()))
                                );
                            } catch (CovRemoteServiceException_Exception e) {
                                throw new IOException(e);
                            }
                        }

                    } else {
                        Set<String> allCheckers = new HashSet<>(getInstance(current.getInstance()).getCimInstanceCheckers());
                        DefectFilters defectFilters = current.getDefectFilters();
                        if (defectFilters != null) {
                            try {
                                current.getDefectFilters().invertCheckers(
                                    allCheckers,
                                    toStrings(currentDescriptor.doFillClassificationDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillActionDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillSeveritiesDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillComponentDefectFilterItems(current.getInstance(), current.getStream()))
                                );
                            } catch (CovRemoteServiceException_Exception e) {
                                throw new IOException(e);
                            }
                        }
                    }
                    req.setAttribute("descriptor", currentDescriptor);
                    req.setAttribute("instance", current);
                    req.setAttribute("id", id);
                }
            }
            rsp.forward(currentDescriptor, "defectFilters", req);
        }

        @JavaScriptMethod
        public void doLoadProjectsForInstance(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {

            JSONObject json = getJSONClassObject(req.getSubmittedForm(), getId());

            CIMStream current = null;
            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                String id = req.getParameterMap().get("id")[0];
                for(CIMStream cs : publisher.getCimStreams()) {
                    if(id.equals(cs.getId())) {
                        current = cs;
                        break;
                    }
                }

                if (current != null) {
                    CIMInstance cimInstance = publisher.getDescriptor().getInstance(current.getInstance());
                    final List<String> projects = new ArrayList<>(CimCache.getInstance().getProjects(cimInstance));
                    final String currentProject = current.getProject();
                    boolean currentProjectIsvalid = true;
                    if (!StringUtils.isEmpty(currentProject) && !projects.contains(currentProject)) {
                        projects.add(currentProject);
                        currentProjectIsvalid = false;
                    }

                    req.setAttribute("id", id);

                    rsp.setContentType("application/json; charset=utf-8");
                    final ServletOutputStream outputStream = rsp.getOutputStream();

                    Collections.sort(projects, String.CASE_INSENSITIVE_ORDER);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("projects", projects);
                    responseObject.put("selectedProject", currentProject);
                    responseObject.put("validSelection", currentProjectIsvalid);

                    String jsonString = responseObject.toString();
                    outputStream.write(jsonString.getBytes("UTF-8"));
                }
            }
        }

        @JavaScriptMethod
        public void doLoadStreamsForProject(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {

            JSONObject json = getJSONClassObject(req.getSubmittedForm(), getId());

            CIMStream current = null;
            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                String id = req.getParameterMap().get("id")[0];
                for(CIMStream cs : publisher.getCimStreams()) {
                    if(id.equals(cs.getId())) {
                        current = cs;
                        break;
                    }
                }

                if (current != null) {
                    CIMInstance cimInstance = publisher.getDescriptor().getInstance(current.getInstance());
                    final List<String> streams = new ArrayList<>(CimCache.getInstance().getStreams(cimInstance ,current.getProject()));
                    final String currentStream = current.getStream();
                    boolean currentStreamIsvalid = true;

                    if (!StringUtils.isEmpty(currentStream) && !streams.contains(currentStream)) {
                        streams.add(currentStream);
                        currentStreamIsvalid = false;
                    }

                    req.setAttribute("id", id);

                    rsp.setContentType("application/json; charset=utf-8");
                    final ServletOutputStream outputStream = rsp.getOutputStream();

                    Collections.sort(streams, String.CASE_INSENSITIVE_ORDER);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("streams", streams);
                    responseObject.put("selectedStream", currentStream);
                    responseObject.put("validSelection", currentStreamIsvalid);

                    String jsonString = responseObject.toString();
                    outputStream.write(jsonString.getBytes("UTF-8"));
                }
            }
        }
    }
}
