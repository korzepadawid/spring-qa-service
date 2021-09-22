package io.github.korzepadawid.springquoraclone.dto;

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

  @Email(message = "Must be valid")
  @NotBlank(message = "Can't be blank")
  @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters.")
  private String email;

  @NotBlank(message = "Can't be blank")
  @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters.")
  private String username;

  @NotBlank(message = "Can't be blank.")
  @Size(max = 72, message = "Max password length is 72.")
  private String password;

  @Size(max = 255, message = "Max length is 255.")
  private String description;
}
