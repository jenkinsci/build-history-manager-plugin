package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import hudson.model.AbstractItem;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import hudson.model.Job;
import hudson.model.Run;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.ConditionBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Run.class, // isKeepLog()
        AbstractItem.class  // getFullName()
})
public class BuildHistoryManagerTest {

    private List<Rule> sampleRules;

    @Before
    public void setUp() {
        sampleRules = Arrays.asList(new RuleBuilder.TestRule(false), new RuleBuilder.TestRule(false));
    }

    @Test
    public void BuildHistoryManagerTest_OnEmptyRules_SavesEmptyRule() {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(null);

        // when
        List<Rule> rules = discarder.getRules();

        // then
        assertThat(rules).isEmpty();
    }

    @Test
    public void getRules_ReturnsRules() {

        // given
        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);

        // when
        List<Rule> rules = discarder.getRules();

        // then
        assertThat(rules).containsAll(sampleRules);
    }

    @Test
    public void perform_InitializesRule() throws IOException, InterruptedException {

        // given
        Rule rule = new Rule(Arrays.asList(new ConditionBuilder.NegativeCondition()), null);
        Deencapsulation.setField(rule, "matchedTimes", 1);
        BuildHistoryManager discarder = new BuildHistoryManager(Arrays.asList(rule));
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        int matchedTimes = Deencapsulation.getField(rule, "matchedTimes");
        assertThat(matchedTimes).isZero();
    }

    @Test
    public void perform_validatesEachRules() throws IOException, InterruptedException {

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
    public void perform_OnKeptBuild_SkipsValidate() throws IOException, InterruptedException {

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
    public void perform_OnNegativeCondition_ValidatesEachRules() throws IOException, InterruptedException {

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
    public void perform_OnPositiveCondition_ValidatesEachRules() throws IOException, InterruptedException {

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
    public void perform_OnNotContinueAfterMatch_ValidatesOnlyFirstRules() throws IOException, InterruptedException {

        // given
        sampleRules = Arrays.asList(new RuleBuilder.TestRule(true), new RuleBuilder.TestRule(true));
        RuleConfiguration configuration = new RuleConfiguration();
        configuration.setContinueAfterMatch(false);
        Deencapsulation.setField(sampleRules.get(0), "configuration", configuration);

        BuildHistoryManager discarder = new BuildHistoryManager(sampleRules);
        Job<?, ?> job = JobBuilder.buildSampleJob();

        // when
        discarder.perform(job);

        // then
        assertThat(((RuleBuilder.TestRule) sampleRules.get(0)).validateConditionsTimes).isOne();
        assertThat(((RuleBuilder.TestRule) sampleRules.get(1)).validateConditionsTimes).isZero();
    }
}
