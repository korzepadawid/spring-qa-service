package io.github.korzepadawid.springquoraclone.model;

import java.io.Serializable;

public enum VoteType implements Serializable {
  UP_VOTE(1), DOWN_VOTE(-1);

  private int direction;

  VoteType(int direction) {

  }

  public Integer getDirection() {
    return direction;
  }
}
