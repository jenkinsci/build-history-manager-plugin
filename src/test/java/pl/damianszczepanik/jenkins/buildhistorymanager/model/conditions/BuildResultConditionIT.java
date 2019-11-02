package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hudson.model.Job;
import hudson.model.Result;
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
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteBuildAction;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.DescriptorMocker;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.JobBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see <a href="https://github.com/jenkinsci/build-history-manager-plugin/wiki/Build-result-condition">documentation</a>
 */
@PrepareForTest(Jenkins.class)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.xml.*")
public class BuildResultConditionIT {

    @Before
    public void setUp() {
        new DescriptorMocker(new DeleteBuildActionDescriptor());
    }

    @Test
    public void testBuildResultCondition() throws IOException, InterruptedException {

        // given
        RunStub run56 = new RunStub(56, Result.SUCCESS);

        RunStub run54 = new RunStub(54, Result.FAILURE);
        run56.setPreviousBuild(run54);

        RunStub run52 = new RunStub(52, Result.SUCCESS);
        run54.setPreviousBuild(run52);

        RunStub run50 = new RunStub(50, Result.FAILURE);
        run52.setPreviousBuild(run50);

        RunStub run41 = new RunStub(41, Result.FAILURE);
        run50.setPreviousBuild(run41);

        RunStub run35 = new RunStub(35, Result.SUCCESS);
        run41.setPreviousBuild(run35);

        RunStub run32 = new RunStub(32, Result.UNSTABLE);
        run35.setPreviousBuild(run32);

        RunStub run30 = new RunStub(30, Result.SUCCESS);
        run32.setPreviousBuild(run30);


        Rule rule1 = new Rule(Collections.emptyList(), Collections.emptyList());
        rule1.setContinueAfterMatch(false);
        rule1.setMatchAtMost(3);

        BuildResultCondition buildResultCondition = new BuildResultCondition();
        buildResultCondition.setMatchSuccess(true);
        Rule rule2 = new Rule(Collections.singletonList(buildResultCondition), Collections.emptyList());
        rule2.setContinueAfterMatch(false);
        rule2.setMatchAtMost(1);

        Rule rule3 = new Rule(Collections.emptyList(), Collections.singletonList(new DeleteBuildAction()));

        List<Rule> rules = Arrays.asList(rule1, rule2, rule3);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run56);

        // when
        buildHistoryManager.perform(job);

        // then
        run56.assertBuildIsAvailable();
        run54.assertBuildIsAvailable();
        run52.assertBuildIsAvailable();
        run50.assertBuildWasDeleted();
        run41.assertBuildWasDeleted();
        run35.assertBuildIsAvailable();
        run32.assertBuildWasDeleted();
        run30.assertBuildWasDeleted();
    }
}
