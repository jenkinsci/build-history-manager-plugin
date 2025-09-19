package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import hudson.Util;
import hudson.model.ParameterValue;
import hudson.model.Run;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches builds that build parameter value matches given pattern.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildParameterCondition extends Condition {

    private static final Logger LOG = Logger.getLogger(BuildParameterCondition.class.getName());

    private String parameterName;
    private String parameterValue;

    @DataBoundConstructor
    public BuildParameterCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getParameterName() {
        return parameterName;
    }

    @DataBoundSetter
    public void setParameterName(String parameterName) {
        this.parameterName = Util.fixNull(parameterName);
    }

    public String getParameterValue() {
        return parameterValue;
    }

    @DataBoundSetter
    public void setParameterValue(String parameterValue) {
        this.parameterValue = Util.fixNull(parameterValue);
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        if (StringUtils.isEmpty(parameterName)) {
            // should not happen for valid configuration
            LOG.log(Level.WARNING, () -> String.format("Parameter name is empty for job %s",
                    parameterName, run.getDisplayName()));
            return false;
        }
        if (StringUtils.isEmpty(parameterValue)) {
            // should not happen for valid configuration
            LOG.log(Level.WARNING, () -> String.format("Parameter value is empty for job %s",
                    parameterName, run.getDisplayName()));
            return false;
        }
        ParameterValue parameter = getParameter(run);
        if (parameter == null) {
            // this build does not have defined expected parameter
            LOG.log(Level.WARNING, () -> String.format("Parameter %s is not present for job %s",
                    parameterName, run.getDisplayName()));
            return false;
        } else {
            Object buildValue = parameter.getValue();
            if (buildValue == null) {
                return false;
            } else {
                Pattern pattern = Pattern.compile(this.parameterValue);
                return pattern.matcher(buildValue.toString()).matches();
            }
        }
    }

    ParameterValue getParameter(Run<?, ?> run) {
        Optional<ParameterValue> parameterValue = run.getParameterValues().stream()
                .filter(param -> param.getName().equals(parameterName)).findFirst();

        if (parameterValue.isEmpty()) {
            return null;
        } else {
            return parameterValue.get();
        }
    }
}
