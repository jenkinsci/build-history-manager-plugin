package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteBuildAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteBuildAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("DeleteBuild")
public class DeleteBuildActionDescriptor extends Descriptor<Action> {

    public DeleteBuildActionDescriptor() {
        super(DeleteBuildAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Delete build";
    }
}
