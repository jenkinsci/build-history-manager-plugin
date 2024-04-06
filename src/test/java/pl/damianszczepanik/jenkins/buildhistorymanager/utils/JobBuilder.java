package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.Job;
import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class JobBuilder {

    public static Job buildSampleJob() {
        return buildSampleJob(mock(Run.class));
    }

    public static Job buildSampleJob(Run lastBuild) {
        Job job = mock(Job.class);
        when(job.getFullName()).thenReturn("sampleJob");
        when(job.getLastCompletedBuild()).thenReturn(lastBuild);

        return job;
    }
}
