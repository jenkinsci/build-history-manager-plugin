package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildDescriptionCondition.MatchingMethodType;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class BuildDescriptionDescriptorTest {

    @Test
    void doFillMatchingMethodItems_ReturnsMethodItems(JenkinsRule j) {

        // given
        BuildDescriptionDescriptor descriptor = (BuildDescriptionDescriptor)
                SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildDescription");

        // when
        ListBoxModel listBoxModel = descriptor.doFillMatchingMethodItems();

        // then
        assertThat(listBoxModel.stream().map(option -> option.value))
                .containsExactly(
                        MatchingMethodType.EQUALS.name(),
                        MatchingMethodType.CONTAINS.name(),
                        MatchingMethodType.MATCHES.name());
    }

    @Test
    void getDisplayName_ReturnsDescriptorName(JenkinsRule j) {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildDescription");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build description");
    }
}
