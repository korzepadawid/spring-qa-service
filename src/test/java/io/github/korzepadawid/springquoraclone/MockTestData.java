package io.github.korzepadawid.springquoraclone;

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
}
