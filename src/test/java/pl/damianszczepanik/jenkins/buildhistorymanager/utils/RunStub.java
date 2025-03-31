package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hudson.model.Cause;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.security.Permission;
import org.apache.commons.lang.StringUtils;
import org.powermock.reflect.Whitebox;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RunStub extends Run {
    public enum LogFileAvailability {
        PRESENT, ABSENT
    }

    private Result result;
    private LogFileAvailability logFileState = LogFileAvailability.PRESENT;
    private List<Cause> causes;

    private int deleteArtifactsTimes;
    private int deleteTimes;

    public RunStub(LogFileAvailability logFileState) throws IOException {
        this();
        this.logFileState = logFileState;
    }

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
                ? Collections.emptyList() : List.of(new MockCause(causeClass));
    }

    public RunStub(String[] causeClass) throws IOException {
        this();
        causes = new ArrayList<>();
        for (String cause : causeClass) {
            causes.add(new MockCause(cause));
        }
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
        Whitebox.setInternalState(this, "startTime", startTime);
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
        when(logFile.exists()).thenReturn(logFileState == LogFileAvailability.PRESENT);
        when(logFile.delete()).thenReturn(deleteLogFile());

        return logFile;
    }

    // skips serialization which is quite problematic
    @Override
    public synchronized void save() {
    }

    // skips security checking which is quite problematic
    @Override
    public void checkPermission(Permission permission) {
    }

    private boolean deleteLogFile() {
        boolean deleteResult = logFileState == LogFileAvailability.PRESENT;
        logFileState = LogFileAvailability.ABSENT;
        return deleteResult;
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

    public void assertLogFileIsNotAvailable() {
        assertThat(logFileState).isEqualTo(LogFileAvailability.ABSENT);
    }

    public void assertLogFileIsAvailable() {
        assertThat(logFileState).isEqualTo(LogFileAvailability.PRESENT);
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
