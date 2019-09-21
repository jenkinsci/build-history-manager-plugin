package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RuleDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        RuleDescriptor descriptor = new RuleDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Rule");
    }
}
