package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.model.Job;
import hudson.model.Run;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder.NegativeCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class BuildHistoryManagerTest {

    private List<Rule> sampleRules;

    @BeforeEach
    void setUp() {
        sampleRules = Arrays.asList(new RuleBuilder.TestRule(false), new RuleBuilder.TestRule(false));

        Logger logger = Logger.getLogger(BuildHistoryManager.class.getName());
        logger.setLevel(Level.ALL);
        Whitebox.setInternalState(BuildHistoryManager.class, "LOG", logger);
    }

    @Test
    void BuildHistoryManagerTest_OnEmptyRules_SavesEmptyRule() {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(null);

        // when
        List<Rule> rules = discarder.getRules();

        // then
        assertThat(rules).isEmpty();
    }

    @Test
    void getRules_ReturnsRules() {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);

        // when
        List<Rule> rules = discarder.getRules();

        // then
        assertThat(rules).containsAll(sampleRules);
    }

    @Test
    void perform_InitializesRule() throws IOException, InterruptedException {

        // given
        Rule rule = new Rule(List.of(new NegativeCondition()), null);
        Whitebox.setInternalState(rule, "matchedTimes", 1);
        BuildHistoryManager discarder = new BuildHistoryManager(List.of(rule));
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        int matchedTimes = Whitebox.getInternalState(rule, "matchedTimes");
        assertThat(matchedTimes).isZero();
    }

    @Test
    void perform_validatesEachRules() throws IOException, InterruptedException {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        for (Rule rule : sampleRules) {
            assertThat(((RuleBuilder.TestRule) rule).validateConditionsTimes).isOne();
        }
    }

    @Test
    void perform_OnKeptBuild_SkipsValidate() throws IOException, InterruptedException {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);
        Run keptRun = mock(Run.class);
        when(keptRun.isKeepLog()).thenReturn(true);
        Job<?, ?> job = JobBuilder.buildSampleJob(keptRun);

        // when
        discarder.perform(job);

        // then
        for (Rule rule : sampleRules) {
            assertThat(((RuleBuilder.TestRule) rule).validateConditionsTimes).isZero();
        }
    }

    @Test
    void perform_OnNegativeCondition_ValidatesEachRules() throws IOException, InterruptedException {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        for (Rule rule : sampleRules) {
            assertThat(((RuleBuilder.TestRule) rule).performActionsTimes).isZero();
        }
    }

    @Test
    void perform_OnPositiveCondition_ValidatesEachRules() throws IOException, InterruptedException {

        // given
        sampleRules = Arrays.asList(new RuleBuilder.TestRule(true), new RuleBuilder.TestRule(true));

        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        for (Rule rule : sampleRules) {
            assertThat(((RuleBuilder.TestRule) rule).performActionsTimes).isOne();
        }
    }

    @Test
    void perform_OnNotContinueAfterMatch_ValidatesOnlyFirstRules() throws IOException, InterruptedException {

        // given
        sampleRules = Arrays.asList(new RuleBuilder.TestRule(true), new RuleBuilder.TestRule(true));
        RuleConfiguration configuration = new RuleConfiguration();
        configuration.setContinueAfterMatch(false);
        Whitebox.setInternalState(sampleRules.get(0), "configuration", configuration);

        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        assertThat(((RuleBuilder.TestRule) sampleRules.get(0)).validateConditionsTimes).isOne();
        assertThat(((RuleBuilder.TestRule) sampleRules.get(1)).validateConditionsTimes).isZero();
    }
}
