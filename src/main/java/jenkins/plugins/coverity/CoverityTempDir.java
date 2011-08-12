package jenkins.plugins.coverity;

import hudson.FilePath;
import hudson.model.InvisibleAction;

/**
 * Created by CoverityLauncherDecorator to store the location of the temporary directory for Coverity for a certain build
 */
class CoverityTempDir extends InvisibleAction {
    transient final FilePath tempDir;

    CoverityTempDir(FilePath tempDir) {
        this.tempDir = tempDir;
    }
}
