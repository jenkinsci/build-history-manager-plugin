package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.util.SortedMap;

import hudson.model.Job;
import hudson.model.Run;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public abstract class JobStub extends Job {

    public int times;

    public JobStub() {
        super(null, null);
    }

    @Override
    public boolean isBuildable() {
        return false;
    }

    @Override
    protected SortedMap _getRuns() {
        return null;
    }

    @Override
    protected void removeRun(Run run) {

    }
}
