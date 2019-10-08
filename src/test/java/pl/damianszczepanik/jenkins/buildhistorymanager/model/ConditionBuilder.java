package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hudson.model.Descriptor;
import hudson.model.Run;
import javax.annotation.Nonnull;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConditionBuilder {

    public static final List<Condition> buildSampleConditions() {
        return Collections.unmodifiableList(Arrays.asList(new TestCondition(), new TestCondition()));
    }

    public static class TestCondition extends Condition {

        public int matchesTimes;

        @Override
        public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
            matchesTimes++;
            return true;
        }

        @Override
        public Descriptor getDescriptor() {
            return new Descriptor() {

                @Nonnull
                @Override
                public String getDisplayName() {
                    return this.getClass().getName();
                }
            };
        }
    }
}
