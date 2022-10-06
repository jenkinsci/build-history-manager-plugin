package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Deletes the build log file.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see Run#delete()
 */
public class DeleteLogFileAction extends Action {

    @DataBoundConstructor
    public DeleteLogFileAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        File logFile = run.getLogFile();
        if (logFile.exists()) {
            Files.delete(logFile.toPath());
        }
    }
}
