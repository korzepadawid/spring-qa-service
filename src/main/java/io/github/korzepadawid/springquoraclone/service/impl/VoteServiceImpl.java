package io.github.korzepadawid.springquoraclone.service.impl;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;
import io.github.korzepadawid.springquoraclone.exception.AnswerNotFoundException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Vote;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import io.github.korzepadawid.springquoraclone.repository.VoteRepository;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import io.github.korzepadawid.springquoraclone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

  private final AnswerRepository answerRepository;
  private final VoteRepository voteRepository;
  private final AuthService authService;

  @Transactional
  @Override
  public void createVote(VoteDto voteDto, Long answerId) {
    answerRepository.findById(answerId)
        .ifPresentOrElse(answer -> {
          AppUser currentlyLogged = authService.getCurrentlyLoggedUser();
          voteRepository.findByAnswerAndAppUser(answer, currentlyLogged)
              .ifPresentOrElse(vote -> vote.setVoteType(voteDto.getVoteType()), () -> {
                Vote vote = Vote.builder()
                    .appUser(currentlyLogged)
                    .voteType(voteDto.getVoteType())
                    .answer(answer)
                    .build();
                voteRepository.save(vote);
              });
        }, () -> {
          throw new AnswerNotFoundException(answerId);
        });
  }

  @Override
  public void removeVote(Long answerId) {
  }

  @Override
  public VoteDto checkVote(Long answerId) {
    return null;
  }
}
