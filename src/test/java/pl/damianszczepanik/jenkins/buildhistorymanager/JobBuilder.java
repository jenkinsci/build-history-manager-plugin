package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import hudson.model.Job;
import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class JobBuilder {

    public static <JobT extends Job<JobT, RunT>, RunT extends Run<JobT, RunT>> Job buildSampleJob() {
        Job<JobT, RunT> job = mock(Job.class);

        RunT lastBuild = (RunT) mock(Run.class);
        when(job.getLastBuild()).thenReturn(lastBuild);

        return job;
    }
}
