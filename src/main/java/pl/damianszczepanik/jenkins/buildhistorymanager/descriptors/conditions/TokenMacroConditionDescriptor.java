package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.TokenMacroCondition;

/**
 * Descriptor implementation needed to render UI for {@link TokenMacroCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("TokenMacro")
public class TokenMacroConditionDescriptor extends Descriptor<Condition> {

    public TokenMacroConditionDescriptor() {
        super(TokenMacroCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "Token Macro";
    }
}
