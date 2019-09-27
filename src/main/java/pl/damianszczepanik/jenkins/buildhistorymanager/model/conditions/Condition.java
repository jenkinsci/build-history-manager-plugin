package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Run;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Decides if the build matches given criteria to be updated or not.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 *         (this class must be abstract, otherwise Jenkins reports warning
 *         with NullPointerException when the plusin is saved after update)
 */
public abstract class Condition extends AbstractDescribableImpl<Condition> {

    /**
     * Decides if the build matches given criteria to be updated or not.
     *
     * @param run           job which should be evaluated
     * @param configuration configuration from the role
     * @return <code>true</code> if the job matches given criteria, otherwise <code>false</code>
     */
    public abstract boolean matches(Run<?, ?> run, RuleConfiguration configuration);
}
