package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleDescriptorTest {

    @Test
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = new RuleDescriptor();

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Rule");
    }

    @Test
    public void doCheckMatchAtMost_OnValidNumber_ReturnsOK() {

        // given
        RuleDescriptor descriptor = new RuleDescriptor();

        // when
        FormValidation formValidation = descriptor.doCheckMatchAtMost(124 + "");

        // then
        assertThat(formValidation).isEqualTo(FormValidation.ok());
    }

    @Test
    public void doCheckMatchAtMost_OnInvalidNumber_ReturnsError() {

        // given
        RuleDescriptor descriptor = new RuleDescriptor();

        // when
        FormValidation formValidation = descriptor.doCheckMatchAtMost(124.5 + "");

        // then
        assertThat(formValidation.kind).isEqualTo(FormValidation.Kind.ERROR);
    }

    @Test
    public void doCheckMatchAtMost_OnOutOfRangeNumber_ReturnsError() {

        // given
        RuleDescriptor descriptor = new RuleDescriptor();

        // when
        FormValidation formValidation = descriptor.doCheckMatchAtMost(-124 + "");

        // then
        assertThat(formValidation.kind).isEqualTo(FormValidation.Kind.ERROR);
    }
}
