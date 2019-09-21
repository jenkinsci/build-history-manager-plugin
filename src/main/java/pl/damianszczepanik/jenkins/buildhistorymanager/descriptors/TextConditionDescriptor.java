package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Condition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.TextCondition;

/**
 * Descriptor implementation needed for rendering UI for {@link TextCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class TextConditionDescriptor extends Descriptor<Condition> {

    public TextConditionDescriptor() {
        super(TextCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Text Condition";
    }
}
