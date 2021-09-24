package io.github.korzepadawid.springquoraclone.dto;

import io.github.korzepadawid.springquoraclone.model.Vote;
import io.github.korzepadawid.springquoraclone.model.VoteType;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

  @ApiModelProperty(required = true, example = "DOWN_VOTE")
  @NotNull(message = "Can't be null.")
  private VoteType voteType;

  public VoteDto(Vote vote) {
    this.voteType = vote.getVoteType();
  }
}
