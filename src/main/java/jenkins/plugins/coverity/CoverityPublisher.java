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

import com.coverity.ws.v5.CovRemoteServiceException_Exception;
import com.coverity.ws.v5.DefectService;
import com.coverity.ws.v5.MergedDefectDataObj;
import com.coverity.ws.v5.MergedDefectFilterSpecDataObj;
import com.coverity.ws.v5.PageSpecDataObj;
import com.coverity.ws.v5.SnapshotIdDataObj;
import com.coverity.ws.v5.StreamIdDataObj;
import com.coverity.ws.v5.StreamSnapshotFilterSpecDataObj;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Executor;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ArgumentListBuilder;
import hudson.util.FormValidation;
import hudson.util.IOUtils;
import hudson.util.ListBoxModel;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
							 DefectFilters defectFilters) {
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

		if(isOldDataPresent()) {
			logger.info("Old data format detected. Converting to new format.");
			convertOldData();
		}
	}

	private void convertOldData() {
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

		CoverityTempDir temp = build.getAction(CoverityTempDir.class);

		Node node = Executor.currentExecutor().getOwner().getNode();
		String home = getDescriptor().getHome(node, build.getEnvironment(listener));
		if(invocationAssistance != null && invocationAssistance.getSaOverride() != null) {
			home = new CoverityInstallation(invocationAssistance.getSaOverride()).forEnvironment(build.getEnvironment(listener)).getHome();
		}

		// If WAR files specified, emit them prior to running analysis
		// Do not check for presence of Java streams or Java in build
		String javaWarFile = invocationAssistance.getJavaWarFile();
		listener.getLogger().println("[Coverity] Specified WAR file '" + javaWarFile + "' in config");
		if(javaWarFile != null) {
			String covEmitJava = "cov-emit-java";
			covEmitJava = new FilePath(launcher.getChannel(), home).child("bin").child(covEmitJava).getRemote();

			List<String> cmd = new ArrayList<String>();
			cmd.add(covEmitJava);
			cmd.add("--dir");
			cmd.add(temp.tempDir.getRemote());
			cmd.add("--webapp-archive");
			cmd.add(javaWarFile);

			try {
				CoverityLauncherDecorator.SKIP.set(true);

				int result = launcher.
						launch().
						cmds(new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]))).
						pwd(build.getWorkspace()).
						stdout(listener).
						join();

				if(result != 0) {
					listener.getLogger().println("[Coverity] " + covEmitJava + " returned " + result + ", aborting...");
					build.setResult(Result.FAILURE);
					return false;
				}
			} finally {
				CoverityLauncherDecorator.SKIP.set(false);
			}
		}

		Set<String> analyzedLanguages = new HashSet<String>();

		for(CIMStream cimStream : getCimStreams()) {
			CIMInstance cim = getDescriptor().getInstance(cimStream.getInstance());

			String language = null;
			try {
				language = getLanguage(cimStream);
			} catch(CovRemoteServiceException_Exception e) {
				e.printStackTrace(listener.error("Error while retrieving stream information for " + cimStream.getStream()));
				return false;
			}

			if(invocationAssistance != null) {
				InvocationAssistance effectiveIA = invocationAssistance;
				if(cimStream.getInvocationAssistanceOverride() != null) {
					effectiveIA = invocationAssistance.merge(cimStream.getInvocationAssistanceOverride());
				}

				try {
					if("CSHARP".equals(language) && effectiveIA.getCsharpAssemblies() != null) {
						String csharpAssembliesStr = effectiveIA.getCsharpAssemblies();
						listener.getLogger().println("[Coverity] C# Project detected, assemblies to analyze are: " + csharpAssembliesStr);
					}

					String covAnalyze = null;
					if("JAVA".equals(language)) {
						covAnalyze = "cov-analyze-java";
					} else if("CSHARP".equals(language)) {
						covAnalyze = "cov-analyze-cs";
					} else {
						covAnalyze = "cov-analyze";
					}

					String covCommitDefects = "cov-commit-defects";

					if(home != null) {
						covAnalyze = new FilePath(launcher.getChannel(), home).child("bin").child(covAnalyze).getRemote();
						covCommitDefects = new FilePath(launcher.getChannel(), home).child("bin").child(covCommitDefects).getRemote();
					}

					CoverityLauncherDecorator.SKIP.set(true);

					if(!analyzedLanguages.contains(language)) {
						List<String> cmd = new ArrayList<String>();
						cmd.add(covAnalyze);
						cmd.add("--dir");
						cmd.add(temp.tempDir.getRemote());

						// For C# add the list of assemblies
						if("CSHARP".equals(language)) {
							String csharpAssemblies = effectiveIA.getCsharpAssemblies();
							if(csharpAssemblies != null) {
								cmd.add(csharpAssemblies);
							}
						}

						listener.getLogger().println("[Coverity] cmd so far is: " + cmd.toString());
						if(effectiveIA.getAnalyzeArguments() != null) {
							for(String arg : Util.tokenize(effectiveIA.getAnalyzeArguments())) {
								cmd.add(arg);
							}
						}

						int result = launcher.
								launch().
								cmds(new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]))).
								pwd(build.getWorkspace()).
								stdout(listener).
								join();

						analyzedLanguages.add(language);

						if(result != 0) {
							listener.getLogger().println("[Coverity] " + covAnalyze + " returned " + result + ", aborting...");
							build.setResult(Result.FAILURE);
							return false;
						}
					} else {
						listener.getLogger().println("Skipping analysis, because language " + language + " has already been analyzed");
					}

					boolean useDataPort = cim.getDataPort() != 0;

					List<String> cmd = new ArrayList<String>();
					cmd.add(covCommitDefects);
					cmd.add("--dir");
					cmd.add(temp.tempDir.getRemote());
					cmd.add("--host");
					cmd.add(cim.getHost());
					cmd.add(useDataPort ? "--dataport" : "--port");
					cmd.add(useDataPort ? Integer.toString(cim.getDataPort()) : Integer.toString(cim.getPort()));
					cmd.add("--stream");
					cmd.add(cimStream.getStream());
					cmd.add("--user");
					cmd.add(cim.getUser());

					if(effectiveIA.getCommitArguments() != null) {
						for(String arg : Util.tokenize(effectiveIA.getCommitArguments())) {
							cmd.add(arg);
						}
					}

					ArgumentListBuilder args = new ArgumentListBuilder(cmd.toArray(new String[cmd.size()]));

					int result = launcher.
							launch().
							cmds(args).
							envs(Collections.singletonMap("COVERITY_PASSPHRASE", cim.getPassword())).
							stdout(listener).
							stderr(listener.getLogger()).
							join();

					if(result != 0) {
						listener.getLogger().println("[Coverity] cov-commit-defects returned " + result + ", aborting...");

						build.setResult(Result.FAILURE);
						return false;
					}
				} finally {
					CoverityLauncherDecorator.SKIP.set(false);
				}
			}
		}

		//keep if keepIntDir is set, or if the int dir is the default (keepIntDir is only useful if a custom int
		//dir is set)
		if(temp != null) {
			//same as !(keepIntDir && !temp.def)
			if(!keepIntDir || temp.def) {
				listener.getLogger().println("[Coverity] deleting intermediate directory");
				temp.tempDir.deleteRecursive();
			} else {
				listener.getLogger().println("[Coverity] preserving intermediate directory: " + temp.tempDir);
			}
		}

		//TODO: handle multiple snapshots
		Pattern snapshotPattern = Pattern.compile(".*New snapshot ID (\\d*) added.");
		BufferedReader reader = new BufferedReader(build.getLogReader());
		String line = null;
		List<Long> snapshotIds = new ArrayList<Long>();
		try {
			while((line = reader.readLine()) != null) {
				Matcher m = snapshotPattern.matcher(line);
				if(m.matches()) {
					snapshotIds.add(Long.parseLong(m.group(1)));
				}
			}
		} finally {
			reader.close();
		}

		if(snapshotIds.size() != getCimStreams().size()) {
			listener.getLogger().println("[Coverity] Wrong number of snapshot IDs found in build log");
			build.setResult(Result.FAILURE);
			return false;
		}

		listener.getLogger().println("[Coverity] Found snapshot IDs " + snapshotIds);

		if(!skipFetchingDefects) {
			for(int i = 0; i < cimStreams.size(); i++) {
				CIMStream cimStream = cimStreams.get(i);
				long snapshotId = snapshotIds.get(i);
				try {
					CIMInstance cim = getDescriptor().getInstance(cimStream.getInstance());

					listener.getLogger().println("[Coverity] Fetching defects for stream " + cimStream.getStream());

					List<MergedDefectDataObj> defects = getDefectsForSnapshot(cimStream, snapshotId);

					listener.getLogger().println("[Coverity] Found " + defects.size() + " defects");

					Set<String> checkers = new HashSet<String>();
					for(MergedDefectDataObj defect : defects) {
						checkers.add(defect.getCheckerName());
					}
					getDescriptor().updateCheckers(getLanguage(cimStream), checkers);

					List<Long> matchingDefects = new ArrayList<Long>();

					for(MergedDefectDataObj defect : defects) {
						if(cimStream.getDefectFilters() == null) {
							matchingDefects.add(defect.getCid());
						} else {
							boolean match = cimStream.getDefectFilters().matches(defect);
							if(match) {
								matchingDefects.add(defect.getCid());
							}
						}
					}

					if(!matchingDefects.isEmpty()) {
						listener.getLogger().println("[Coverity] Found " + matchingDefects.size() + " defects matching all filters: " + matchingDefects);
						if(failBuild) {
							if(build.getResult().isBetterThan(Result.FAILURE)) {
								build.setResult(Result.FAILURE);
							}
						}
					} else {
						listener.getLogger().println("[Coverity] No defects matched all filters.");
					}

					CoverityBuildAction action = new CoverityBuildAction(build, cimStream.getProject(), cimStream.getStream(), cimStream.getInstance(), matchingDefects);
					build.addAction(action);

					if(!matchingDefects.isEmpty() && mailSender != null) {
						mailSender.execute(action, listener);
					}

					String rootUrl = Hudson.getInstance().getRootUrl();
					if(rootUrl != null) {
						listener.getLogger().println("Coverity details: " + Hudson.getInstance().getRootUrl() + build.getUrl() + Util.rawEncode(action.getUrlName()));
					}

				} catch(CovRemoteServiceException_Exception e) {
					e.printStackTrace(listener.error("[Coverity] An error occurred while fetching defects"));
					build.setResult(Result.FAILURE);
					return false;
				}
			}
		}
		return true;
	}

	public List<MergedDefectDataObj> getDefectsForSnapshot(CIMStream cimStream, long snapshotId) throws IOException, CovRemoteServiceException_Exception {
		CIMInstance cim = getDescriptor().getInstance(cimStream.getInstance());
		DefectService ds = cim.getDefectService();

		PageSpecDataObj pageSpec = new PageSpecDataObj();
		pageSpec.setPageSize(100);
		pageSpec.setSortAscending(true);

		StreamIdDataObj streamId = new StreamIdDataObj();
		streamId.setName(cimStream.getStream());

		MergedDefectFilterSpecDataObj filter = new MergedDefectFilterSpecDataObj();
		StreamSnapshotFilterSpecDataObj sfilter = new StreamSnapshotFilterSpecDataObj();
		SnapshotIdDataObj snapid = new SnapshotIdDataObj();
		snapid.setId(snapshotId);

		sfilter.setStreamId(streamId);

		sfilter.getSnapshotIdIncludeList().add(snapid);
		filter.getStreamSnapshotFilterSpecIncludeList().add(sfilter);

		return ds.getMergedDefectsForStreams(Arrays.asList(streamId), filter, pageSpec).getMergedDefects();
	}

	public String getLanguage(CIMStream cimStream) throws IOException, CovRemoteServiceException_Exception {
		return getDescriptor().getInstance(cimStream.getInstance()).getStream(cimStream.getStream()).getLanguage();
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

		public String getJavaCheckers() {
			return javaCheckers;
		}

		public void setJavaCheckers(String javaCheckers) {
			this.javaCheckers = Util.fixEmpty(javaCheckers);
			try {
				//if(this.javaCheckers == null)
				this.javaCheckers = IOUtils.toString(getClass().getResourceAsStream("java-checkers.txt"));
			} catch(IOException e) {
			}
		}

		public String getCxxCheckers() {
			return cxxCheckers;
		}

		public void setCxxCheckers(String cxxCheckers) {
			this.cxxCheckers = Util.fixEmpty(cxxCheckers);
			try {
				//if(this.cxxCheckers == null)
				this.cxxCheckers = IOUtils.toString(getClass().getResourceAsStream("cxx-checkers.txt"));
			} catch(IOException e) {
			}
		}

		public String getCsharpCheckers() {
			return csharpCheckers;
		}

		public void setCsharpCheckers(String csharpCheckers) {
			this.csharpCheckers = Util.fixEmpty(csharpCheckers);
			try {
				//if(this.csharpCheckers == null)
				this.csharpCheckers = IOUtils.toString(getClass().getResourceAsStream("csharp-checkers.txt"));
			} catch(IOException e) {
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

		public void doDefectFiltersConfig(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
			logger.info(req.getSubmittedForm().toString());

			JSONObject json = null;
			//try old-style json format
			JSONObject jsonA = req.getSubmittedForm().getJSONObject(getJsonSafeClassName());
			if(jsonA == null || jsonA.toString().equals("null")) {
				//new style json format
				JSON jsonB = (JSON) req.getSubmittedForm().get("publisher");
				String targetClass = getId();
				if(jsonB.isArray()) {
					JSONArray arr = (JSONArray) jsonB;
					for(Object i : arr) {
						JSONObject ji = (JSONObject) i;
						if(targetClass.equals(ji.get("stapler-class"))) {
							json = ji;
							break;
						}
					}
				} else {
					json = (JSONObject) jsonB;
				}
			} else {
				json = jsonA;
			}

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
