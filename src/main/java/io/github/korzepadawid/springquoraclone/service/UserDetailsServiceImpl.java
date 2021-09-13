package io.github.korzepadawid.springquoraclone.service;

import static java.util.Collections.singletonList;

import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return appUserRepository.findByUsernameOrEmailLike(username)
        .map(appUser -> new org.springframework.security.core.userdetails.User(
            appUser.getUsername(),
            appUser.getPassword(),
            singletonList(new SimpleGrantedAuthority("USER"))
        ))
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format("User %s not found", username)));
  }
}
