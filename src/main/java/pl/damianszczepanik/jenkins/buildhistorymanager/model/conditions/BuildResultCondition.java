package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Result;
import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches builds that have expected build results.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildResultCondition extends Condition {

    private boolean matchSuccess;
    private boolean matchUnstable;
    private boolean matchFailure;
    private boolean matchAborted;

    @DataBoundConstructor
    public BuildResultCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public boolean getMatchSuccess() {
        return matchSuccess;
    }

    @DataBoundSetter
    public void setMatchSuccess(boolean matchSuccess) {
        this.matchSuccess = matchSuccess;
    }

    public boolean getMatchUnstable() {
        return matchUnstable;
    }

    @DataBoundSetter
    public void setMatchUnstable(boolean matchUnstable) {
        this.matchUnstable = matchUnstable;
    }

    public boolean getMatchFailure() {
        return matchFailure;
    }

    @DataBoundSetter
    public void setMatchFailure(boolean matchFailure) {
        this.matchFailure = matchFailure;
    }

    public boolean getMatchAborted() {
        return matchAborted;
    }

    @DataBoundSetter
    public void setMatchAborted(boolean matchAborted) {
        this.matchAborted = matchAborted;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        Result result = run.getResult();
        if (matchSuccess && result == Result.SUCCESS) {
            return true;
        }
        if (matchUnstable && result == Result.UNSTABLE) {
            return true;
        }
        if (matchFailure && result == Result.FAILURE) {
            return true;
        }
        if (matchAborted && result == Result.ABORTED) {
            return true;
        }
        return false;
    }
}
