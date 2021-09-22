package io.github.korzepadawid.springquoraclone.controller;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;
import io.github.korzepadawid.springquoraclone.service.VoteService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VoteController {

  private final VoteService voteService;

  @PutMapping("/api/v1/answers/{answerId}/votes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createVote(@Valid @RequestBody VoteDto voteDto, @PathVariable Long answerId) {
    voteService.createVote(voteDto, answerId);
  }

  @DeleteMapping("/api/v1/answers/{answerId}/votes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteVoteById(@PathVariable Long answerId) {
    voteService.deleteVoteById(answerId);
  }

  @GetMapping("/api/v1/answers/{answerId}/votes/me")
  @ResponseStatus(HttpStatus.OK)
  public VoteDto findVoteByAnswerId(@PathVariable Long answerId) {
    return voteService.findVoteByAnswerId(answerId);
  }
}