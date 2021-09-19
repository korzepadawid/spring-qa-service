package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepositorySql extends AnswerRepository, CrudRepository<Answer, Long> {

  List<Answer> findByQuestion(Question question);

  Optional<Answer> findByIdAndAuthor(Long id, AppUser author);
}
