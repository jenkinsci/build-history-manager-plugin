package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import hudson.Util;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.BuildDiscarder;
import org.kohsuke.stapler.DataBoundConstructor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Custom implementation of {@link BuildDiscarder}
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see hudson.tasks.LogRotator
 */
public class BuildHistoryManager extends BuildDiscarder {

    private static final Logger LOG = Logger.getLogger(BuildHistoryManager.class.getName());

    private final List<Rule> rules;

    @DataBoundConstructor
    public BuildHistoryManager(List<Rule> rules) {
        this.rules = Util.fixNull(rules);
    }

    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Entry point for the discarding process. Iterates over the completed builds and rules.
     *
     * @see BuildDiscarder#perform(Job)
     * @see Job#logRotate()
     */
    @Override
    public synchronized void perform(Job<?, ?> job) throws IOException, InterruptedException {
        // reset counters of matched builds
        for (Rule rule : rules) {
            rule.initialize();
        }

        Run<?, ?> run = job.getLastCompletedBuild();
        // for each completed build...
        while (run != null) {
            LOG.info("Processing build #" + run.getNumber());
            if (!run.isKeepLog()) {
                for (int i = 0; i < rules.size(); i++) {
                    Rule rule = rules.get(i);
                    LOG.info("Processing rule no " + (i + 1));
                    if (rule.validateConditions(run)) {
                        LOG.info("Processing actions for rule no " + (i + 1));
                        rule.performActions(run);

                        // if other rules should not be proceed, shift to next build
                        if (!rule.getContinueAfterMatch()) {
                            break;
                        }
                    }
                }
            }

            // validateConditions rules for previous build - completed in case some previous are still building
            run = run.getPreviousCompletedBuild();
            // stop when the iteration reach the oldest build
        }
    }
}
