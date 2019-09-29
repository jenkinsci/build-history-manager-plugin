package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.io.IOException;
import java.util.List;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Job;
import hudson.model.Run;
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

    private final RuleConfiguration configuration = new RuleConfiguration();

    @DataBoundConstructor
    public Rule(List<Condition> conditions, List<Action> actions) {
        this.conditions = conditions;
        this.actions = actions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

    @DataBoundSetter
    public void setMatchAtMost(int matchAtMost) {
        configuration.setMatchAtMost(matchAtMost);
    }

    public int getMatchAtMost() {
        return configuration.getMatchAtMost();
    }

    @DataBoundSetter
    public void setContinueAfterMatch(boolean continueAfterMatch) {
        configuration.setContinueAfterMatch(continueAfterMatch);
    }

    public boolean getContinueAfterMatch() {
        return configuration.isContinueAfterMatch();
    }

    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
        int matchedTimes = 0;

        Run<?, ?> run = job.getLastBuild();
        // for each build from the project history...
        do {
            // in case there is no condition defined, all actions should be performed
            boolean overallMatch = true;
            // validate condition one by one...
            for (Condition condition : conditions) {
                boolean conditionMatched = condition.matches(run, configuration);
                // stop checking rest conditions when at least condition does not match
                if (!conditionMatched) {
                    overallMatch = false;
                    break;
                }
            }

            if (overallMatch) {
                matchedTimes++;
                for (Action action : actions) {
                    action.perform(run);
                }
            }
            // validate rules for previous build
            run = run.getPreviousBuild();
        } while (
                // stop when the iteration reach the oldest build
                run != null &&
                // stop checking if max number of processed builds is reached
                matchedTimes <= configuration.getMatchAtMost());
    }
}
