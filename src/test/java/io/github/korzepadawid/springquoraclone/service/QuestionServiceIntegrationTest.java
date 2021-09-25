package io.github.korzepadawid.springquoraclone.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import io.github.korzepadawid.springquoraclone.util.AbstractContainerBaseTest;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class QuestionServiceIntegrationTest extends AbstractContainerBaseTest {

  public static final String USERNAME = "bmurray";

  @Autowired
  private QuestionService questionService;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private AppUserRepository appUserRepository;

  @Test
  @Transactional
  @WithMockUser(QuestionServiceIntegrationTest.USERNAME)
  void shouldUpdateQuestionWhenQuestionBelongsToCurrentUser() {
    AppUser appUser = createTestUser(QuestionServiceIntegrationTest.USERNAME);
    Question question = MockTestData.returnsQuestion(false);
    question.setAuthor(appUser);
    Question baseQuestion = questionRepository.save(question);
    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.builder()
        .title("NEW TITLE IS HERE")
        .build();

    questionService.updateQuestionById(questionUpdateDto, baseQuestion.getId());

    Question resultQuestion = questionRepository.findById(baseQuestion.getId())
        .orElseThrow(RuntimeException::new);
    assertThat(resultQuestion.getTitle()).isEqualTo(questionUpdateDto.getTitle());
    assertThat(resultQuestion.getId()).isEqualTo(baseQuestion.getId());
  }

  @Test
  @Transactional
  @WithMockUser("otherUser")
  void shouldNotUpdateQuestionWhenQuestionDoesNotBelongToCurrentUser() {
    createTestUser("otherUser");
    AppUser appUser = createTestUser(QuestionServiceIntegrationTest.USERNAME);
    Question question = MockTestData.returnsQuestion(false);
    question.setAuthor(appUser);
    Question savedQuestion = questionRepository.save(question);

    Throwable throwable = catchThrowable(
        () -> questionService.updateQuestionById(QuestionUpdateDto.builder()
            .title("NEW TITLE IS HERE")
            .build(), savedQuestion.getId()));

    assertThat(throwable).isInstanceOf(QuestionNotFoundException.class);
  }

  AppUser createTestUser(String username) {
    AppUser appUser = MockTestData.returnsAppUser();
    appUser.setUsername(username);
    appUser.setEmail(username + "@email.com");
    return appUserRepository.save(appUser);
  }
}