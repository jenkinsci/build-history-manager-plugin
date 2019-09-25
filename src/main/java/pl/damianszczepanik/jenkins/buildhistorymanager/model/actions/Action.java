package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 *         (this class must be abstract, otherwise Jenkins reports warning
 *         with NullPointerException when the plusin is saved after update)
 */
public abstract class Action extends AbstractDescribableImpl<Action> {

    /**
     * Performs operation defined by given action.
     *
     * @param run job which should be updated
     */
    public abstract void perform(Run<?, ?> run) throws IOException, InterruptedException;
}
