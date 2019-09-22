package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.TextCondition;

/**
 * Descriptor implementation needed to render UI for {@link TextCondition}.
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
