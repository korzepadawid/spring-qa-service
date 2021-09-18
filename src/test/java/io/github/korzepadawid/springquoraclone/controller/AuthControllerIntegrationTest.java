package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.util.AbstractContainerBaseTest;
import io.github.korzepadawid.springquoraclone.util.JsonMapper;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest extends AbstractContainerBaseTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Transactional
  void shouldRegisterUserAndLoginWhenValid() throws Exception {
    AppUserWriteDto appUserWriteDto = MockTestData.returnsAppUserWriteDto();

    mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(appUserWriteDto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username", is(appUserWriteDto.getUsername())))
        .andReturn();

    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(LoginDto.builder()
            .username(appUserWriteDto.getUsername())
            .password(appUserWriteDto.getPassword())
            .build())))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isNotEmpty());
  }

  @Test
  void shouldReturn403ForbiddenWhenInvalidUser() throws Exception {
    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(LoginDto.builder()
            .username("john")
            .password("pass")
            .build())))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").isNotEmpty());
  }
}