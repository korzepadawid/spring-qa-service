package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.dto.TokenDto;
import io.github.korzepadawid.springquoraclone.model.AppUser;

public interface AuthService {

  AppUserReadDto register(AppUserWriteDto appUserWriteDto);

  TokenDto login(LoginDto loginDto);

  AppUser getCurrentlyLoggedUser();
}
