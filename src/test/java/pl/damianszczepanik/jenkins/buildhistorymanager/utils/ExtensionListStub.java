package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import hudson.ExtensionList;
import jenkins.model.Jenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.GlobalLevelConfiguration;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.PrecedenceMode;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ExtensionListStub extends ExtensionList {

    private GlobalLevelConfiguration globalConfig;

    public ExtensionListStub() {
        this(Collections.emptyList(), PrecedenceMode.JOB_ONLY);
    }

    public ExtensionListStub(List<Rule> rules, PrecedenceMode precedenceMode) {
        super((Jenkins) null, GlobalLevelConfiguration.class);
        this.globalConfig = mockGlobalLevelConfiguration(rules, precedenceMode);
    }

    private GlobalLevelConfiguration mockGlobalLevelConfiguration(List<Rule> rules, PrecedenceMode precedenceMode) {
        GlobalLevelConfiguration globalLevelConfiguration = mock(GlobalLevelConfiguration.class);
        when(globalLevelConfiguration.getRules()).thenReturn(rules);
        when(globalLevelConfiguration.getPrecedenceMode()).thenReturn(precedenceMode.name());

        return globalLevelConfiguration;
    }

    @Override
    public GlobalLevelConfiguration get(Class type) {
        return globalConfig;
    }
}
