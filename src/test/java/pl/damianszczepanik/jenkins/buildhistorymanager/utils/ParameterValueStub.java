package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import hudson.model.ParameterValue;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ParameterValueStub extends ParameterValue {

    private String value;

    public ParameterValueStub(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
