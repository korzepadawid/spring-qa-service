package io.github.korzepadawid.springquoraclone.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.exception.UserAlreadyExistsException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import io.github.korzepadawid.springquoraclone.service.impl.AuthServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock
  private AppUserRepository appUserRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthServiceImpl authService;

  @Test
  void shouldThrowUserAlreadyExistsExceptionWhenUserAlreadyExists() {
    AppUserWriteDto appUserWriteDto = MockTestData.returnsAppUserWriteDto();
    AppUser appUser = MockTestData.returnsAppUser();
    when(appUserRepository
        .findByUsernameOrEmail(appUserWriteDto.getUsername(), appUserWriteDto.getEmail()))
        .thenReturn(Optional.of(appUser));

    Throwable throwable = catchThrowable(() -> authService.register(appUserWriteDto));

    assertThat(throwable)
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessageContaining("already exists");

    verifyNoInteractions(passwordEncoder);
  }

  @Test
  void shouldSaveAndReturnUserWithHashedPasswordWhenUserDoesNotExist() {
    AppUserWriteDto appUserWriteDto = MockTestData.returnsAppUserWriteDto();
    AppUser appUser = MockTestData.returnsAppUser();
    String hashedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
    appUser.setPassword(hashedPassword);
    when(appUserRepository
        .findByUsernameOrEmail(appUserWriteDto.getUsername(), appUserWriteDto.getEmail()))
        .thenReturn(Optional.empty());
    when(passwordEncoder.encode(appUserWriteDto.getPassword())).thenReturn(hashedPassword);
    when(appUserRepository.save(appUser)).thenReturn(appUser);

    AppUserReadDto appUserReadDto = authService.register(appUserWriteDto);

    assertThat(appUserReadDto).isNotNull();
    assertThat(appUserReadDto)
        .usingRecursiveComparison()
        .isEqualTo(new AppUserReadDto(appUser));
  }
}