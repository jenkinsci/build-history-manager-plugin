package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.mockito.Mockito.mock;

import hudson.ExtensionList;
import jenkins.model.Jenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.GlobalLevelConfiguration;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ExtensionListStub extends ExtensionList {

    private GlobalLevelConfiguration globalConfig;

    public ExtensionListStub() {
        this(mock(GlobalLevelConfiguration.class));
    }

    public ExtensionListStub(GlobalLevelConfiguration globalConfig) {
        super((Jenkins) null, GlobalLevelConfiguration.class);
        this.globalConfig = globalConfig;
    }

    @Override
    public GlobalLevelConfiguration get(Class type) {
        return globalConfig;
    }
}
