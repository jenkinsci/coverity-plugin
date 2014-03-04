package jenkins.plugins.coverity;

import com.coverity.ws.v6.CovRemoteServiceException_Exception;
import com.coverity.ws.v6.StreamDataObj;
import com.coverity.ws.v6.StreamFilterSpecDataObj;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractDescribableImpl;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Executor;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.util.FormValidation;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A configuration checker, and the results of such a check.
 */
public class CheckConfig extends AbstractDescribableImpl<CheckConfig> {
    private CoverityPublisher publisher;
    private final List<Status> status;
    private Launcher launcher;
    private AbstractBuild<?, ?> build;
    private BuildListener listener;

    /**
     * Create a new check.
     *
     * @param publisher required
     * @param build     optional (without this, node checking will be skipped)
     * @param launcher  optional (without this, node checking will be skipped)
     * @param listener  optional (without this, node checking will be skipped)
     */
    public CheckConfig(CoverityPublisher publisher, AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        this.publisher = publisher;
        this.launcher = launcher;
        this.build = build;
        this.listener = listener;
        this.status = new ArrayList<Status>();
    }

    /**
     * Returns true if all checked aspects of the configuration are valid. If this is true, then it's almost certain
     * that the corresponding build will succeed.
     */
    public boolean isValid() {
        for(Status s : status) {
            if(!s.isValid()) {
                return false;
            }
        }
        return true;
    }

    public List<Status> getStatus() {
        return status;
    }

    public CoverityPublisher getPublisher() {
        return publisher;
    }

    public void check() {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        status.clear();

        for(CIMStream cs : publisher.getCimStreams()) {
            status.add(checkStream(publisher, cs));
        }

        if(launcher != null) {
            NodeStatus ns = checkNode(publisher, build, launcher, listener);
            status.add(ns);

            //don't bother continuing unless we're all valid so far

            if(!isValid()) {
                return;
            }

            CoverityVersion analysisVersion = ns.getVersion();

            //lots of checks can only happen if we know the analysis version

            //is any target CIM version < analysis version?
            {
                List<Status> newStatus = new ArrayList<Status>();
                for(Status s : status) {
                    if(s instanceof StreamStatus) {
                        StreamStatus ss = (StreamStatus) s;
                        if(ss.getVersion().compareMajMin(analysisVersion)) {
                            newStatus.add(new Status(false, "Connect instance " + ss.getStream().toPrettyString() + " (version " +
                                    ss.getVersion() + "|" + ss.getVersion().getEffectiveVersion() +
                                    ") is incompatible with analysis version " + analysisVersion));
                        }
                    }
                }
                status.addAll(newStatus);
            }

            //is there a mixed stream, and analysis < fresno?
            {
                if(analysisVersion.compareTo(CoverityVersion.codeNameEquivalents.get("fresno")) < 0) {
                    List<Status> newStatus = new ArrayList<Status>();
                    for(Status s : status) {
                        if(s instanceof StreamStatus) {
                            StreamStatus ss = (StreamStatus) s;
                            if(ss.getStream().getDomain().equals("MIXED")) {
                                newStatus.add(new Status(false, "Stream " + ss.getStream().toPrettyString() + " (any language) is incompatible with analysis version " + analysisVersion));
                            }
                        }
                    }
                    status.addAll(newStatus);
                }
            }

            //can we commit to each stream, given the languages we are analyzing? (assuming analysis >= fresno
            {
                if(analysisVersion.compareTo(CoverityVersion.codeNameEquivalents.get("fresno")) >= 0) {
                    List<Status> newStatus = new ArrayList<Status>();
                    Set<String> languagesToAnalyze = new HashSet<String>();
                    boolean singleDomainStreamExists = false;

                    for(Status s : status) {
                        if(s instanceof StreamStatus) {
                            StreamStatus ss = (StreamStatus) s;
                            if(!ss.getStream().getLanguage().equals("ALL")) {
                                singleDomainStreamExists = true;
                                languagesToAnalyze.add(ss.getStream().getLanguage());
                            } else {
                                languagesToAnalyze.add("JAVA");
                                languagesToAnalyze.add("CXX");
                                languagesToAnalyze.add("CSHARP");
                            }
                        }
                    }

                    if(singleDomainStreamExists && languagesToAnalyze.size() > 1) {
                        System.out.println(languagesToAnalyze);
                        newStatus.add(new Status(false, "There is a single-domain stream, but there are multiple languages to be committed."));
                    }

                    if(languagesToAnalyze.size() == 1) {
                        String language = languagesToAnalyze.iterator().next();
                        for(Status s : status) {
                            if(s instanceof StreamStatus) {
                                StreamStatus ss = (StreamStatus) s;
                                if(ss.getStream().getDomain().equals("MIXED") || ss.getStream().getDomain().equals(language)) {
                                    //we're fine so far
                                } else {
                                    newStatus.add(new Status(false, "Language " + language + " cannot be committed to stream " + ss.getStream().toPrettyString()));
                                }
                            }
                        }
                    }

                    status.addAll(newStatus);
                }
            }
        }
    }

