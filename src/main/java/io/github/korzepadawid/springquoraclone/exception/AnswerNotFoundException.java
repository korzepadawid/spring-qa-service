package io.github.korzepadawid.springquoraclone.exception;

public class AnswerNotFoundException extends ResourceNotFoundException {

  public AnswerNotFoundException(Long id) {
    super(String.format("Answer with id %d not found.", id));
  }
}
