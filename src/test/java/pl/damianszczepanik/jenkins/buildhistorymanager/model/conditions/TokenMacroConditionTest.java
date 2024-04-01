package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;

import hudson.model.Run;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("com.thoughtworks.xstream.converters.extended.DurationConverter")
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
    public void matches_OnInvalidTemplateSyntax_ReturnsFalse() {

        // given
        String template = "myTemplate$$";
        String value = "myValue";
        TokenMacroCondition condition = new TokenMacroCondition(template, value);

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
        return run;
    }
}
