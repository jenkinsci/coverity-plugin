package jenkins.plugins.coverity;

import hudson.FilePath;
import hudson.model.InvisibleAction;

class CoverityTempDir extends InvisibleAction {
    transient final FilePath tempDir;

    CoverityTempDir(FilePath tempDir) {
        this.tempDir = tempDir;
    }
}
