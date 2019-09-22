package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hudson.model.Job;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ActionBuilder {

    public static final List<Action> sampleActions = Collections.unmodifiableList(Arrays.asList(
            new SampleAction(), new SampleAction()));

    private static class SampleAction extends Action {

        @Override
        public void perform(Job<?, ?> job) {
            throw new NotImplementedException();
        }
    }
}
