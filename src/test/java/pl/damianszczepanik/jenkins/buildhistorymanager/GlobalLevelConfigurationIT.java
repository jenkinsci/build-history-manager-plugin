package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleBuilder;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class GlobalLevelConfigurationIT {

    @Test
    void configure_AfterDeletingLastRule_SavesNoRules(JenkinsRule jenkins) throws Exception {

        // given
        GlobalLevelConfiguration.get().setRules(List.of(new RuleBuilder.TestRule(false)));

        HtmlPage page = jenkins.createWebClient().goTo("manage/configure");
        HtmlForm form = page.getFormByName("config");
        // expand "Advanced" section to make Delete button for section with roles visible
        HtmlElement advancedButton = form.getFirstByXPath("//div[@id='build-history-manager']/..//button[contains(@class,'advancedButton')]");
        advancedButton.click();

        HtmlElement deleteButton = form.getFirstByXPath("//div[@id='build-history-manager']/..//button[contains(@class,'repeatable-delete')]");

        // when
        deleteButton.click();

        // when
        jenkins.submit(form);

        // then
        jenkins.waitUntilNoActivity();
        assertThat(GlobalLevelConfiguration.get().getRules()).isEmpty();
    }
}
