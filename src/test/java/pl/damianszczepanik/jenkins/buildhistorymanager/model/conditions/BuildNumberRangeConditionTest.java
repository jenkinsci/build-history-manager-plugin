package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import hudson.model.Run;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildNumberRangeConditionTest {

    private final int minBuildNumberRange = 10;
    private final int maxBuildNumberRange = 20;

    @Test
    public void setMinBuildNumber_SetsMinBuildNumber() {

        // given
        BuildNumberRangeCondition condition = new BuildNumberRangeCondition();
        final int minBuildNumber = 123654;
        condition.setMinBuildNumber(minBuildNumber);

        // when
        int returnedMinBuildNumber = condition.getMinBuildNumber();

        // then
        assertThat(returnedMinBuildNumber).isEqualTo(minBuildNumber);
    }

    @Test
    public void setMaxBuildNumber_SetsMinBuildNumber() {

        // given
        BuildNumberRangeCondition condition = new BuildNumberRangeCondition();
        final int maxBuildNumber = 654123;
        condition.setMaxBuildNumber(maxBuildNumber);

        // when
        int returnedMaxBuildNumber = condition.getMaxBuildNumber();

        // then
        assertThat(returnedMaxBuildNumber).isEqualTo(maxBuildNumber);
    }

    @Test
    public void matches_ForSmallerBuildNumber_ReturnsFalse() {

        // given
        BuildNumberRangeCondition condition = new BuildNumberRangeCondition();
        condition.setMinBuildNumber(minBuildNumberRange);
        condition.setMaxBuildNumber(maxBuildNumberRange);

        // when
        boolean matches = condition.matches(mockRun(minBuildNumberRange - 1), null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    public void matches_ForGreaterBuildNumber_ReturnsFalse() {

        // given
        BuildNumberRangeCondition condition = new BuildNumberRangeCondition();
        condition.setMinBuildNumber(minBuildNumberRange);
        condition.setMaxBuildNumber(maxBuildNumberRange);

        // when
        boolean matches = condition.matches(mockRun(maxBuildNumberRange + 1), null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    public void matches_ForValidBuildNumber_ReturnsTrue() {

        // given
        BuildNumberRangeCondition condition = new BuildNumberRangeCondition();
        condition.setMinBuildNumber(minBuildNumberRange);
        condition.setMaxBuildNumber(maxBuildNumberRange);

        // when
        boolean matches = condition.matches(mockRun((minBuildNumberRange + maxBuildNumberRange) / 2), null);

        // then
        assertThat(matches).isTrue();
    }

    private static Run mockRun(int buildNumber) {
        Run run = mock(Run.class);
        when(run.getNumber()).thenReturn(buildNumber);
        return run;
    }
}
