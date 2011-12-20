package jenkins.plugins.coverity;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.emailext.EmailType;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailContent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension(optional=true)
public class EmailContentImpl implements EmailContent {
    public String getToken() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public List<String> getArguments() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getHelpText() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public <P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>> String getContent(AbstractBuild<P, B> pbAbstractBuild, ExtendedEmailPublisher extendedEmailPublisher, EmailType emailType, Map<String, ?> stringMap) throws IOException, InterruptedException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean hasNestedContent() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
