package io.github.korzepadawid.springquoraclone.controller;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;
import io.github.korzepadawid.springquoraclone.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(
    value = "Votes",
    tags = {"Votes"})
@RequiredArgsConstructor
@RestController
public class VoteController {

  private final VoteService voteService;

  @ApiOperation(
      value = "Puts vote for specific answer",
      notes =
          "You can vote once for a specific "
              + "answer. Your vote will be replaced if you've already put one.")
  @PutMapping("/api/v1/answers/{answerId}/votes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createVote(@Valid @RequestBody VoteDto voteDto, @PathVariable Long answerId) {
    voteService.createVote(voteDto, answerId);
  }

  @ApiOperation(value = "Removes your vote for specific answer")
  @DeleteMapping("/api/v1/answers/{answerId}/votes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteVoteById(@PathVariable Long answerId) {
    voteService.deleteVoteById(answerId);
  }

  @ApiOperation(value = "Checks your vote for specific answer")
  @GetMapping("/api/v1/answers/{answerId}/votes")
  @ResponseStatus(HttpStatus.OK)
  public VoteDto findVoteByAnswerId(@PathVariable Long answerId) {
    return voteService.findVoteByAnswerId(answerId);
  }
}
