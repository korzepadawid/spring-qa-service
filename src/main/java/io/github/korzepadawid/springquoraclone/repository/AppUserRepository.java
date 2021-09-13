package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import java.util.Optional;

public interface AppUserRepository {

  AppUser save(AppUser appUser);

  Optional<AppUser> findByUsernameOrEmailLike(String string);

  Optional<AppUser> findByUsernameOrEmail(String username, String email);
}
