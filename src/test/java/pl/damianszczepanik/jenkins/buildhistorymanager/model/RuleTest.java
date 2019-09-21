package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleTest {

    private List<Condition> sampleConditions = Arrays.asList(new Condition() {
    });
    private List<Action> sampleActions = Arrays.asList(new Action() {
    });

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
