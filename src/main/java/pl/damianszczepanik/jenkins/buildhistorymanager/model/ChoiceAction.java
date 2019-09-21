package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class ChoiceAction extends Action {

    private final String choice;

    @DataBoundConstructor
    public ChoiceAction(String choice) {
        this.choice = choice;
    }

    public String getChoice() {
        return choice;
    }

    @Extension
    public static class ChoiceActionDescriptor extends Descriptor<Action> {

        @Override
        public String getDisplayName() {
            return "Choice Action";
        }

        public ListBoxModel doFillChoiceItems() {

            return new ListBoxModel().add("good").add("bad").add("ugly");
        }
    }
}
