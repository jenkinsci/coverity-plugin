package jenkins.plugins.coverity.analysis;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Api;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * This object is added to {@link AbstractBuild#getActions()}.
 * This persists the Git related information of that build.
 */
@ExportedBean(defaultVisibility = 999)
public abstract class CoverityData implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    private List<Long> previous;
    private List<Long> current;
    private String cidString;

    public CoverityData () {
	this.previous = null;
	this.current = null;
	this.cidString = "";
    }

    @Exported
    public String getCidString () {
	return this.cidString;
    }

    public void setBuildDefects (List<Long> cids) {
	this.current = cids;
	this.cidString = "";
	for (i Long : this.current) {
	    this.cidString += i + "; ";
	}
    }

    public Api getApi () {
        return new Api (this);
    }

    public String getDisplayName () {
        return "Coverity Build Data";
    }

    public String getUrlName() {
        return "coverity";
    }
}
