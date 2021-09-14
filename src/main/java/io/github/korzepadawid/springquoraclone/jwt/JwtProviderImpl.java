package io.github.korzepadawid.springquoraclone.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.korzepadawid.springquoraclone.config.JwtConfig;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtProviderImpl implements JwtProvider {

  private final JwtConfig jwtConfig;

  @Override
  public String generateToken(User user) {
    return JWT.create()
        .withExpiresAt(new Date(
            System.currentTimeMillis() + 1000 * jwtConfig.getAccessTokenExpirationInSeconds()))
        .withSubject(user.getUsername())
        .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
  }
}
