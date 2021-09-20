package io.github.korzepadawid.springquoraclone.exception;

public abstract class BusinessLogicException extends RuntimeException {

  public BusinessLogicException(String message) {
    super(message);
  }
}
