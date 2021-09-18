package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.Question;
import java.util.List;

public interface AnswerRepository {

  Answer save(Answer answer);

  List<Answer> findByQuestion(Question question);
}
