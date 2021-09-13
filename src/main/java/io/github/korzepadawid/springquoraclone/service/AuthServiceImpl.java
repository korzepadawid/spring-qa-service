package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.exception.UserAlreadyExistsException;
import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;

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

  private AppUser mapDtoToEntity(AppUserWriteDto appUserWriteDto) {
    AppUser appUser = new AppUser();
    BeanUtils.copyProperties(appUserWriteDto, appUser, "id", "password");
    appUser.setPassword(passwordEncoder.encode(appUserWriteDto.getPassword()));
    return appUser;
  }
}