package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.Util;
import hudson.model.Cause;
import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches builds for which class name of {@link Cause} matches given class.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class CauseCondition extends Condition {

    private String causeClass;

    @DataBoundConstructor
    public CauseCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getCauseClass() {
        return causeClass;
    }

    @DataBoundSetter
    public void setCauseClass(String causeClass) {
        this.causeClass = Util.fixNull(causeClass);
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        for (Cause cause : run.getCauses()) {
            // use contains() method to avoid problem with class name
            // for causes which are often inner class and name contains $ character
            if (cause.toString().contains(causeClass)) {
                return true;
            }
        }
        return false;
    }
}
