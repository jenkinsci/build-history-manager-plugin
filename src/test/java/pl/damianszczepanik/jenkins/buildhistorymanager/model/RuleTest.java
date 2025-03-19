package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder.buildSampleConditions;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.ActionBuilder.buildSampleActions;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import hudson.model.Run;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.ActionBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class RuleTest {

    @Test
    void getConditions_ReturnsConditions() {

        // given
        List<Condition> conditions = buildSampleConditions();
        Rule rule = new Rule(conditions, buildSampleActions());

        // when
        List<Condition> returnedConditions = rule.getConditions();

        // then
        assertThat(returnedConditions).containsAll(conditions);
    }

    @Test
    void getConditions_ReturnsActions() {

        // given
        List<Action> actions = buildSampleActions();
        Rule rule = new Rule(buildSampleConditions(), actions);

        // when
        List<Action> returnedActions = rule.getActions();

        // then
        assertThat(returnedActions).containsAll(actions);
    }

    @Test
    void getMatchAtMost_ReturnsMatchAtMost() {

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
    void newRule_SetsContinueAfterMatch() {

        // given
        Rule rule = new Rule(buildSampleConditions(), buildSampleActions());

        // when
        boolean returnedValue = rule.getContinueAfterMatch();

        // then
        assertThat(returnedValue).isTrue();
    }

    @Test
    void getContinueAfterMatch_ReturnsContinueAfterMatch() {

        // given
        Rule rule = new Rule(buildSampleConditions(), buildSampleActions());
        final boolean continueAfterMatch = false;
        rule.setContinueAfterMatch(continueAfterMatch);

        // when
        boolean returnedValue = rule.getContinueAfterMatch();

        // then
        assertThat(returnedValue).isEqualTo(continueAfterMatch);
    }

    @Test
    void validateConditions_MatchesAllConditions() {

        // given
        Rule rule = new Rule(buildSampleConditions(), Collections.emptyList());
        Run<?, ?> run = mock(Run.class);

        // when
        rule.validateConditions(run);

        // then
        for (Condition condition : rule.getConditions()) {
            assertThat(((ConditionBuilder.AbstractSampleCondition) condition).matchesTimes).isOne();
        }
    }

    @Test
    void validateConditions_OnNegativeCondition_DoesNotIncrementMatchedTimes() {

        // given
        Rule rule = new Rule(List.of(new ConditionBuilder.NegativeCondition()), Collections.emptyList());
        Run<?, ?> run = mock(Run.class);

        // when
        rule.validateConditions(run);

        // then
        int matchedTimes = Whitebox.getInternalState(rule, "matchedTimes");
        assertThat(matchedTimes).isZero();
    }

    @Test
    void performActions_PerformsAllActions() throws IOException, InterruptedException {

        // given
        Rule rule = new Rule(buildSampleConditions(), buildSampleActions());
        Run<?, ?> run = mock(Run.class);

        // when
        rule.performActions(run);

        // then
        for (Action action : rule.getActions()) {
            assertThat(((ActionBuilder.TestAction) action).performTimes).isOne();
        }
    }

    @Test
    void validateConditions_PerformsNTimes() {

        // given
        Rule rule = new Rule(buildSampleConditions(), Collections.emptyList());
        rule.setMatchAtMost(1);
        Run<?, ?> run = mock(Run.class);

        // when
        rule.validateConditions(run);
        rule.validateConditions(run);

        // then
        int matchedTimes = Whitebox.getInternalState(rule, "matchedTimes");
        assertThat(matchedTimes).isOne();

        for (Condition condition : rule.getConditions()) {
            assertThat(((ConditionBuilder.AbstractSampleCondition) condition).matchesTimes).isOne();
        }

    }
}
