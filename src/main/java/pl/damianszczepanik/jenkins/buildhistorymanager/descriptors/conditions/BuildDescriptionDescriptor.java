package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.Messages;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildDescriptionCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildDescriptionCondition.MatchingMethodType;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link BuildDescriptionCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("BuildDescription")
public class BuildDescriptionDescriptor extends Descriptor<Condition> {

    public BuildDescriptionDescriptor() {
        super(BuildDescriptionCondition.class);
    }

    // names must refer to the field name
    public ListBoxModel doFillMatchingMethodItems() {
        return new ListBoxModel(
                // default option should be listed first
                new ListBoxModel.Option(Messages.buildDescription_matchingMethodType_EQUALS(), MatchingMethodType.EQUALS.name()),
                new ListBoxModel.Option(Messages.buildDescription_matchingMethodType_CONTAINS(), MatchingMethodType.CONTAINS.name()),
                new ListBoxModel.Option(Messages.buildDescription_matchingMethodType_MATCHES(), MatchingMethodType.MATCHES.name())
        );
    }

    @Override
    public String getDisplayName() {
        return "Build description";
    }
}
