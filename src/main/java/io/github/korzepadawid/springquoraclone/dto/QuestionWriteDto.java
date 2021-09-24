package io.github.korzepadawid.springquoraclone.dto;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(required = true, example = "Do you like to travel?")
  @NotBlank(message = "Title can't be blank")
  @Size(max = 255, message = "Max length is 255.")
  private String title;

  @ApiModelProperty(example = "You can share your favourite travel destinations.")
  @Size(max = 255, message = "Max length is 255.")
  private String description;

  @ApiModelProperty(notes = "Question author won't be displayed.")
  private Boolean anonymous = false;
}
