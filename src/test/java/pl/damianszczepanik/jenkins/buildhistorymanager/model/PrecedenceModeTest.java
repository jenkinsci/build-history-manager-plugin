package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class PrecedenceModeTest {

    // JOB_ONLY should be always present for backwards compatibility
    // and should be always located on the first position
    private final static int JOB_ONLY_ORDINAL = 0;

    @Test
    void JOB_ONLY_has_lowest_ordinal() {

        // given
        PrecedenceMode precedenceMode = PrecedenceMode.JOB_ONLY;

        // when
        int ordinal = precedenceMode.ordinal();

        // then
        assertThat(ordinal).isEqualTo(JOB_ONLY_ORDINAL);
    }
}
