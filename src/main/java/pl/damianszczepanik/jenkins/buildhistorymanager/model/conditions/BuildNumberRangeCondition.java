package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches builds with build number between given number range.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildNumberRangeCondition extends Condition {

    private int minBuildNumber;
    private int maxBuildNumber;

    @DataBoundConstructor
    public BuildNumberRangeCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public int getMinBuildNumber() {
        return minBuildNumber;
    }

    @DataBoundSetter
    public void setMinBuildNumber(int minBuildNumber) {
        this.minBuildNumber = minBuildNumber;
    }

    public int getMaxBuildNumber() {
        return maxBuildNumber;
    }

    @DataBoundSetter
    public void setMaxBuildNumber(int maxBuildNumber) {
        this.maxBuildNumber = maxBuildNumber;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        return run.getNumber() >= minBuildNumber && run.getNumber() <= maxBuildNumber;
    }
}
