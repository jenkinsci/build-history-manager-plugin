package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.Result;
import hudson.util.RunList;

public class BuildPositionConditionIT {

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();
 
    private WorkflowJob createPipelineJobFromScriptFile(String fileName) throws IOException {
        Path pipelineScriptPath = Paths.get("src/test/resources", fileName);
        String pipelineScript = new String(Files.readAllBytes(pipelineScriptPath));

        CpsFlowDefinition cpsFlowDefinition = new CpsFlowDefinition(pipelineScript, true);
        WorkflowJob pipelineJob = jenkinsRule.jenkins.createProject(WorkflowJob.class, fileName);

        pipelineJob.setDefinition(cpsFlowDefinition);
        return pipelineJob;
    }

    private WorkflowRun executeAndWaitForSuccessfulRun(WorkflowJob pipelineJob) throws InterruptedException, ExecutionException {
        WorkflowRun run = pipelineJob.scheduleBuild2(0).get();
        jenkinsRule.waitForCompletion(run);
        return run;
    }

    @Test
    public void queueMultipleRuns() throws Exception {
        WorkflowJob pipelineJob = createPipelineJobFromScriptFile("buildPositionCondition_KeepLastTenBuildsTest.jf");
        List<WorkflowRun> arrayList = new ArrayList<>();
        for (int i = 1; i <= 15; i++ ) {
            WorkflowRun run = executeAndWaitForSuccessfulRun(pipelineJob);
            arrayList.add(run);
        }

        RunList<WorkflowRun> runList = pipelineJob.getBuilds();
        int numOfRuns = 0;
        for (WorkflowRun run : runList) {
            numOfRuns++;
        }        
        Assert.assertEquals(10, numOfRuns);
    }
}
