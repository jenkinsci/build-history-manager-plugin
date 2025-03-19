package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import hudson.model.Run;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class CauseConditionTest {

    @Test
    void setCause_Sets_Cause() {

        // given
        CauseCondition condition = new CauseCondition();
        String cause = "myTrigger";

        // when
        condition.setCauseClass(cause);

        // then
        assertThat(condition.getCauseClass()).isEqualTo(cause);
    }

    @Test
    void match_OnSameCause_ReturnsTrue() throws IOException {

        // given
        final String causeClass = "onUniverseDiscovery";
        CauseCondition condition = new CauseCondition();
        condition.setCauseClass(causeClass);
        Run<?, ?> run = new RunStub(causeClass);

        // when
        boolean matched = condition.matches(run, null);

        // that
        assertThat(matched).isTrue();
    }

    @Test
    void match_OnSubStringCause_ReturnsTrue() throws IOException {

        // given
        final String causeClass = "onUniverseDiscovery";
        CauseCondition condition = new CauseCondition();
        condition.setCauseClass(causeClass);
        Run<?, ?> run = new RunStub(causeClass + "Ever");

        // when
        boolean matched = condition.matches(run, null);

        // that
        assertThat(matched).isTrue();
    }

    @Test
    void match_OnEmptyCause_ReturnsFalse() throws IOException {

        // given
        final String causeClass = "onUniverseDiscovery";
        CauseCondition condition = new CauseCondition();
        condition.setCauseClass(causeClass);
        Run<?, ?> run = new RunStub("");

        // when
        boolean matched = condition.matches(run, null);

        // that
        assertThat(matched).isFalse();
    }

    @Test
    void match_OnMismatchedCause_ReturnsFalse() throws IOException {

        // given
        final String causeClass = "onUniverseDiscovery";
        CauseCondition condition = new CauseCondition();
        condition.setCauseClass(causeClass);
        Run<?, ?> run = new RunStub("onUniverseDisaster");

        // when
        boolean matched = condition.matches(run, null);

        // that
        assertThat(matched).isFalse();
    }

    @Test
    void match_OnMultiplyCause_ReturnsTrue() throws IOException {

        // given
        final String[] causeClasses = {"one", "two"};
        CauseCondition condition = new CauseCondition();
        condition.setCauseClass(causeClasses[1]);
        Run<?, ?> run = new RunStub(causeClasses);

        // when
        boolean matched = condition.matches(run, null);

        // that
        assertThat(matched).isTrue();
    }
}
