package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.File;
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
        File logFile;
        try {
            logFile = run.getLogFile();
        } catch (UnsupportedOperationException e) {
            log(run.getParent().getFullDisplayName(), "accessing log as file is unsupported");
            return;
        }
        if (logFile.exists()) {
            if(!logFile.delete()) {
                log(run.getParent().getFullDisplayName(), "Cannot delete log file");
            }
        }
    }

    private static void log(String jobName, String message) {
        LOG.fine(String.format("[%s] %s", jobName, message));
    }
}
