package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import hudson.model.Run;
import mockit.Deencapsulation;
import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildAgeRangeConditionTest {

    @Test
    public void setMinAgeNumber_SetsMinBuildNumber() {

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
    public void setMaxAgeNumber_SetsMinBuildNumber() {

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
    public void matches_OnTodayBuild_ReturnsTrue() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(0);
        condition.setMinDaysAge(0);

        Run<?, ?> run = new RunBuildTime();

        // when
        boolean matches = condition.matches(run, null);

        assertThat(matches).isTrue();
    }

    @Test
    public void matches_OnBuildTimeBelowMax_ReturnsFalse() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(10);
        condition.setMinDaysAge(5);


        long buildTimeMinus3Days = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 15;
        Run<?, ?> run = new RunBuildTime(buildTimeMinus3Days);

        // when
        boolean matches = condition.matches(run, null);

        assertThat(matches).isFalse();
    }

    @Test
    public void matches_OnBuildTimeAboveMin_ReturnsFalse() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(10);
        condition.setMinDaysAge(5);


        long buildTimeMinus3Days = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3;
        Run<?, ?> run = new RunBuildTime(buildTimeMinus3Days);

        // when
        boolean matches = condition.matches(run, null);

        assertThat(matches).isFalse();
    }

    @Test
    public void matches_OnBuildTimeInRange_ReturnsTrue() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(10);
        condition.setMinDaysAge(5);

        long buildTimeMinus3Days = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 6;
        Run<?, ?> run = new RunBuildTime(buildTimeMinus3Days);

        // when
        boolean matches = condition.matches(run, null);

        assertThat(matches).isTrue();
    }

    @Test
    public void matches_OnInvalidRange_ReturnsFalse() throws IOException {

        // given
        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMaxDaysAge(5);
        condition.setMinDaysAge(10);

        Run<?, ?> run = new RunBuildTime();

        // when
        boolean matches = condition.matches(run, null);

        assertThat(matches).isFalse();
    }
}

class RunBuildTime extends RunStub {

    public RunBuildTime() throws IOException {
        this(System.currentTimeMillis());
    }

    public RunBuildTime(long startTime) throws IOException {
        Deencapsulation.setField(this, "startTime", startTime);
    }

    @Override
    public long getDuration() {
        return 0;
    }
}