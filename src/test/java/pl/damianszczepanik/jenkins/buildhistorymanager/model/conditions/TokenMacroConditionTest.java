package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import hudson.model.Run;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManagerTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class TokenMacroConditionTest {

    @BeforeEach
    void setUp() {
        BuildHistoryManagerTest.setUpLogger();
    }

    @Test
    void getTemplate_ReturnsTemplate() {

        // given
        String template = "myTemplate";
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setTemplate(template);

        // when
        String returnedTemplate = condition.getTemplate();

        // then
        assertThat(returnedTemplate).isEqualTo(template);
    }

    @Test
    void getValue_ReturnsValue() {

        // given
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setValue(value);

        // when
        String returnedValue = condition.getValue();

        // then
        assertThat(returnedValue).isEqualTo(value);
    }


    @Test
    void setTemplate_IgnoresNullValue() {

        // given
        String template = "myNotNullTemplate";
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setTemplate(template);

        // when
        condition.setTemplate(null);

        // then
        assertThat(condition.getTemplate()).isEqualTo(template);
    }

    @Test
    void setValue_IgnoresNullValue() {

        // given
        String value = "myNotNullValue";
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setValue(value);

        // when
        condition.setValue(null);

        // then
        assertThat(condition.getValue()).isEqualTo(value);
    }

    @Test
    void matches_OnMatchedTemplate_ReturnsTrue() {

        // given
        String template = "myTemplate";
        String value = template;
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setTemplate(template);
        condition.setValue(value);

        Run run = mockRun();

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    void matches_OnMismatchedTemplate_ReturnsFalse() {

        // given
        String template = "myTemplate";
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setTemplate(template);
        condition.setValue(value);

        Run run = mockRun();

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    void matches_OnInvalidTemplateSyntax_ReturnsFalse() {

        // given
        String template = "myTemplate$$";
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition();
        condition.setTemplate(template);
        condition.setValue(value);

        Run run = mockRun();

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    private static Run mockRun() {
        Run run = mock(Run.class);
        File workspace = mock(File.class);
        when(workspace.getPath()).thenReturn("somePath");
        when(run.getRootDir()).thenReturn(workspace);
        when(run.getNumber()).thenReturn(13);
        return run;
    }
}
