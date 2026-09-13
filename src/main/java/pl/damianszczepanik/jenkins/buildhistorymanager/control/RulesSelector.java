package pl.damianszczepanik.jenkins.buildhistorymanager.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.damianszczepanik.jenkins.buildhistorymanager.model.PrecedenceMode;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Takes global and job roles and decides which should be executed.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RulesSelector {

    private final PrecedenceMode precedenceMode;

    private List<Rule> globalRules = Collections.emptyList();
    private List<Rule> jobRules = Collections.emptyList();

    public RulesSelector(PrecedenceMode precedenceMode) {
        this.precedenceMode = precedenceMode;
    }

    public void setGlobalRules(List<Rule> globalRules) {
        this.globalRules = globalRules;
    }

    public void setJobRules(List<Rule> jobRules) {
        this.jobRules = jobRules;
    }

    public List<Rule> selectRolesToExecute() {
        switch (precedenceMode) {
            case JOB_ONLY:
                return jobRules;
            case GLOBAL_ONLY:
                return globalRules;
            case GLOBAL_THEN_JOB:
                return joinArrays(globalRules, jobRules);
            case JOB_THEN_GLOBAL:
                return joinArrays(jobRules, globalRules);
            default:
                throw new IllegalArgumentException("Unsupported precedence mode: " + precedenceMode);
        }
    }

    private List<Rule> joinArrays(List<Rule>... rules) {
        List<Rule> allRules = new ArrayList<>();
        for (List<Rule> rule : rules) {
            allRules.addAll(rule);
        }
        return allRules;
    }
}
