package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.io.IOException;
import java.util.List;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Job;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Rule extends AbstractDescribableImpl<Rule> {

    private final List<Condition> conditions;

    private final List<Action> actions;

    private int matchAtMost;
    private boolean continueAfterMatch;

    @DataBoundConstructor
    public Rule(List<Condition> conditions, List<Action> actions) {
        this.conditions = conditions;
        this.actions = actions;
        this.continueAfterMatch = true;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

    @DataBoundSetter
    public void setMatchAtMost(int matchAtMost) {
        this.matchAtMost = matchAtMost;
    }

    public int getMatchAtMost() {
        return matchAtMost;
    }

    @DataBoundSetter
    public void setContinueAfterMatch(boolean continueAfterMatch) {
        this.continueAfterMatch = continueAfterMatch;
    }

    public boolean getContinueAfterMatch() {
        return continueAfterMatch;
    }

    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
    }
}
