package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteJobArtifactsAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteJobArtifactsAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class DeleteJobArtifactsActionDescriptor extends Descriptor<Action> {

    public DeleteJobArtifactsActionDescriptor() {
        super(DeleteJobArtifactsAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Delete job artifacts";
    }
}
