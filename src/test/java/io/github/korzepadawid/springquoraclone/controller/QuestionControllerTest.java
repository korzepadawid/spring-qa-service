package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.JsonMapper;
import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.exception.GlobalExceptionHandler;
import io.github.korzepadawid.springquoraclone.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

  private MockMvc mockMvc;

  @Mock
  private QuestionService questionService;

  @InjectMocks
  private QuestionController questionController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(questionController)
        .setControllerAdvice(GlobalExceptionHandler.class)
        .build();
  }

  @Test
  void shouldReturn401WhenUnauthorizedRequest() throws Exception {
    final String exceptionMessage = "You must be authorized!";
    QuestionWriteDto questionWriteDto = MockTestData.returnsQuestionWriteDto(true);
    when(questionService.createQuestion(any(QuestionWriteDto.class)))
        .thenThrow(new UsernameNotFoundException(exceptionMessage));

    mockMvc.perform(post(QuestionController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(questionWriteDto)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message", is(exceptionMessage)));
  }

  @Test
  void shouldReturn400BadRequestWhenValidationError() throws Exception {
    QuestionWriteDto questionWriteDto = QuestionWriteDto.builder()
        .anonymous(true)
        .title("")
        .build();

    mockMvc.perform(post(QuestionController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(questionWriteDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.title").isNotEmpty());
  }

  @Test
  void shouldReturnAnonymousQuestionWhenAnonymousIsTrue() throws Exception {
    QuestionWriteDto questionWriteDto = MockTestData.returnsQuestionWriteDto(true);
    QuestionReadDto questionReadDto = MockTestData
        .returnsQuestionReadDto(questionWriteDto.getAnonymous());
    when(questionService.createQuestion(any(QuestionWriteDto.class))).thenReturn(questionReadDto);

    mockMvc.perform(post(QuestionController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(questionWriteDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title", is(questionWriteDto.getTitle())));
  }
}