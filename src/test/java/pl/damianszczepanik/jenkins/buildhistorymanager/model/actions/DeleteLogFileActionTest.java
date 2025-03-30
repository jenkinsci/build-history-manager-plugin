package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class DeleteLogFileActionTest {

    @Test
    void perform_OnExistingLogFile_DeletesLogFile() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(RunStub.LogFileAvailability.PRESENT);

        // when
        action.perform(run);

        // then
        run.assertLogFileIsNotAvailable();
    }

    @Test
    void perform_OnMissingLogFile_SkipDeletion() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(RunStub.LogFileAvailability.ABSENT);

        // when
        action.perform(run);

        // then
        run.assertLogFileIsNotAvailable();
    }

    @Test
    void perform_OnGetLogFileThrowsUnsupportedOperationException_CatchesExceptionAndDoesNothing() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(RunStub.LogFileAvailability.PRESENT) {
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
    void perform_DeleteLogFileDoesNotWork_IgnoresFailure() throws IOException, InterruptedException {

        // given
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(RunStub.LogFileAvailability.PRESENT) {
            @Override
            public File getLogFile() {
                File logFile = mock(File.class);
                when(logFile.exists()).thenReturn(true);
                when(logFile.delete()).thenReturn(false);
                return logFile;
            }
        };

        // when
        action.perform(run);

        // then
        run.assertLogFileIsAvailable();
    }

}
