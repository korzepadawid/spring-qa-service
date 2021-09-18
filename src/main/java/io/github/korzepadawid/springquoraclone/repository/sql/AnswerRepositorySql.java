package io.github.korzepadawid.springquoraclone.repository.sql;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepositorySql extends AnswerRepository, JpaRepository<Answer, Long> {

}
