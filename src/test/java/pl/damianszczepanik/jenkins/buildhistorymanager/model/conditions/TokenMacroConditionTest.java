package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.IOException;

import hudson.FilePath;
import hudson.model.Run;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@RunWith(PowerMockRunner.class)
public class TokenMacroConditionTest {

    @Test
    public void getTemplate_ReturnsTemplate() {

        // given
        String template = "myTemplate";
        TokenMacroCondition condition = new TokenMacroCondition(template, null);

        // when
        String returnedTemplate = condition.getTemplate();

        // then
        assertThat(returnedTemplate).isEqualTo(template);
    }

    @Test
    public void getValue_ReturnsValue() {

        // given
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition(null, value);

        // when
        String returnedValue = condition.getValue();

        // then
        assertThat(returnedValue).isEqualTo(value);
    }

    @Test
    public void matches_OnMatchedTemplate_ReturnsTrue() {

        // given
        String template = "myTemplate";
        String value = template;
        TokenMacroCondition condition = new TokenMacroCondition(template, value);

        Run run = mockRun();

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void matches_OnMismatchedTemplate_ReturnsFalse() {

        // given
        String template = "myTemplate";
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition(template, value);

        Run run = mockRun();

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    @PrepareForTest({TokenMacro.class, FilePath.class})
    public void matches_OnInvalidTemplate_ReturnsFalse() {

        // given
        String template = "myTemplate";
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition(template, value);

        Run run = mockRun();
        mockStatic(TokenMacro.class);
        mockStatic(FilePath.class);

        try {
            when(TokenMacro.expandAll(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any()))
                    .thenThrow(new MacroEvaluationException("ups!"));
        } catch (MacroEvaluationException | IOException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }

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
        return run;
    }
}
