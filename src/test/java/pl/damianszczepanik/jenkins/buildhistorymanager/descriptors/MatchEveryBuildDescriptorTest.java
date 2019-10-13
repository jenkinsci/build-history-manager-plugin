package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class MatchEveryBuildDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        MatchEveryBuildConditionDescriptor descriptor = new MatchEveryBuildConditionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Match every build");
    }
}
