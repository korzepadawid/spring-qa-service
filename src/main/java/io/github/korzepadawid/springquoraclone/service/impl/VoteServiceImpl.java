package io.github.korzepadawid.springquoraclone.service.impl;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;
import io.github.korzepadawid.springquoraclone.exception.AnswerNotFoundException;
import io.github.korzepadawid.springquoraclone.exception.VoteNotFoundException;
import io.github.korzepadawid.springquoraclone.model.Answer;
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
    Answer answer = getAnswerById(answerId);
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
  }

  @Transactional
  @Override
  public void removeVote(Long answerId) {
    Answer answer = getAnswerById(answerId);
    AppUser currentlyLogged = authService.getCurrentlyLoggedUser();
    voteRepository.findByAnswerAndAppUser(answer, currentlyLogged)
        .ifPresentOrElse(voteRepository::delete, () -> {
          throw new VoteNotFoundException(answerId, currentlyLogged.getUsername());
        });
  }

  @Transactional
  @Override
  public VoteDto checkVote(Long answerId) {
    Answer answer = getAnswerById(answerId);
    AppUser currentlyLogged = authService.getCurrentlyLoggedUser();
    return voteRepository.findByAnswerAndAppUser(answer, currentlyLogged)
        .map(VoteDto::new)
        .orElseThrow(() -> new VoteNotFoundException(answerId, currentlyLogged.getUsername()));
  }

  private Answer getAnswerById(Long answerId) {
    return answerRepository.findById(answerId)
        .orElseThrow(() -> new AnswerNotFoundException(answerId));
  }
}
