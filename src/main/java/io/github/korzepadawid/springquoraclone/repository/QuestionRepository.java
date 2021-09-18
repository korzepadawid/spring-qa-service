package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface QuestionRepository {

  Question save(Question question);

  Optional<Question> findById(Long id);

  Optional<Question> findByIdAndAuthor(Long id, AppUser author);

  void delete(Question question);

  List<Question> findAllByKeyword(String keyword, Pageable pageable);
}
