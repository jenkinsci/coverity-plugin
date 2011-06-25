package jenkins.plugins.coverity;

import com.coverity.ws.v3.*;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ArgumentListBuilder;
import hudson.util.ComboBoxModel;
import hudson.util.ListBoxModel;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 4/06/11
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class CoverityPublisher extends Recorder {

    private final String cimInstance;
    private final InvocationAssistance invocationAssistance;
    private final String project;
    private final String stream;

    private final DefectFilterSet defectFilters;

    @DataBoundConstructor
    public CoverityPublisher(String cimInstance, InvocationAssistance invocationAssistance, String project, String stream, DefectFilterSet defectFilters) {
        this.cimInstance = cimInstance;
        this.invocationAssistance = invocationAssistance;
        this.project = project;
        this.stream = stream;
        this.defectFilters = defectFilters;
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

    public DefectFilterSet getDefectFilters() {
        return defectFilters;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new CoverityProjectAction(project);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        CIMInstance cim = Hudson.getInstance().getDescriptorByType(CIMInstance.DescriptorImpl.class).getInstance(cimInstance);

        if (invocationAssistance != null) {
            CoverityLauncherDecorator.SKIP.set(true);
            CoverityTempDir temp = build.getAction(CoverityTempDir.class);
            try {
                int result = launcher.
                        launch().
                        cmds(new ArgumentListBuilder("cov-analyze-java", "--dir", temp.tempDir.getRemote())).
                        stdout(listener).
                        join();
                if (result != 0) {
                    listener.getLogger().println("[Coverity] cov-analyze-java returned " + result + ", aborting...");
                    return false;
                }

                ArgumentListBuilder args = new ArgumentListBuilder(
                        "cov-commit-defects",
                        "--dir",
                        temp.tempDir.getRemote(),
                        "--host",
                        cim.getHost(),
                        "--port",
                        Integer.toString(cim.getPort()),
                        "--stream",
                        stream,
                        "--user",
                        cim.getUser());

                result = launcher.
                        launch().
                        cmds(args).
                        envs(Collections.singletonMap("COVERITY_PASSPHRASE", cim.getPassword())).
                        stdout(listener).
                        stderr(listener.getLogger()).
                        join();

                if (result != 0) {
                    listener.getLogger().println("[Coverity] cov-analyze-java returned " + result + ", aborting...");
                    return false;
                }
            } finally {
                CoverityLauncherDecorator.SKIP.set(false);
                temp.tempDir.deleteRecursive();
            }
        }
        
        try {
            listener.getLogger().println("[Coverity] Fetching defects for stream " + stream);
            StreamIdDataObj streamId = new StreamIdDataObj();
            streamId.setName(stream);
            streamId.setType("STATIC");
            List<Long> cids = cim.getDefectService().getCIDsForStreams(Arrays.asList(streamId), new MergedDefectFilterSpecDataObj());
            listener.getLogger().println("[Coverity] Found " + cids.size() + " defects");

            List<MergedDefectDataObj> defects = cim.getDefects(stream, cids);

            List<Long> matchingDefects = new ArrayList<Long>();
            if (defectFilters != null) {
                for (MergedDefectDataObj defect: defects) {
                    if (defectFilters.matches(defect)) {
                        matchingDefects.add(defect.getCid());
                    }
                }
            }

            if (!matchingDefects.isEmpty()) {
                listener.getLogger().println("[Coverity] Found " + matchingDefects.size() + " defects matching all filters: " + matchingDefects);
                if (build.getResult().isBetterThan(Result.FAILURE)) {
                    build.setResult(Result.FAILURE);
                }
            } else {
                listener.getLogger().println("[Coverity] No defects matched all filters.");
            }

            CoverityBuildAction action = new CoverityBuildAction(build, project, stream, cimInstance, matchingDefects);
            build.addAction(action);
            
            if (!matchingDefects.isEmpty()) {
                new CoverityMailSender().execute(action, listener);
            }
            
        } catch (CovRemoteServiceException_Exception e) {
            e.printStackTrace(listener.error("[Coverity] An error occurred while fetching defects"));
            return false;
        }
        return true;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Coverity";
        }

        public List<String> getSeverities() {
            return SeveritiesDefectFilter.OPTIONS; //TODO get from server, parameterized?
        }

        public List<String> getComponents() {
            return Arrays.asList("Default.Other", "Comp1","Comp2","Comp3","Thirdparty"); //TODO get from server, parameterized by project
        }

        public List<String> getCheckers() {
            return Arrays.asList("ARRAY_VS_SINGLETON","BAD_COMPARE","UNINIT","NULL_RETURNS", "FORWARD_NULL"); //TODO get from server, parameterized by project
        }

        public ListBoxModel doFillCimInstanceItems() {
            ListBoxModel result =new ListBoxModel();
            for (CIMInstance instance: CIMInstance.DESCRIPTOR.getInstances()) {
                result.add(instance.getName());
            }
            return result;
        }

        public ListBoxModel doFillProjectItems(@QueryParameter String cimInstance) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if (!StringUtils.isEmpty(cimInstance)) {
                for (ProjectDataObj project: CIMInstance.DESCRIPTOR.getInstance(cimInstance).getProjects()) {
                    result.add(project.getId().getName());
                }
            }
            return result;
        }

        public ListBoxModel doFillStreamItems(@QueryParameter String cimInstance, @QueryParameter String project) throws IOException, CovRemoteServiceException_Exception {
            ListBoxModel result = new ListBoxModel();
            if (!StringUtils.isEmpty(cimInstance) && !StringUtils.isEmpty(project)) {
                for (StreamDataObj stream: CIMInstance.DESCRIPTOR.getInstance(cimInstance).getStaticStreams(project)) {
                    result.add(stream.getId().getName());
                }
            }
            return result;
        }
    }

}
