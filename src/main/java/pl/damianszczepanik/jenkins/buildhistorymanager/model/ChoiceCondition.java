package pl.damianszczepanik.jenkins.buildhistorymanager.model;

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
}
