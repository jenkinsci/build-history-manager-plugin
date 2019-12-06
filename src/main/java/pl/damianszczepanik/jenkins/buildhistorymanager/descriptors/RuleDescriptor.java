package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.QueryParameter;
import pl.damianszczepanik.jenkins.buildhistorymanager.Messages;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Descriptor implementation needed to render UI for {@link Rule}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class RuleDescriptor extends Descriptor<Rule> {

    public RuleDescriptor() {
        super(Rule.class);
    }

    @Override
    public String getDisplayName() {
        return "Rule";
    }

    public FormValidation doCheckMatchAtMost(@QueryParameter String matchAtMost) {
        return isValidInteger(matchAtMost);
    }

    private static FormValidation isValidInteger(String value) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < -1) {
                return FormValidation.error(Messages.role_notValidMatchAtMost());
            }
        } catch (NumberFormatException e) {
            return FormValidation.error(Messages.notInteger());
        }
        return FormValidation.ok();
    }
}
