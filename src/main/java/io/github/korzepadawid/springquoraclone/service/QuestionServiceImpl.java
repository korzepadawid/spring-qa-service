package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

  private final AuthService authService;
  private final QuestionRepository questionRepository;

  @Override
  public QuestionReadDto createQuestion(QuestionWriteDto questionWriteDto) {
    AppUser currentlyLoggedUser = authService.getCurrentlyLoggedUser();
    Question question = mapDtoToEntity(questionWriteDto);

    question.setAuthor(currentlyLoggedUser);

    Question savedQuestion = questionRepository.save(question);

    return new QuestionReadDto(savedQuestion);
  }

  private Question mapDtoToEntity(QuestionWriteDto questionWriteDto) {
    Question question = new Question();
    BeanUtils.copyProperties(questionWriteDto, question, "author", "answers", "dateAudit");
    return question;
  }
}
