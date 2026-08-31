package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import hudson.util.ListBoxModel;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.PrecedenceMode;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.GlobalLevelConfigurationStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class GlobalLevelConfigurationTest {

    @Test
    void getPrecedenceMode_ReturnsDefaultValue() {

        // given
        GlobalLevelConfiguration configuration = GlobalLevelConfigurationStub.instance();

        // when
        PrecedenceMode precedenceMode = PrecedenceMode.valueOf(configuration.getPrecedenceMode());

        // then
        assertThat(precedenceMode).isEqualTo(PrecedenceMode.JOB_ONLY);
    }

    @Test
    void setRules_FixesNullValue() {

        // given
        GlobalLevelConfiguration configuration = GlobalLevelConfigurationStub.instance();
        List<Rule> initialRules = Lists.list(new Rule(null, null));
        configuration.setRules(initialRules);

        // when
        configuration.setRules(null);

        // then
        assertThat(configuration.getRules()).hasSize(0);
    }

    @Test
    void getRules_ReturnsNotNullList() {

        // given
        GlobalLevelConfiguration configuration = GlobalLevelConfigurationStub.instance();

        // when
        List<Rule> rules = configuration.getRules();

        // then
        assertThat(rules).hasSize(0);
    }

    @Test
    void getPrecedenceMode_ReturnsPrecedenceMode() {

        // given
        GlobalLevelConfiguration configuration = GlobalLevelConfigurationStub.instance();
        PrecedenceMode precedenceMode = PrecedenceMode.GLOBAL_THEN_JOB;
        configuration.setPrecedenceMode(precedenceMode.name());

        // when
        String returnedValue = configuration.getPrecedenceMode();

        // then
        assertThat(precedenceMode.name()).isEqualTo(returnedValue);
    }

    @Test
    void doFillPrecedenceModeItems_ReturnsItemsForEveryPrecedenceMode() {

        // given
        GlobalLevelConfiguration configuration = GlobalLevelConfigurationStub.instance();

        // when
        ListBoxModel items = configuration.doFillPrecedenceModeItems();

        // then
        assertThat(items).hasSameSizeAs(PrecedenceMode.values());
    }


    @Test
    void doFillPrecedenceModeItems_ReturnsDefaultItemOnFirstPosition() {

        // given
        GlobalLevelConfiguration configuration = GlobalLevelConfigurationStub.instance();

        // when
        ListBoxModel items = configuration.doFillPrecedenceModeItems();

        // then
        assertThat(items.get(0).value).isEqualTo(PrecedenceMode.JOB_ONLY.name());
    }
}
