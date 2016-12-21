/*******************************************************************************
 * Copyright (c) 2016 Synopsys, Inc
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
import hudson.Launcher;
import hudson.Util;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.plugins.coverity.analysis.CoverityToolHandler;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
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

    //deprecated fields
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
                             String cimInstance,
                             String project,
                             String stream,
                             DefectFilters defectFilters,
                             TaOptionBlock taOptionBlock,
                             ScmOptionBlock scmOptionBlock) {
        this.cimStreams = cimStreams;
        this.invocationAssistance = invocationAssistance;
        this.failBuild = failBuild;
        this.unstable = unstable;
        this.keepIntDir = keepIntDir;
        this.skipFetchingDefects = skipFetchingDefects;
        this.hideChart = hideChart;
        this.cimInstance = cimInstance;
        this.project = project;
        this.stream = stream;
        this.defectFilters = defectFilters;
        this.taOptionBlock = taOptionBlock;
        this.scmOptionBlock = scmOptionBlock;
        this.unstableBuild = false;
        if(isOldDataPresent()) {
            logger.info("Old data format detected. Converting to new format.");
            convertOldData();
        }
    }

    /**
     * Converts the old data values cimInstance, project, stream, defectFilters to a {@link CIMStream} object
     * and adds to the configured streams. Then trims any configured streams which are not valid (when a stream
     * is missing a instance, project or stream name it is not valid).
     */
    private void convertOldData() {
        CIMStream newcs = new CIMStream(cimInstance, project, stream, defectFilters, null, null, null);

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
     * Checks for old data values cimInstance, project, stream, defectFilters being set
     */
    private boolean isOldDataPresent() {
        return cimInstance != null || project != null || stream != null || defectFilters != null;
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

        if(isOldDataPresent()) {
            logger.info("Old data format detected. Converting to new format.");
            convertOldData();
        }

        if(build.getResult().isWorseOrEqualTo(Result.FAILURE)) return true;

        try{
            CoverityVersion version = CheckConfig.checkNode(this, build, launcher, listener).getVersion();

            if(version == null){
                throw new Exception("Coverity Version is null. Please verify the version file under your Coverity Analysis installation.");
            }

            CoverityToolHandler cth = CoverityToolHandler.getHandler(version);

            if(cth == null){
                throw new Exception("Could not create a CoverityToolHanldler for the given analysis version. Please verify the version file under your Coverity Analysis installation.");
            }

            cth.perform(build, launcher, listener, this);
            
            if(isUnstableBuild()){
                build.setResult(Result.UNSTABLE);
            }
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

    public String getLanguage(CIMStream cimStream) throws IOException, com.coverity.ws.v9.CovRemoteServiceException_Exception {
        String domain = getStream(cimStream.getStream(), getDescriptor().getInstance(cimStream.getInstance())).getLanguage();
        return "MIXED".equals(domain) ? cimStream.getLanguage() : domain;
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        private List<CIMInstance> instances = new ArrayList<CIMInstance>();
        private String home;
        private SSLConfigurations sslConfigurations;
        /**
         * This field contains all checkers on txt files and on CIM (obtained via the ws v9 call getCheckersnames() ).
         * It's populated during DescriptorImpl instantiation.
         */
        private String allCheckers;

        public DescriptorImpl() {
            super(CoverityPublisher.class);
            load();

            setAllCIMCheckers("");
            Set<String> repeatedCheckers = new HashSet<String>(split2(allCheckers ));
            List<String> checkersWithoutRepetitions = new ArrayList<String>(repeatedCheckers);
            Collections.sort(checkersWithoutRepetitions);
            this.allCheckers = StringUtils.join(checkersWithoutRepetitions,'\n');
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

        /**
         * Gets all checkers from CIM.
         * This method will use getCheckerNames() on v9
         */
        public List<String> getAllCimCheckers() {
            List<String> checkers = new ArrayList<String>();
            for(CIMInstance instance :instances){
                try {
                    checkers.addAll(instance.getConfigurationService().getCheckerNames());
                } catch(Exception e) {
                }
            }
            return checkers;
        }

        public String getAllCheckers() {
            return allCheckers;
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

        /**
         * Adds to allCheckers all checkers from CIM.
         */
        public void setAllCIMCheckers(String allCheckers) {
            this.allCheckers = Util.fixEmpty(allCheckers);
            this.allCheckers = StringUtils.join(getAllCimCheckers(), '\n');
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

        public ListBoxModel split(String string) {
            ListBoxModel result = new ListBoxModel();
            for(String s : string.split("[\r\n]")) {
                s = Util.fixEmptyAndTrim(s);
                if(s != null) {
                    result.add(s);
                }
            }
            return result;
        }

        public Set<String> split2(String string) {
            Set<String> result = new TreeSet<String>();
            for(String s : string.split("[\r\n]")) {
                s = Util.fixEmptyAndTrim(s);
                if(s != null) {
                    result.add(s);
                }
            }
            return result;
        }
        // Spliting checker strings into a usable list
        public List<String> split2List(String string) {
            List<String> result = new LinkedList<String>();
            for(String s : string.split("[\r\n]")) {
                s = Util.fixEmptyAndTrim(s);
                if(s != null) {
                    result.add(s);
                }
            }
            return result;
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

        private String join(Collection<String> c) {
            StringBuffer result = new StringBuffer();
            for(String s : c) result.append(s).append("\n");
            return result.toString();
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
                        Set<String> allCheckers = split2(getInstance(current.getInstance()).getCimInstanceCheckers());
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
                } catch(Exception e) {
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
                String id = ((String[]) req.getParameterMap().get("id"))[0];
                for(CIMStream cs : publisher.getCimStreams()) {
                    if(id.equals(cs.getId())) {
                        current = cs;
                    }
                }

                currentDescriptor = ((CIMStream.DescriptorImpl) current.getDescriptor());

                if(StringUtils.isEmpty(current.getInstance()) || StringUtils.isEmpty(current.getStream()) || StringUtils.isEmpty(current.getProject())) {
                    //do nothing
                } else {
                    Set<String> allCheckers = split2(getInstance(current.getInstance()).getCimInstanceCheckers());
                    DefectFilters defectFilters = current.getDefectFilters();
                    if(defectFilters != null) {
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
            rsp.forward(currentDescriptor, "defectFilters", req);
        }
    }
}
