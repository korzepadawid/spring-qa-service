package io.github.korzepadawid.springquoraclone.model;

import java.io.Serializable;
import java.util.Arrays;

public enum VoteType implements Serializable {
  UPVOTE(1), DOWN_VOTE(-1),
  ;

  private int direction;

  VoteType(int direction) {

  }

  public static VoteType lookup(Integer direction) {
    return Arrays.stream(VoteType.values())
        .filter(value -> value.getDirection().equals(direction))
        .findAny()
        .orElseThrow(() -> new RuntimeException("Vote not found"));
  }

  public Integer getDirection() {
    return direction;
  }
}
