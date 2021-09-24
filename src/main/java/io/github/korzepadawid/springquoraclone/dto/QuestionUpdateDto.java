package io.github.korzepadawid.springquoraclone.dto;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(example = "Do you like to travel?")
  @Size(min = 1, max = 255, message = "Must be between 1 and 255 characters.")
  private String title;

  @ApiModelProperty(example = "You can share your favourite travel destinations.")
  @Size(max = 255, message = "Max length is 255.")
  private String description;
}
