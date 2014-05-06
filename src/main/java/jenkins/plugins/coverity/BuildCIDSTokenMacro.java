package jenkins.plugins.coverity;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import jenkins.plugins.coverity.analysis.CoverityData;
import jenkins.plugins.coverity.CoverityBuildAction;

@Extension
public class BuildCIDSTokenMacro extends DataBoundTokenMacro {

    @Override
    public boolean acceptsMacroName (String macroName) {
        return macroName.equals ("COVERITY_CIDS");
    }

    @Override
    public String evaluate (AbstractBuild<?, ?> context, TaskListener listener, String macroName)
	throws MacroEvaluationException
    {
	CoverityData data = context.getAction (CoverityData.class);
	CoverityBuildAction build = context.getAction (CoverityBuildAction.class);
	if (data == null || build == null)
	    return "None";
	return data.getCidString (build);
    }
}
