package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteArtifactsWithPatternAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteArtifactsWithPatternAction}.
 */
@Extension
@Symbol("DeleteArtifactsWithPattern")
public class DeleteArtifactsWithPatternActionDescriptor extends Descriptor<Action>{

    private String include;
    private String exclude;

    public DeleteArtifactsWithPatternActionDescriptor() {
        super(DeleteArtifactsWithPatternAction.class);
    }

    public String getInclude() {
        return include;
    }

    @DataBoundSetter
    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    @DataBoundSetter
    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    @Override
    public String getDisplayName() {
        return "Delete artifacts with pattern";
    }
}
