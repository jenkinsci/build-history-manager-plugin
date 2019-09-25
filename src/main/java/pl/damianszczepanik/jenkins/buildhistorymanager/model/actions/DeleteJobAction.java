package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Deletes the job.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobAction extends Action {

    @DataBoundConstructor
    public DeleteJobAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        // forever, without any condition
        run.delete();
    }
}
