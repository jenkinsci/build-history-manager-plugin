package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class DeleteArtifactsActionTest {

    @Test
    void perform_DeletesJob() throws IOException, InterruptedException {

        // given
        Action action = new DeleteArtifactsAction();
        RunStub run = new RunStub();

        // when
        action.perform(run);

        // then
        run.assertArtifactsWereDeleted();
    }
}
