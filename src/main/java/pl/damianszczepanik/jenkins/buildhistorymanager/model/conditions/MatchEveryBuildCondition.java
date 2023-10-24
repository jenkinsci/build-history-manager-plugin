package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches every build.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchEveryBuildCondition extends Condition {

    @DataBoundConstructor
    public MatchEveryBuildCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration, int buildPosition) {
        return true;
    }

    // Add an overloaded version of the 'matches' method to set the default value for buildPosition
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        // Set a default value for buildPosition, e.g., -1
        int buildPosition = -1;
        return matches(run, configuration, buildPosition);
    }
}
