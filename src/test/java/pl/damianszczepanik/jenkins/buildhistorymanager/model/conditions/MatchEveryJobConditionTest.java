package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchEveryJobConditionTest {

    @Test
    public void matches_ReturnTrue() throws IOException, InterruptedException {

        // given
        MatchEveryJobCondition condition = new MatchEveryJobCondition();

        // when
        boolean matches = condition.matches(null, null);

        // then
        assertThat(matches).isTrue();
    }
}
