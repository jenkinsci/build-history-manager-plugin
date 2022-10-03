package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import hudson.model.Result;
import hudson.model.Run;
import org.junit.Test;
import pl.damianszczepanik.jenkins.buildhistorymanager.utils.RunStub;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class BuildResultConditionTest {

    @Test
    public void setMatchSuccess_Sets_MatchSuccess() {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        boolean matchSuccess = true;

        // when
        condition.setMatchSuccess(matchSuccess);

        // then
        assertThat(condition.getMatchSuccess()).isEqualTo(matchSuccess);
    }


    @Test
    public void setMatchUnstable_Sets_MatchUnstable() {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        boolean matchUnstable = true;

        // when
        condition.setMatchUnstable(matchUnstable);

        // then
        assertThat(condition.getMatchUnstable()).isEqualTo(matchUnstable);
    }


    @Test
    public void setMatchFailure_Sets_MatchFailure() {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        boolean matchFailure = true;

        // when
        condition.setMatchFailure(matchFailure);

        // then
        assertThat(condition.getMatchFailure()).isEqualTo(matchFailure);
    }


    @Test
    public void setMatchAborted_Sets_MatchAborted() {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        boolean matchAborted = true;

        // when
        condition.setMatchAborted(matchAborted);

        // then
        assertThat(condition.getMatchAborted()).isEqualTo(matchAborted);
    }

    @Test
    public void match_OnMatchSuccessAndResultSuccess_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchSuccess(true);
        Run<?, ?> run = new RunStub(Result.SUCCESS);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void match_OnNotMatchSuccessAndResultSuccess_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchSuccess(false);
        Run<?, ?> run = new RunStub(Result.SUCCESS);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void match_OnMatchSuccessAndResultNotSuccess_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchSuccess(true);
        Run<?, ?> run = new RunStub(Result.FAILURE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }


    @Test
    public void match_OnMatchUnstableAndResultUnstable_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchUnstable(true);
        Run<?, ?> run = new RunStub(Result.UNSTABLE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void match_OnNotMatchUnstableAndResultUnstable_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchUnstable(false);
        Run<?, ?> run = new RunStub(Result.UNSTABLE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void match_OnMatchUnstableAndResultNotUnstable_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchUnstable(true);
        Run<?, ?> run = new RunStub(Result.FAILURE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }


    @Test
    public void match_OnMatchFailureAndResultFailure_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchFailure(true);
        Run<?, ?> run = new RunStub(Result.FAILURE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void match_OnNotMatchFailureAndResultFailure_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchFailure(false);
        Run<?, ?> run = new RunStub(Result.FAILURE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void match_OnMatchFailureAndResultNotFailure_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchFailure(true);
        Run<?, ?> run = new RunStub(Result.SUCCESS);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }


    @Test
    public void match_OnMatchAbortedAndResultAborted_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchAborted(true);
        Run<?, ?> run = new RunStub(Result.ABORTED);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void match_OnNotMatchAbortedAndResultAborted_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchAborted(false);
        Run<?, ?> run = new RunStub(Result.ABORTED);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void match_OnMatchAbortedAndResultNotAborted_ReturnsTrue() throws IOException {

        // given
        BuildResultCondition condition = new BuildResultCondition();
        condition.setMatchAborted(true);
        Run<?, ?> run = new RunStub(Result.FAILURE);

        // when
        boolean matched = condition.matches(run, null);

        // then
        assertThat(matched).isFalse();
    }
}
