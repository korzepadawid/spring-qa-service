package io.github.korzepadawid.springquoraclone.dto;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerReadDto {

  private Long id;
  private String text;
  private AppUserReadDto author;
  private Integer upVotes = 0;
  private Integer downVotes = 0;

  public AnswerReadDto(Answer answer) {
    BeanUtils.copyProperties(answer, this, "author", "upVotes", "downVotes");
    this.author = new AppUserReadDto(answer.getAuthor());
    answer.getVotes().forEach(vote -> {
      this.upVotes =
          vote.getVoteType().equals(VoteType.UP_VOTE) ? this.upVotes + 1 : this.upVotes;
      this.downVotes =
          vote.getVoteType().equals(VoteType.DOWN_VOTE) ? this.downVotes + 1 : this.downVotes;
    });
  }
}
