package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.Run;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Prepend to job description <pre>[build-history-manager]</pre> so it is easy to find
 * which builds should be affected before it really happens.<br>
 * <b>This action is recommended for debug and test conditions.</b>
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ChangeBuildDescriptionAction extends Action {

    private static final String PRE_DESCRIPTION = "[build-history-manager]\n";

    @DataBoundConstructor
    public ChangeBuildDescriptionAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        // null-safe concatenation
        String description = StringUtils.defaultString(run.getDescription());

        // do not append if already updated
        if (!description.startsWith(PRE_DESCRIPTION)) {
            run.setDescription(PRE_DESCRIPTION + description);
        }
    }
}
