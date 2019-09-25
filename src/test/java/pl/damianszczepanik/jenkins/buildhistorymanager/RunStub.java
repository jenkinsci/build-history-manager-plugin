package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import hudson.model.Job;
import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class RunStub extends Run {

    public int times;

    public RunStub() throws IOException {
        super(mock(Job.class));
    }
}
