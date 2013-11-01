package jenkins.plugins.coverity;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Random;

public class CheckConfig extends AbstractDescribableImpl<CheckConfig> {
    private boolean valid;
    private CoverityPublisher publisher;

    @DataBoundConstructor
    public CheckConfig(CoverityPublisher publisher) {
        this.valid = new Random().nextBoolean();
        this.publisher = publisher;
    }

    public boolean getValid() {
        return valid;
    }

    public void check() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
