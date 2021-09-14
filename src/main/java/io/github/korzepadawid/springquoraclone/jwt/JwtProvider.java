package io.github.korzepadawid.springquoraclone.jwt;

import org.springframework.security.core.userdetails.User;

public interface JwtProvider {

  String generateToken(User user);
}
