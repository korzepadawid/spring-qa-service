package io.github.korzepadawid.springquoraclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(QuestionController.BASE_URL)
public class QuestionController {

  public static final String BASE_URL = "/api/v1/questions";

  @GetMapping
  public String getAllQuestions(){
    return BASE_URL;
  }
}
