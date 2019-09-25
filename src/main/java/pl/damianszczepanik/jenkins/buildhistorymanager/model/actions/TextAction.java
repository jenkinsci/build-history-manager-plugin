package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.Run;
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

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {

    }
}
