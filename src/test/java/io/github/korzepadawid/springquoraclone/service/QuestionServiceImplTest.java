package io.github.korzepadawid.springquoraclone.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.exception.ResourceNotFoundException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private QuestionServiceImpl questionService;

  private static Long ID = 2L;

  @Test
  void shouldReturnSavedQuestionWithAuthorIdWhenNotAnonymousQuestion() {
    QuestionWriteDto questionWriteDto = MockTestData.returnsQuestionWriteDto(false);
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.save(any(Question.class)))
        .thenReturn(MockTestData.returnsQuestion(questionWriteDto.getAnonymous()));

    QuestionReadDto questionReadDto = questionService.createQuestion(questionWriteDto);

    assertThat(questionReadDto).isNotNull()
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

    assertThat(questionReadDto).isNotNull()
        .hasFieldOrPropertyWithValue("title", questionWriteDto.getTitle());
    assertThat(questionReadDto.getAuthor()).isNull();
  }

  @Test
  void shouldThrowQuestionNotFoundExceptionWhenQuestionDoesNotExist() {
    when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.getQuestionById(ID));

    assertThat(throwable)
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(ID.toString());
  }

  @Test
  void shouldReturnQuestionWhenQuestionExists() {
    Question question = MockTestData.returnsQuestion(true);
    question.setId(ID);
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
    when(questionRepository.findByIdAndAuthor(ID, appUser)).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.updateQuestionById(null, ID));

    assertThat(throwable)
        .isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldUpdateDescriptionWhenDescriptionNotNull() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.builder()
        .description("its a test, isn't it?")
        .build();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(ID, appUser)).thenReturn(Optional.of(question));

    questionService.updateQuestionById(questionUpdateDto, ID);

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
    when(questionRepository.findByIdAndAuthor(ID, appUser)).thenReturn(Optional.of(question));

    questionService.updateQuestionById(questionUpdateDto, ID);

    assertThat(question.getTitle()).isEqualTo(questionUpdateDto.getTitle());
  }

  @Test
  void shouldNotUpdateWhenUpdatesAreNull() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    final String title = question.getTitle();
    final String description = question.getDescription();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(ID, appUser)).thenReturn(Optional.of(question));

    questionService.updateQuestionById(null, ID);

    assertThat(question.getTitle()).isEqualTo(title);
    assertThat(question.getDescription()).isEqualTo(description);
  }

  @Test
  void shouldThrowQuestionNotFoundExceptionAndStopDeletingWhenQuestionDoesNotExist() {
    AppUser appUser = MockTestData.returnsAppUser();
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(ID, appUser)).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.deleteQuestionById(ID));

    assertThat(throwable)
        .isInstanceOf(QuestionNotFoundException.class);
  }

  @Test
  void shouldDeleteQuestionWhenQuestionExists() {
    AppUser appUser = MockTestData.returnsAppUser();
    Question question = MockTestData.returnsQuestion(true);
    when(authService.getCurrentlyLoggedUser()).thenReturn(appUser);
    when(questionRepository.findByIdAndAuthor(ID, appUser)).thenReturn(Optional.of(question));

    questionService.deleteQuestionById(ID);

    verify(questionRepository, times(1)).delete(question);
  }
}