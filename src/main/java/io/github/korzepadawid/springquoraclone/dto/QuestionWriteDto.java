package io.github.korzepadawid.springquoraclone.dto;

import javax.validation.constraints.NotBlank;
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
public class QuestionWriteDto {

  @NotBlank(message = "Title can't be blank")
  @Size(max = 255, message = "Max length is 255.")
  private String title;

  @Size(max = 255, message = "Max length is 255.")
  private String description;

  private Boolean anonymous = false;
}
