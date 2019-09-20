package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.Extension;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class TextRule extends Rule {

    private final String text;

    @DataBoundConstructor
    public TextRule(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Extension
    public static class TextRuleDescriptor extends Descriptor<Rule> {

        @Override
        public String getDisplayName() {
            return "Text Rule";
        }
    }
}
