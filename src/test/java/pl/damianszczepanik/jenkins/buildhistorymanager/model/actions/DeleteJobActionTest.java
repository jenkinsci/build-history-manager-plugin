package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobActionTest {

    class DeleteRun extends RunStub {

        public DeleteRun() throws IOException {
        }

        @Override
        public void delete() {
            times++;
        }
    }

    @Test
    public void perform_DeletesJob() throws IOException, InterruptedException {

        // given
        Action action = new DeleteBuildAction();
        RunStub run = new DeleteRun();

        // when
        action.perform(run);

        // then
        assertThat(run.times).isOne();
    }
}
