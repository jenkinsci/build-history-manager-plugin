package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.MatchEveryJobCondition;

/**
 * Descriptor implementation needed to render UI for {@link MatchEveryJobCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class MatchEveryJobDescriptor extends Descriptor<Condition> {

    public MatchEveryJobDescriptor() {
        super(MatchEveryJobCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Match every job";
    }

}
