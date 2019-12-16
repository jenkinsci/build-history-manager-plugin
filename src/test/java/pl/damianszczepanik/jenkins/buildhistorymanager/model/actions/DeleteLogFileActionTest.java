package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.IOException;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteLogFileActionTest {

    @Test
    public void perform_OnExistingLogFile_DeletesLogFile() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub();
        run.setLogFile(true);

        // when
        action.perform(run);

        // then
        run.assertLogFileWasDeleted();
    }


    @Test
    public void perform_OnMissingLogFile_SkipDeletion() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub();

        // when
        action.perform(run);

        // then
        run.assertLogFileWasNotDeleted();
    }
}
