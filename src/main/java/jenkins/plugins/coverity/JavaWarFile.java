package jenkins.plugins.coverity;

import hudson.model.AbstractDescribableImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Util;

public class JavaWarFile extends AbstractDescribableImpl<JavaWarFile> {

    public String warFile;

    public String getWarFile() {
        return warFile;
    }

    public void setWarFile(String filePath) {
        this.warFile = filePath;
    }

    @DataBoundConstructor
    public JavaWarFile(String warFile){
        this.warFile=Util.fixEmpty(warFile);
    }

}
