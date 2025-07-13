package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Job;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteBuildAction;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

import java.io.IOException;
import java.util.List;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class FileScannerConditionIT {

    @Test
    void perform_OnAsteriskPattern_DeletesBuild(JenkinsRule r) throws IOException, InterruptedException {

        // given
        RunStub run = new RunStub(11);

        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("*");
        Rule rule = new Rule(List.of(condition), List.of(new DeleteBuildAction()));

        List<Rule> rules = List.of(rule);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run);

        // when
        buildHistoryManager.perform(job);

        // then
        run.assertBuildWasDeleted();
    }

    @Test
    void perform_OnExcludeAlPattern_DeletesBuild(JenkinsRule r) throws IOException, InterruptedException {

        // given
        RunStub run = new RunStub(12);

        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("*.*");
        condition.setExcludePattern("*");
        Rule rule = new Rule(List.of(condition), List.of(new DeleteBuildAction()));

        List<Rule> rules = List.of(rule);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run);

        // when
        buildHistoryManager.perform(job);

        // then
        run.assertBuildIsAvailable();
    }

    @Test
    void perform_OnMismatchedPattern_KeepsBuild(JenkinsRule r) throws IOException, InterruptedException {

        // given
        RunStub run = new RunStub(13);

        FileScannerCondition condition = new FileScannerCondition();
        condition.setIncludePattern("wishIHaveThisFileIs.present");
        Rule rule = new Rule(List.of(condition), List.of(new DeleteBuildAction()));

        List<Rule> rules = List.of(rule);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run);

        // when
        buildHistoryManager.perform(job);

        // then
        run.assertBuildIsAvailable();
    }
}
