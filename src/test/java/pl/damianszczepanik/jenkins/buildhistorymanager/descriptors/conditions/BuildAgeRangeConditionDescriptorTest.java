package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildAgeRangeConditionDescriptorTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildAgeRange");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build age range");
    }
}
