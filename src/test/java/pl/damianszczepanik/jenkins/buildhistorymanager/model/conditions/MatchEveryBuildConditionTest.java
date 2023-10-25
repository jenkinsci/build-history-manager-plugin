package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchEveryBuildConditionTest {

    @Test
    public void matches_ReturnsTrue() throws IOException, InterruptedException {

        // given
        MatchEveryBuildCondition condition = new MatchEveryBuildCondition();

        // when
        boolean matches =condition.matches(null, null, 0);

        // then
        assertThat(matches).isTrue();
    }
}
