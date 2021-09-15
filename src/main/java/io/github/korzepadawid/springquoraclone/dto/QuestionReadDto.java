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

  private Long id;
  private String title;
  private String description;
  private AppUserReadDto author;

  public QuestionReadDto(Question question) {
    BeanUtils.copyProperties(question, this, "author");
    if (question.getAnonymous()) {
      this.author = null;
    } else {
      this.author = new AppUserReadDto(question.getAuthor());
    }
  }
}
