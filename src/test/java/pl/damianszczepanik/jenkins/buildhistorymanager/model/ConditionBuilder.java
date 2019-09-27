package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConditionBuilder {

    public static final List<Condition> sampleConditions = Collections.unmodifiableList(Arrays.asList(
            new SampleCondition(), new SampleCondition()));

    private static class SampleCondition extends Condition {

        @Override
        public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
            throw new NotImplementedException();
        }
    }
}
