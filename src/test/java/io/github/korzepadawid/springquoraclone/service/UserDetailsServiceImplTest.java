package io.github.korzepadawid.springquoraclone.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

import io.github.korzepadawid.springquoraclone.MockTestData;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @Mock
  private AppUserRepository appUserRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Test
  void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
    final String username = "user";
    when(appUserRepository.findByUsernameOrEmail(username)).thenReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> userDetailsService.loadUserByUsername(username));

    assertThat(throwable)
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining(username);
  }

  @Test
  void shouldReturnNewSpringSecurityUserWhenUserExists() {
    AppUser appUser = MockTestData.returnAppUser();
    when(appUserRepository.findByUsernameOrEmail(appUser.getUsername()))
        .thenReturn(Optional.of(appUser));

    UserDetails userDetails = userDetailsService.loadUserByUsername(appUser.getUsername());

    assertThat(userDetails)
        .isNotNull()
        .hasFieldOrPropertyWithValue("username", appUser.getUsername())
        .hasFieldOrPropertyWithValue("password", appUser.getPassword());
  }
}