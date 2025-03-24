package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.util.Arrays;
import java.util.List;

import hudson.model.Descriptor;
import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ActionBuilder {

    public static List<Action> buildSampleActions() {
        return Arrays.asList(new TestAction(), new TestAction());
    }

    public static class TestAction extends Action {

        public int performTimes;
        public Run<?, ?> run;

        @Override
        public void perform(Run<?, ?> run) {
            performTimes++;
            this.run = run;
        }

        @Override
        public Descriptor getDescriptor() {
            return new Descriptor() {

                @Override
                public String getDisplayName() {
                    return "TestAction";
                }
            };
        }
    }
}
