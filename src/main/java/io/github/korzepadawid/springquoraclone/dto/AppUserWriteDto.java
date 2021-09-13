package io.github.korzepadawid.springquoraclone.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AppUserWriteDto {

  @Email(message = "Email must be valid.")
  @NotBlank(message = "Email can't be blank.")
  @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
  private String email;

  @NotBlank(message = "Username can't be blank.")
  @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
  private String username;

  @NotBlank
  private String password;

  @Size(max = 255)
  private String description;
}
