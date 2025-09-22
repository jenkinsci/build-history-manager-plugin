package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteArtifactsAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteArtifactsAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("DeleteArtifacts")
public class DeleteArtifactsActionDescriptor extends Descriptor<Action> {

    public DeleteArtifactsActionDescriptor() {
        super(DeleteArtifactsAction.class);
    }

    @Override
    @NonNull
    public String getDisplayName() {
        return "Delete artifacts";
    }
}
