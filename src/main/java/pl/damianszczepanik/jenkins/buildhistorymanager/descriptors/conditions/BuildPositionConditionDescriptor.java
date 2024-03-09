package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import org.jenkinsci.Symbol;

import hudson.Extension;
import hudson.model.Descriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildPositionCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
@Extension
@Symbol("BuildPosition")
public class BuildPositionConditionDescriptor extends Descriptor<Condition> {

    public BuildPositionConditionDescriptor() {
        super(BuildPositionCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Build Position";
    }
    
}
