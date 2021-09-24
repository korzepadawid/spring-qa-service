package io.github.korzepadawid.springquoraclone.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
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
public class AppUserWriteDto {

  @ApiModelProperty(required = true, example = "email@example.com")
  @Email(message = "Must be valid")
  @NotBlank(message = "Can't be blank")
  @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters.")
  private String email;

  @ApiModelProperty(required = true, example = "user")
  @NotBlank(message = "Can't be blank")
  @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters.")
  private String username;

  @ApiModelProperty(required = true, example = "str0ngPas5!3", notes = "At least 8 characters, "
      + "mixture of both uppercase and lowercase letters, mixture of letters and numbers, inclusion"
      + " of at least one special character, e.g., ! @ # ? ]")
  @NotBlank(message = "Can't be blank.")
  @Size(max = 72, message = "Max password length is 72.")
  private String password;

  @ApiModelProperty(example = "DevOps Kubernetes Engineer at XYZ")
  @Size(max = 255, message = "Max length is 255.")
  private String description;
}
