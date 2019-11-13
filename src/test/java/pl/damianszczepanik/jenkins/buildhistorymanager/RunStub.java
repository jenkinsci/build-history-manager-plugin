package pl.damianszczepanik.jenkins.buildhistorymanager;

import static org.assertj.core.api.Assertions.assertThat;
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

    private int deleteArtifactsTimes;
    private int deleteTimes;

    public RunStub(int buildNumber) throws IOException {
        this();
        setBuildNumber(buildNumber);
    }

    public RunStub(Result result) throws IOException {
        this();
        this.result = result;
    }

    public RunStub(long startTime) throws IOException {
        this();
        setStartTime(startTime);
    }

    public RunStub() throws IOException {
        super(mock(Job.class));
        setStartTime(System.currentTimeMillis());
    }

    public void setBuildNumber(int buildNumber) {
        this.number = buildNumber;
    }

    public void setPreviousBuild(Run previousBuild) {
        this.previousBuild = previousBuild;
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

    public void assertBuildWasDeleted() {
        assertThat(deleteTimes).isOne();
    }

    public void assertBuildIsAvailable() {
        assertThat(deleteTimes).isZero();
    }

    public void assertArtifactsWereDeleted() {
        assertThat(deleteArtifactsTimes).isOne();
    }

    public void assertArtifactsAreAvailable() {
        assertThat(deleteArtifactsTimes).isZero();
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public boolean isBuilding() {
        return false;
    }
}
