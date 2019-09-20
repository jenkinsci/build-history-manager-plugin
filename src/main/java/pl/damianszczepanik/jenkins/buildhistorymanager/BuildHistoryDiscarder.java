package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;
import java.util.List;

import hudson.Extension;
import hudson.Util;
import hudson.model.Job;
import jenkins.model.BuildDiscarder;
import jenkins.model.BuildDiscarderDescriptor;
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

    @Override
    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
    }

    @Extension
    public static final class BuildHistoryDiscarderDescriptor extends BuildDiscarderDescriptor {

        public String getDisplayName() {
            return "Build History Manager";
        }
    }
}
