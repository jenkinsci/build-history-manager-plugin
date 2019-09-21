package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
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
}
