package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class ChangeBuildDescriptionActionTest {

    private static final String PRE_DESCRIPTION = "[build-history-manager]\n";

    @Test
    void perform__UpdatedAgain() throws IOException, InterruptedException {

        // given
        ChangeBuildDescriptionAction action = new ChangeBuildDescriptionAction();
        RunStub run = new RunStub();

        final String oldDescription = "Yellow Duck";
        run.setDescription(oldDescription);

        // when
        action.perform(run);

        // then
        assertThat(run.getDescription()).startsWith(PRE_DESCRIPTION + oldDescription);
    }

    @Test
    void perform_OnUpdatedDescription_DoesNotUpdatedAgain() throws IOException, InterruptedException {

        // given
        ChangeBuildDescriptionAction action = new ChangeBuildDescriptionAction();
        RunStub run = new RunStub();

        final String oldDescription = "Yellow Duck";
        run.setDescription(PRE_DESCRIPTION + oldDescription);

        // when
        action.perform(run);

        // then
        assertThat(run.getDescription()).startsWith(PRE_DESCRIPTION + oldDescription);
    }
}
