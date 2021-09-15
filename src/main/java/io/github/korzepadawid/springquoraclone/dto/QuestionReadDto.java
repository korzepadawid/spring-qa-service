package io.github.korzepadawid.springquoraclone.dto;

import io.github.korzepadawid.springquoraclone.model.Question;
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
public class QuestionReadDto {

  private String title;
  private String description;
  private Long authorId;

  public QuestionReadDto(Question question) {
    BeanUtils.copyProperties(question, this, "authorId");
    if(question.getAnonymous()){
      this.authorId = null;
    } else {
      this.authorId = question.getAuthor().getId();
    }
  }
}
