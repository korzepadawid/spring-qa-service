package io.github.korzepadawid.springquoraclone.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
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
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private QuestionServiceImpl questionService;

  @Test
  void shouldReturnSavedQuestionWithAuthorIdWhenNotAnonymousQuestion() {
    QuestionWriteDto questionWriteDto = MockTestData.returnsQuestionWriteDto(false);
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.save(any(Question.class)))
        .thenReturn(MockTestData.returnsQuestion(questionWriteDto.getAnonymous()));

    QuestionReadDto questionReadDto = questionService.createQuestion(questionWriteDto);

    assertThat(questionReadDto)
        .isNotNull()
        .hasFieldOrPropertyWithValue("title", questionWriteDto.getTitle());
    assertThat(questionReadDto.getAuthor()).isNotNull();
  }

  @Test
  void shouldReturnSavedQuestionWithoutAuthorIdWhenAnonymousQuestion() {
    QuestionWriteDto questionWriteDto = MockTestData.returnsQuestionWriteDto(true);
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.save(any(Question.class)))
        .thenReturn(MockTestData.returnsQuestion(questionWriteDto.getAnonymous()));

    QuestionReadDto questionReadDto = questionService.createQuestion(questionWriteDto);

    assertThat(questionReadDto)
        .isNotNull()
        .hasFieldOrPropertyWithValue("title", questionWriteDto.getTitle())
        .hasFieldOrPropertyWithValue("author", null);
  }

  @Test
  void shouldThrowQuestionNotFoundExceptionWhenQuestionDoesNotExist() {
    when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.getQuestionById(MockTestData.ID));

    assertThat(throwable).isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldReturnQuestionWhenQuestionExists() {
    Question question = MockTestData.returnsQuestion(true);
    question.setId(MockTestData.ID);
    when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));

    QuestionReadDto questionReadDto = questionService.getQuestionById(question.getId());

    assertThat(questionReadDto)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", question.getId())
        .hasFieldOrPropertyWithValue("author", null);
  }

  @Test
  void shouldThrowQuestionNotFoundExceptionAndStopUpdatingWhenQuestionDoesNotExist() {
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(MockTestData.ID, appUser)).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.updateQuestionById(null, MockTestData.ID));

    assertThat(throwable).isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldUpdateDescriptionWhenDescriptionNotNull() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.builder()
        .description("its a test, isn't it?")
        .build();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(MockTestData.ID, appUser)).thenReturn(Optional.of(question));

    questionService.updateQuestionById(questionUpdateDto, MockTestData.ID);

    assertThat(question.getDescription()).isEqualTo(questionUpdateDto.getDescription());
  }

  @Test
  void shouldUpdateDescriptionWhenTitleNotNull() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.builder()
        .title("its a test, isn't it?")
        .build();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(MockTestData.ID, appUser)).thenReturn(Optional.of(question));

    questionService.updateQuestionById(questionUpdateDto, MockTestData.ID);

    assertThat(question.getTitle()).isEqualTo(questionUpdateDto.getTitle());
  }

  @Test
  void shouldNotUpdateWhenUpdatesAreNull() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    final String title = question.getTitle();
    final String description = question.getDescription();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(MockTestData.ID, appUser)).thenReturn(Optional.of(question));

    questionService.updateQuestionById(null, MockTestData.ID);

    assertThat(question)
        .isNotNull()
        .hasFieldOrPropertyWithValue("title", title)
        .hasFieldOrPropertyWithValue("description", description);
  }

  @Test
  void shouldThrowQuestionNotFoundExceptionAndStopDeletingWhenQuestionDoesNotExist() {
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(MockTestData.ID, appUser)).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.deleteQuestionById(MockTestData.ID));

    assertThat(throwable).isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldDeleteQuestionWhenQuestionExists() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(MockTestData.ID, appUser)).thenReturn(Optional.of(question));

    questionService.deleteQuestionById(MockTestData.ID);

    verify(questionRepository, times(1)).delete(question);
  }


  @Test
  void shouldReturnEmptyListWhenNoResults() {
    when(questionRepository.findAllByKeyword(anyString(), any(PageRequest.class)))
        .thenReturn(new ArrayList<>());

    List<QuestionReadDto> questionReadDtos = questionService.findQuestions("", 5);

    assertThat(questionReadDtos.size()).isEqualTo(0);
  }

  @Test
  void shouldReturnResultListWhenResults() {
    List<Question> questions = Arrays.asList(
        MockTestData.returnsQuestion(false),
        MockTestData.returnsQuestion(true),
        MockTestData.returnsQuestion(false)
    );
    when(questionRepository.findAllByKeyword(anyString(), any(PageRequest.class)))
        .thenReturn(questions);

    List<QuestionReadDto> questionReadDtos = questionService.findQuestions("", 1);

    assertThat(questionReadDtos.size()).isEqualTo(questions.size());
  }
}