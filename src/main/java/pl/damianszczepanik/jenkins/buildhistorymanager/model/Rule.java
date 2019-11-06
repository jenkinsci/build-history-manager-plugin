package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Rule extends AbstractDescribableImpl<Rule> {

    private static final Logger LOG = Logger.getLogger(Rule.class.getName());

    private final List<Condition> conditions;

    private final List<Action> actions;

    private final RuleConfiguration configuration = new RuleConfiguration();

    private transient int matchedTimes;

    @DataBoundConstructor
    public Rule(List<Condition> conditions, List<Action> actions) {
        this.conditions = Util.fixNull(conditions);
        this.actions = Util.fixNull(actions);
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

    /**
     * Resets local counters and variables before processing conditions and actions.
     */
    public void initialize() {
        matchedTimes = 0;
    }

    /**
     * Checks if passed build matches with all conditions from this rule.
     *
     * @param run build to validate
     * @return <code>true</code> if all conditions match otherwise <code>false</code>
     */
    public boolean validateConditions(Run<?, ?> run) {
        // stop checking if max number of processed builds is reached
        if (matchedTimes == getMatchAtMost()) {
            LOG.info(String.format("Skipping rule because matched %d times", matchedTimes));
            return false;
        }

        // validateConditions condition one by one...
        for (Condition condition : conditions) {
            LOG.info(String.format("Processing condition '%s'", condition.getDescriptor().getDisplayName()));
            boolean conditionMatched = condition.matches(run, configuration);
            // stop checking rest conditions when at least condition does not match
            if (!conditionMatched) {
                LOG.info(String.format("Condition '%s' does not match", condition.getDescriptor().getDisplayName()));
                return false;
            }
        }

        matchedTimes++;
        return true;
    }

    public void performActions(Run<?, ?> run) throws IOException, InterruptedException {
        for (Action action : actions) {
            LOG.info(String.format("Processing action '%s' for build #%d",
                    action.getDescriptor().getDisplayName(), run.getNumber()));
            action.perform(run);
        }
    }
}
