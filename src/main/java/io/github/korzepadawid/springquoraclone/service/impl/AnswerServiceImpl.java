package io.github.korzepadawid.springquoraclone.service.impl;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import io.github.korzepadawid.springquoraclone.exception.AnswerNotFoundException;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.model.Answer;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
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
    Question question =
        questionRepository
            .findById(questionId)
            .orElseThrow(() -> new QuestionNotFoundException(questionId));
    Answer answer = mapDtoToEntity(answerWriteDto, authService.getCurrentlyLoggedUser(), question);
    return new AnswerReadDto(answerRepository.save(answer));
  }

  @Transactional(readOnly = true)
  @Override
  public List<AnswerReadDto> findAllAnswersByQuestionId(Long questionId) {
    return questionRepository
        .findById(questionId)
        .map(
            question ->
                answerRepository.findByQuestion(question).stream()
                    .map(AnswerReadDto::new)
                    .collect(Collectors.toList()))
        .orElseThrow(() -> new QuestionNotFoundException(questionId));
  }

  @Transactional(readOnly = true)
  @Override
  public AnswerReadDto findAnswerById(Long answerId) {
    return answerRepository
        .findById(answerId)
        .map(AnswerReadDto::new)
        .orElseThrow(() -> new AnswerNotFoundException(answerId));
  }

  @Transactional
  @Override
  public void deleteAnswerById(Long answerId) {
    Answer answer = findAnswerByIdForCurrentUser(answerId);
    answerRepository.delete(answer);
  }

  @Transactional
  @Override
  public void updateAnswerById(AnswerWriteDto answerWriteDto, Long answerId) {
    Answer answer = findAnswerByIdForCurrentUser(answerId);
    if (answerWriteDto != null && answerWriteDto.getText() != null) {
      answer.setText(answerWriteDto.getText());
    }
  }

  private Answer mapDtoToEntity(AnswerWriteDto answerWriteDto, AppUser appUser, Question question) {
    return Answer.builder()
        .text(answerWriteDto.getText())
        .author(appUser)
        .question(question)
        .build();
  }

  private Answer findAnswerByIdForCurrentUser(Long answerId) {
    return answerRepository
        .findByIdAndAuthor(answerId, authService.getCurrentlyLoggedUser())
        .orElseThrow(() -> new AnswerNotFoundException(answerId));
  }
}
