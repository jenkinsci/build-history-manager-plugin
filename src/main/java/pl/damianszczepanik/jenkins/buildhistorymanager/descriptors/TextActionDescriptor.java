package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.TextAction;

/**
 * Descriptor implementation needed to render UI for {@link pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.TextAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class TextActionDescriptor extends Descriptor<Action> {

    public TextActionDescriptor() {
        super(TextAction.class);
    }

    @Override
    public String getDisplayName() {
        return "Text Action";
    }
}
