package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.Result;
import hudson.model.Run;

public class DeleteArtifactsWithPatternActionIT {

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

    private String generateJoinedArtifactsList(WorkflowRun run) {
        // Create a stream of artifacts from the run's artifacts list
        Stream<Run<WorkflowJob, WorkflowRun>.Artifact> artifactsStream = run.getArtifacts().stream();

        // Map each artifact to its string representation and then join them with a comma and a space in between to create a string of joined artifacts.
        String joinedArtifacts = artifactsStream.map(Object::toString).collect(Collectors.joining(", "));
        return joinedArtifacts;
    }

    @Test //Test #1
    public void perform_smokeTest() throws Exception {
        WorkflowJob pipelineJob = createPipelineJobFromScriptFile("perform_smokeTest.jf");

        // Run the Jenkins build
        WorkflowRun run = executeAndWaitForSuccessfulRun(pipelineJob);

        String joinedArtifacts = generateJoinedArtifactsList(run);

        // exclude: "**/*.log", include: "**"
        Assert.assertEquals(2, run.getArtifacts().size());
        Assert.assertEquals("testFolder/testLog1.log, testLog.log", joinedArtifacts);
    }

    @Test //Test #2
    public void perform_withKeepForever() throws Exception {
        WorkflowJob pipelineJob = createPipelineJobFromScriptFile("perform_withKeepForever.jf");

        //Create a list to hold the WorkflowRuns
        List<WorkflowRun> runList = new ArrayList<>();

        // Add the WorkflowRuns to the list
        for (int i = 1; i <= 5; i++) {
            WorkflowRun run = executeAndWaitForSuccessfulRun(pipelineJob);
            if (i == 2 || i == 4){
                // Mark the second and fourth build to be kept
                run.keepLog();
            }
            runList.add(run);
        }

        for (int i = 1; i <= 5; i++) {        
            String joined_Artifacts = generateJoinedArtifactsList(runList.get(i-1));
            if (i == 2 || i == 4 || i == 5) {
                Assert.assertEquals("test.xml, testFolder/test1.xml, testFolder/testLog1.log, testFolder/testTxt1.txt, testLog.log, testTxt.txt", joined_Artifacts);
            } else {
                Assert.assertEquals("testFolder/testLog1.log, testLog.log", joined_Artifacts);
            }
        }
    }

    @Test //Test #3
    // in this test, we simulate the date(age) using build number range instead of waiting for 5 days, 10 days or whatever. Assuming that we're simulating a job which runs daily after 7 runs, 7 days should have elapsed.
    // CHTAH-1384 scenarios : 1) delete all binaries after 2 days ---> delete everything except the logs after 2 days.
    // 2) delete all logs after 5 days ---> delete everything else after 5 days.

    public void perform_deleteArtifactsGraduallyOverTime () throws Exception {
        WorkflowJob pipelineJob = createPipelineJobFromScriptFile("perform_deleteArtifactsGraduallyOverTime.jf");

        //Create a list to hold the WorkflowRuns
        List<WorkflowRun> runList = new ArrayList<>();

        // Add the WorkflowRuns to the list
        for (int i = 1; i <= 7; i++) {
            WorkflowRun run = executeAndWaitForSuccessfulRun(pipelineJob);
            runList.add(run);
        }

        for (int i = 1; i <= 7; i++) {
            String joined_Artifacts = generateJoinedArtifactsList(runList.get(i-1));
            if (i <= 2) {
                // run1 and run2 delete everything
                Assert.assertEquals("", joined_Artifacts);
            } else if (i >= 3 && i <= 5) {
                // run3, run4, and run5 should have only log files
                Assert.assertEquals("testFolder/testLog1.log, testLog.log", joined_Artifacts);
            } else {
                // run6 and run7 should have everything
                Assert.assertEquals("test.xml, testFolder/test1.xml, testFolder/testLog1.log, testFolder/testTxt1.txt, testLog.log, testTxt.txt", joined_Artifacts);
            }
        }
    }
}
