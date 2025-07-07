package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.structs.SymbolLookup;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@WithJenkins
class FileScannerConditionDescriptorTest {

    @Test
    void getDisplayName_ReturnsDescriptorName(JenkinsRule j) {

        // given
        Descriptor descriptor = SymbolLookup.get().findDescriptor(AbstractDescribableImpl.class, "FileScanner");

        // when
        String displayName = descriptor.getDisplayName();

        // then
        assertThat(displayName).isEqualTo("File scanner");
    }
}
