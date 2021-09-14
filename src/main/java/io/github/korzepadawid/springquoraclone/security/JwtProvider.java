package io.github.korzepadawid.springquoraclone.security;

import org.springframework.security.core.userdetails.User;

public interface JwtProvider {

  String generateToken(User user);

  Boolean validate(String jwt);

  String getUsername(String jwt);
}
