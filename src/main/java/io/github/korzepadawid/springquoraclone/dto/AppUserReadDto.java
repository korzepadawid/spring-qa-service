package io.github.korzepadawid.springquoraclone.dto;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserReadDto {

  private Long id;
  private String email;
  private String username;
  private String description;

  public AppUserReadDto(AppUser appUser) {
    BeanUtils.copyProperties(appUser, this);
  }
}
