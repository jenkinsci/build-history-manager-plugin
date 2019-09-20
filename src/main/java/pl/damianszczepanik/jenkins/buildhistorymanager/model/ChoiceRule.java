package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class ChoiceRule extends Rule {

    private final String choice;

    @DataBoundConstructor
    public ChoiceRule(String choice) {
        this.choice = choice;
    }

    public String getChoice() {
        return choice;
    }

    @Extension
    public static class ChoiceRuleDescriptor extends Descriptor<Rule> {

        @Override
        public String getDisplayName() {
            return "Choice Rule";
        }

        public ListBoxModel doFillChoiceItems() {
            return new ListBoxModel().add("good").add("bad").add("ugly");
        }
    }
}
