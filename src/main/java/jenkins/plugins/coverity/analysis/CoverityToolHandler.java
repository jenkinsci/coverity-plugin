package jenkins.plugins.coverity.analysis;

import com.coverity.ws.v6.*;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.util.ArgumentListBuilder;
import jenkins.plugins.coverity.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * CoverityToolHandler handles the actual executing of Coverity executables. This class provides common functionality,
 * and {@link #getHandler(CoverityVersion)} to choose an appropriate subclass.}
 */
public abstract class CoverityToolHandler {
    /**
     * Find a suitable {@link CoverityToolHandler} to run analysis.
     * @param version the version of Coverity analysis present on the {@link hudson.model.Node} where the analysis will
     *                run.
     * @return A {@link CoverityToolHandler} that can run the given version of analysis.
     */
    public static CoverityToolHandler getHandler(CoverityVersion version) {
        // A fail safe for testing so that when we have new versions of analysis and platform, the build steps will
        // Automatically go to fresnotoolhandler
        if (version.isCodeName() && !version.containsCodeName()){
            return new FresnoToolHandler(version);
        }

        if(version.compareTo(CoverityVersion.VERSION_FRESNO) < 0) {
            return new PreFresnoToolHandler();
        } else {
            return new FresnoToolHandler(version);
        }
    }

    public static Collection<File> listFiles(
            File directory,
            FilenameFilter filter,
            boolean recurse) {
        Vector<File> files = new Vector<File>();
        File[] entries = directory.listFiles();

        for(File entry : entries) {
            if(filter == null || filter.accept(directory, entry.getName())) {
                files.add(entry);
            }

            if(recurse && entry.isDirectory()) {
                files.addAll(listFiles(entry, filter, recurse));
            }
        }

        return files;
    }

    public static File[] listFilesAsArray(
            File directory,
            FilenameFilter filter,
            boolean recurse) {
        Collection<File> files = listFiles(directory, filter, recurse);

        File[] arr = new File[files.size()];
        return files.toArray(arr);
    }

    public abstract boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, CoverityPublisher publisher) throws InterruptedException, IOException, CovRemoteServiceException_Exception;

    public File[] findMsvscaOutputFiles(String dirName) {
        File dir = new File(dirName);

        return listFilesAsArray(dir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith("CodeAnalysisLog.xml");
            }
        }, true);
    }

    public File[] findAssemblies(String dirName) {
        File dir = new File(dirName);

        return listFilesAsArray(dir, new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                if(filename.endsWith(".exe") || filename.endsWith(".dll")) {
                    String pathname = dir.getAbsolutePath();
                    pathname = pathname + File.separator + filename;
                    ;
                    String pdbFilename = pathname.replaceAll(".exe$", ".pdb");
                    pdbFilename = pdbFilename.replaceAll(".dll$", ".pdb");

                    File pdbFile = new File(pdbFilename);

                    return pdbFile.exists();
                }

                return false;
            }

        }, true);
    }

    public List<MergedDefectDataObj> getDefectsForSnapshot(CIMInstance cim, CIMStream cimStream, long snapshotId, BuildListener listener) throws IOException, CovRemoteServiceException_Exception  {
        int defectSize = 3000; // Maximum amount of defect to pull
        int pageSize = 1000; // Size of page to be pulled
        List<MergedDefectDataObj> mergeList = new ArrayList<MergedDefectDataObj>();
        DefectFilters defectFilter = cimStream.getDefectFilters();

        DefectService ds = cim.getDefectService();

        PageSpecDataObj pageSpec = new PageSpecDataObj();

        StreamIdDataObj streamId = new StreamIdDataObj();
        streamId.setName(cimStream.getStream());

        MergedDefectFilterSpecDataObj filter = new MergedDefectFilterSpecDataObj();


        StreamSnapshotFilterSpecDataObj sfilter = new StreamSnapshotFilterSpecDataObj();
        SnapshotIdDataObj snapid = new SnapshotIdDataObj();
        snapid.setId(snapshotId);

        sfilter.setStreamId(streamId);

        sfilter.getSnapshotIdIncludeList().add(snapid);
        filter.getStreamSnapshotFilterSpecIncludeList().add(sfilter);
        // The loop will pull up to the maximum amount of defect, doing per page size
        for(int pageStart = 0; pageStart < defectSize; pageStart += pageSize){
            pageSpec.setPageSize(pageSize);
            pageSpec.setStartIndex(pageStart);
            pageSpec.setSortAscending(true);
            mergeList.addAll(ds.getMergedDefectsForStreams(Arrays.asList(streamId), filter, pageSpec).getMergedDefects());

        }
        return mergeList;
    }

    public MergedDefectFilterSpecDataObj addFilters(CIMInstance cim, CIMStream cimStream, long snapshotId, BuildListener listener)throws IOException,CovRemoteServiceException_Exception{
        MergedDefectFilterSpecDataObj filter = new MergedDefectFilterSpecDataObj();
        DefectFilters defectFilter = cimStream.getDefectFilters();

        // Add all the classifications
        filter.getClassificationNameList().addAll(defectFilter.getClassifications());
        filter.getActionNameList().addAll(defectFilter.getActions());
        filter.getSeverityNameList().addAll(defectFilter.getSeverities());
        filter.getComponentIdList().addAll(defectFilter.getComponents());
        // Check to see if checker list is empty because of pre-existing settings
        // We reset the checker list to have all checkers.
        if(defectFilter.getCheckersList() == null){
            defectFilter.setCheckers(cim,snapshotId);
        }
        filter.getCheckerSubcategoryFilterSpecList().addAll(defectFilter.getCheckers(listener));
        // Getting the cutoff date to add into filter. But only should be done if the cut off date is specified.
        if(defectFilter.getCutOffDate() != null){
            filter.setFirstDetectedStartDate(defectFilter.getXMLCutOffDate());
        }

        return filter;
    }

    public boolean covEmitWar(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, String home, CoverityTempDir temp, String javaWarFile) throws IOException, InterruptedException {
        String covEmitJava = "cov-emit-java";
        covEmitJava = new FilePath(launcher.getChannel(), home).child("bin").child(covEmitJava).getRemote();

        List<String> cmd = new ArrayList<String>();
        cmd.add(covEmitJava);
        cmd.add("--dir");
        cmd.add(temp.getTempDir().getRemote());
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
                return false;
            }
        } finally {
            CoverityLauncherDecorator.SKIP.set(false);
        }

        return true;
    }

    public boolean importMsvsca(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, String home,
                                CoverityTempDir temp, boolean csharpMsvsca, String csharpMsvscaOutputFiles) throws IOException, InterruptedException {
        String covImportMsvsca = "cov-import-msvsca";
        covImportMsvsca = new FilePath(launcher.getChannel(), home).child("bin").child(covImportMsvsca).getRemote();

        List<String> importCmd = new ArrayList<String>();
        importCmd.add(covImportMsvsca);
        importCmd.add("--dir");
        importCmd.add(temp.getTempDir().getRemote());
        importCmd.add("--append");

        listener.getLogger().println("[Coverity] Searching for Microsoft Code Analysis results...");
        File[] msvscaOutputFiles = {};

        if(csharpMsvsca) {
            msvscaOutputFiles = findMsvscaOutputFiles(build.getWorkspace().getRemote());
        }

        for(File outputFile : msvscaOutputFiles) {
            //importCmd.add(outputFile.getName());
            importCmd.add(outputFile.getAbsolutePath());
        }

        if(csharpMsvscaOutputFiles != null && csharpMsvscaOutputFiles.length() > 0) {
            importCmd.add(csharpMsvscaOutputFiles);
        }

        if(msvscaOutputFiles.length == 0 && (csharpMsvscaOutputFiles == null || csharpMsvscaOutputFiles.length() == 0)) {
            listener.getLogger().println("[MSVSCA] MSVSCA No results found, skipping");
        } else {
            listener.getLogger().println("[MSVSCA] MSVSCA Import cmd so far is: " + importCmd.toString());

            int importResult = launcher.
                    launch().
                    cmds(new ArgumentListBuilder(importCmd.toArray(new String[importCmd.size()]))).
                    pwd(build.getWorkspace()).
                    stdout(listener).
                    join();
            if(importResult != 0) {
                listener.getLogger().println("[Coverity] " + covImportMsvsca + " returned " + importResult + ", aborting...");
                return false;
            }
        }

        return true;
    }
}
