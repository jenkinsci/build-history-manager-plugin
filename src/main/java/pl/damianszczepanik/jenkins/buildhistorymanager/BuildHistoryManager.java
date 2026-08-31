package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.Util;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.BuildDiscarder;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundConstructor;
import pl.damianszczepanik.jenkins.buildhistorymanager.control.RuleValidator;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Custom implementation of {@link BuildDiscarder}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see hudson.tasks.LogRotator
 */
public class BuildHistoryManager extends BuildDiscarder {

    /**
     * Logger used by all plugin classes.
     */
    public static final Logger LOG = Logger.getLogger(BuildHistoryManager.class.getName());

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
        GlobalLevelConfiguration globalConfiguration = GlobalConfiguration.all().get(GlobalLevelConfiguration.class);

        String jobName = job.getFullName();
        logMessage(jobName, "Start evaluating build history for build " + job.getFullName());
        if (globalConfiguration == null) {
            logMessage(jobName, String.format("Found none global rule and %d job rules", rules.size()));
        } else {
            logMessage(jobName, String.format("Found %d global rules and %d job rules", globalConfiguration.getRules().size(), rules.size()));
        }
        List<RuleValidator> ruleValidators = new ArrayList<>();
        // reset counters of matched builds
        for (Rule rule : rules) {
            ruleValidators.add(new RuleValidator(rule, jobName));
        }

        Run<?, ?> run = job.getLastCompletedBuild();
        // for each completed build...
        while (run != null) {
            logMessage(jobName, "Processing build #" + run.getNumber());
            if (run.isKeepLog()) {
                logMessage(jobName, "Build #" + run.getNumber() + " is marked as keep forever -> skip processing");
            } else {
                processRules(ruleValidators, run, jobName);
            }

            // validateConditions rules for previous build - completed in case some previous are still building
            run = run.getPreviousCompletedBuild();
            // stop when the iteration reach the oldest build
        }
    }

    // just to reduce complexity
    private void processRules(List<RuleValidator> ruleValidators, Run<?, ?> run, String jobName) throws IOException, InterruptedException {
        for (int i = 0; i < ruleValidators.size(); i++) {
            RuleValidator ruleValidator = ruleValidators.get(i);
            logMessage(jobName, "Processing rule no " + (i + 1));
            if (ruleValidator.validateConditions(run)) {
                logMessage(jobName, "Processing actions for rule no " + (i + 1));
                ruleValidator.getRule().performActions(run);

                // if other rules should not be proceed, shift to next build
                if (!ruleValidator.getRule().getContinueAfterMatch()) {
                    break;
                }
            }
        }
    }

    public static void logMessage(String jobName, String message) {
        LOG.log(Level.FINE, () -> String.format("[%s] %s", jobName, message));
    }
}
