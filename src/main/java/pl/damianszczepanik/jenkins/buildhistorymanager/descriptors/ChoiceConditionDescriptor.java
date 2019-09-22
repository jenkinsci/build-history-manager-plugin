package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.ChoiceCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link ChoiceCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class ChoiceConditionDescriptor extends Descriptor<Condition> {

    public ChoiceConditionDescriptor() {
        super(ChoiceCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Choice Condition";
    }

    public ListBoxModel doFillChoiceItems() {

        return new ListBoxModel().add("red").add("blue").add("white");
    }
}
