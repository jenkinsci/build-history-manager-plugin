package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Job;
import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryDiscarder;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Rule extends AbstractDescribableImpl<Rule> {

    private static final Logger LOG = Logger.getLogger(BuildHistoryDiscarder.class.getName());

    private final List<Condition> conditions;

    private final List<Action> actions;

    private final RuleConfiguration configuration = new RuleConfiguration();

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

    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
        int matchedTimes = 0;

        Run<?, ?> run = job.getLastBuild();
        LOG.info("Processing build #" + run.getNumber());
        // for each build from the project history...
        do {
            // in case there is no condition defined, all actions should be performed
            boolean overallMatch = true;
            // validate condition one by one...
            for (Condition condition : conditions) {
                LOG.info(String.format("Processing condition '%s'", condition.getDescriptor().getDisplayName()));
                boolean conditionMatched = condition.matches(run, configuration);
                // stop checking rest conditions when at least condition does not match
                if (!conditionMatched) {
                    overallMatch = false;
                    LOG.info("Condition does not match");
                    break;
                }
            }

            if (overallMatch) {
                matchedTimes++;
                for (Action action : actions) {
                    LOG.info(String.format("Processing action '%s' for build #%d",
                            action.getDescriptor().getDisplayName(), run.getNumber()));
                    action.perform(run);
                }
            }
            // validate rules for previous build
            run = run.getPreviousBuild();
            LOG.info(String.format("Matched %d times of maximum %d", matchedTimes, getMatchAtMost()));
        } while (shouldContinue(run, matchedTimes));
    }

    private boolean shouldContinue(Run<?, ?> run, int matchedTimes) {
        // stop when the iteration reach the oldest build
        return run != null &&
                // stop checking if max number of processed builds is reached
                (matchedTimes <= getMatchAtMost() ||
                        // of there is no limit about matched jobs
                        getMatchAtMost() == RuleConfiguration.MATCH_UNLIMITED);
    }
}
