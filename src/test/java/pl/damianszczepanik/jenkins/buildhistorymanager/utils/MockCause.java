package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import hudson.model.Cause;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MockCause extends Cause {

    private final String causeClass;

    public MockCause(String causeClass) {
        this.causeClass = causeClass;
    }

    @Override
    public String getShortDescription() {
        return causeClass;
    }

    @Override
    public String toString() {
        return causeClass;
    }
}
