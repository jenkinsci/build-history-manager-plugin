package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder.buildSampleConditions;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.ActionBuilder.buildSampleActions;

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
        List<Condition> conditions = buildSampleConditions();
        Rule rule = new Rule(conditions, buildSampleActions());

        // when
        List<Condition> returnedConditions = rule.getConditions();

        // then
        assertThat(returnedConditions).containsAll(conditions);
    }

    @Test
    public void getConditions_ReturnsActions() {

        // given
        List<Action> actions = buildSampleActions();
        Rule rule = new Rule(buildSampleConditions(), actions);

        // when
        List<Action> returnedActions = rule.getActions();

        // then
        assertThat(returnedActions).containsAll(actions);
    }

    @Test
    public void getMatchAtMost_ReturnsMatchAtMost() {

        // given
        Rule rule = new Rule(buildSampleConditions(), buildSampleActions());
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
        Rule rule = new Rule(buildSampleConditions(), buildSampleActions());

        // when
        boolean returnedValue = rule.getContinueAfterMatch();

        // then
        assertThat(returnedValue).isTrue();
    }

    @Test
    public void getContinueAfterMatch_ReturnsContinueAfterMatch() {

        // given
        Rule rule = new Rule(buildSampleConditions(), buildSampleActions());
        final boolean continueAfterMatch = false;
        rule.setContinueAfterMatch(continueAfterMatch);

        // when
        boolean returnedValue = rule.getContinueAfterMatch();

        // then
        assertThat(returnedValue).isEqualTo(continueAfterMatch);
    }

}
