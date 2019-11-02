package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Descriptor;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobArtifactsActionDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = new DeleteArtifactsActionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Delete artifacts");
    }
}
