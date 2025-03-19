package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class BuildResultConditionDescriptorTest {

    @Test
    void getDisplayName_ReturnsDescriptorName(JenkinsRule j) {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildResult");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build result");
    }
}
