package io.github.korzepadawid.springquoraclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AnswerController {

  @GetMapping("/api/v1/questions/{questionId}/answers")
  public void getAllAnswersForQuestion(@PathVariable Long questionId) {
  }

  @PostMapping("/api/v1/questions/{questionId}/answers")
  public void createAnswerForQuestion(@PathVariable Long questionId) {
  }

  @GetMapping("/api/v1/answers/{answerId}")
  public void getOneAnswer(@PathVariable Long answerId) {
  }

  @PatchMapping("/api/v1/answers/{answerId}")
  public void updateAnswer(@PathVariable Long answerId) {
  }

  @DeleteMapping("/api/v1/answers/{answerId}")
  public void deleteAnswer(@PathVariable Long answerId) {
  }
}
