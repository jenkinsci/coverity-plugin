package jenkins.plugins.coverity;

import hudson.model.Result;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class BuildThreshold {

    private int value;
    private Result type;

    @DataBoundConstructor
    public BuildThreshold (int thresholdValue, String buildType)
	throws Descriptor.FormException
    {
	this.value = thresholdValue;
	if (buildType.equals ("unstable"))
	    this.type = Result.UNSTABLE;
	else if (buildType.equals ("failure"))
	    this.type = Result.FAILURE;
    }

    public int getThresholdValue () {
	return this.value;
    }

    public Result getType () {
	return this.type;
    }
}
