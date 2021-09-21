package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AnswerRepositorySql extends AnswerRepository, CrudRepository<Answer, Long> {

  @Query("select a from Answer a left join fetch a.votes v join fetch a.author a1 "
      + "where a.question = :question")
  List<Answer> findByQuestion(@Param("question") Question question);

  Optional<Answer> findByIdAndAuthor(Long id, AppUser author);
}
