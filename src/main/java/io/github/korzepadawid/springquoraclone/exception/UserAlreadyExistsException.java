package io.github.korzepadawid.springquoraclone.exception;

public class UserAlreadyExistsException extends BusinessLogicException {

  public UserAlreadyExistsException() {
    super("User already exists.");
  }
}
