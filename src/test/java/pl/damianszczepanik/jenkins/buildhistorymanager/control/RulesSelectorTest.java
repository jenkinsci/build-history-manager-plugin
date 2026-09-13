package pl.damianszczepanik.jenkins.buildhistorymanager.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.PrecedenceMode;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class RulesSelectorTest {

    private final static Rule GLOBAL_FIRST = new NamedRule("GLOBAL_FIRST");
    private final static Rule GLOBAL_SECOND = new NamedRule("GLOBAL_SECOND");

    private final static Rule JOB_FIRST = new NamedRule("JOB_FIRST");
    private final static Rule JOB_SECOND = new NamedRule("JOB_SECOND");

    @Test
    void selectRolesToExecute_ForJOB_ONLY_ReturnsOnlyJobRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.JOB_ONLY);
        rulesSelector.setJobRules(List.of(JOB_FIRST, JOB_SECOND));
        rulesSelector.setGlobalRules(List.of(GLOBAL_FIRST, GLOBAL_SECOND));

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).containsExactly(JOB_FIRST, JOB_SECOND);
    }

    @Test
    void selectRolesToExecute_ForGLOBAL_ONLY_ReturnsOnlyGlobalRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.GLOBAL_ONLY);
        rulesSelector.setJobRules(List.of(JOB_FIRST, JOB_SECOND));
        rulesSelector.setGlobalRules(List.of(GLOBAL_FIRST, GLOBAL_SECOND));

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).containsExactly(GLOBAL_FIRST, GLOBAL_SECOND);
    }

    @Test
    void selectRolesToExecute_ForGLOBAL_THEN_JOB_ReturnsGlobalWithJobRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.GLOBAL_THEN_JOB);
        rulesSelector.setJobRules(List.of(JOB_FIRST, JOB_SECOND));
        rulesSelector.setGlobalRules(List.of(GLOBAL_FIRST, GLOBAL_SECOND));

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).containsExactly(GLOBAL_FIRST, GLOBAL_SECOND, JOB_FIRST, JOB_SECOND);
    }


    @Test
    void selectRolesToExecute_ForJOB_THEN_GLOBAL_ReturnsJobWithGlobalRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.JOB_THEN_GLOBAL);
        rulesSelector.setJobRules(List.of(JOB_FIRST, JOB_SECOND));
        rulesSelector.setGlobalRules(List.of(GLOBAL_FIRST, GLOBAL_SECOND));

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).containsExactly(JOB_FIRST, JOB_SECOND, GLOBAL_FIRST, GLOBAL_SECOND);
    }

    @Test
    void selectRolesToExecute_ForNoJobRules_ReturnsNoRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.JOB_ONLY);
        rulesSelector.setGlobalRules(List.of(GLOBAL_FIRST, GLOBAL_SECOND));

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).isEmpty();
    }

    @Test
    void selectRolesToExecute_ForNoGlobalRules_ReturnsNoRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.GLOBAL_ONLY);
        rulesSelector.setJobRules(List.of(JOB_FIRST, JOB_SECOND));

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).isEmpty();
    }

    @Test
    void selectRolesToExecute_ForNoRules_ReturnsNoRules() {

        // given
        RulesSelector rulesSelector = new RulesSelector(PrecedenceMode.GLOBAL_THEN_JOB);

        // when
        List<Rule> selectedRules = rulesSelector.selectRolesToExecute();

        // then
        assertThat(selectedRules).isEmpty();
    }

    private static class NamedRule extends Rule {
        private final String ruleName;

        NamedRule(String ruleName) {
            super(null, null);
            this.ruleName = ruleName;
        }

        public String toString() {
            return ruleName;
        }
    }
}
