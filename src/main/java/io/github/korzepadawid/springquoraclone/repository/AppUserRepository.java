package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import java.util.Optional;

public interface AppUserRepository {

  AppUser save(AppUser appUser);

  Optional<AppUser> findByUsernameOrEmail(String string);
}
