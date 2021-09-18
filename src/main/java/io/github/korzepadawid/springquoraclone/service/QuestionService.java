package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import java.util.List;

public interface QuestionService {

  List<QuestionReadDto> findQuestions(String keyword, Integer page);

  QuestionReadDto createQuestion(QuestionWriteDto questionWriteDto);

  QuestionReadDto getQuestionById(Long id);

  void updateQuestionById(QuestionUpdateDto updates, Long id);

  void deleteQuestionById(Long id);
}
