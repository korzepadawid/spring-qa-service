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
public class AnswerWriteDto {

  @NotBlank(message = "Can't be blank")
  @Size(min = 1, max = 255, message = "Must be between 1 and 255 characters")
  private String text;
}
