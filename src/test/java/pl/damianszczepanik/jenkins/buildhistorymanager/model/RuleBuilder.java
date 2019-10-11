package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.model.Descriptor;
import hudson.model.Run;
import javax.annotation.Nonnull;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleBuilder {

    public static class TestRule extends Rule {

        private boolean validateResult;

        public int validateConditionsTimes;
        public int performActionsTimes;

        public TestRule(boolean validateResult) {
            super(null, null);
            this.validateResult = validateResult;
        }

        @Override
        public boolean validateConditions(Run<?, ?> run) {
            validateConditionsTimes++;
            return validateResult;
        }

        @Override
        public void performActions(Run<?, ?> run) {
            performActionsTimes++;
        }

        @Override
        public Descriptor getDescriptor() {
            return new Descriptor() {

                @Nonnull
                @Override
                public String getDisplayName() {
                    return "TestRule";
                }
            };
        }
    }
}