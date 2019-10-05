package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import hudson.model.Job;
import org.junit.Before;
import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleBuilder;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildHistoryDiscarderTest {

    private List<Rule> sampleRules;

    @Before
    public void setUp() {
        sampleRules = Arrays.asList(new RuleBuilder.TestRule(), new RuleBuilder.TestRule());
    }

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

    @Test
    public void perform_InitializeRules() throws IOException, InterruptedException {

        // given
        BuildHistoryDiscarder discarder = new BuildHistoryDiscarder(sampleRules);
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        for (Rule rule : sampleRules) {
            assertThat(((RuleBuilder.TestRule) rule).initializeTimes).isOne();
        }
    }
}
