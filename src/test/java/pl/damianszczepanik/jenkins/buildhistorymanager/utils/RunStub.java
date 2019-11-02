package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
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
    private boolean isLogFilePresent;

    private int deleteArtifactsTimes;
    private int deleteTimes;
    private int deleteLogFile;

    public RunStub(int buildNumber) throws IOException {
        this();
        setBuildNumber(buildNumber);
    }

    public RunStub(Result result) throws IOException {
        this();
        this.result = result;
    }

    public RunStub(int buildNumber, Result result) throws IOException {
        this(buildNumber);
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

    public void setLogFile(boolean isPresent) {
        isLogFilePresent = isPresent;
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
    public File getLogFile() {
        File logFile = mock(File.class);
        when(logFile.exists()).thenReturn(isLogFilePresent);
        when(logFile.delete()).thenReturn(deleteLogFile(isLogFilePresent));

        return logFile;
    }

    private boolean deleteLogFile(boolean isLogFilePresent) {
        deleteLogFile++;
        return isLogFilePresent;
    }

    public void assertBuildWasDeleted() {
        assertThat(deleteTimes).isOne();
    }

    public void assertLogFileWasDeleted() {
        assertThat(deleteLogFile).isOne();
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

    public void assertLogFileWasNotDeleted() {
        assertThat(deleteLogFile).isOne();
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
