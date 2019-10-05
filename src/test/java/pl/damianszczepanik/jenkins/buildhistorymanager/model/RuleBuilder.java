package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.Arrays;
import java.util.List;

import hudson.model.Descriptor;
import hudson.model.Run;
import javax.annotation.Nonnull;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleBuilder {

    public static final List<TestRule> sampleRules = Arrays.asList(new TestRule(), new TestRule());

    public static class TestRule extends Rule {

        public int times;

        public TestRule() {
            super(null, null);
        }

        @Override
        public boolean validateConditions(Run<?, ?> run) {
            times++;
            return false;
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