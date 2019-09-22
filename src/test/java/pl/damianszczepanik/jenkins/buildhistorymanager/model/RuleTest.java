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

    @Test
    public void getMatchAtMost_ReturnsMatchAtMost() {

        // given
        Rule rule = new Rule(sampleConditions, sampleActions);
        int matchAtMost = 123;
        rule.setMatchAtMost(matchAtMost);

        // when
        int returnedValue = rule.getMatchAtMost();

        // then
        assertThat(returnedValue).isEqualTo(matchAtMost);
    }

    @Test
    public void newRule_SetsContinueAfterMatch() {

        // given
        Rule rule = new Rule(sampleConditions, sampleActions);

        // when
        boolean returnedValue = rule.getContinueAfterMatch();

        // then
        assertThat(returnedValue).isTrue();
    }

    @Test
    public void getContinueAfterMatch_ReturnsContinueAfterMatch() {

        // given
        Rule rule = new Rule(sampleConditions, sampleActions);
        boolean continueAfterMatch = false;
        rule.setContinueAfterMatch(continueAfterMatch);

        // when
        boolean returnedValue = rule.getContinueAfterMatch();

        // then
        assertThat(returnedValue).isEqualTo(continueAfterMatch);
    }
}
