package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import hudson.model.Run;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Deletes the artifacts.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobArtifactsAction extends Action {

    @DataBoundConstructor
    public DeleteJobArtifactsAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        run.deleteArtifacts();
    }
}
