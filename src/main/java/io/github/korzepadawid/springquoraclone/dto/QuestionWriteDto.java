package io.github.korzepadawid.springquoraclone.dto;

import javax.validation.constraints.NotBlank;
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
public class QuestionWriteDto {

  @NotBlank(message = "Title can't be blank")
  private String title;

  private String description;

  private Boolean anonymous = false;
}
