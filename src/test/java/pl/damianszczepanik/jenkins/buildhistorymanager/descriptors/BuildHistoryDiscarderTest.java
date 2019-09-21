package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BuildHistoryDiscarderTest {

    @Test
    public void getDisplayName_ReturnsPluginName() {

        // given
        BuildHistoryDiscarderDescriptor descriptor = new BuildHistoryDiscarderDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build History Manager");
    }
}
