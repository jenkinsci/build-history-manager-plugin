package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import hudson.model.Run;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class BuildParameterConditionTest {

    @Test
    void setParameterName_SetsName() {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String name = "branch";
        condition.setParameterName(name);

        // when
        String returnedValue = condition.getParameterName();

        // then
        assertThat(returnedValue).isEqualTo(name);
    }

    @Test
    void setParameterValue_SetsValue() {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String value = "develop";
        condition.setParameterValue(value);

        // when
        String returnedValue = condition.getParameterValue();

        // then
        assertThat(returnedValue).isEqualTo(value);
    }

    @Test
    void matches_OnEmptyParameterName_ReturnsFalse() throws IOException {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String parameterName = null;
        String parameterValue = "release";
        condition.setParameterName(parameterName);
        condition.setParameterValue(parameterValue);

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnEmptyParameterValue_ReturnsFalse() throws IOException {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String parameterName = "language";
        String parameterValue = null;
        condition.setParameterName(parameterName);
        condition.setParameterValue(parameterValue);
        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnNullParameters_ReturnsFalse() throws IOException {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String parameterName = "os";
        String parameterValue = "unix.*";
        condition.setParameterName(parameterName);
        condition.setParameterValue(parameterValue);

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnNullValueParameter_ReturnsFalse() throws IOException {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String parameterName = "branch";
        String parameterValue = "^(dev.*|master)$";
        condition.setParameterName(parameterName);
        condition.setParameterValue(parameterValue);

        RunStub run = new RunStub();
        run.setParameterValues(parameterName, null);

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnMatchedParameterValue_ReturnsTrue() throws IOException {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String parameterName = "branch";
        String parameterValue = "^(dev.*|master)$";
        condition.setParameterName(parameterName);
        condition.setParameterValue(parameterValue);

        RunStub run = new RunStub();
        run.setParameterValues(parameterName, "develop");

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnMismatchedParameterValue_ReturnsTrue() throws IOException {

        // given
        BuildParameterCondition condition = new BuildParameterCondition();
        String parameterName = "branch";
        String parameterValue = "develop";
        condition.setParameterName(parameterName);
        condition.setParameterValue(parameterValue);

        RunStub run = new RunStub();
        run.setParameterValues(parameterName, "master");

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

}
