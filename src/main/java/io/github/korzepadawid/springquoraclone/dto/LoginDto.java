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
public class LoginDto {

  @NotBlank(message = "Username can't be blank")
  private String username;

  @NotBlank(message = "Password can't be blank")
  private String password;
}
