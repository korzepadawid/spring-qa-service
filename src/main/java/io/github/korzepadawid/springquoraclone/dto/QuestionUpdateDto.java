package io.github.korzepadawid.springquoraclone.dto;

import javax.validation.constraints.Size;
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
public class QuestionUpdateDto {

  @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters.")
  private String title;

  @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters.")
  private String description;
}
