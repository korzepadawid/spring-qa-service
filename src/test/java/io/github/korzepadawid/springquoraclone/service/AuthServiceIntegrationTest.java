package io.github.korzepadawid.springquoraclone.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import io.github.korzepadawid.springquoraclone.AbstractContainerBaseTest;
import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.dto.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AuthServiceIntegrationTest extends AbstractContainerBaseTest {

  @Autowired
  private AuthService authService;

  @Test
  void shouldThrowBadCredentialsExceptionWhenBadCredentials() {
    Throwable exception = catchThrowable(() -> authService.login(
        LoginDto.builder()
            .username("test")
            .password("iNv@lidCr3dent1@l5123!")
            .build()
    ));

    assertThat(exception).isInstanceOf(BadCredentialsException.class);
  }

  @Test
  @Transactional
  void shouldReturnTokenDtoWhenValidUser() {
    AppUserWriteDto appUserWriteDto = MockTestData.returnsAppUserWriteDto();
    authService.register(appUserWriteDto);

    TokenDto tokenDto = authService.login(
        LoginDto.builder()
            .username(appUserWriteDto.getUsername())
            .password(appUserWriteDto.getPassword())
            .build()
    );

    assertThat(tokenDto.getAccessToken()).isNotBlank();
  }
}