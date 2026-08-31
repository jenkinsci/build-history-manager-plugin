package pl.damianszczepanik.jenkins.buildhistorymanager;

import java.util.Collections;
import java.util.List;

import hudson.Extension;
import hudson.Util;
import hudson.util.ListBoxModel;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.verb.POST;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.PrecedenceMode;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.Rule;

/**
 * Global configuration for Build History Manager plugin.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public class GlobalLevelConfiguration extends GlobalConfiguration {

    private List<Rule> rules = Collections.emptyList();
    // stored in config XML file as String but defined here as enum
    private PrecedenceMode precedenceMode = PrecedenceMode.JOB_ONLY;

    @DataBoundConstructor
    public GlobalLevelConfiguration() {
        super.load();
    }

    @Override
    public boolean configure(StaplerRequest2 req, JSONObject formData) throws FormException {
        // list must be bind additionally because when the list is cleared
        // then setRules() method is not invoked and previous values persists incorrectly
        rules = req.bindJSONToList(Rule.class, formData.get("rules"));
        req.bindJSON(this, formData);
        save();

        return super.configure(req, formData);
    }

    @DataBoundSetter
    public void setRules(List<Rule> rules) {
        this.rules = Util.fixNull(rules);
    }

    public List<Rule> getRules() {
        return rules;
    }

    @DataBoundSetter
    public void setPrecedenceMode(String precedenceMode) {
        this.precedenceMode = PrecedenceMode.valueOf(precedenceMode);
    }

    public String getPrecedenceMode() {
        return precedenceMode.name();
    }

    // names must refer to the field name
    @POST
    public ListBoxModel doFillPrecedenceModeItems() {
        return new ListBoxModel(
                // default option should be listed first
                new ListBoxModel.Option(Messages.configuration_precedenceMode_JOB_ONLY(), PrecedenceMode.JOB_ONLY.name()),
                new ListBoxModel.Option(Messages.configuration_precedenceMode_GLOBAL_ONLY(), PrecedenceMode.GLOBAL_ONLY.name()),
                new ListBoxModel.Option(Messages.configuration_precedenceMode_GLOBAL_THEN_JOB(), PrecedenceMode.GLOBAL_THEN_JOB.name()),
                new ListBoxModel.Option(Messages.configuration_precedenceMode_JOB_THEN_GLOBAL(), PrecedenceMode.JOB_THEN_GLOBAL.name())
        );
    }
}
