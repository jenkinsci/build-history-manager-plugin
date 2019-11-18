package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hudson.model.Job;
import jenkins.model.Jenkins;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.JobBuilder;
import pl.damianszczepanik.jenkins.buildhistorymanager.RunStub;
import pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.DeleteBuildActionDescriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see <a href="https://github.com/jenkinsci/build-history-manager-plugin/wiki/Delete-artifacts">documentation</a>
 */
@PrepareForTest(Jenkins.class)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.xml.*")
public class DeleteBuildActionIT {

    @Test
    public void testDeletesArtifacts() throws IOException, InterruptedException {

        // given
        RunStub run23 = new RunStub(23);

        RunStub run24 = new RunStub(24);
        run23.setPreviousBuild(run24);

        RunStub run25 = new RunStub(25);
        run24.setPreviousBuild(run25);

        RunStub run30 = new RunStub(30);
        run25.setPreviousBuild(run30);

        RunStub run31 = new RunStub(31);
        run30.setPreviousBuild(run31);

        RunStub run32 = new RunStub(32);
        run31.setPreviousBuild(run32);

        RunStub run35 = new RunStub(35);
        run32.setPreviousBuild(run35);


        Rule rule1 = new Rule(Collections.emptyList(), Collections.emptyList());
        rule1.setContinueAfterMatch(false);
        rule1.setMatchAtMost(2);

        Rule rule2 = new Rule(Collections.emptyList(), Collections.singletonList(new DeleteArtifactsAction()));
        rule2.setContinueAfterMatch(false);
        rule2.setMatchAtMost(3);

        Rule rule3 = new Rule(Collections.emptyList(), Collections.singletonList(new DeleteBuildAction()));

        List<Rule> rules = Arrays.asList(rule1, rule2, rule3);
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(rules);
        Job job = JobBuilder.buildSampleJob(run23);
        mockDescriptor();

        // when
        buildHistoryManager.perform(job);

        // then
        run23.assertBuildIsAvailable();
        run23.assertArtifactsAreAvailable();

        run24.assertBuildIsAvailable();
        run24.assertArtifactsAreAvailable();

        run25.assertBuildIsAvailable();
        run25.assertArtifactsWereDeleted();

        run30.assertBuildIsAvailable();
        run30.assertArtifactsWereDeleted();

        run31.assertBuildIsAvailable();
        run31.assertArtifactsWereDeleted();

        run32.assertBuildWasDeleted();

        run35.assertBuildWasDeleted();
    }

    private static void mockDescriptor() {
        mockStatic(Jenkins.class);
        Jenkins jenkins = mock(Jenkins.class);
        when(Jenkins.getInstance()).thenReturn(jenkins);
        when(jenkins.getDescriptorOrDie(Matchers.any())).thenReturn(new DeleteBuildActionDescriptor());
    }
}
