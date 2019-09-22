package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.JobStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class DeleteJobActionTest {

    class DeleteJob extends JobStub {

        @Override
        public void delete() {
            times++;
        }
    }

    @Test
    public void perform_DeletesJob() throws IOException, InterruptedException {

        // given
        DeleteJobAction action = new DeleteJobAction();
        JobStub job = new DeleteJob();

        // when
        action.perform(job);

        // then
        assertThat(job.times).isOne();
    }

}
