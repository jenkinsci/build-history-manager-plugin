package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ChangeBuildDescriptionActionTest {

    @Test
    public void perform_DescriptionUpdate() throws IOException, InterruptedException {

        // given
        ChangeBuildDescriptionAction action = new ChangeBuildDescriptionAction();
        RunStub run = new RunStub();

        // when
        action.perform(run);

        // then
        assertThat(run.getDescription()).startsWith("[build-history-manager]");
    }
}
