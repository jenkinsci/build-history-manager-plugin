package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

public class DeleteArtifactsWithPatternActionDescriptorTest {
    private DeleteArtifactsWithPatternActionDescriptor descriptor;

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Before
    public void setUp() {
        descriptor = new DeleteArtifactsWithPatternActionDescriptor();
    }

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "DeleteArtifactsWithPattern");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Delete artifacts with pattern");
    }
    
    @Test // testing getInclude method for code coverage
    public void testGetInclude() throws IOException, InterruptedException {

        // given
        String include = "includePattern";
        descriptor.setInclude(include);

        // when
        String result = descriptor.getInclude();

        // then
        Assert.assertEquals(include, result);
    }

    @Test
    public void testGetExclude() {
        // Given
        String exclude = "excludePattern";
        descriptor.setExclude(exclude);

        // When
        String result = descriptor.getExclude();

        // Then
        Assert.assertEquals(exclude, result);
    }
}
