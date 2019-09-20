package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import hudson.model.AbstractDescribableImpl;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 *         (this class must be abstract, otherwise Jenkins reports warning
 *         with NullPointerException when the plusin is saved after update)
 */
public abstract class Rule extends AbstractDescribableImpl<Rule> {
}
