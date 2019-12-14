package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Descriptor;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchEveryBuildConditionDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = new MatchEveryBuildConditionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Match every build");
    }
}
