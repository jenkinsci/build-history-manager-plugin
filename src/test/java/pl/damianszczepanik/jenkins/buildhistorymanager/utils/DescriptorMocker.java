package pl.damianszczepanik.jenkins.buildhistorymanager.utils;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import org.mockito.ArgumentMatchers;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DescriptorMocker {

    public DescriptorMocker(Descriptor descriptor) {
        mockStatic(Jenkins.class);
        Jenkins jenkins = mock(Jenkins.class);
        when(Jenkins.get()).thenReturn(jenkins); // for new Jenkins
        when(Jenkins.getInstanceOrNull()).thenReturn(jenkins); // for old Jenkins
        when(jenkins.getDescriptorOrDie(ArgumentMatchers.any())).thenReturn(descriptor);
    }
}
