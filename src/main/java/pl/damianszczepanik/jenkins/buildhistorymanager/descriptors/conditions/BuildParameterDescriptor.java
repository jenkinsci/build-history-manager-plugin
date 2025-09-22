package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;
import pl.damianszczepanik.jenkins.buildhistorymanager.Messages;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.BuildParameterCondition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;

/**
 * Descriptor implementation needed to render UI for {@link BuildParameterCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("BuildParameter")
public class BuildParameterDescriptor extends Descriptor<Condition> {

    public BuildParameterDescriptor() {
        super(BuildParameterCondition.class);
    }

    @Override
    @NonNull
    public String getDisplayName() {
        return "Build parameter";
    }

    @POST
    public FormValidation doCheckParameterName(@QueryParameter String parameterName) {
        return isNotBlank(parameterName);
    }

    @POST
    public FormValidation doCheckParameterValue(@QueryParameter String parameterValue) {
        return isNotBlank(parameterValue);
    }

    @POST
    private static FormValidation isNotBlank(String value) {
        if (StringUtils.isNotBlank(value)) {
            return FormValidation.ok();
        }
        return FormValidation.error(Messages.isEmpty());
    }
}
