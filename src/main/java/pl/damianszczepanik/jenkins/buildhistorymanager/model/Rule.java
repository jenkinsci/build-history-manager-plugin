package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.util.List;

import hudson.model.AbstractDescribableImpl;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Rule extends AbstractDescribableImpl<Rule> {

    private final List<Condition> conditions;

    private final List<Action> actions;

    @DataBoundConstructor
    public Rule(List<Condition> conditions, List<Action> actions) {
        this.conditions = conditions;
        this.actions = actions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Action> getActions() {
        return actions;
    }
}
