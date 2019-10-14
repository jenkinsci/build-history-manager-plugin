package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildNumberRangeCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link BuildNumberRangeCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class BuildNumberRangeConditionDescriptor extends Descriptor<Condition> {

    public BuildNumberRangeConditionDescriptor() {
        super(BuildNumberRangeCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Build number range";
    }

}
