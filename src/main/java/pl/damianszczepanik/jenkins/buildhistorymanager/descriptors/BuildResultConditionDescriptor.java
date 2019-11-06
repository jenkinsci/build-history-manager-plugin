package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildResultCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link BuildResultCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("BuildResult")
public class BuildResultConditionDescriptor extends Descriptor<Condition> {

    public BuildResultConditionDescriptor() {
        super(BuildResultCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Build result";
    }
}
