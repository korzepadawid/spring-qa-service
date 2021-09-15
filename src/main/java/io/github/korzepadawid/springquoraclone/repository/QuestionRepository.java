package io.github.korzepadawid.springquoraclone.repository;

import io.github.korzepadawid.springquoraclone.model.Question;

public interface QuestionRepository {

  Question save(Question question);
}
