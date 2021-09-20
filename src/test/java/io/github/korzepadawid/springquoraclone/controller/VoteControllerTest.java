package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.dto.VoteDto;
import io.github.korzepadawid.springquoraclone.exception.AnswerNotFoundException;
import io.github.korzepadawid.springquoraclone.exception.GlobalExceptionHandler;
import io.github.korzepadawid.springquoraclone.exception.VoteNotFoundException;
import io.github.korzepadawid.springquoraclone.model.VoteType;
import io.github.korzepadawid.springquoraclone.service.VoteService;
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
class VoteControllerTest {

  private MockMvc mockMvc;

  @Mock
  private VoteService voteService;

  @InjectMocks
  private VoteController voteController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(voteController)
        .setControllerAdvice(GlobalExceptionHandler.class)
        .build();
  }

  @Test
  void shouldReturn404AndStopVotingWhenAnswerDoesNotExist() throws Exception {
    doThrow(new AnswerNotFoundException(MockTestData.ID)).when(voteService)
        .createVote(any(VoteDto.class), anyLong());

    mockMvc.perform(put("/api/v1/answers/1/votes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsVoteDto(VoteType.DOWN_VOTE))))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204WhenVotedSuccess() throws Exception {
    mockMvc.perform(put("/api/v1/answers/1/votes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsVoteDto(VoteType.DOWN_VOTE))))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404AndStopRemovingWhenAnswerDoesNotExist() throws Exception {
    doThrow(new AnswerNotFoundException(MockTestData.ID)).when(voteService).removeVote(anyLong());

    mockMvc.perform(delete("/api/v1/answers/1/votes"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn404AndStopRemovingWhenVoteDoesNotExist() throws Exception {
    doThrow(new VoteNotFoundException(MockTestData.ID, "usernamegoeshere")).when(voteService)
        .removeVote(anyLong());

    mockMvc.perform(delete("/api/v1/answers/1/votes"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204WhenVoteRemovedSuccessfully() throws Exception {
    mockMvc.perform(delete("/api/v1/answers/1/votes"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenVoteDoesNotExist() throws Exception {
    when(voteService.checkVote(anyLong()))
        .thenThrow(new VoteNotFoundException(MockTestData.ID, "username"));

    mockMvc.perform(get("/api/v1/answers/1/votes/me"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn404WhenAnswerDoesNotExist() throws Exception {
    when(voteService.checkVote(anyLong())).thenThrow(new AnswerNotFoundException(MockTestData.ID));

    mockMvc.perform(get("/api/v1/answers/1/votes/me"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn200WhenVoteExists() throws Exception {
    VoteDto voteDto = MockTestData.returnsVoteDto(VoteType.DOWN_VOTE);
    when(voteService.checkVote(anyLong())).thenReturn(voteDto);

    mockMvc.perform(get("/api/v1/answers/1/votes/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.voteType", is(voteDto.getVoteType().toString())));
  }
}