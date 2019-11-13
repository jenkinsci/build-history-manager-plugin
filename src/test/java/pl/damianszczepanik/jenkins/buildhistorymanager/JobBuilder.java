package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

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

        when(job.getLastCompletedBuild()).thenReturn(lastBuild);

        return job;
    }
}
