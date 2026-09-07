package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Run;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class FileScannerConditionTest {

    @BeforeAll
    static void createFileForPattern() throws IOException {
        File.createTempFile(
                FileScannerCondition.class.getSimpleName() + "_",
                Long.toString(System.currentTimeMillis()),
                new File("target"));
    }

    @Test
    void setIncludePattern_SetsIncludePattern() {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        String pattern = "include*Pattern";

        // when
        condition.setIncludePattern(pattern);

        // then
        assertThat(condition.getIncludePattern()).isEqualTo(pattern);
    }

    @Test
    void setExcludePattern_SetsExcludePattern() {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        String pattern = "exclude?Pattern";

        // when
        condition.setExcludePattern(pattern);

        // then
        assertThat(condition.getExcludePattern()).isEqualTo(pattern);
    }


    @Test
    void setIncludePattern_IgnoresNullValue() {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        String pattern = "includeNotNullPattern";
        condition.setIncludePattern(pattern);

        // when
        condition.setIncludePattern(null);

        // then
        assertThat(condition.getIncludePattern()).isEqualTo(pattern);
    }

    @Test
    void setExcludePattern_IgnoresNullValue() {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        String pattern = "excludeNotNullPattern";
        condition.setExcludePattern(pattern);

        // when
        condition.setExcludePattern(null);

        // then
        assertThat(condition.getExcludePattern()).isEqualTo(pattern);
    }

    @Test
    void setCaseSensitive_SetsCaseSensitive() {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        boolean caseSensitive = true;
        condition.setCaseSensitive(caseSensitive);

        // when
        boolean returnedCaseSensitive = condition.isCaseSensitive();

        // then
        assertThat(returnedCaseSensitive).isEqualTo(caseSensitive);
    }

    @Test
    void matches_OnAnyPattern_ReturnsTrue() throws IOException {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("*");

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnEmptyPattern_ReturnsFalse() throws IOException {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("");

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnExcludeAllPattern_ReturnsFalse() throws IOException {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("*Scanner*");
        condition.setExcludePattern("*");

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    void matches_OnRegularExpressionPattern_ReturnsTrue() throws IOException {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("*Scanner*");

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void matches_OnCaseInsensitive_ReturnsTrue() throws IOException {

        // given
        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("*scanner*");
        condition.setCaseSensitive(false);

        Run<?, ?> run = new RunStub();

        // when
        boolean matches = condition.matches(run, null);

        // then
        assertThat(matches).isTrue();
    }

}
