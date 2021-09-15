package io.github.korzepadawid.springquoraclone.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
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
    final Long id = 1L;
    when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> questionService.getQuestionById(id));

    assertThat(throwable)
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(id.toString());
  }

  @Test
  void shouldReturnQuestionWhenQuestionExists() {
    Question question = MockTestData.returnsQuestion(true);
    question.setId(1L);
    when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));

    QuestionReadDto questionReadDto = questionService.getQuestionById(question.getId());

    assertThat(questionReadDto)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", question.getId())
        .hasFieldOrPropertyWithValue("author", null);
  }
}