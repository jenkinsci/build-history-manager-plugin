package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobActionDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        DeleteJobActionDescriptor descriptor = new DeleteJobActionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Delete job");
    }
}
