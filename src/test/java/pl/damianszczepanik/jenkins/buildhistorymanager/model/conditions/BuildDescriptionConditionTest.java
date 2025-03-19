package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import hudson.model.Run;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildDescriptionCondition.MatchingMethodType;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class BuildDescriptionConditionTest {

    @Test
    void setPattern_SetsPattern() {

        // given
        BuildDescriptionCondition condition = new BuildDescriptionCondition();
        String pattern = "[release] 22.0r5";
        condition.setPattern(pattern);

        // when
        String returnedPattern = condition.getPattern();

        // then
        assertThat(returnedPattern).isEqualTo(pattern);
    }

    @Test
    void setMatchingMethod_SetsMatchingMethod() {

        // given
        BuildDescriptionCondition condition = new BuildDescriptionCondition();
        MatchingMethodType matchingMethodType = MatchingMethodType.CONTAINS;

        // make sure that default matching method is not same
        // as the one used by this test
        assertThat(condition.getMatchingMethod()).isNotEqualTo(matchingMethodType.name());
        condition.setMatchingMethod(matchingMethodType.name());

        // when
        String returnedMethodType = condition.getMatchingMethod();

        // then
        assertThat(returnedMethodType).isEqualTo(matchingMethodType.name());
    }

    @Test
    void matches_OnEqualDescription_ReturnsTrue() throws IOException {

        // given
        BuildDescriptionCondition condition = new BuildDescriptionCondition();
        condition.setMatchingMethod(MatchingMethodType.EQUALS.name());
        final String description = "[release] 11.3.4.1";
        condition.setPattern(description);

        Run<?, ?> run = new RunStub();
        run.setDescription(description);

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnLongerDescription_ReturnsTrue() throws IOException {

        // given
        BuildDescriptionCondition condition = new BuildDescriptionCondition();
        condition.setMatchingMethod(MatchingMethodType.CONTAINS.name());
        final String description = "[release] 1987.3.14";
        condition.setPattern(description);

        Run<?, ?> run = new RunStub();
        run.setDescription(description + "some extra description");

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnMatchedDescription_ReturnsTrue() throws IOException {

        // given
        BuildDescriptionCondition condition = new BuildDescriptionCondition();
        condition.setMatchingMethod(MatchingMethodType.MATCHES.name());
        final String description = "[release] 11.3.4";
        condition.setPattern(".*release.*");

        Run<?, ?> run = new RunStub();
        run.setDescription(description + "some extra description");

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnNullDescription_ReturnsFalse() throws IOException {

        // given
        BuildDescriptionCondition condition = new BuildDescriptionCondition();

        Run<?, ?> run = new RunStub();
        run.setDescription(null);

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

}
