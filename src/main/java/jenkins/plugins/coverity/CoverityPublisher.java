/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import com.coverity.ws.v6.*;
import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Node;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.IOUtils;
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
     * ID of the CIM instance used
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
    private final CoverityMailSender mailSender;

    private final TaOptionBlock taOptionBlock;

    private final ScmOptionBlock scmOptionBlock;

    @DataBoundConstructor
    public CoverityPublisher(List<CIMStream> cimStreams,
                             InvocationAssistance invocationAssistance,
                             boolean failBuild,
                             boolean keepIntDir,
                             boolean skipFetchingDefects,
                             boolean hideChart,
                             CoverityMailSender mailSender,
                             String cimInstance,
                             String project,
                             String stream,
                             DefectFilters defectFilters,
                             TaOptionBlock taOptionBlock,
                             ScmOptionBlock scmOptionBlock) {
        this.cimStreams = cimStreams;
        this.invocationAssistance = invocationAssistance;
        this.failBuild = failBuild;
        this.mailSender = mailSender;
        this.keepIntDir = keepIntDir;
        this.skipFetchingDefects = skipFetchingDefects;
        this.hideChart = hideChart;
        this.cimInstance = cimInstance;
        this.project = project;
        this.stream = stream;
        this.defectFilters = defectFilters;
        this.taOptionBlock = taOptionBlock;
        this.scmOptionBlock = scmOptionBlock;

        if(isOldDataPresent()) {
            logger.info("Old data format detected. Converting to new format.");
            convertOldData();
        }
    }

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

    private boolean isOldDataPresent() {
        return cimInstance != null || project != null || stream != null || defectFilters != null;
    }

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

    public CoverityMailSender getMailSender() {
        return mailSender;
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
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        if(isOldDataPresent()) {
            logger.info("Old data format detected. Converting to new format.");
            convertOldData();
        }

        if(build.getResult().isWorseOrEqualTo(Result.FAILURE)) return true;

        CoverityVersion version = CheckConfig.checkNode(this, build, launcher, listener).getVersion();

        CoverityToolHandler cth = CoverityToolHandler.getHandler(version);
        try{
            return cth.perform(build, launcher, listener, this);
        } catch(CovRemoteServiceException_Exception e){
            throw new InterruptedException("Cov Remote Service Error " + e.getMessage());
        }
    }

    public String getLanguage(CIMStream cimStream) throws IOException, CovRemoteServiceException_Exception {
        String domain = getDescriptor().getInstance(cimStream.getInstance()).getStream(cimStream.getStream()).getLanguage();
        return "MIXED".equals(domain) ? cimStream.getLanguage() : domain;
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
        private String csharpCheckers;

        public DescriptorImpl() {
            super(CoverityPublisher.class);
            load();

            setJavaCheckers(javaCheckers);
            setCxxCheckers(cxxCheckers);
            setCsharpCheckers(csharpCheckers);
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

        public String getJavaCheckers(){return javaCheckers;}

        public List<String> getCimJavaCheckers() {
            List<String> checkers = new ArrayList<String>();
            try {
                for(CIMInstance instance :instances){
                    ConfigurationService configurationService = instance.getConfigurationService();
                    CheckerPropertyFilterSpecDataObj checkerPropFilter = new CheckerPropertyFilterSpecDataObj();
                    checkerPropFilter.getDomainList().add("STATIC_JAVA");
                    List<CheckerPropertyDataObj> checkerPropertyList = configurationService.getCheckerProperties(checkerPropFilter);
                    for(CheckerPropertyDataObj checkerProp : checkerPropertyList){
                        CheckerSubcategoryIdDataObj checkerSub = checkerProp.getCheckerSubcategoryId();
                        if(!checkers.contains(checkerSub.getCheckerName())){
                            checkers.add(checkerSub.getCheckerName());
                        }
                    }
                }
            } catch(Exception e) {
            }
            return checkers;
        }

        public void setJavaCheckers(String javaCheckers) {
            this.javaCheckers = Util.fixEmpty(javaCheckers);
            try{
                this.javaCheckers = IOUtils.toString(getClass().getResourceAsStream("java-checkers.txt"));
            }catch(IOException e){
                logger.info("Failed loading Java Checkers text file.");
            }
        }

        public void setCimJavaCheckers(String javaCheckers) {
            this.javaCheckers = Util.fixEmpty(javaCheckers);
            this.javaCheckers = StringUtils.join(getCimJavaCheckers(),'\n');
        }

        public String getCxxCheckers() {
            return cxxCheckers;
        }

        public List<String> getCimCxxCheckers() {
            List<String> checkers = new ArrayList<String>();
            try {
                for(CIMInstance instance :instances){
                    ConfigurationService configurationService = instance.getConfigurationService();
                    CheckerPropertyFilterSpecDataObj checkerPropFilter = new CheckerPropertyFilterSpecDataObj();
                    checkerPropFilter.getDomainList().add("STATIC_C");
                    List<CheckerPropertyDataObj> checkerPropertyList = configurationService.getCheckerProperties(checkerPropFilter);
                    for(CheckerPropertyDataObj checkerProp : checkerPropertyList){
                        CheckerSubcategoryIdDataObj checkerSub = checkerProp.getCheckerSubcategoryId();
                        if(!checkers.contains(checkerSub.getCheckerName())){
                            checkers.add(checkerSub.getCheckerName());
                        }
                    }
                }
            } catch(Exception e) {
            }
            return checkers;
        }

        public void setCxxCheckers(String cxxCheckers) {
            this.cxxCheckers = Util.fixEmpty(cxxCheckers);
            try{
                this.cxxCheckers = IOUtils.toString(getClass().getResourceAsStream("cxx-checkers.txt"));
            }catch(IOException e){
                logger.info("Failed to load Cxx Checkers text file");
            }

        }

        public void setCIMCxxCheckers(String cxxCheckers) {
            this.cxxCheckers = Util.fixEmpty(cxxCheckers);
            this.cxxCheckers = StringUtils.join(getCimCxxCheckers(),'\n');

        }

        public String getCsharpCheckers() {
            return csharpCheckers;
        }

        public List<String> getCimCsharpCheckers() {
            List<String> checkers = new ArrayList<String>();
            try {
                for(CIMInstance instance :instances){
                    ConfigurationService configurationService = instance.getConfigurationService();
                    CheckerPropertyFilterSpecDataObj checkerPropFilter = new CheckerPropertyFilterSpecDataObj();
                    checkerPropFilter.getDomainList().add("STATIC_CS");
                    List<CheckerPropertyDataObj> checkerPropertyList = configurationService.getCheckerProperties(checkerPropFilter);
                    for(CheckerPropertyDataObj checkerProp : checkerPropertyList){
                        CheckerSubcategoryIdDataObj checkerSub = checkerProp.getCheckerSubcategoryId();
                        if(!checkers.contains(checkerSub.getCheckerName())){
                            checkers.add(checkerSub.getCheckerName());
                        }
                    }
                }
            } catch(Exception e) {
            }
            return checkers;
        }

        public void setCsharpCheckers(String csharpCheckers) {
            this.csharpCheckers = Util.fixEmpty(csharpCheckers);

            try{
                this.csharpCheckers = IOUtils.toString(getClass().getResourceAsStream("csharp-checkers.txt"));
            }catch(IOException e){
                logger.info("Failed to load C sharp Checkers text file");
            }
        }

        public void setCimCsharpCheckers(String csharpCheckers) {
            this.csharpCheckers = Util.fixEmpty(csharpCheckers);

            this.csharpCheckers = StringUtils.join(getCimCsharpCheckers(),'\n');

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

        public FormValidation doCheckInstance(@QueryParameter String host, @QueryParameter int port, @QueryParameter boolean useSSL, @QueryParameter String user, @QueryParameter String password, @QueryParameter int dataPort) throws IOException {
            return new CIMInstance("", host, port, user, password, useSSL, dataPort).doCheck();
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

        public String getCheckers(String language) {
            if("CXX".equals(language)) return cxxCheckers;
            if("JAVA".equals(language)) return javaCheckers;
            if("CSHARP".equals(language)) return csharpCheckers;
            if("ALL".equals(language)) return cxxCheckers + javaCheckers + csharpCheckers;
            throw new IllegalArgumentException("Unknown language: " + language);
        }

        public void setCheckers(String language, Set<String> checkers) {
            if("CXX".equals(language)) {
                cxxCheckers = join(checkers);
            } else if("JAVA".equals(language)) {
                javaCheckers = join(checkers);
            } else if("CSHARP".equals(language)) {
                csharpCheckers = join(checkers);
            } else {
                throw new IllegalArgumentException(language);
            }
        }

        public void updateCheckers(String language, Set<String> checkers) {
            String oldCheckers = getCheckers(language);

            Set<String> newCheckers = new TreeSet<String>();
            Set<String> c = new TreeSet<String>();
            for(ListBoxModel.Option s : split(oldCheckers)) {
                c.add(s.name);
            }
            for(String s : checkers) {
                if(c.add(s)) {
                    newCheckers.add(s);
                }
            }
            setCheckers(language, c);

            save();
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
                        String language = publisher.getLanguage(current);
                        Set<String> allCheckers = split2(getCheckers(language));
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

        public void doDefectFiltersConfig(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
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
                    try {
                        String language = publisher.getLanguage(current);

                        Set<String> allCheckers = split2(getCheckers(language));
                        DefectFilters defectFilters = current.getDefectFilters();
                        if(defectFilters != null) {
                            current.getDefectFilters().invertCheckers(
                                    allCheckers,
                                    toStrings(currentDescriptor.doFillClassificationDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillActionDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillSeveritiesDefectFilterItems(current.getInstance())),
                                    toStrings(currentDescriptor.doFillComponentDefectFilterItems(current.getInstance(), current.getStream()))
                            );
                        }
                    } catch(CovRemoteServiceException_Exception e) {
                        throw new IOException(e);
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
