package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildHistoryDiscarderTest {

    private List<Rule> sampleRules = Arrays.asList(new Rule(null, null));

    @Test
    public void BuildHistoryDiscarderTest_OnEmptyRules_SavesEmptyRule() {

        // given
        BuildHistoryDiscarder discarder = new BuildHistoryDiscarder(null);

        // when
        List<Rule> rules = discarder.getRules();

        // then
        assertThat(rules).isEmpty();
    }

    @Test
    public void getRules_ReturnsRules() {

        // given
        BuildHistoryDiscarder discarder = new BuildHistoryDiscarder(sampleRules);

        // when
        List<Rule> rules = discarder.getRules();

        // then
        assertThat(rules).containsAll(sampleRules);
    }
}
