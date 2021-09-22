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

  @NotBlank(message = "Can't be blank")
  private String username;

  @NotBlank(message = "Can't be blank")
  private String password;
}
