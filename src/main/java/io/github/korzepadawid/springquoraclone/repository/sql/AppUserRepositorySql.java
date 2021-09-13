package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AppUserRepositorySql extends CrudRepository<AppUser, Long> {

  @Query("select a from AppUser a where lower(a.username) = lower(:string) or "
      + "lower(a.email) = lower(:string) ")
  Optional<AppUser> findByUsernameOrEmail(@Param("string") String string);
}
