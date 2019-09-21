package pl.damianszczepanik.jenkins.buildhistorymanager.model;

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

}
