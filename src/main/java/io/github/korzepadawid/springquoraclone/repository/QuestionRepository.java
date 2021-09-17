package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import java.util.Optional;

public interface QuestionRepository {

  Question save(Question question);

  Optional<Question> findById(Long id);

  Optional<Question> findByIdAndAuthor(Long id, AppUser author);

  void delete(Question question);
}
