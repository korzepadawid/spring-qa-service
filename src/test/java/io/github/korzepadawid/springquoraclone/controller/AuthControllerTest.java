package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.JsonMapper;
import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.exception.GlobalExceptionHandler;
import io.github.korzepadawid.springquoraclone.exception.UserAlreadyExistsException;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import org.hamcrest.core.StringContains;
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
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;

  private static final String REGISTER_URL = AuthController.BASE_URL + "/register";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController)
        .setControllerAdvice(GlobalExceptionHandler.class)
        .build();
  }

  @Test
  void shouldReturnBadRequestWhenValidationErrors() throws Exception {
    AppUserWriteDto appUserWriteDto = AppUserWriteDto.builder()
        .username("x")
        .email("thisIsNotAnEmail")
        .password("s".repeat(100))
        .description("x".repeat(400))
        .build();

    mockMvc.perform(post(REGISTER_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(appUserWriteDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.password").isNotEmpty())
        .andExpect(jsonPath("$.errors.username").isNotEmpty())
        .andExpect(jsonPath("$.errors.email").isNotEmpty())
        .andExpect(jsonPath("$.errors.description").isNotEmpty());
  }

  @Test
  void shouldReturnBadRequestWhenUserAlreadyExists() throws Exception {
    AppUserWriteDto appUserWriteDto = MockTestData.returnsAppUserWriteDto();
    when(authService.register(any(AppUserWriteDto.class)))
        .thenThrow(new UserAlreadyExistsException());

    mockMvc.perform(post(REGISTER_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(appUserWriteDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message",
            StringContains.containsStringIgnoringCase("already exists")));
  }

  @Test
  void shouldReturnCreatedUserDetailsWhenCreatedUser() throws Exception {
    AppUserWriteDto appUserWriteDto = MockTestData.returnsAppUserWriteDto();
    AppUserReadDto appUserReadDto = MockTestData.returnsAppUserReadDto();
    when(authService.register(any(AppUserWriteDto.class))).thenReturn(appUserReadDto);

    mockMvc.perform(post(REGISTER_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(appUserWriteDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)));
  }
}