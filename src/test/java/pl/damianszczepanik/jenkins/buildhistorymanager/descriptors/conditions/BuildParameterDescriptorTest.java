package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class BuildParameterDescriptorTest {

    @Test
    void doCheckParameterName_ValidatesEmptyString(JenkinsRule j) {

        // given
        BuildParameterDescriptor descriptor = (BuildParameterDescriptor)
                SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildParameter");
        String name = "";

        // when
        FormValidation validation = descriptor.doCheckParameterName(name);

        // then
        assertThat(validation.kind).isEqualTo(FormValidation.Kind.ERROR);
    }

    @Test
    void doCheckParameterValue_ValidatesEmptyString(JenkinsRule j) {

        // given
        BuildParameterDescriptor descriptor = (BuildParameterDescriptor)
                SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildParameter");
        String value = "";

        // when
        FormValidation validation = descriptor.doCheckParameterValue(value);

        // then
        assertThat(validation.kind).isEqualTo(FormValidation.Kind.ERROR);
    }

    @Test
    void doCheckParameterValue_ValidatesNoEmptyString(JenkinsRule j) {

        // given
        BuildParameterDescriptor descriptor = (BuildParameterDescriptor)
                SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildParameter");
        String value = "develop";

        // when
        FormValidation validation = descriptor.doCheckParameterValue(value);

        // then
        assertThat(validation.kind).isEqualTo(FormValidation.Kind.OK);
    }

    @Test
    void getDisplayName_ReturnsDescriptorName(JenkinsRule j) {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildParameter");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build parameter");
    }
}
