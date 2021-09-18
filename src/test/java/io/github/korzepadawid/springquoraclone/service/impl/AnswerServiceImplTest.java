package io.github.korzepadawid.springquoraclone.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private AnswerServiceImpl answerService;

  @Test
  void shouldThrowQuestionNotFoundAndStopAnsweringExceptionWhenQuestionDoesNotExist() {
    when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> answerService.createAnswer(null, MockTestData.ID));

    assertThat(throwable).isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldStopAnsweringQuestionWhenUserDoesNotExist() {
    Question question = MockTestData.returnsQuestion(false);
    when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
    when(authService.getCurrentlyLoggedUser()).thenThrow(UsernameNotFoundException.class);

    Throwable throwable = catchThrowable(() -> answerService.createAnswer(null, MockTestData.ID));

    assertThat(throwable).isInstanceOf(UsernameNotFoundException.class);
    verifyNoInteractions(answerRepository);
  }

  @Test
  void shouldReturnCreatedAnswerWhenQuestionAndUserExist() {
    Question question = MockTestData.returnsQuestion(false);
    AppUser currentlyLogged = MockTestData.returnsAppUser();
    Answer answer = MockTestData.returnsAnswer();
    when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
    when(authService.getCurrentlyLoggedUser()).thenReturn(currentlyLogged);
    when(answerRepository.save(any(Answer.class))).thenReturn(answer);

    AnswerReadDto answerReadDto = answerService
        .createAnswer(MockTestData.returnsAnswerWriteDto(), MockTestData.ID);

    assertThat(answerReadDto)
        .isNotNull()
        .hasFieldOrPropertyWithValue("text", answer.getText())
        .hasFieldOrPropertyWithValue("author.id", currentlyLogged.getId());
    assertThat(question.getAnswers().size()).isEqualTo(1);
    assertThat(answer.getQuestion()).isNotNull();
  }

  @Test
  void shouldThrowQuestionNotFoundExceptionAndStopListingAnswersWhenQuestionDoesNotExist() {
    when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(
        () -> answerService.findAllQuestionAnswers(MockTestData.ID));

    assertThat(throwable).isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldReturnEmptyListWhenZeroResults() {
    Question question = MockTestData.returnsQuestion(true);
    when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
    when(answerRepository.findByQuestion(any(Question.class))).thenReturn(new ArrayList<>());

    List<AnswerReadDto> allQuestionAnswers = answerService.findAllQuestionAnswers(MockTestData.ID);

    assertThat(allQuestionAnswers.size()).isEqualTo(0);
  }

  @Test
  void shouldReturnResultsWhenThereAreMatches() {
    List<Answer> answers = Arrays.asList(
        MockTestData.returnsAnswer(),
        MockTestData.returnsAnswer()
    );
    Question question = MockTestData.returnsQuestion(true);
    when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
    when(answerRepository.findByQuestion(any(Question.class))).thenReturn(answers);

    List<AnswerReadDto> allQuestionAnswers = answerService.findAllQuestionAnswers(MockTestData.ID);

    assertThat(allQuestionAnswers.size()).isEqualTo(answers.size());
  }
}