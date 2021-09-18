package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import io.github.korzepadawid.springquoraclone.exception.GlobalExceptionHandler;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.service.AnswerService;
import io.github.korzepadawid.springquoraclone.util.JsonMapper;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AnswerControllerTest {

  @Mock
  private AnswerService answerService;

  @InjectMocks
  private AnswerController answerController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(answerController)
        .setControllerAdvice(GlobalExceptionHandler.class)
        .build();
  }

  @Test
  void shouldReturn404AndStopCreatingAnswerWhenQuestionNotFound() throws Exception {
    when(answerService.createAnswer(any(AnswerWriteDto.class), anyLong()))
        .thenThrow(new QuestionNotFoundException(
            MockTestData.ID));

    mockMvc.perform(post("/api/v1/questions/1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsAnswerWriteDto())))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn201WhenCreatedAnswer() throws Exception {
    AnswerReadDto answerReadDto = MockTestData.returnsAnswerReadDto();
    when(answerService.createAnswer(any(AnswerWriteDto.class), anyLong())).thenReturn(answerReadDto);

    mockMvc.perform(post("/api/v1/questions/1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsAnswerWriteDto())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.text", is(answerReadDto.getText())));
  }
}