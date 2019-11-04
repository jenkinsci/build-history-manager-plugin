package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.MatchEveryBuildCondition;

/**
 * Descriptor implementation needed to render UI for {@link MatchEveryBuildCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("MatchEveryBuild")
public class MatchEveryBuildConditionDescriptor extends Descriptor<Condition> {

    public MatchEveryBuildConditionDescriptor() {
        super(MatchEveryBuildCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Match every build";
    }

}
