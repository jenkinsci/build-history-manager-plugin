package pl.damianszczepanik.jenkins.buildhistorymanager.control;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder.buildSampleConditions;

import java.util.Collections;
import java.util.List;

import hudson.model.Run;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManagerTest;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class RuleValidatorTest {

    @BeforeEach
    void setUp() {
        BuildHistoryManagerTest.setUpLogger();
    }

    @Test
    void validateConditions_OnNegativeCondition_DoesNotIncrementMatchedTimes() {

        // given
        Rule rule = new Rule(List.of(new ConditionBuilder.NegativeCondition()), Collections.emptyList());
        Run<?, ?> run = mock(Run.class);
        RuleValidator ruleValidator = new RuleValidator(rule, "jobName");

        // when
        ruleValidator.validateConditions(run);

        // then
        assertThat(ruleValidator.getMatchedTimes()).isZero();
    }

    @Test
    void validateConditions_PerformsNTimes() {

        // given
        Rule rule = new Rule(buildSampleConditions(), Collections.emptyList());
        rule.setMatchAtMost(1);
        Run<?, ?> run = mock(Run.class);
        RuleValidator ruleValidator = new RuleValidator(rule, "jobName");

        // when
        ruleValidator.validateConditions(run);
        ruleValidator.validateConditions(run);

        // then
        assertThat(ruleValidator.getMatchedTimes()).isOne();

        for (Condition condition : rule.getConditions()) {
            assertThat(((ConditionBuilder.AbstractSampleCondition) condition).matchesTimes).isOne();
        }

    }
}
