package io.github.korzepadawid.springquoraclone.exception;

public class VoteNotFoundException extends ResourceNotFoundException {

  public VoteNotFoundException(Long answerId, String username) {
    super(String.format("User %s hasn't voted for answer %d yet.", username, answerId));
  }
}
