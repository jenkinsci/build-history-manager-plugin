package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

public class BuildPositionCondition extends Condition {

    private int buildPosition;

    @DataBoundConstructor
    public BuildPositionCondition() {
      // Jenkins stapler requires to have public constructor with @DataBoundConstructor  
    }

    public int getBuildPosition() {
        return buildPosition;
    }

    @DataBoundSetter
    public void setBuildPosition(int buildPosition) {
        this.buildPosition = buildPosition;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        return false;
        // return run.getNumber() >= minBuildNumber && run.getNumber() <= maxBuildNumber;
    } 
}
