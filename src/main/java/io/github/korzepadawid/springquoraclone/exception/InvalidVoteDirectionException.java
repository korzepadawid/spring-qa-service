package io.github.korzepadawid.springquoraclone.exception;

public class InvalidVoteDirectionException extends BusinessLogicException {

  public InvalidVoteDirectionException() {
    super("Vote direction not found");
  }
}
