package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.Util;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.BuildDiscarder;
import org.kohsuke.stapler.DataBoundConstructor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Custom implementation of {@link BuildDiscarder}.
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
     * This method is invoked by {@link Job#logRotate()}, which is called for example by <br>
     * {@link jenkins.model.JobGlobalBuildDiscarderStrategy#apply(Job)}, <br>
     * <a href="https://github.com/jenkinsci/workflow-job-plugin/blob/master/src/main/java/org/jenkinsci/plugins/workflow/job/WorkflowRun.java#L658">org.jenkinsci.plugins.workflow.job.WorkflowRun#finish()</a> <br>
     * thus this method can be invoked several time after completed build.
     *
     * @see BuildDiscarder#perform(Job)
     * @see Job#logRotate()
     */
    @Override
    public synchronized void perform(Job<?, ?> job) throws IOException, InterruptedException {
        String uniquePerformName = job.getFullName();
        log(uniquePerformName, "Start evaluating build history for build " + job.getFullName());

        // reset counters of matched builds
        for (Rule rule : rules) {
            rule.initialize(uniquePerformName);
        }

        Run<?, ?> run = job.getLastCompletedBuild();
        // for each completed build...
        while (run != null) {
            log(uniquePerformName, "Processing build #" + run.getNumber());
            if (run.isKeepLog()) {
                log(uniquePerformName, "Build #" + run.getNumber() + " is marked as keep forever -> skip processing");
            } else {
                processRules(run, uniquePerformName);
            }

            // validateConditions rules for previous build - completed in case some previous are still building
            run = run.getPreviousCompletedBuild();
            // stop when the iteration reach the oldest build
        }
    }

    // just to reduce complexity
    private void processRules(Run<?, ?> run, String uniquePerformName) throws IOException, InterruptedException {
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            log(uniquePerformName, "Processing rule no " + (i + 1));
            if (rule.validateConditions(run)) {
                log(uniquePerformName, "Processing actions for rule no " + (i + 1));
                rule.performActions(run);

                // if other rules should not be proceed, shift to next build
                if (!rule.getContinueAfterMatch()) {
                    break;
                }
            }
        }
    }

    private static void log(String jobName, String message) {
        LOG.log(Level.FINE, () -> String.format("[%s] %s", jobName, message));
    }
}
