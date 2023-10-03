package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import org.jenkinsci.Symbol;

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

    public DeleteArtifactsWithPatternActionDescriptor() {
        super(DeleteArtifactsWithPatternAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Delete artifacts with pattern";
    }
}
