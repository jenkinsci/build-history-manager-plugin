package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hudson.model.Cause;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import mockit.Deencapsulation;
import org.apache.commons.lang.StringUtils;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RunStub extends Run {

    private Result result;
    private boolean isLogFilePresent;
    private List<Cause> causes;

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

    public RunStub(int buildNumber, long startTime) throws IOException {
        this(buildNumber);
        setStartTime(startTime);
    }

    public RunStub(String causeClass) throws IOException {
        this();
        this.causes = StringUtils.isEmpty(causeClass)
                ? Collections.emptyList() : Arrays.asList(new MockCause(causeClass));
    }

    public RunStub(String[] causeClass) throws IOException {
        this();
        List<Cause> causes = new ArrayList<>();
        for (String cause : causeClass) {
            causes.add(new MockCause(cause));
        }
        this.causes = causes;
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
    public List<Cause> getCauses() {
        return causes;
    }

    @Override
    public boolean isBuilding() {
        return false;
    }

    @Override
    public String toString() {
        return "RunStub:"
                + (number != 0 ? " number=" + number : "")
                + (result != null ? " result=" + result : "")
                + (getStartTimeInMillis() != 0 ? " startTime=" + new Date(getStartTimeInMillis()) : "");
    }
}
