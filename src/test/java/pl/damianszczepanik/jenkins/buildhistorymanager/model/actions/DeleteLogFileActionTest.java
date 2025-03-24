package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class DeleteLogFileActionTest {

    @Test
    void perform_OnMissingLogFile_SkipDeletion() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub();

        // when
        action.perform(run);

        // then
        run.assertLogFileWasNotDeleted();
    }
}
