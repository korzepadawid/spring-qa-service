package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.dto.TokenDto;
import io.github.korzepadawid.springquoraclone.exception.UserAlreadyExistsException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import io.github.korzepadawid.springquoraclone.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @Transactional
  @Override
  public AppUserReadDto register(AppUserWriteDto appUserWriteDto) {
    appUserRepository.findByUsernameOrEmail(appUserWriteDto.getUsername(),
        appUserWriteDto.getEmail())
        .ifPresent(appUser -> {
          throw new UserAlreadyExistsException();
        });

    AppUser appUser = mapDtoToEntity(appUserWriteDto);
    AppUser savedAppUser = appUserRepository.save(appUser);

    return new AppUserReadDto(savedAppUser);
  }

  @Override
  public TokenDto login(LoginDto loginDto) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginDto.getUsername(),
            loginDto.getPassword()
        )
    );

    User user = (User) authentication.getPrincipal();
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new TokenDto(jwtProvider.generateToken(user));
  }

  @Override
  public AppUser getCurrentlyLoggedUser() {
    User user = (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    return appUserRepository.findByUsernameOrEmailLike(user.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
  }

  private AppUser mapDtoToEntity(AppUserWriteDto appUserWriteDto) {
    AppUser appUser = new AppUser();
    BeanUtils.copyProperties(appUserWriteDto, appUser, "id", "password");
    appUser.setPassword(passwordEncoder.encode(appUserWriteDto.getPassword()));
    return appUser;
  }
}
