package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;
import java.util.logging.Logger;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Deletes the build log file.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see Run#delete()
 */
public class DeleteLogFileAction extends Action {

    private static final Logger LOG = Logger.getLogger(DeleteLogFileAction.class.getName());

    @DataBoundConstructor
    public DeleteLogFileAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        if (run.getLogFile().exists()) {
            boolean wasDeleted = run.getLogFile().delete();
            if (!wasDeleted) {
                LOG.warning(String.format("Log file for build %d could not be deleted", run.getNumber()));
            }
            // ToDo: consider https://github.com/jenkinsci/delete-log-plugin/blob/master/src/main/java/jenkinsci/plugin/deletelog/LogDelete.java#L47-L51
            // for this and other actions
        }
    }
}
