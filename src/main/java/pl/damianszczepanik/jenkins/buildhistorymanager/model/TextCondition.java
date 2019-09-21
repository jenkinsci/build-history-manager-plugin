package pl.damianszczepanik.jenkins.buildhistorymanager.model;

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
}
