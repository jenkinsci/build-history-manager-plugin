package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteArtifactsAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteArtifactsAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class DeleteArtifactsActionDescriptor extends Descriptor<Action> {

    public DeleteArtifactsActionDescriptor() {
        super(DeleteArtifactsAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Delete artifacts";
    }
}
