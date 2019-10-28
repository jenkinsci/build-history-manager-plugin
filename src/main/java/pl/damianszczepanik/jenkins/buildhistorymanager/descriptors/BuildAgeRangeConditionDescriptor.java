package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildAgeRangeCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link BuildAgeRangeCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class BuildAgeRangeConditionDescriptor extends Descriptor<Condition> {

    public BuildAgeRangeConditionDescriptor() {
        super(BuildAgeRangeCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Build age range";
    }
}
