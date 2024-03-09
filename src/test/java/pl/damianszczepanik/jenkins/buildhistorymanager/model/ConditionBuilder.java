package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.Arrays;
import java.util.List;

import hudson.model.Descriptor;
import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConditionBuilder {

    public static final List<Condition> buildSampleConditions() {
        return Arrays.asList(new PositiveCondition(), new PositiveCondition());
    }

    public static abstract class AbstractSampleCondition extends Condition {

        public int matchesTimes;

        @Override
        public Descriptor getDescriptor() {
            return new Descriptor() {

                @Override
                public String getDisplayName() {
                    return this.getClass().getName();
                }
            };
        }
    }

    public static class PositiveCondition extends AbstractSampleCondition {

        @Override
        public boolean matches(Run<?, ?> run, RuleConfiguration configuration, int buildPosition) {
            matchesTimes++;
            return true;
        }
    }

    public static class NegativeCondition extends AbstractSampleCondition {

        @Override
        public boolean matches(Run<?, ?> run, RuleConfiguration configuration, int buildPosition) {
            return false;
        }
    }
}
