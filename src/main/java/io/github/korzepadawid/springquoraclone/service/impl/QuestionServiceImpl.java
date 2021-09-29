package io.github.korzepadawid.springquoraclone.service.impl;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import io.github.korzepadawid.springquoraclone.service.QuestionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

  private final AuthService authService;
  private final QuestionRepository questionRepository;

  public static final Integer MIN_PAGE = 1;
  public static final Integer MAX_PAGE_LIMIT = 5;

  @Override
  public List<QuestionReadDto> findAllQuestions(String keyword, Integer page) {
    int parsedPage = page < 1 ? MIN_PAGE : page;
    return questionRepository
        .findAllByKeyword(keyword, PageRequest.of(parsedPage - 1, MAX_PAGE_LIMIT))
        .stream()
        .map(QuestionReadDto::new)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public QuestionReadDto createQuestion(QuestionWriteDto questionWriteDto) {
    Question question = mapDtoToEntity(questionWriteDto, authService.getCurrentlyLoggedUser());
    return new QuestionReadDto(questionRepository.save(question));
  }

  @Transactional(readOnly = true)
  @Override
  public QuestionReadDto findQuestionById(Long id) {
    return questionRepository
        .findById(id)
        .map(QuestionReadDto::new)
        .orElseThrow(() -> new QuestionNotFoundException(id));
  }

  @Transactional
  @Override
  public void updateQuestionById(QuestionUpdateDto updates, Long id) {
    Question question = findQuestionByIdForCurrentUser(id);
    if (updates != null) {
      if (updates.getDescription() != null) {
        question.setDescription(updates.getDescription());
      }
      if (updates.getTitle() != null) {
        question.setTitle(updates.getTitle());
      }
    }
  }

  @Transactional
  @Override
  public void deleteQuestionById(Long id) {
    Question question = findQuestionByIdForCurrentUser(id);
    questionRepository.delete(question);
  }

  private Question mapDtoToEntity(QuestionWriteDto questionWriteDto, AppUser appUser) {
    Question question = new Question();
    BeanUtils.copyProperties(questionWriteDto, question, "author", "answers", "dateAudit");
    question.setAuthor(appUser);
    return question;
  }

  private Question findQuestionByIdForCurrentUser(Long id) {
    return questionRepository
        .findByIdAndAuthor(id, authService.getCurrentlyLoggedUser())
        .orElseThrow(() -> new QuestionNotFoundException(id));
  }
}
