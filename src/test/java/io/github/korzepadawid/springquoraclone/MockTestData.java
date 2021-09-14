package io.github.korzepadawid.springquoraclone;

import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.model.AppUser;

public abstract class MockTestData {

  public static AppUser returnsAppUser() {
    return AppUser.builder()
        .id(1L)
        .username("bmurray")
        .email("bmurray@gmail.com")
        .password("ajkdfshjsdgfhasgdfja")
        .build();
  }

  public static AppUserWriteDto returnsAppUserWriteDto() {
    AppUser appUser = returnsAppUser();
    return AppUserWriteDto.builder()
        .username(appUser.getUsername())
        .email(appUser.getEmail())
        .password(appUser.getPassword())
        .description(appUser.getDescription())
        .build();
  }

  public static AppUserReadDto returnsAppUserReadDto() {
    return new AppUserReadDto(returnsAppUser());
  }

  public static LoginDto returnsLoginDto() {
    AppUser appUser = returnsAppUser();
    return new LoginDto(appUser.getUsername(), appUser.getPassword());
  }
}
