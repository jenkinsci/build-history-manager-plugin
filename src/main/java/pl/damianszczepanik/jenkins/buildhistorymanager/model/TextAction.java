package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public final class TextAction extends Action {

    private final String text;

    @DataBoundConstructor
    public TextAction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
