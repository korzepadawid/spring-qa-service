package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.JsonMapper;
import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.QuestionReadDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionUpdateDto;
import io.github.korzepadawid.springquoraclone.dto.QuestionWriteDto;
import io.github.korzepadawid.springquoraclone.exception.GlobalExceptionHandler;
import io.github.korzepadawid.springquoraclone.exception.QuestionNotFoundException;
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

  @Test
  void shouldReturn404WhenQuestionDoesNotExist() throws Exception {
    final Long id = 1L;
    when(questionService.getQuestionById(anyLong())).thenThrow(new QuestionNotFoundException(id));

    mockMvc.perform(get(singleQuestionUrl(id)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn400WhenWrongTypeOfId() throws Exception {
    mockMvc.perform(get(QuestionController.BASE_URL + "/test"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnQuestionWhenQuestionExists() throws Exception {
    QuestionReadDto questionReadDto = MockTestData.returnsQuestionReadDto(false);
    when(questionService.getQuestionById(anyLong())).thenReturn(questionReadDto);

    mockMvc.perform(get(singleQuestionUrl(1L)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(questionReadDto.getTitle())));
  }

  @Test
  void shouldReturn404WhenDeletedQuestionNotFound() throws Exception {
    final Long id = 1L;
    doThrow(new QuestionNotFoundException(id)).when(questionService).deleteQuestionById(anyLong());

    mockMvc.perform(delete(singleQuestionUrl(id)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204WhenDeletedQuestion() throws Exception {
    mockMvc.perform(delete(singleQuestionUrl(1L)))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenUpdatedQuestionNotFound() throws Exception {
    final Long id = 1L;
    doThrow(new QuestionNotFoundException(id)).when(questionService)
        .updateQuestionById(any(QuestionUpdateDto.class), anyLong());

    mockMvc.perform(patch(singleQuestionUrl(id))
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(new QuestionUpdateDto())))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn400WhenValidationErrors() throws Exception {
    final Long id = 1L;
    QuestionUpdateDto questionUpdateDto = QuestionUpdateDto.builder()
        .title("x".repeat(256))
        .description("x".repeat(256))
        .build();

    mockMvc.perform(patch(singleQuestionUrl(id))
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(questionUpdateDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.title").isNotEmpty())
        .andExpect(jsonPath("$.errors.description").isNotEmpty());
  }

  @Test
  void shouldReturn204WhenUpdatedQuestion() throws Exception {
    final Long id = 1L;

    mockMvc.perform(patch(singleQuestionUrl(id))
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(QuestionUpdateDto.builder()
            .title("Is it okay?")
            .build())))
        .andExpect(status().isNoContent());
  }

  private String singleQuestionUrl(Long id) {
    return String.format("%s/%d", QuestionController.BASE_URL, id);
  }
}