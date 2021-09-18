package io.github.korzepadawid.springquoraclone.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.korzepadawid.springquoraclone.util.JsonMapper;
import io.github.korzepadawid.springquoraclone.util.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.dto.TokenDto;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;

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

    mockMvc.perform(post("/api/v1/auth/register")
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

    mockMvc.perform(post("/api/v1/auth/register")
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

    mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(appUserWriteDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)));
  }

  @Test
  void shouldReturnErrorDetailsWhenBlankPasswordAndUsername() throws Exception {
    LoginDto invalidLoginDto = new LoginDto(" ", " ");

    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(invalidLoginDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is(400)))
        .andExpect(
            jsonPath("$.message", StringContains.containsStringIgnoringCase("validation error")))
        .andExpect(jsonPath("$.errors.username").isNotEmpty())
        .andExpect(jsonPath("$.errors.password").isNotEmpty());
  }

  @Test
  void shouldReturnErrorDetailsWhenBadCredentialsException() throws Exception {
    final String exceptionMessage = "Bad credentials";
    when(authService.login(any(LoginDto.class)))
        .thenThrow(new BadCredentialsException(exceptionMessage));

    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsLoginDto())))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.code", is(403)))
        .andExpect(jsonPath("$.message", is(exceptionMessage)));
  }

  @Test
  void shouldReturnTokenAnd200WhenValidCredentials() throws Exception {
    TokenDto tokenDto = new TokenDto(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9"
            + "lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
    when(authService.login(any(LoginDto.class)))
        .thenReturn(tokenDto);

    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonMapper.toJson(MockTestData.returnsLoginDto())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken", is(tokenDto.getAccessToken())));
  }
}