package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import java.util.List;

public interface AnswerService {

  AnswerReadDto createAnswer(AnswerWriteDto answerWriteDto, Long questionId);

  List<AnswerReadDto> findAllQuestionAnswers(Long questionId);

  AnswerReadDto getAnswerById(Long answerId);

  void deleteAnswerById(Long answerId);

  void updateAnswerById(AnswerWriteDto answerWriteDto, Long answerId);
}
