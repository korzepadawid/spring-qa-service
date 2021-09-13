package io.github.korzepadawid.springquoraclone.exception;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException() {
    super("User already exists.");
  }
}
