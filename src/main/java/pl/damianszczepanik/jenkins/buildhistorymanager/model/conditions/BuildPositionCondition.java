package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

public class BuildPositionCondition extends Condition {

    private int maxBuildPosition;

    @DataBoundConstructor
    public BuildPositionCondition() {
      // Jenkins stapler requires to have public constructor with @DataBoundConstructor  
    }

    public int getMaxBuildPosition() {
        return maxBuildPosition;
    }

    @DataBoundSetter
    public void setMaxBuildPosition(int maxBuildPosition) {
        this.maxBuildPosition = maxBuildPosition;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        // return false;
        // return run.getNumber() >= minBuildNumber && run.getNumber() <= maxBuildNumber;
    } 
}
