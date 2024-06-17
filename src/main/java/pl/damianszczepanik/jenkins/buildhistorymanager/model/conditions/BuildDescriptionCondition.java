package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.Util;
import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches builds that description is equal, contains or matches given pattern.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildDescriptionCondition extends Condition {

    /**
     * Matches method for the pattern
     */
    public enum MatchingMethodType {
        EQUALS,
        CONTAINS,
        MATCHES;
    }

    private String pattern;

    private MatchingMethodType matchingMethod = MatchingMethodType.EQUALS;

    @DataBoundConstructor
    public BuildDescriptionCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getPattern() {
        return pattern;
    }

    @DataBoundSetter
    public void setPattern(String pattern) {
        this.pattern = Util.fixNull(pattern);
    }

    public String getMatchingMethod() {
        return matchingMethod.name();
    }

    @DataBoundSetter
    public void setMatchingMethod(String matchingMethod) {
        this.matchingMethod = MatchingMethodType.valueOf(matchingMethod);
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        // by default build description is null
        String buildDescription = Util.fixNull(run.getDescription());

        switch (matchingMethod) {
            case CONTAINS:
                return buildDescription.contains(pattern);
            case MATCHES:
                return buildDescription.matches(pattern);
            case EQUALS:
            default: // any unspecified value should be fallback to default one -> equals
                return buildDescription.equals(pattern);
        }
    }
}
