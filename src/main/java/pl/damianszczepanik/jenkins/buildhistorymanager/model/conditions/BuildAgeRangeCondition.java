package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.util.Calendar;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches builds that age is between given days range. It calculates days only without checking build time.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildAgeRangeCondition extends Condition {

    private int minDaysAge;
    private int maxDaysAge;

    @DataBoundConstructor
    public BuildAgeRangeCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public int getMinDaysAge() {
        return minDaysAge;
    }

    @DataBoundSetter
    public void setMinDaysAge(int minDaysAge) {
        this.minDaysAge = minDaysAge;
    }

    public int getMaxDaysAge() {
        return maxDaysAge;
    }

    @DataBoundSetter
    public void setMaxDaysAge(int maxDaysAge) {
        this.maxDaysAge = maxDaysAge;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration, int buildPosition) {

        Calendar buildTime = Calendar.getInstance();
        buildTime.setTimeInMillis(run.getStartTimeInMillis() + run.getDuration());
        clearTime(buildTime);

        Calendar maxDays = Calendar.getInstance();
        maxDays.add(Calendar.DAY_OF_MONTH, -maxDaysAge);
        clearTime(maxDays);

        Calendar minDays = Calendar.getInstance();
        minDays.add(Calendar.DAY_OF_MONTH, -minDaysAge);
        clearTime(minDays);

        return buildTime.compareTo(minDays) <= 0 && buildTime.compareTo(maxDays) >= 0;
    }

    protected void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
