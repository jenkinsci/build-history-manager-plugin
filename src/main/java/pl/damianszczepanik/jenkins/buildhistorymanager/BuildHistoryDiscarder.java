package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.io.IOException;

import hudson.Extension;
import hudson.model.Job;
import jenkins.model.BuildDiscarder;
import jenkins.model.BuildDiscarderDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class BuildHistoryDiscarder extends BuildDiscarder {

    public String limit;

    @DataBoundConstructor
    public BuildHistoryDiscarder(String limit) {
        this.limit = limit;
    }

    @Override
    public void perform(Job<?, ?> job) throws IOException, InterruptedException {
    }

    public String getLimit() {
        return limit;
    }

    @Extension
    public static final class EnhancedOldBuildDiscarderDescriptor extends BuildDiscarderDescriptor {
        public String getDisplayName() {
            return "Build History Manager";
        }
    }
}
