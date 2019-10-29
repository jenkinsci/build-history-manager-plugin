package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildResultConditionDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        BuildResultConditionDescriptor descriptor = new BuildResultConditionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build result");
    }
}
