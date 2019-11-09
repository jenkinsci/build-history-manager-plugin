package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import mockit.Deencapsulation;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RunStub extends Run {

    private Result result;

    public int deleteArtifactsTimes;
    public int deleteTimes;

    public RunStub() throws IOException {
        super(mock(Job.class));
        setStartTime(System.currentTimeMillis());
    }

    public RunStub(Result result) throws IOException {
        this();
        this.result = result;
    }

    public RunStub(long startTime) throws IOException {
        this();
        setStartTime(startTime);
    }

    private void setStartTime(long startTime) {
        Deencapsulation.setField(this, "startTime", startTime);
    }

    @Override
    public void deleteArtifacts() {
        deleteArtifactsTimes++;
    }

    @Override
    public void delete() {
        deleteTimes++;
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public long getDuration() {
        return 0;
    }
}
