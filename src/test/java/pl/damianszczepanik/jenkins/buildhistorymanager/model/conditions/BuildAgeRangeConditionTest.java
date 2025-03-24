package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import hudson.model.Run;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class BuildAgeRangeConditionTest {

    @Test
    void setMinAgeNumber_SetsMinBuildNumber() {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        final int minBuildAge = 123654;
        condition.setMinDaysAge(minBuildAge);

        // when
        int returnedMinBuildAge = condition.getMinDaysAge();

        // then
        assertThat(returnedMinBuildAge).isEqualTo(minBuildAge);
    }

    @Test
    void setMaxAgeNumber_SetsMinBuildNumber() {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        final int maxBuildAge = 123654;
        condition.setMaxDaysAge(maxBuildAge);

        // when
        int returnedMaxBuildAge = condition.getMaxDaysAge();

        // then
        assertThat(returnedMaxBuildAge).isEqualTo(maxBuildAge);
    }

    @Test
    void matches_OnTodayBuild_ReturnsTrue() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(0);
        condition.setMinDaysAge(0);

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnBuildTimeBelowMax_ReturnsFalse() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(10);
        condition.setMinDaysAge(5);


        long buildTimeMinus3Days = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 15;
        Run<?, ?> run = new RunStub(1, buildTimeMinus3Days);

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnBuildTimeAboveMin_ReturnsFalse() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(10);
        condition.setMinDaysAge(5);


        long buildTimeMinus3Days = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3;
        Run<?, ?> run = new RunStub(1, buildTimeMinus3Days);

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnBuildTimeInRange_ReturnsTrue() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(10);
        condition.setMinDaysAge(5);

        long buildTimeMinus3Days = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 6;
        Run<?, ?> run = new RunStub(1, buildTimeMinus3Days);

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnInvalidRange_ReturnsFalse() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(5);
        condition.setMinDaysAge(10);

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }
}
