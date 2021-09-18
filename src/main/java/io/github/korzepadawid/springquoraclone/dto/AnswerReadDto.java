package io.github.korzepadawid.springquoraclone.dto;

import io.github.korzepadawid.springquoraclone.model.Answer;
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

  public AnswerReadDto(Answer answer) {
    BeanUtils.copyProperties(answer, this, "author");
    this.author = new AppUserReadDto(answer.getAuthor());
  }
}
