package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;

public interface VoteService {

  void createVote(VoteDto voteDto, Long answerId);

  void removeVote(Long answerId);

  VoteDto checkVote(Long answerId);
}
