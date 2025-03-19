package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import hudson.model.Job;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteArtifactsAction;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see <a href="https://github.com/jenkinsci/build-history-manager-plugin/wiki/Build-age-range-condition">documentation</a>
 */
@WithJenkins
class BuildAgeRangeConditionIT {

    @Test
    void testBuildNumberRangeCondition(JenkinsRule r) throws IOException, InterruptedException {

        // given
        Calendar midnight = createMidnight();
        RunStub run80 = new RunStub(80, midnight.getTimeInMillis());

        RunStub run76 = new RunStub(76, midnight.getTimeInMillis());
        run80.setPreviousBuild(run76);

        Calendar yesterday = createMidnight();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        RunStub run75 = new RunStub(75, yesterday.getTimeInMillis());
        run76.setPreviousBuild(run75);

        Calendar twoDaysPast = createMidnight();
        twoDaysPast.add(Calendar.DAY_OF_MONTH, -2);
        RunStub run73 = new RunStub(73, twoDaysPast.getTimeInMillis());
        run75.setPreviousBuild(run73);

        Calendar threeDaysPast = createMidnight();
        threeDaysPast.add(Calendar.DAY_OF_MONTH, -3);
        RunStub run71 = new RunStub(71, threeDaysPast.getTimeInMillis());
        run73.setPreviousBuild(run71);


        BuildAgeRangeCondition condition = new BuildAgeRangeCondition();
        condition.setMinDaysAge(1);
        condition.setMaxDaysAge(2);
        Rule rule = new Rule(List.of(condition), List.of(new DeleteArtifactsAction()));


        List<Rule> rules = List.of(rule);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run80);

        // when
        buildHistoryManager.perform(job);

        // then
        run80.assertArtifactsAreAvailable();
        run76.assertArtifactsAreAvailable();
        run75.assertArtifactsWereDeleted();
        run73.assertArtifactsWereDeleted();
        run71.assertArtifactsAreAvailable();
    }

    private static Calendar createMidnight() {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        return midnight;
    }
}
