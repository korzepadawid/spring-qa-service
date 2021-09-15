package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;

public interface QuestionService {

  QuestionReadDto createQuestion(QuestionWriteDto questionWriteDto);

  QuestionReadDto getQuestionById(Long id);
}
