package pl.damianszczepanik.jenkins.buildhistorymanager.model;

/**
 * Stores configuration used by {@link Rule}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleConfiguration {

    /**
     * Indicates that there is no limitation about {@link #matchAtMost}.
     */
    public static final int MATCH_UNLIMITED = -1;

    private int matchAtMost;
    private boolean continueAfterMatch;

    RuleConfiguration() {
        this.matchAtMost = MATCH_UNLIMITED;
        this.continueAfterMatch = true;
    }

    public int getMatchAtMost() {
        return matchAtMost;
    }

    public void setMatchAtMost(int matchAtMost) {
        this.matchAtMost = matchAtMost;
    }

    public boolean isContinueAfterMatch() {
        return continueAfterMatch;
    }

    public void setContinueAfterMatch(boolean continueAfterMatch) {
        this.continueAfterMatch = continueAfterMatch;
    }
}
