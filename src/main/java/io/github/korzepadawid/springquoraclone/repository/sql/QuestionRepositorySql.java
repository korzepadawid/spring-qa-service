package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepositorySql extends QuestionRepository, JpaRepository<Question, Long> {

  @Override
  Optional<Question> findById(Long id);
}
