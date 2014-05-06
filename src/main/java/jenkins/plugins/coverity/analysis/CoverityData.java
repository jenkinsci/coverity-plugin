package jenkins.plugins.coverity.analysis;

import hudson.Functions;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Api;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import jenkins.plugins.coverity.CoverityBuildAction;

import java.io.Serializable;
import java.util.*;

@ExportedBean(defaultVisibility = 999)
public class CoverityData implements Action, Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private List<Long> previous;
    private List<Long> now;

    public CoverityData (List<Long> previous, List<Long> now) {
	this.previous = previous;
	this.now = now;
    }

    @Exported
    public String getCidString (CoverityBuildAction build) {
	String retval = "Coverity Stream Diff:\n";
	retval += "\tTotal defects found: [" + this.now.size () +
	    "] Previous number of defects [" +  this.previous.size () + "]\n";
	if (this.now.size () == this.previous.size ())
	    retval += "\tNo Change!";
	else {
	    for (Long i : this.now) {
		boolean found = false;
		for (Long y : this.previous) {
		    if (y.longValue () == i.longValue ()) {
			found = true;
			break;
		    }
		}
		if (!found) {
		    try {
			String url = build.getURL (i);
			retval += "\t ++ " + i.longValue () + " " + url + "\n";
		    } catch (Exception e) {
			// ...
		    }
		}
	    }
	    for (Long i : this.previous) {
		boolean found = false;
		for (Long y : this.now) {
		    if (y.longValue () == i.longValue ()) {
			found = true;
			break;
		    }
		}
		if (!found) {
		    try {
			String url = build.getURL (i);
			retval += "\t -- " + i.longValue () + " " + url + "\n";
		    } catch (Exception e) {
			// ...
		    }
		}
	    }
	}
	return retval;
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

    public String getIconFileName () {
	return Functions.getResourcePath()+"/plugin/coverity/icons/coverity-logo.gif";
    }
}
