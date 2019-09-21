package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class ChoiceCondition extends Condition {

    private final String choice;

    @DataBoundConstructor
    public ChoiceCondition(String choice) {
        this.choice = choice;
    }

    public String getChoice() {
        return choice;
    }

    @Extension
    public static class ChoiceConditionDescriptor extends Descriptor<Condition> {

        @Override
        public String getDisplayName() {
            return "Choice Condition";
        }

        public ListBoxModel doFillChoiceItems() {

            return new ListBoxModel().add("red").add("blue").add("white");
        }
    }
}
