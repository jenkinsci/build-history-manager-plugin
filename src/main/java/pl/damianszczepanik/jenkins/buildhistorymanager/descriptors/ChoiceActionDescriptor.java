package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.ChoiceAction;

/**
 * Descriptor implementation needed for rendering UI for {@link ChoiceAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class ChoiceActionDescriptor extends Descriptor<Action> {

    public ChoiceActionDescriptor() {
        super(ChoiceAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Choice Action";
    }

    public ListBoxModel doFillChoiceItems() {

        return new ListBoxModel().add("good").add("bad").add("ugly");
    }
}
