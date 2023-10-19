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
        // Get the path to the test file in the test resources directory
        Path pipelineScriptPath = Paths.get("src/test/resources", fileName);

        // Read the content of the test file into a string
        String pipelineScript = new String(Files.readAllBytes(pipelineScriptPath));

        // Create the CpsFlowDefinition with the loaded script
        CpsFlowDefinition cpsFlowDefinition = new CpsFlowDefinition(pipelineScript, true);

        // Create a Jenkins pipeline job
        WorkflowJob pipelineJob = jenkinsRule.jenkins.createProject(WorkflowJob.class, fileName);

        // Set the pipeline script for the job
        pipelineJob.setDefinition(cpsFlowDefinition);
        return pipelineJob;
    }

    private WorkflowRun executeAndWaitForSuccessfulRun(WorkflowJob pipelineJob) throws InterruptedException, ExecutionException, IOException {
        WorkflowRun run = pipelineJob.scheduleBuild2(0).get();

        // Wait for the build to complete
        jenkinsRule.waitForCompletion(run);

        // Get the build result
        Result result = run.getResult();

        // Assertion for the build success
        Assert.assertEquals(run.getLog(), Result.SUCCESS, result);
        return run;
    }

    @Test
    public void queueMultipleRuns() throws Exception {
        WorkflowJob pipelineJob = createPipelineJobFromScriptFile("buildPositionCondition_KeepLastTenBuildsTest.jf");
        //Create an ArrayList to store the runs
        List<WorkflowRun> arrayList = new ArrayList<>();
        for (int i = 1; i <= 15; i++ ) {    
            // Execute the job and wait for a successful run
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
