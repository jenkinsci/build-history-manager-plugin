package pl.damianszczepanik.jenkins.buildhistorymanager.control;

import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Controller for the @{@link pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleValidator {

    private static final Logger LOG = Logger.getLogger(BuildHistoryManager.class.getName());

    private final Rule rule;

    private String jobName;

    private int matchedTimes;

    public RuleValidator(Rule rule, String jobName) {
        this.rule = rule;
        rule.setJobName(jobName);
        this.jobName = jobName;
    }

    public int getMatchedTimes() {
        return matchedTimes;
    }

    public Rule getRule() {
        return rule;
    }

    public boolean validateConditions(Run<?, ?> run) {
        // stop checking if max number of processed builds is reached
        if (matchedTimes == rule.getMatchAtMost()) {
            log(jobName, String.format("Skipping rule because matched %d times", matchedTimes));
            return false;
        }

        boolean matched = rule.matchesConditions(run);
        if (matched) {
            matchedTimes++;
        }
        return matched;
    }

    private static void log(String jobName, String message) {
        LOG.log(Level.FINE, () -> String.format("[%s] %s", jobName, message));
    }
}
