package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;

public interface VoteService {

  void createVote(VoteDto voteDto, Long answerId);

  void deleteVoteById(Long answerId);

  VoteDto findVoteByAnswerId(Long answerId);
}
