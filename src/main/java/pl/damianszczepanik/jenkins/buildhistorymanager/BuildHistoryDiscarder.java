package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;
import java.util.List;

import hudson.Util;
import hudson.model.Job;
import jenkins.model.BuildDiscarder;
import org.kohsuke.stapler.DataBoundConstructor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildHistoryDiscarder extends BuildDiscarder {

    private final List<Rule> rules;

    @DataBoundConstructor
    public BuildHistoryDiscarder(List<Rule> rules) {
        this.rules = Util.fixNull(rules);
    }

    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Entry point for the discarding process.
     *
     * @see BuildDiscarder#perform(Job)
     * @see Job#logRotate()
     */
    @Override
    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
        for (Rule rule : rules) {
            rule.perform(job);
        }
    }
}
