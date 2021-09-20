package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Vote;
import java.util.Optional;

public interface VoteRepository {

  Optional<Vote> findByAnswerAndAppUser(Answer answer, AppUser appUser);

  Vote save(Vote vote);
}
