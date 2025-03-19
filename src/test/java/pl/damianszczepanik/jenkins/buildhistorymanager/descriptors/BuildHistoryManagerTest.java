package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Descriptor;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class BuildHistoryManagerTest {

    @Test
    void getDisplayName_ReturnsPluginName() {

        // given
        Descriptor descriptor = new BuildHistoryManagerDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build History Manager");
    }
}
