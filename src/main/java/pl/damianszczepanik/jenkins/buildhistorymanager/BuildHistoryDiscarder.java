package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.Job;
import jenkins.model.BuildDiscarder;
import jenkins.model.BuildDiscarderDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class BuildHistoryDiscarder extends BuildDiscarder {

    private static final Logger LOG = Logger.getLogger(BuildHistoryDiscarder.class.getName());

    private String limit;

    @DataBoundConstructor
    public BuildHistoryDiscarder(String limit) {
        this.limit = limit;
    }

    @Override
    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
        LOG.info("Running perform() method");
    }

    public String getLimit() {
        return limit;
    }

    @Extension
    public static final class BuildHistoryDiscarderDescriptor extends BuildDiscarderDescriptor {
        public String getDisplayName() {
            return "Build History Manager";
        }
    }
}
