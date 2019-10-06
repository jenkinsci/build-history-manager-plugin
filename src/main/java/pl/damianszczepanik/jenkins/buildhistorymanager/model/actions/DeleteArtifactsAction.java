package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Deletes the artifacts.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 * @see Run#deleteArtifacts()
 */
public class DeleteArtifactsAction extends Action {

    @DataBoundConstructor
    public DeleteArtifactsAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        run.deleteArtifacts();
    }
}
