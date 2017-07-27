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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.remoting.RoleChecker;

import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.StreamDataObj;
import com.coverity.ws.v9.StreamFilterSpecDataObj;

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
import hudson.util.FormValidation.Kind;

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

        status.add(checkStream(publisher, publisher.getCimStream()));

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
                        CoverityVersion cimVersion = ss.getVersion();
                        CIMStream stream = ss.getStream();
                        if(cimVersion != null && !cimVersion.compareToAnalysis(analysisVersion) && stream != null) {
                            newStatus.add(new Status(false, "Connect instance " + stream.toPrettyString() +
                                    " (version " + cimVersion.toString() + ") is incompatible with analysis version " + analysisVersion));
                        }
                    }
                }
                status.addAll(newStatus);
            }
        }

        TaOptionBlock taOptionBlock = publisher.getTaOptionBlock();
        if (taOptionBlock != null) {
            status.add(checkTaOptionBlock(taOptionBlock));
        }

        ScmOptionBlock scmOptionBlock = publisher.getScmOptionBlock();
        if (scmOptionBlock != null) {
            status.add(checkScmOptionBlock(scmOptionBlock));
        }
    }

    public static StreamStatus checkStream(CoverityPublisher publisher, CIMStream cs) {

        if(cs == null || cs.getInstance() == null){
            return new StreamStatus(false, "Could not connect to a Coverity instance. \n " +
                    "Verify that a Coverity instance has been configured for this job", cs, null);
        }

        CIMInstance ci = publisher.getDescriptor().getInstance(cs.getInstance());

        if(ci == null){
            return new StreamStatus(false, "Could not connect to a Coverity instance. \n " +
                    "Verify that a Coverity instance has been configured for this job", cs, null);
        }

        if(cs.getStream() == null){
            return new StreamStatus(false, "Could not find any Stream that matches the given configuration for this job.", cs, null);
        }

        //check if instance is valid
        {
            try {
                FormValidation fv = ci.doCheck();
                if(fv.kind == Kind.ERROR) {
                    return new StreamStatus(false, "Could not connect to instance: " + fv, cs, null);
                }
            } catch(Exception e) {
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
                return new StreamStatus(false, "Could not retrieve version info: " + e, cs, null);
            } catch(IOException e) {
                e.printStackTrace();
                return new StreamStatus(false, "Could not retrieve version info: " + e, cs, null);
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
            if(ia != null && ia.getSaOverride() != null && !ia.getSaOverride().isEmpty()) {
                home = new CoverityToolInstallation("global-override", ia.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
            }

            if(home == null) {
                return new NodeStatus(false, "Could not find Coverity Analysis home directory. [" + home + "]", node, null);
            }

            try {
                CoverityUtils.checkDir(launcher.getChannel(), home);
            } catch (Exception e) {
                e.printStackTrace();
                return new NodeStatus(false, "Could not find Coverity Analysis home directory. [" + home + "]", node, null);
            }

            FilePath homePath = new FilePath(launcher.getChannel(), home);
            CoverityVersion version = getVersion(homePath);

            if(version.compareTo(CoverityVersion.MINIMUM_SUPPORTED_VERSION) < 0) {
                return new NodeStatus(false,
                    "\"Coverity Static Analysis\" version " + version.toString() + " is not supported. " +
                    "The minimum supported version is " + CoverityVersion.MINIMUM_SUPPORTED_VERSION.toString(),
                    node,
                    version);
            }

            return new NodeStatus(true, "version " + version, node, version);

        } catch(IOException e) {
            e.printStackTrace();
            return new NodeStatus(false, "Error checking node: " + e.toString(), node, null);
        } catch(InterruptedException e) {
            e.printStackTrace();
            return new NodeStatus(false, "Interrupted while checking node.", node, null);
        }
    }

    /*
    Performs the validation on TaOptionBlock
     */
    public static Status checkTaOptionBlock(TaOptionBlock taOptionBlock) {
        String taCheck = taOptionBlock.checkTaConfig();
        if(!taCheck.equals("Pass")){
            return new Status(false, taCheck);
        }
        return new Status(true, "[Test Advisor] Configuration is valid!");
    }

    /*
    Performs the validation on ScmOptionBlock
     */
    public static Status checkScmOptionBlock(ScmOptionBlock scmOptionBlock) {
        String scmCheck = scmOptionBlock.checkScmConfig();
        if(!scmCheck.equals("Pass")){
            return new Status(false, scmCheck);
        }
        return new Status(true, "[SCM] Configuration is valid!");
    }

    /*
     * Gets the {@link CoverityVersion} given a static analysis tools home directory by finding the VERSION file,
     * then reading the version number
     */
    public static CoverityVersion getVersion(FilePath homePath) throws IOException, InterruptedException {
        CoverityVersion version = homePath.child("VERSION").act(new FilePath.FileCallable<CoverityVersion>() {
            @Override
            public void checkRoles(RoleChecker roleChecker) throws SecurityException {
            }

            public CoverityVersion invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));

                final String prefix = "externalVersion=";
                String line, version = "";
                try {
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith(prefix)) {
                            version = line.substring(prefix.length(), line.length());
                            break;
                        }
                    }
                } finally {
                    br.close();

                    return CoverityVersion.parse(version);
                }
            }
        });

        return version;
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

        @Override
        public String getStatus() {
            if (stream != null) {
                return "[Stream] " + stream.toPrettyString() + " : " + status;
            }
            return status;
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

        @Override
        public String getStatus() {
            if (node != null) {
                return "[Node] " + node.getDisplayName() + " : " + status;
            }
            return status;
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