    public static StreamStatus checkStream(CoverityPublisher publisher, CIMStream cs) {
        CIMInstance ci = publisher.getDescriptor().getInstance(cs.getInstance());

        //check if instance is valid
        {
            try {
                FormValidation fv = ci.doCheck();
                if(fv.kind != FormValidation.Kind.OK) {
                    return new StreamStatus(false, "Could not connect to instance: " + fv, cs, null);
                }
            } catch(IOException e) {
                e.printStackTrace();
                return new StreamStatus(false, "Could not connect to instance: " + e, cs, null);
            }
        }

        //check instance version
        CoverityVersion version = null;
        {
            try {
                version = CoverityVersion.parse(ci.getConfigurationService().getVersion().getExternalVersion());
            } catch(CovRemoteServiceException_Exception e) {
                e.printStackTrace();
                return new StreamStatus(false, "Could not retrive version info: " + e, cs, null);
            } catch(IOException e) {
                e.printStackTrace();
                return new StreamStatus(false, "Could not retrive version info: " + e, cs, null);
            }
        }

        //check stream
        {
            try {
                StreamFilterSpecDataObj sf = new StreamFilterSpecDataObj();
                sf.setNamePattern(cs.getStream());
                List<StreamDataObj> ls = ci.getConfigurationService().getStreams(sf);
                if(ls.size() == 0) {
                    return new StreamStatus(false, "Stream does not exist", cs, version);
                }
            } catch(CovRemoteServiceException_Exception e) {
                e.printStackTrace();
                return new StreamStatus(false, "Could not find stream: " + e, cs, version);
            } catch(IOException e) {
                e.printStackTrace();
                return new StreamStatus(false, "Could not find stream: " + e, cs, version);
            }
        }

        return new StreamStatus(true, "OK (version: " + version + ")", cs, version);
    }

    public static NodeStatus checkNode(CoverityPublisher publisher, AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener) {
        Node node = Executor.currentExecutor().getOwner().getNode();
        try {
            String home = publisher.getDescriptor().getHome(node, build.getEnvironment(listener));
            InvocationAssistance ia = publisher.getInvocationAssistance();
            if(ia != null && ia.getSaOverride() != null) {
                home = new CoverityInstallation(ia.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
            }
            if(home == null) {
                return new NodeStatus(false, "Could not find Coverity Analysis home directory.", node, null);
            }

            FilePath homePath = new FilePath(launcher.getChannel(), home);
            CoverityVersion version = homePath.child("VERSION").act(new FilePath.FileCallable<CoverityVersion>() {
                public CoverityVersion invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                    FileInputStream fis = new FileInputStream(f);
                    CoverityVersion cv = getCoverityVersion(IOUtils.toString(fis));
                    fis.close();
                    return cv;
                }
            });

            return new NodeStatus(true, "version " + version, node, version);

        } catch(IOException e) {
            e.printStackTrace();
            return new NodeStatus(false, "Error checking node: " + e.toString(), node, null);
        } catch(InterruptedException e) {
            e.printStackTrace();
            return new NodeStatus(false, "Interrupted while checking node.", node, null);
        }
    }

    public static CoverityVersion getCoverityVersion(String versionFileContents) throws IOException {
        String[] lines = versionFileContents.split("\n");

        for(String line : lines) {
            if(line.startsWith("externalVersion=")) {
                return CoverityVersion.parse(line.split("=")[1]);
            }
        }

        return null;
    }

    /**
     * The result of a single check. Non-subclasses are general configuration statuses, not associated with a Node or
     * Stream, for example.
     */
    public static class Status {
        boolean valid;
        String status;

        private Status(boolean valid, String status) {
            this.valid = valid;
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }

        public boolean isValid() {
            return valid;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class StreamStatus extends Status {
        private final CIMStream stream;
        private final CoverityVersion version;

        public StreamStatus(boolean valid, String status, CIMStream stream, CoverityVersion version) {
            super(valid, status);
            this.stream = stream;
            this.version = version;
        }

        public CIMStream getStream() {
            return stream;
        }

        public CoverityVersion getVersion() {
            return version;
        }
    }

    public static class NodeStatus extends Status {
        private final Node node;
        private final CoverityVersion version;

        public NodeStatus(boolean valid, String status, Node node, CoverityVersion version) {
            super(valid, status);
            this.version = version;
            this.node = node;
        }

        public Node getNode() {
            return node;
        }

        public CoverityVersion getVersion() {
            return version;
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<CheckConfig> {
        @Override
        public String getDisplayName() {
            return "";
        }
    }
}
