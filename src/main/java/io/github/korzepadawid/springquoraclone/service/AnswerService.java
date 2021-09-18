package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;

public interface AnswerService {

  AnswerReadDto createAnswer(AnswerWriteDto answerWriteDto, Long questionId);
}
