package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import org.jenkinsci.Symbol;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteArtifactsMatchingPatternsAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteArtifactsMatchingPatternsAction}.
 */
@Extension
@Symbol("DeleteArtifactsMatchingPatterns")
public class DeleteArtifactsMatchingPatternsActionDescriptor extends Descriptor<Action>{

    public DeleteArtifactsMatchingPatternsActionDescriptor() {
        super(DeleteArtifactsMatchingPatternsAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Delete artifacts matching patterns";
    }
}
