package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.FilePath;
import hudson.Util;
import hudson.model.Run;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.kohsuke.stapler.DataBoundConstructor;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Matches macro with given value.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TokenMacroCondition extends Condition {

    private static final Logger LOG = Logger.getLogger(TokenMacroCondition.class.getName());

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
    public TokenMacroCondition(String template, String value) {
        this.template = Util.fixNull(template);
        this.value = Util.fixNull(value);
    }

    public String getTemplate() {
        return template;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {

        try {
            File workspace = run.getRootDir();
            String evaluatedMacro = TokenMacro.expandAll(run, new FilePath(workspace), null, template);
            LOG.log(Level.INFO, () -> String.format("Evaluated macro '%s' to '%s'", template, evaluatedMacro));
            return StringUtils.defaultString(value).equals(evaluatedMacro);

        } catch (InterruptedException | IOException | MacroEvaluationException e) {
            LOG.log(Level.WARNING, () ->String.format("Exception when processing template '%s' for build #%d: %s",
                    template, run.getNumber(), e.getMessage()));
            return false;
        }
    }
}
