package jenkins.plugins.coverity;

import com.coverity.ws.v5.CovRemoteServiceException_Exception;
import com.coverity.ws.v5.StreamDataObj;
import com.coverity.ws.v5.StreamFilterSpecDataObj;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.List;
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
            e.printStackTrace();
        }

        if (false) {
            StringBuilder status = new StringBuilder();

            for (CIMStream cs : publisher.getCimStreams()) {
                CIMInstance ci = publisher.getDescriptor().getInstance(cs.getInstance());

                //check if instance is valid
                boolean instanceValid = true;
                try {
                    FormValidation fv = ci.doCheck();
                    if (fv.kind != FormValidation.Kind.OK) {
                        instanceValid = false;
                    }
                } catch (IOException e) {
                    instanceValid = false;
                }
                if (!instanceValid) {
                    status.append("Could not connect to instance: " + ci.getHost() + ":" + ci.getPort());
                    status.append('\n');
                    break;
                }

                try {
                    StreamFilterSpecDataObj sf = new StreamFilterSpecDataObj();
                    sf.setNamePattern(cs.getStream());
                    List<StreamDataObj> ls = ci.getConfigurationService().getStreams(sf);
                    if(ls.size() == 0) {
                        status.append("Stream does not exist: " + ci.getHost() + ":" + ci.getPort() + "/" + cs.getStream());
                        break;
                    }

                } catch (CovRemoteServiceException_Exception e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
