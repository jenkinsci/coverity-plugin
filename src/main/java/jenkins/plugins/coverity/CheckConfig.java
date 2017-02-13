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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * A configuration checker, and the results of such a check.
 */
public class CheckConfig extends AbstractDescribableImpl<CheckConfig> {
    private static final Logger logger = Logger.getLogger(CIMStream.class.getName());
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
                        CIMStream stream = ss.getStream();
                        if(!ss.getVersion().compareToAnalysis(analysisVersion) && stream != null) {
                            newStatus.add(new Status(false, "Connect instance " + stream.toPrettyString() + " (version " +
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
                            CIMStream stream = ss.getStream();
                            if(stream != null && stream.getDomain().equals("MIXED")) {
                                newStatus.add(new Status(false, "Stream " + stream.toPrettyString() + " (any language) is incompatible with analysis version " + analysisVersion));
                            }
                        }
                    }
                    status.addAll(newStatus);
                }
            }
        }
    }

    public static StreamStatus checkStream(CoverityPublisher publisher, CIMStream cs) {

        if(cs == null || cs.getInstance() == null){
            return new StreamStatus(false, "Could not connect to a Coverity instance. \n " +
                    "Verify that a Coverity instance have been configured for this job", cs, null);
        }

        CIMInstance ci = publisher.getDescriptor().getInstance(cs.getInstance());

        if(ci == null){
            return new StreamStatus(false, "Could not connect to a Coverity instance. \n " +
                    "Verify that a Coverity instance have been configured for this job", cs, null);
        }

        if(cs.getStream() == null){
            return new StreamStatus(false, "Could not find any Stream that matches the given configuration for this job.", cs, null);
        }

        //check if instance is valid
        {
            try {
                FormValidation fv = ci.doCheck();
                if(fv.kind != FormValidation.Kind.OK) {
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
            if(ia != null && ia.getSaOverride() != null && !ia.getSaOverride().isEmpty()) {
                home = new CoverityInstallation(ia.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
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

            final TaskListener listen = listener; // Final copy of listner to help print debugging messages

            CoverityVersion version = getVersion(homePath, listen);

            if(version.compareTo(CoverityVersion.MINIMUM_SUPPORTED_VERSION) < 0) {
                return new NodeStatus(false,
                    "\"Coverity Static Analysis\" version " + version.toString() + " is not supported. " +
                    "The minimum supported version is " + CoverityVersion.MINIMUM_SUPPORTED_VERSION.getEffectiveVersion().toString(),
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
     * Gets the {@link CoverityVersion} given a static analysis tools home directory by finding the VERSION.xml file,
     * then reading the version number
     */
    public static CoverityVersion getVersion(FilePath homePath, final TaskListener listener) throws IOException, InterruptedException {
        CoverityVersion version = homePath.child("VERSION.xml").act(new FilePath.FileCallable<CoverityVersion>() {
                                                                        public CoverityVersion invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                                                                            InputStream fis = new FileInputStream(f);

                                                                            // Setting up reader into UTF-8 format since xml document is that format
                                                                            Reader reader = new InputStreamReader(fis,"UTF-8");
                                                                            InputSource is = new InputSource(reader);
                                                                            is.setEncoding("UTF-8");
                                                                            CoverityVersion cv = parseVersionXML(is, listener);
                                                                            fis.close();
                                                                            return cv;
                                                                        }});

        return version;
    }

    /**
     * Parse Version XML File
     * We use SAX Parser to go thought the VERSION.xml file, and extract the Major, Minor, Revision, and Beta elements.
     * These parts make up the version of Analysis, and is later used to compare with the cim version
     * @param path
     * @param listener
     * @return {@link CoverityVersion}
     */
    private static CoverityVersion parseVersionXML(InputSource path, TaskListener listener){
        String errorMessage;

        try{
            // Setting up SAX Parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser xmlParser = factory.newSAXParser();
            XMLReader xmlReader = xmlParser.getXMLReader();
            ConnectorParser connectorParser = new ConnectorParser();

            // Setting up XML Reader so that it ignores the <!DOCTYPE
            xmlReader.setContentHandler(connectorParser);
            xmlReader.setFeature("http://xml.org/sax/features/validation", false);
            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);

            // Parse the xml
            xmlReader.parse(path);

            // Checks to see if beta was set or not, since its not required on releases.
            try{
                if(connectorParser.beta != null){
                    return new CoverityVersion(Integer.parseInt(connectorParser.major),
                                               Integer.parseInt(connectorParser.minor),
                                               Integer.parseInt(connectorParser.revision),
                                               Integer.parseInt(connectorParser.beta));
                }else{
                    return new CoverityVersion(Integer.parseInt(connectorParser.major),
                                               Integer.parseInt(connectorParser.minor),
                                               Integer.parseInt(connectorParser.revision));
                }
            }catch(NumberFormatException e){
                    return new CoverityVersion(connectorParser.major);
            }
        }catch(ParserConfigurationException x){
            errorMessage = "Unable to configure XML parser: " + x.getMessage();
        }catch(SAXException x){
            errorMessage = "Unable to parse VERSION.xml: " + x.getMessage();
        }catch(FileNotFoundException x){
            errorMessage = "Could not find VERSION.xml file at: " + path.toString();
        }catch(IOException x){
            errorMessage = "IOException reading VERSION.xml: " + x.getMessage();
        }

        if (listener != null) {
            listener.fatalError(errorMessage);
        } else {
            logger.warning(errorMessage);
        }

        return null;
    }

    /**
     *  Custom Handler that is ran when the XML is parse and find when major, minor, revision, and beta occurs
     *  within the xml, and then store its number
     */
    private static class ConnectorParser extends DefaultHandler{
        public String major = null;
        public String minor = null;
        public String revision = null;
        public String beta = null;
        public boolean bmajor =false;
        public boolean bminor =false;
        public boolean brevision =false;
        public boolean bbeta =false;

        // Checks the start of each element it sees, then flags that specific keywords are found
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException{
            if(qName.equalsIgnoreCase("major")){
                bmajor = true;
            }
            if(qName.equalsIgnoreCase("minor")){
                bminor = true;
            }
            if(qName.equalsIgnoreCase("revision")){
                brevision = true;
            }
            if(qName.equalsIgnoreCase("beta")){
                bbeta = true;
            }
        }

        // At each entry, it will check if specific flags are set and then store the values of the set flags
        public void characters(char ch[],int start, int length){
            if(this.bmajor){
                major = new String(ch,start,length);
                bmajor = false;
            }
            if(this.bminor){
                minor = new String(ch,start,length);
                bminor = false;
            }
            if(this.brevision){
                revision = new String(ch,start,length);
                brevision = false;
            }
            if(this.bbeta){
                beta = new String(ch,start,length);
                bbeta = false;
            }
        }
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
                return stream.toPrettyString() + " : " + status;
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
                return node.getDisplayName() + " : " + status;
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
