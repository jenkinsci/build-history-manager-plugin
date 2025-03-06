package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteLogFileActionTest {

    @Test
    public void perform_OnMissingLogFile_SkipDeletion() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(false);

        // when
        action.perform(run);

        // then
        run.assertLogFileIsAvailable();
    }

    @Test
    public void perform_OnGetLogFileThrowsUnsupportedOperationException_CatchesExceptionAndDoesNothing() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub() {
            @Override
            public File getLogFile() {
                throw new UnsupportedOperationException("Operation not supported");
            }
        };

        // when
        action.perform(run);

        // then
        run.assertLogFileIsAvailable();
    }

    @Test
    public void perform_OnExistingLogFile_DeletesLogFile() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(true);

        // when
        action.perform(run);

        // then
        run.assertLogFileWasDeleted();
    }
}
