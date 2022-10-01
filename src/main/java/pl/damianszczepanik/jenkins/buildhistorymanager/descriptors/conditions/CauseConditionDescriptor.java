package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.CauseCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link CauseCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("Cause")
public class CauseConditionDescriptor extends Descriptor<Condition> {

    public CauseConditionDescriptor() {
        super(CauseCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Build cause";
    }
}
