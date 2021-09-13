package io.github.korzepadawid.springquoraclone.service;

import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;

public interface AuthService {

  AppUserReadDto register(AppUserWriteDto appUserWriteDto);
}
