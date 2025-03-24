package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class MatchEveryBuildConditionTest {

    @Test
    void matches_ReturnsTrue() {

        // given
        MatchEveryBuildCondition condition = new MatchEveryBuildCondition();

        // when
        boolean matches = condition.matches(null, null);

        // then
        assertThat(matches).isTrue();
    }
}
