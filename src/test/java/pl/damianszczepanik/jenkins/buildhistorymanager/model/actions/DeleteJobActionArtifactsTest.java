package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobActionArtifactsTest {

    @Test
    public void perform_DeletesJob() throws IOException, InterruptedException {

        // given
        Action action = new DeleteArtifactsAction();
        RunStub run = new DeleteArtifactsRun();

        // when
        action.perform(run);

        // then
        assertThat(run.times).isOne();
    }
}

class DeleteArtifactsRun extends RunStub {

    public DeleteArtifactsRun() throws IOException {
    }

    @Override
    public void deleteArtifacts() {
        times++;
    }
}