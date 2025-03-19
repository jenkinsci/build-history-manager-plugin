package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Descriptor;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class BuildNumberRangeConditionDescriptorTest {

    @Test
    void getDisplayName_ReturnsDescriptorName(JenkinsRule j) {

        // given
        Descriptor descriptor = new BuildNumberRangeConditionDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build number range");
    }
}
