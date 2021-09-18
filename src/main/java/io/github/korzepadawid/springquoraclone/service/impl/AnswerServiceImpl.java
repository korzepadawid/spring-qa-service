package io.github.korzepadawid.springquoraclone.service.impl;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.repository.AnswerRepository;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import io.github.korzepadawid.springquoraclone.service.AnswerService;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

  private final AuthService authService;
  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;

  @Transactional
  @Override
  public AnswerReadDto createAnswer(AnswerWriteDto answerWriteDto, Long questionId) {
    return questionRepository.findById(questionId)
        .map(question -> {
          AppUser currentlyLoggedUser = authService.getCurrentlyLoggedUser();
          Answer answer = mapDtoToEntity(answerWriteDto);
          assert answer != null;
          answer.setAuthor(currentlyLoggedUser);
          Answer savedAnswer = answerRepository.save(answer);
          question.addAnswer(savedAnswer);
          return new AnswerReadDto(savedAnswer);
        })
        .orElseThrow(() -> new QuestionNotFoundException(questionId));
  }

  @Transactional
  @Override
  public List<AnswerReadDto> findAllQuestionAnswers(Long questionId) {
    return questionRepository.findById(questionId)
        .map(question -> answerRepository.findByQuestion(question)
            .stream()
            .map(AnswerReadDto::new)
            .collect(Collectors.toList()))
        .orElseThrow(() -> new QuestionNotFoundException(questionId));
  }

  private Answer mapDtoToEntity(AnswerWriteDto answerWriteDto) {
    return Answer.builder()
        .text(answerWriteDto.getText())
        .build();
  }
}
