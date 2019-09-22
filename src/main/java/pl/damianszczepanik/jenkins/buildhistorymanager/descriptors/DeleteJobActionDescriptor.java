package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteJobAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteJobAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class DeleteJobActionDescriptor extends Descriptor<Action> {

    public DeleteJobActionDescriptor() {
        super(DeleteJobAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Delete job";
    }
}
