package io.github.korzepadawid.springquoraclone.controller;

import io.github.korzepadawid.springquoraclone.dto.AppUserReadDto;
import io.github.korzepadawid.springquoraclone.dto.AppUserWriteDto;
import io.github.korzepadawid.springquoraclone.dto.LoginDto;
import io.github.korzepadawid.springquoraclone.dto.TokenDto;
import io.github.korzepadawid.springquoraclone.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(AuthController.BASE_URL)
public class AuthController {

  public static final String BASE_URL = "/api/v1/auth";

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AppUserReadDto register(@Valid @RequestBody AppUserWriteDto appUserWriteDto) {
    return authService.register(appUserWriteDto);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public TokenDto login(@Valid @RequestBody LoginDto loginDto){
    return authService.login(loginDto);
  }
}
