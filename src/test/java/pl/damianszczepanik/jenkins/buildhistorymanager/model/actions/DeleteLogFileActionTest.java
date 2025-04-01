package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class DeleteLogFileActionTest {

    @Test
    void perform_OnExistingLogFile_DeletesLogFile() throws IOException, InterruptedException {
        // given
        File logFile = mock(File.class);
        Path logFilePath = mock(Path.class);
        when(logFile.exists()).thenReturn(true);
        when(logFile.toPath()).thenReturn(logFilePath);
        
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(logFile);

        
        try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {
            // when
            action.perform(run);
            // then
            mockFiles.verify(() -> Files.delete(logFilePath), times(1));
        }
    }

    @Test
    void perform_OnMissingLogFile_SkipDeletion() throws IOException, InterruptedException {

        // given
        File logFile = mock(File.class);
        Path logFilePath = mock(Path.class);
        when(logFile.exists()).thenReturn(false);
        when(logFile.toPath()).thenReturn(logFilePath);
        
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(logFile);

        try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {
            // when
            action.perform(run); 
            // then
            mockFiles.verify(() -> Files.delete(logFilePath), times(0));
        }

    }

    @Test
    void perform_OnGetLogFileThrowsUnsupportedOperationException_CatchesExceptionAndDoesNothing() throws IOException, InterruptedException {
        // given
        final boolean[] exceptionWasThrown = {false};
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub() {
            @NonNull
            @Override
            @Deprecated
            public File getLogFile() {
                exceptionWasThrown[0] = true;
                throw new UnsupportedOperationException("Operation not supported");
            }
        };

        // when
        action.perform(run);

        // then
        assertThat(exceptionWasThrown[0]).isTrue();
    }

    @Test
    void perform_DeleteLogFileDoesNotWork_Throws() throws IOException {
        // given
        Path logFilePath = mock(Path.class);
        File logFile = mock(File.class);
        when(logFile.exists()).thenReturn(true);
        when(logFile.toPath()).thenReturn(logFilePath);
        
        Action action = new DeleteLogFileAction();
        RunStub run = new RunStub(logFile);
                
        try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {
            mockFiles.when(() -> Files.delete(logFilePath)).thenThrow(new IOException("File deletion failed"));

            // when / then
            assertThrows(IOException.class, () -> action.perform(run));
        }
    }

}
