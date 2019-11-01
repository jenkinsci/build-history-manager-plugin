package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Descriptor;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildHistoryDiscarderTest {

    @Test
    public void getDisplayName_ReturnsPluginName() {

        // given
        Descriptor descriptor = new BuildHistoryDiscarderDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build History Manager");
    }
}
