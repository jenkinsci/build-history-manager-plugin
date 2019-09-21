package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.TextAction;

/**
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
