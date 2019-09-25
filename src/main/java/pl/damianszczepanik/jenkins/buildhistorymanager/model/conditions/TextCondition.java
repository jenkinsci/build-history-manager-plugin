package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class TextCondition extends Condition {

    private final String text;

    @DataBoundConstructor
    public TextCondition(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean matches(Run<?, ?> run) {
        return false;
    }
}
