package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildAgeRangeConditionDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        BuildAgeRangeConditionDescriptor descriptor = new BuildAgeRangeConditionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build age range");
    }
}
