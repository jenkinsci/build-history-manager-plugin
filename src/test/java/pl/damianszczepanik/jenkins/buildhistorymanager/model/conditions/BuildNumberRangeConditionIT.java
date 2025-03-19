package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import hudson.model.Job;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteBuildAction;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see <a href="https://github.com/jenkinsci/build-history-manager-plugin/wiki/Build-number-range-condition">documentation</a>
 */
@WithJenkins
class BuildNumberRangeConditionIT {

    @Test
    void testBuildNumberRangeCondition(JenkinsRule r) throws IOException, InterruptedException {

        // given
        RunStub run80 = new RunStub(80);

        RunStub run76 = new RunStub(76);
        run80.setPreviousBuild(run76);

        RunStub run75 = new RunStub(75);
        run76.setPreviousBuild(run75);

        RunStub run73 = new RunStub(73);
        run75.setPreviousBuild(run73);

        RunStub run71 = new RunStub(71);
        run73.setPreviousBuild(run71);

        RunStub run70 = new RunStub(70);
        run71.setPreviousBuild(run70);

        RunStub run2 = new RunStub(2);
        run70.setPreviousBuild(run2);


        BuildNumberRangeCondition condition1 = new BuildNumberRangeCondition();
        condition1.setMinBuildNumber(73);
        condition1.setMaxBuildNumber(75);
        Rule rule1 = new Rule(List.of(condition1), List.of(new DeleteBuildAction()));

        BuildNumberRangeCondition condition2 = new BuildNumberRangeCondition();
        condition2.setMaxBuildNumber(70);
        Rule rule2 = new Rule(List.of(condition2), List.of(new DeleteBuildAction()));

        List<Rule> rules = Arrays.asList(rule1, rule2);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run80);

        // when
        buildHistoryManager.perform(job);

        // then
        run80.assertBuildIsAvailable();
        run76.assertBuildIsAvailable();
        run75.assertBuildWasDeleted();
        run73.assertBuildWasDeleted();
        run71.assertBuildIsAvailable();
        run70.assertBuildWasDeleted();
        run2.assertBuildWasDeleted();
    }
}
