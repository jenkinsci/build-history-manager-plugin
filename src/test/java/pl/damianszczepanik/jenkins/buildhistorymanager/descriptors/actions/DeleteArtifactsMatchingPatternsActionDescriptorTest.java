package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import static org.assertj.core.api.Assertions.assertThat;

import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

public class DeleteArtifactsMatchingPatternsActionDescriptorTest {
    private DeleteArtifactsMatchingPatternsActionDescriptor descriptor;

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Before
    public void setUp() {
        descriptor = new DeleteArtifactsMatchingPatternsActionDescriptor();
    }

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "DeleteArtifactsMatchingPatterns");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Delete artifacts matching patterns");
    }
}
