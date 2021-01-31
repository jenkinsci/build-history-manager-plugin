package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import hudson.model.AbstractItem;
import hudson.model.Job;
import jenkins.model.Jenkins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions.DeleteBuildActionDescriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteArtifactsAction;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.DescriptorMocker;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see <a href="https://github.com/jenkinsci/build-history-manager-plugin/wiki/Build-age-range-condition">documentation</a>
 */
@PrepareForTest({
        Jenkins.class,
        AbstractItem.class  // getFullName()
})
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.xml.*")
public class BuildAgeRangeConditionIT {

    @Before
    public void setUp() {
        new DescriptorMocker(new DeleteBuildActionDescriptor());
    }

    @Test
    public void testBuildNumberRangeCondition() throws IOException, InterruptedException {

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
        Rule rule = new Rule(Arrays.asList(condition), Arrays.asList(new DeleteArtifactsAction()));


        List<Rule> rules = Arrays.asList(rule);
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

    private Calendar createMidnight() {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        return midnight;
    }
}
