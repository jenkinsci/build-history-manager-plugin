package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConditionBuilder {

    public static final List<Condition> buildSampleConditions() {
        return Collections.unmodifiableList(Arrays.asList(new SampleCondition(), new SampleCondition()));
    }

    public static class SampleCondition extends Condition {

        @Override
        public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
            return true;
        }
    }

    public static class NegativeCondition extends Condition {

        @Override
        public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
            return false;
        }
    }
}
