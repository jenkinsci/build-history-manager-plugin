package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.ActionBuilder.sampleActions;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder.sampleConditions;

import java.util.List;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleTest {

    @Test
    public void getConditions_ReturnsConditions() {

        // given
        Rule rule = new Rule(sampleConditions, sampleActions);

        // when
        List<Condition> conditions = rule.getConditions();

        // then
        assertThat(conditions).containsAll(sampleConditions);
    }

    @Test
    public void getConditions_ReturnsActions() {

        // given
        Rule rule = new Rule(sampleConditions, sampleActions);

        // when
        List<Action> actions = rule.getActions();

        // then
        assertThat(actions).containsAll(sampleActions);
    }
}
