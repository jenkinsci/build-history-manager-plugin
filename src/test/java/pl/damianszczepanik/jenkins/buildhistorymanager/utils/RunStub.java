package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.Cause;
import hudson.model.Job;
import hudson.model.ParameterValue;
import hudson.model.Result;
import hudson.model.Run;
import hudson.security.Permission;
import org.apache.commons.lang.StringUtils;
import org.powermock.reflect.Whitebox;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RunStub extends Run {
    private Result result;
    private File logFile;
    private List<Cause> causes;
    private List<ParameterValue> parameterValues = List.of();

    private int deleteArtifactsTimes;
    private int deleteTimes;
    private File rootDir = new File("target");

    public RunStub(File logFile) throws IOException {
        this();
        this.logFile = logFile;
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

    public File getRootDir() {
        return rootDir;
    }

    public void setPreviousBuild(Run previousBuild) {
        this.previousBuild = previousBuild;
    }

    private void setStartTime(long startTime) {
        Whitebox.setInternalState(this, "startTime", startTime);
    }

    public void setParameterValues(String paramName, String paramValue) {
        parameterValues = List.of(new ParameterValueStub(paramName, paramValue));
    }

    @Override
    public List<ParameterValue> getParameterValues() {
        return parameterValues;
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
    @Deprecated
    public @NonNull
    File getLogFile() {
        return logFile != null ? logFile : mock(File.class);
    }

    // skips serialization which is quite problematic
    @Override
    public synchronized void save() {
    }

    // skips security checking which is quite problematic
    @Override
    public void checkPermission(Permission permission) {
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
    public @NonNull
    List<Cause> getCauses() {
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
