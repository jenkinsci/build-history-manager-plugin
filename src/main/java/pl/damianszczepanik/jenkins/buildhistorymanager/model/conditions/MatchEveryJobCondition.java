package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Matches every job without any condition.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchEveryJobCondition extends Condition {

    @DataBoundConstructor
    public MatchEveryJobCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public boolean matches(Run<?, ?> run) {
        return true;
    }
}
