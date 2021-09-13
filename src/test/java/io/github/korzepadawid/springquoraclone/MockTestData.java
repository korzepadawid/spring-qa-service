package io.github.korzepadawid.springquoraclone;

import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.model.AppUser;

public abstract class MockTestData {

  public static AppUser returnAppUser() {
    return AppUser.builder()
        .id(1L)
        .username("bmurray")
        .email("bmurray@gmail.com")
        .password("ajkdfshjsdgfhasgdfja")
        .build();
  }

  public static AppUserWriteDto returnsAppUserWriteDto() {
    AppUser appUser = returnAppUser();
    return AppUserWriteDto.builder()
        .username(appUser.getUsername())
        .email(appUser.getEmail())
        .password(appUser.getPassword())
        .description(appUser.getDescription())
        .build();
  }
}
