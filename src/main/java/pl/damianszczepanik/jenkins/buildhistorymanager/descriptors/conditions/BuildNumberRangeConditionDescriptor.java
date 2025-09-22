package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildNumberRangeCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link BuildNumberRangeCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("BuildNumberRange")
public class BuildNumberRangeConditionDescriptor extends Descriptor<Condition> {

    public BuildNumberRangeConditionDescriptor() {
        super(BuildNumberRangeCondition.class);
    }

    @Override
    @NonNull
    public String getDisplayName() {
        return "Build number range";
    }

}
