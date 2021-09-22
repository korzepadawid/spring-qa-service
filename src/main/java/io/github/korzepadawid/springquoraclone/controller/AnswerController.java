package io.github.korzepadawid.springquoraclone.controller;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import io.github.korzepadawid.springquoraclone.service.AnswerService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AnswerController {

  private final AnswerService answerService;

  @GetMapping("/api/v1/questions/{questionId}/answers")
  @ResponseStatus(HttpStatus.OK)
  public List<AnswerReadDto> findAllAnswersByQuestionId(@PathVariable Long questionId) {
    return answerService.findAllAnswersByQuestionId(questionId);
  }

  @PostMapping("/api/v1/questions/{questionId}/answers")
  @ResponseStatus(HttpStatus.CREATED)
  public AnswerReadDto createAnswer(@Valid @RequestBody AnswerWriteDto answerWriteDto,
      @PathVariable Long questionId) {
    return answerService.createAnswer(answerWriteDto, questionId);
  }

  @GetMapping("/api/v1/answers/{answerId}")
  @ResponseStatus(HttpStatus.OK)
  public AnswerReadDto findAnswerById(@PathVariable Long answerId) {
    return answerService.findAnswerById(answerId);
  }

  @PatchMapping("/api/v1/answers/{answerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateAnswerById(@Valid @RequestBody AnswerWriteDto answerWriteDto,
      @PathVariable Long answerId) {
    answerService.updateAnswerById(answerWriteDto, answerId);
  }

  @DeleteMapping("/api/v1/answers/{answerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAnswerById(@PathVariable Long answerId) {
    answerService.deleteAnswerById(answerId);
  }
}
