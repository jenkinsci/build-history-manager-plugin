package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.model.Descriptor;
import hudson.model.Run;
import javax.annotation.Nonnull;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleBuilder {

    public static class TestRule extends Rule {

        public int validateTimes;
        public int initializeTimes;

        public TestRule() {
            super(null, null);
        }

        @Override
        public boolean validateConditions(Run<?, ?> run) {
            validateTimes++;
            return false;
        }

        @Override
        public void initialize() {
            initializeTimes++;
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