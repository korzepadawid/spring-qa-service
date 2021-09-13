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

  @Email(message = "Email must be valid.")
  @NotBlank(message = "Email can't be blank.")
  @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters.")
  private String email;

  @NotBlank(message = "Username can't be blank.")
  @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters.")
  private String username;

  @NotBlank(message = "Password can't be blank.")
  @Size(max = 72, message = "Max password length is 72.")
  private String password;

  @Size(max = 255, message = "Max description length is 255.")
  private String description;
}
