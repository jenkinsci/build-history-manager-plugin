package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.util.Arrays;
import java.util.List;

import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ActionBuilder {

    public static final List<Action> buildSampleActions() {
        return Arrays.asList(new TestAction(), new TestAction());
    }

    public static class TestAction extends Action {

        public int times;
        public Run<?, ?> run;

        @Override
        public void perform(Run<?, ?> run) {
            times++;
            this.run = run;
        }
    }
}
