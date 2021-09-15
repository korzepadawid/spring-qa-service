package io.github.korzepadawid.springquoraclone.exception;

public class QuestionNotFoundException extends ResourceNotFoundException {

  public QuestionNotFoundException(Long questionId) {
    super(String.format("Question with id %d not found", questionId));
  }
}
