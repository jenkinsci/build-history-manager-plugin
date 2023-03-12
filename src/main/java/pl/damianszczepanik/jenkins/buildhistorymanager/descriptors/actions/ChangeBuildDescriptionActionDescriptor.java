package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.ChangeBuildDescriptionAction;

/**
 * Descriptor implementation needed to render UI for {@link ChangeBuildDescriptionAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("ChangeBuildDescription")
public class ChangeBuildDescriptionActionDescriptor extends Descriptor<Action> {

    public ChangeBuildDescriptionActionDescriptor() {
        super(ChangeBuildDescriptionAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Change build description";
    }
}
