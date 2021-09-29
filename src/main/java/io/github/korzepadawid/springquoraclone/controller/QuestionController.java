package io.github.korzepadawid.springquoraclone.controller;

import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(
    value = "Questions",
    tags = {"Questions"})
@RequiredArgsConstructor
@RestController
public class QuestionController {

  private final QuestionService questionService;

  @ApiOperation(
      value = "Finds all questions by keyword",
      notes = "You can specify the keyword, " + "otherwise, all questions will be matched.")
  @GetMapping("/api/v1/questions")
  @ResponseStatus(HttpStatus.OK)
  public List<QuestionReadDto> findAllQuestions(
      @RequestParam(name = "keyword", defaultValue = "") String keyword,
      @RequestParam(name = "page", defaultValue = "1") Integer page) {
    return questionService.findAllQuestions(keyword, page);
  }

  @ApiOperation(value = "Creates a new question", notes = "You can create an anonymous question.")
  @PostMapping("/api/v1/questions")
  @ResponseStatus(HttpStatus.CREATED)
  public QuestionReadDto createQuestion(@Valid @RequestBody QuestionWriteDto questionWriteDto) {
    return questionService.createQuestion(questionWriteDto);
  }

  @ApiOperation(value = "Finds single question by its id")
  @GetMapping("/api/v1/questions/{questionId}")
  @ResponseStatus(HttpStatus.OK)
  public QuestionReadDto findQuestionById(@PathVariable Long questionId) {
    return questionService.findQuestionById(questionId);
  }

  @ApiOperation(value = "Updates question by its id", notes = "You can only update your question.")
  @PatchMapping("/api/v1/questions/{questionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateQuestionById(
      @Valid @RequestBody QuestionUpdateDto questionUpdateDto, @PathVariable Long questionId) {
    questionService.updateQuestionById(questionUpdateDto, questionId);
  }

  @ApiOperation(value = "Deletes question by its id", notes = "You can only delete your question.")
  @DeleteMapping("/api/v1/questions/{questionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteQuestionById(@PathVariable Long questionId) {
    questionService.deleteQuestionById(questionId);
  }
}
