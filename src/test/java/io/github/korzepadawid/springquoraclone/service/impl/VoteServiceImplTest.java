package io.github.korzepadawid.springquoraclone.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.exception.AnswerNotFoundException;
import io.github.korzepadawid.springquoraclone.exception.VoteNotFoundException;
import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Vote;
import io.github.korzepadawid.springquoraclone.model.VoteType;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import io.github.korzepadawid.springquoraclone.repository.VoteRepository;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {

  @Mock
  private AuthService authService;

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private VoteRepository voteRepository;

  @InjectMocks
  private VoteServiceImpl voteService;

  @Test
  void shouldThrowAnswerNotFoundExceptionAndStopVotingWhenAnswerDoesNotExist() {
    when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> voteService.createVote(MockTestData.returnsVoteDto(
        VoteType.DOWN_VOTE), MockTestData.ID));

    assertThat(throwable).isInstanceOf(AnswerNotFoundException.class);
    verifyNoInteractions(authService);
    verifyNoInteractions(voteRepository);
  }

  @Test
  void shouldUpdateVoteWhenVoteAlreadyExists() {
    Answer answer = MockTestData.returnsAnswer();
    Vote vote = MockTestData.returnsVote(VoteType.UP_VOTE);
    AppUser appUser = MockTestData.returnsAppUser();
    when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(voteRepository.findByAnswerAndAppUser(any(Answer.class), any(AppUser.class)))
        .thenReturn(Optional.of(vote));

    voteService.createVote(MockTestData.returnsVoteDto(VoteType.DOWN_VOTE), MockTestData.ID);

    assertThat(vote.getVoteType()).isEqualTo(VoteType.DOWN_VOTE);
    assertThat(vote.getAppUser()).isNotNull();
  }

  @Test
  void shouldCreateNewVoteWhenVoteDoesNotExist() {
    Answer answer = MockTestData.returnsAnswer();
    Vote vote = MockTestData.returnsVote(VoteType.UP_VOTE);
    AppUser appUser = MockTestData.returnsAppUser();
    when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(voteRepository.findByAnswerAndAppUser(any(Answer.class), any(AppUser.class)))
        .thenReturn(Optional.empty());

    voteService.createVote(MockTestData.returnsVoteDto(VoteType.UP_VOTE), MockTestData.ID);

    assertThat(vote.getVoteType()).isEqualTo(VoteType.UP_VOTE);
    assertThat(vote.getAppUser()).isNotNull();
  }

  @Test
  void shouldThrowAnswerNotFoundExceptionAndStopRemovingWhenAnswerDoesNotExist() {
    when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> voteService.removeVote(MockTestData.ID));

    assertThat(throwable).isInstanceOf(AnswerNotFoundException.class);
    verifyNoInteractions(voteRepository);
  }

  @Test
  void shouldThrowVoteNotFoundExceptionAndStopRemovingWhenVoteDoesNotExist() {
    Answer answer = MockTestData.returnsAnswer();
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
    when(voteRepository.findByAnswerAndAppUser(any(Answer.class), any(AppUser.class)))
        .thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> voteService.removeVote(MockTestData.ID));

    assertThat(throwable).isInstanceOf(VoteNotFoundException.class);
  }

  @Test
  void shouldRemoveVoteWhenVoteAndAnswerExist() {
    Answer answer = MockTestData.returnsAnswer();
    AppUser appUser = MockTestData.returnsAppUser();
    Vote vote = MockTestData.returnsVote(VoteType.DOWN_VOTE);
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
    when(voteRepository.findByAnswerAndAppUser(any(Answer.class), any(AppUser.class)))
        .thenReturn(Optional.of(vote));

    voteService.removeVote(MockTestData.ID);

    verify(voteRepository, times(1)).delete(any(Vote.class));
  }
}