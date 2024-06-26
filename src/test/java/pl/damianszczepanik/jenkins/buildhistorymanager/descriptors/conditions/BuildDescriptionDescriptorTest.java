package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildDescriptionCondition.MatchingMethodType;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildDescriptionDescriptorTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void doFillMatchingMethodItems_ReturnsMethodItems() {

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
    public void getDisplayName_ReturnsDescriptorName() {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "BuildDescription");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("Build description");
    }
}
