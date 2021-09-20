package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepositorySql extends QuestionRepository, JpaRepository<Question, Long> {

  Optional<Question> findByIdAndAuthor(Long id, AppUser author);

  @Query("select q from Question q join fetch q.author a"
      + " where concat(lower(q.title) , lower(q.description) ) like  lower(concat('%', :keyword,'%')) ")
  List<Question> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
