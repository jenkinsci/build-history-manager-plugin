package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import hudson.FilePath;
import hudson.Util;
import hudson.model.Run;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches macro with given value.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TokenMacroCondition extends Condition {
    
    /**
     * Template that will be evaluated.
     */
    private String template;

    /**
     * Expected value of the evaluated {@link #template}.
     */
    private String value;

    // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    @DataBoundConstructor
    public TokenMacroCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getTemplate() {
        return template;
    }

    @DataBoundSetter
    public void setTemplate(String template) {
        this.template = Util.fixNull(template, this.template);
    }

    public String getValue() {
        return value;
    }

    @DataBoundSetter
    public void setValue(String value) {
        this.value = Util.fixNull(value, this.value);
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {

        try {
            File workspace = run.getRootDir();
            String evaluatedMacro = TokenMacro.expandAll(run, new FilePath(workspace), null, template);
            BuildHistoryManager.LOG.log(Level.INFO, () -> String.format("Evaluated macro '%s' to '%s'", template, evaluatedMacro));
            return Util.fixNull(value).equals(evaluatedMacro);

        } catch (InterruptedException | IOException | MacroEvaluationException e) {
            BuildHistoryManager.LOG.log(Level.WARNING, () -> String.format("Exception when processing template '%s' for build #%d: %s",
                    template, run.getNumber(), e.getMessage()));
            return false;
        }
    }
}
