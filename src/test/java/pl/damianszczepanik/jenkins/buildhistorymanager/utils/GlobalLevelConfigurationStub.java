package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import jenkins.model.Jenkins;
import org.mockito.MockedStatic;
import pl.damianszczepanik.jenkins.buildhistorymanager.GlobalLevelConfiguration;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class GlobalLevelConfigurationStub {

    public static GlobalLevelConfiguration instance() {

        try (MockedStatic<Jenkins> jenkins = mockStatic(Jenkins.class)) {
            jenkins.when(() -> Jenkins.get()).thenReturn(mock(Jenkins.class));

            return new GlobalLevelConfiguration();
        }
    }
}
