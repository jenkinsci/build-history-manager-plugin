package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteBuildActionDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        DeleteBuildActionDescriptor descriptor = new DeleteBuildActionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Delete build");
    }
}
