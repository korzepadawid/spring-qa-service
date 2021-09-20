package io.github.korzepadawid.springquoraclone.dto;

import io.github.korzepadawid.springquoraclone.model.VoteType;
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

  private VoteType voteType;
}
