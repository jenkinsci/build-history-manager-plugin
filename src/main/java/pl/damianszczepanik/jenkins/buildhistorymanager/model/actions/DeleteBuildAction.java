package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Deletes the build.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see Run#delete()
 */
public class DeleteBuildAction extends Action {

    @DataBoundConstructor
    public DeleteBuildAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        // forever, without any condition
        run.delete();
    }
}
