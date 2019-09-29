package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import hudson.model.Job;
import org.assertj.core.api.Assertions;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ActionAssertion {

    private ActionBuilder.TestAction action;

    private ActionAssertion(Action action) {
        this.action = (ActionBuilder.TestAction) action;
    }

    public static ActionAssertion assertThat(Action action) {
        return new ActionAssertion(action);
    }

    public ActionAssertion performsOnce() {
        Assertions.assertThat(action.times).isOne();
        return this;
    }

    public ActionAssertion skipsAction() {
        Assertions.assertThat(action.times).isZero();
        return this;
    }

    public ActionAssertion withRun(Job<?, ?> job) {
        Assertions.assertThat(action.run).isSameAs(job.getLastBuild());
        return this;
    }
}
