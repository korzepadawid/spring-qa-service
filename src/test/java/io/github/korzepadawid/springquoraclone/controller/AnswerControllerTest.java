package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.dto.AnswerReadDto;
import io.github.korzepadawid.springquoraclone.dto.AnswerWriteDto;
import io.github.korzepadawid.springquoraclone.exception.AnswerNotFoundException;
import io.github.korzepadawid.springquoraclone.exception.GlobalExceptionHandler;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
import io.github.korzepadawid.springquoraclone.service.AnswerService;
import io.github.korzepadawid.springquoraclone.util.JsonMapper;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    when(answerService.createAnswer(any(AnswerWriteDto.class), anyLong()))
        .thenReturn(answerReadDto);

    mockMvc.perform(post("/api/v1/questions/1/answers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsAnswerWriteDto())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.text", is(answerReadDto.getText())));
  }

  @Test
  void shouldReturn404AndStopListingAnswersWhenQuestionNotFound() throws Exception {
    when(answerService.findAllQuestionAnswers(anyLong()))
        .thenThrow(new QuestionNotFoundException(MockTestData.ID));

    mockMvc.perform(get("/api/v1/questions/1/answers"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn200WhenFoundAnswersAndQuestionExists() throws Exception {
    List<AnswerReadDto> answerReadDtos = Arrays.asList(
        MockTestData.returnsAnswerReadDto(),
        MockTestData.returnsAnswerReadDto(),
        MockTestData.returnsAnswerReadDto()
    );
    when(answerService.findAllQuestionAnswers(anyLong())).thenReturn(answerReadDtos);

    mockMvc.perform(get("/api/v1/questions/1/answers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*]", hasSize(answerReadDtos.size())));
  }

  @Test
  void shouldReturn200WhenNoAnswersAndQuestionExists() throws Exception {
    when(answerService.findAllQuestionAnswers(anyLong())).thenReturn(new ArrayList<>());

    mockMvc.perform(get("/api/v1/questions/1/answers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*]", empty()));
  }

  @Test
  void shouldReturn404WhenAnswerDoesNotExist() throws Exception {
    when(answerService.getAnswerById(anyLong()))
        .thenThrow(new AnswerNotFoundException(MockTestData.ID));

    mockMvc.perform(get("/api/v1/answers/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn200AndAnswerWhenAnswerExists() throws Exception {
    AnswerReadDto answerReadDto = MockTestData.returnsAnswerReadDto();
    when(answerService.getAnswerById(anyLong())).thenReturn(answerReadDto);

    mockMvc.perform(get("/api/v1/answers/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.text", is(answerReadDto.getText())));
  }

  @Test
  void shouldReturn404AndStopDeletingWhenAnswerDoesNotExistForCurrentUser() throws Exception {
    doThrow(new AnswerNotFoundException(MockTestData.ID)).when(answerService)
        .deleteAnswerById(anyLong());

    mockMvc.perform(delete("/api/v1/answers/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204WhenDeletedAnswer() throws Exception {
    mockMvc.perform(delete("/api/v1/answers/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404AndStopUpdatingWhenAnswerDoesNotExistForCurrentUser() throws Exception {
    doThrow(new AnswerNotFoundException(MockTestData.ID)).when(answerService)
        .updateAnswerById(any(AnswerWriteDto.class),anyLong());

    mockMvc.perform(patch("/api/v1/answers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsAnswerWriteDto())))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204WhenUpdatedAnswer() throws Exception {
    mockMvc.perform(patch("/api/v1/answers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsAnswerWriteDto())))
        .andExpect(status().isNoContent());
  }
}