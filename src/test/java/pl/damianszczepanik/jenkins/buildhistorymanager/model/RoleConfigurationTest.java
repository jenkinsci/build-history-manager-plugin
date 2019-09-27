package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RoleConfigurationTest {

    @Test
    public void RoleConfiguration_SetsDefaultValues() {

        // given & when
        RuleConfiguration configuration = new RuleConfiguration();

        // then
        assertThat(configuration.getMatchAtMost()).isEqualTo(RuleConfiguration.MATCH_UNLIMITED);
        assertThat(configuration.isContinueAfterMatch()).isTrue();
    }

    @Test
    public void setContinueAfterMatch_SetsContinueAfterMatch() {

        // given
        RuleConfiguration configuration = new RuleConfiguration();
        boolean continueAfterMatch = false;

        // when
        configuration.setContinueAfterMatch(continueAfterMatch);

        // then
        assertThat(configuration.isContinueAfterMatch()).isFalse();
    }

    @Test
    public void setMatchAtMost_SetsMatchAtMost() {

        // given
        RuleConfiguration configuration = new RuleConfiguration();
        int matchAtMost = 34;

        // when
        configuration.setMatchAtMost(matchAtMost);

        // then
        assertThat(configuration.getMatchAtMost()).isEqualTo(matchAtMost);
    }
}
