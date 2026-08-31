package pl.damianszczepanik.jenkins.buildhistorymanager.model;

/**
 * Decides which rules should be evaluated first, which skipped.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum PrecedenceMode {
    /**
     * Use only job level configuration, ignores others.
     * Default option, should be first on the list.
     */
    JOB_ONLY,
    /**
     * Use only global configuration, ignores others.
     */
    GLOBAL_ONLY,
    /**
     * Use global configuration, then job level configuration.
     */
    GLOBAL_THEN_JOB,
    /**
     * Use job level configuration, then global configuration.
     */
    JOB_THEN_GLOBAL
}
