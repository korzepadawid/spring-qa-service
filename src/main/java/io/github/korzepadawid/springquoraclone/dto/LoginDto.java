package io.github.korzepadawid.springquoraclone.dto;

import io.swagger.annotations.ApiModelProperty;
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
public class LoginDto {

  @ApiModelProperty(example = "your_username", required = true, notes = "You can provide either "
      + "an email or a username.")
  @NotBlank(message = "Can't be blank")
  private String username;

  @ApiModelProperty(example = "your_password", required = true)
  @NotBlank(message = "Can't be blank")
  private String password;
}
