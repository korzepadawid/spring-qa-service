package io.github.korzepadawid.springquoraclone.util;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;

public abstract class MockTestData {

  public static final Long ID = 2L;

  public static AppUser returnsAppUser() {
    return AppUser.builder()
        .id(1L)
        .username("bmurray")
        .email("bmurray@gmail.com")
        .password("ajkdfshjsdgfhasgdfja")
        .build();
  }

  public static AppUserWriteDto returnsAppUserWriteDto() {
    AppUser appUser = returnsAppUser();
    return AppUserWriteDto.builder()
        .username(appUser.getUsername())
        .email(appUser.getEmail())
        .password(appUser.getPassword())
        .description(appUser.getDescription())
        .build();
  }

  public static AppUserReadDto returnsAppUserReadDto() {
    return new AppUserReadDto(returnsAppUser());
  }

  public static LoginDto returnsLoginDto() {
    AppUser appUser = returnsAppUser();
    return new LoginDto(appUser.getUsername(), appUser.getPassword());
  }

  public static Question returnsQuestion(boolean anonymous) {
    return Question.builder()
        .title("What are the most viewed questions on Quora?")
        .description("I've been wondering about this for months.")
        .anonymous(anonymous)
        .author(returnsAppUser())
        .build();
  }

  public static QuestionWriteDto returnsQuestionWriteDto(boolean anonymous) {
    Question question = returnsQuestion(anonymous);
    return QuestionWriteDto.builder()
        .title(question.getTitle())
        .description(question.getDescription())
        .anonymous(question.getAnonymous())
        .build();
  }

  public static QuestionReadDto returnsQuestionReadDto(boolean anonymous) {
    return new QuestionReadDto(returnsQuestion(anonymous));
  }

  public static Answer returnsAnswer() {
    return Answer.builder()
        .author(returnsAppUser())
        .question(returnsQuestion(true))
        .id(ID)
        .text("I don't know.")
        .build();
  }

  public static AnswerWriteDto returnsAnswerWriteDto() {
    Answer answer = returnsAnswer();
    return AnswerWriteDto.builder()
        .text(answer.getText())
        .build();
  }

  public static AnswerReadDto returnsAnswerReadDto() {
    return new AnswerReadDto(returnsAnswer());
  }
}
