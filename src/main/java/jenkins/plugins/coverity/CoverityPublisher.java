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

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.*;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.coverity.CoverityTool.CoverityToolHandler;
import jenkins.plugins.coverity.ws.CimCache;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.*;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.xml.ws.WebServiceException;

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

    // deprecated field removed in plugin version 1.9 (removed multiple CIMStreams)
    private transient List<CIMStream> cimStreams;

    /**
     * Configured CIM stream
     */
    private CIMStream cimStream;
    /**
     * Configuration for the invocation assistance feature. Null if this should not be used.
     */
    private InvocationAssistance invocationAssistance;
    /**
     * Should the build be marked as failed if defects are present ?
     */
    private boolean failBuild;

    /**
     * Should the build be marked as unstable if defects are present ?
     */
    private boolean unstable;

    /**
     * Should the intermediate directory be preserved after each build?
     */
    private boolean keepIntDir;
    /**
     * Should defects be fetched after each build? Enabling this prevents the build from being failed due to defects.
     */
    private boolean skipFetchingDefects;
    /**
     * Hide the chart to make page loads faster
     */
    private boolean hideChart;

    private TaOptionBlock taOptionBlock;

    private ScmOptionBlock scmOptionBlock;

    /**
     * Internal variable to notify the Publisher that the build should be marked as unstable
     * since we cannot set the build as unstable within the tool handler
     */
    private boolean unstableBuild;

    @DataBoundConstructor
    public CoverityPublisher(CIMStream cimStream) {
        this.cimStream = cimStream;
        this.unstableBuild = false;
    }

    /**
     * Implement readResolve to update the de-serialized object in the case transient data was found. Transient fields
     * will be read during de-serialization and readResolve allow updating the Publisher object after being created.
     */
    protected Object readResolve() {
        // Check for values removed in plugin version 1.2 (cimInstance, project, stream, defectFilters) set
        if(cimInstance != null || project != null || stream != null || defectFilters != null) {
            logger.info("Old data format detected. Converting to new format.");
            convertTransientDataFields();
            return this;
        }

        // Check for values removed in plugin version 1.9 (streams collection)
        if(cimStreams != null && !cimStreams.isEmpty()) {
            logger.info("Old data format detected. Converting to new format.");
            if (cimStreams.size() > 1) {
                logger.info("Found multiple commit streams configured. Discarding all but the first stream configured");
            }

            cimStream = cimStreams.get(0);
            cimStreams = null;

            // merge in any invocation assistance override values
            if (cimStream.getInvocationAssistanceOverride() != null) {
                this.invocationAssistance = this.getInvocationAssistance().merge(cimStream.getInvocationAssistanceOverride());
            }
        }

        return this;
    }

    /**
     * Converts the old data values cimInstance, project, stream, defectFilters (which were removed in plugin version 1.2)
     * to a {@link CIMStream} object
     */
    private void convertTransientDataFields() {
        CIMStream newcs = new CIMStream(cimInstance, project, stream);
        newcs.setDefectFilters(defectFilters);

        cimInstance = null;
        project = null;
        stream = null;
        defectFilters = null;

        if(cimStream == null) {
            this.cimStream = newcs;
        }
    }

    public CIMStream getCimStream() {
        return cimStream;
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

    @DataBoundSetter
    public void setInvocationAssistance(InvocationAssistance invocationAssistance){
        this.invocationAssistance = invocationAssistance;
    }

    public InvocationAssistance getInvocationAssistance() {
        return invocationAssistance;
    }

    @DataBoundSetter
    public void setFailBuild(boolean failBuild){
        this.failBuild = failBuild;
    }

    public boolean isFailBuild() {
        return failBuild;
    }

    @DataBoundSetter
    public void setKeepIntDir(boolean keepIntDir){
        this.keepIntDir = keepIntDir;
    }

    public boolean getKeepIntDir() {
        return keepIntDir;
    }

    @DataBoundSetter
    public void setSkipFetchingDefects(boolean skipFetchingDefects){
        this.skipFetchingDefects = skipFetchingDefects;
    }

    public boolean getSkipFetchingDefects() {
        return skipFetchingDefects;
    }

    @DataBoundSetter
    public void setHideChart(boolean hideChart){
        this.hideChart = hideChart;
    }

    public boolean getHideChart() {
        return hideChart;
    }

    @DataBoundSetter
    public void setUnstable(boolean unstable){
        this.unstable = unstable;
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

    @DataBoundSetter
    public void setTaOptionBlock(TaOptionBlock taOptionBlock){
        this.taOptionBlock = taOptionBlock;
    }

    public TaOptionBlock getTaOptionBlock(){return taOptionBlock;}

    @DataBoundSetter
    public void setScmOptionBlock(ScmOptionBlock scmOptionBlock){
        this.scmOptionBlock = scmOptionBlock;
    }

    public ScmOptionBlock getScmOptionBlock(){return scmOptionBlock;}

    public List<CIMStream> getCimStreams() {
        if(cimStreams == null) {
            return new ArrayList<CIMStream>();
        }
        return cimStreams;
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

            CoverityToolHandler cth = new CoverityToolHandler();
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

    public void deleteIntermediateDirectory(BuildListener listener, CoverityTempDir temp) {
        if (temp != null) {
            try{
                if(!getKeepIntDir() || temp.isDef()) {
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

        @Deprecated
        private String home;

        private List<CIMInstance> instances = new ArrayList<CIMInstance>();
        private SSLConfigurations sslConfigurations;
        private CoverityToolInstallation[] installations = new CoverityToolInstallation[0];

        public DescriptorImpl() {
            super(CoverityPublisher.class);
            load();
        }

        /**
         * Implement readResolve to update the Publisher object to migrate an existing home directory to a default tool
         * installation.
         */
        protected Object readResolve() {
            // add new "default" tool installation for home if it does not yet exist
            if (StringUtils.isNotEmpty(home)) {
                boolean defaultExists = false;
                if (installations.length > 0) {
                    for (CoverityToolInstallation installation : installations) {
                        if (CoverityToolInstallation.DEFAULT_NAME.equalsIgnoreCase(installation.getName()) ||
                            home.equals(installation.getHome())) {
                            defaultExists = true;
                        }
                    }
                }

                if (!defaultExists) {
                    final List<CoverityToolInstallation> installationList = new ArrayList<>(Arrays.asList(installations));
                    installationList.add(new CoverityToolInstallation(CoverityToolInstallation.DEFAULT_NAME, home));
                    installations = installationList.toArray(new CoverityToolInstallation[installationList.size()]);
                }
            }

            return this;
        }

        public CIMStream.DescriptorImpl getCIMStreamDescriptor() {
            return Jenkins.getInstance().getDescriptorByType(CIMStream.DescriptorImpl.class);
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

        @Deprecated
        public String getHome() {
            return home;
        }

        @Deprecated
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

        public CIMInstance getInstance(CoverityPublisher publisher) {
            CIMStream stream = publisher.getCimStream();
            if (stream != null) {
                CIMInstance instance = getInstance(stream.getInstance());
                if (instance != null && StringUtils.isNotEmpty(stream.getCredentialId())) {
                    return instance.cloneWithCredential(stream.getCredentialId());
                }

                return instance;
            }

            return null;
        }

        public CoverityToolInstallation[] getInstallations() {
            return Arrays.copyOf(installations, installations.length);
        }

        public void setInstallations(CoverityToolInstallation... installations) {
            this.installations = installations;
            this.save();
        }

        @Override
        public String getDisplayName() {
            return "Coverity";
        }

        /**
         * Validate the configured global cim instance..
         * This is called when the user clicks the 'Validate' button for an instance.
         */
        public FormValidation doCheckInstance(@QueryParameter String host, @QueryParameter int port,
                                              @QueryParameter boolean useSSL, @QueryParameter String credentialId) {
            CIMInstance instance = new CIMInstance("", host, port, credentialId);
            instance.setUseSSL(useSSL);
            return instance.doCheck();
        }

        public FormValidation doCheckHome(@QueryParameter String home) {
            if (StringUtils.isNotEmpty(home)) {
                return FormValidation.warning("Static Analysis Location is deprecated in Coverity plugin version 1.10 and later. " +
                    "Please use the Coverity Static Analysis Tools global configuration instead.");
            }

            return FormValidation.ok();
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
        public Publisher newInstance(@CheckForNull StaplerRequest req, @Nonnull JSONObject formData) throws FormException {
            logger.info(formData.toString());

            // even though request is always non-null, needs check (see note on Descriptor.newInstance)
            if (req == null) {
                return super.newInstance(req, formData);
            }

            String cutOffDate = Util.fixEmpty(req.getParameter("cutOffDate"));
            try {
                if(cutOffDate != null) new SimpleDateFormat("yyyy-MM-dd").parse(cutOffDate);
            } catch(ParseException e) {
                throw new Descriptor.FormException("Could not parse date '" + cutOffDate + "', yyyy-MM-dd expected", "cutOffDate");
            }
            CoverityPublisher publisher = (CoverityPublisher) super.newInstance(req, formData);

            CIMStream cimStream = publisher.getCimStream();
            CIMStream.DescriptorImpl cimStreamDescriptor = ((CIMStream.DescriptorImpl) cimStream.getDescriptor());

            String cimInstance = cimStream.getInstance();

            try {
                if(cimStream.isValid()) {
                    DefectFilters defectFilters = cimStream.getDefectFilters();
                    if(defectFilters != null) {
                        CIMInstance instance = getInstance(publisher);
                        List<String> allCheckers = instance != null ? instance.getCimInstanceCheckers() : new ArrayList<String>();
                        List<String> allComponents = toStrings(cimStreamDescriptor.doFillComponentDefectFilterItems(cimInstance, cimStream.getStream()));
                        defectFilters.invertCheckers(allCheckers);
                        defectFilters.invertComponents(allComponents);
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

        /**
         * Validate the publisher for a job.
         * This is called when the user clicks the 'Check Configuration' button for on the publisher.
         */
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

            CIMStream.DescriptorImpl cimStreamDescriptor = null;
            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                CIMStream cimStream = publisher.getCimStream();

                if (cimStream != null)
                    cimStreamDescriptor = ((CIMStream.DescriptorImpl) cimStream.getDescriptor());

                if (cimStreamDescriptor != null) {
                    if (StringUtils.isEmpty(cimStream.getInstance()) || StringUtils.isEmpty(cimStream.getProject()) || StringUtils.isEmpty(cimStream.getStream())) {
                        //do nothing when any of instance / project / stream is not yet configured
                    } else {
                        //initialize 'new' defectFilters item with default values selected

                        CIMInstance instance = getInstance(publisher);
                        List<String> allCheckers = instance != null ? instance.getCimInstanceCheckers() : new ArrayList<String>();
                        DefectFilters defectFilters = cimStream.getDefectFilters();
                        if (defectFilters != null) {
                            try {
                                cimStream.getDefectFilters().initializeFilter(
                                    allCheckers,
                                    toStrings(cimStreamDescriptor.doFillClassificationDefectFilterItems(cimStream.getInstance())),
                                    toStrings(cimStreamDescriptor.doFillActionDefectFilterItems(cimStream.getInstance())),
                                    toStrings(cimStreamDescriptor.doFillSeveritiesDefectFilterItems(cimStream.getInstance())),
                                    toStrings(cimStreamDescriptor.doFillComponentDefectFilterItems(cimStream.getInstance(), cimStream.getStream())),
                                    toStrings(cimStreamDescriptor.doFillImpactDefectFilterItems(cimStream.getInstance())));
                            } catch (CovRemoteServiceException_Exception e) {
                                throw new IOException(e);
                            }
                        }
                    }
                    req.setAttribute("descriptor", cimStreamDescriptor);
                    req.setAttribute("instance", cimStream);
                }
            }
            rsp.forward(cimStreamDescriptor, "defectFilters", req);
        }

        @JavaScriptMethod
        public void doLoadProjectsForInstance(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {

            JSONObject json = getJSONClassObject(req.getSubmittedForm(), getId());

            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                CIMStream cimStream = publisher.getCimStream();

                if (cimStream != null) {
                    CIMInstance cimInstance = getInstance(publisher);
                    final List<String> projects = cimInstance != null
                            ? new ArrayList<>(CimCache.getInstance().getProjects(cimInstance))
                            : new ArrayList<String>();
                    final String selectedProject = cimStream.getProject();
                    boolean selectedProjectIsvalid = true;
                    if (!StringUtils.isEmpty(selectedProject) && !projects.contains(selectedProject)) {
                        projects.add(selectedProject);
                        selectedProjectIsvalid = false;
                    }

                    rsp.setContentType("application/json; charset=utf-8");
                    final ServletOutputStream outputStream = rsp.getOutputStream();

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("projects", projects);
                    responseObject.put("selectedProject", selectedProject);
                    responseObject.put("validSelection", selectedProjectIsvalid);

                    String jsonString = responseObject.toString();
                    outputStream.write(jsonString.getBytes("UTF-8"));
                }
            }
        }

        @JavaScriptMethod
        public void doLoadStreamsForProject(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {

            JSONObject json = getJSONClassObject(req.getSubmittedForm(), getId());


            if(json != null && !json.isNullObject()) {
                CoverityPublisher publisher = req.bindJSON(CoverityPublisher.class, json);
                CIMStream cimStream = publisher.getCimStream();

                if (cimStream != null) {
                    CIMInstance cimInstance = getInstance(publisher);
                    final List<String> streams = cimInstance != null
                            ? new ArrayList<>(CimCache.getInstance().getStreams(cimInstance ,cimStream.getProject()))
                            : new ArrayList<String>();
                    final String selectedStream = cimStream.getStream();
                    boolean selectedStreamIsvalid = true;

                    if (!StringUtils.isEmpty(selectedStream) && !streams.contains(selectedStream)) {
                        streams.add(selectedStream);
                        selectedStreamIsvalid = false;
                    }

                    rsp.setContentType("application/json; charset=utf-8");
                    final ServletOutputStream outputStream = rsp.getOutputStream();

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("streams", streams);
                    responseObject.put("selectedStream", selectedStream);
                    responseObject.put("validSelection", selectedStreamIsvalid);

                    String jsonString = responseObject.toString();
                    outputStream.write(jsonString.getBytes("UTF-8"));
                }
            }
        }

        public ListBoxModel doFillCredentialIdItems(@AncestorInPath Jenkins context) {
            return new StandardListBoxModel()
                    .withEmptySelection()
                    .withMatching(
                            CredentialsMatchers.anyOf(
                                    CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class)
                            ),
                            CredentialsProvider.lookupCredentials(StandardUsernamePasswordCredentials.class,
                                    context,
                                    ACL.SYSTEM,
                                    new ArrayList<DomainRequirement>())
                    );
        }

        public FormValidation doCheckUser(@QueryParameter String user){
            if (StringUtils.isEmpty(user)) {
                return FormValidation.ok();
            }

            return FormValidation.warning("User is deprecated in Coverity plugin version 1.10 and later. Please use Credentials above for more secure username.");
        }

        public FormValidation doCheckPassword(@QueryParameter String password) {
            if (StringUtils.isEmpty(password)) {
                return FormValidation.ok();
            }

            return FormValidation.warning("Password is deprecated in Coverity plugin version 1.10 and later. Please use Credentials above for more secure password.");
        }

        public FormValidation doCheckPostCovBuildCmd(@QueryParameter String postCovBuildCmd){
            if (StringUtils.isEmpty(postCovBuildCmd)){
                return FormValidation.error("Post cov-build command cannot be empty!");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckPostCovAnalyzeCmd(@QueryParameter String postCovAnalyzeCmd){
            if (StringUtils.isEmpty(postCovAnalyzeCmd)){
                return FormValidation.error("Post cov-analyze command cannot be empty!");
            }

            return FormValidation.ok();
        }
		
		public ListBoxModel doFillToolInstallationNameItems() {
            ListBoxModel result = new ListBoxModel();
            for(CoverityToolInstallation toolInstallation : getInstallations()) {
                result.add(toolInstallation.getName());
            }
            return result;
        }
    }
}
