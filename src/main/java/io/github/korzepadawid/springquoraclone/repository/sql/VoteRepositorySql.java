package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Vote;
import io.github.korzepadawid.springquoraclone.repository.VoteRepository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepositorySql extends VoteRepository, CrudRepository<Vote, Long> {

  Optional<Vote> findByAnswerAndAppUser(Answer answer, AppUser appUser);
}
