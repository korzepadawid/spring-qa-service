package io.github.korzepadawid.springquoraclone.exception;

public abstract class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
