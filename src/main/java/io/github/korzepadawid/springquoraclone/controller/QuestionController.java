package io.github.korzepadawid.springquoraclone.controller;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.service.QuestionService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(QuestionController.BASE_URL)
public class QuestionController {

  public static final String BASE_URL = "/api/v1/questions";

  private final QuestionService questionService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<QuestionReadDto> getAllQuestions(
      @RequestParam(name = "keyword", defaultValue = "") String keyword,
      @RequestParam(name = "page", defaultValue = "1") Integer page) {
    return questionService.findQuestions(keyword, page);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public QuestionReadDto createQuestion(@Valid @RequestBody QuestionWriteDto questionWriteDto) {
    return questionService.createQuestion(questionWriteDto);
  }

  @GetMapping("/{questionId}")
  @ResponseStatus(HttpStatus.OK)
  public QuestionReadDto getQuestionById(@PathVariable Long questionId) {
    return questionService.getQuestionById(questionId);
  }

  @PatchMapping("/{questionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateQuestionById(@Valid @RequestBody QuestionUpdateDto questionUpdateDto,
      @PathVariable Long questionId) {
    questionService.updateQuestionById(questionUpdateDto, questionId);
  }

  @DeleteMapping("/{questionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteQuestionById(@PathVariable Long questionId) {
    questionService.deleteQuestionById(questionId);
  }
}
