package io.github.korzepadawid.springquoraclone.security;

import io.github.korzepadawid.springquoraclone.config.JwtConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProviderImpl implements JwtProvider {

  private final JwtConfig jwtConfig;

  @Override
  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(new Date(
            System.currentTimeMillis() + jwtConfig.getAccessTokenExpirationInSeconds() * 1000))
        .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
        .compact();
  }

  @Override
  public Boolean validate(String jwt) {
    try {
      Jwts.parser()
          .setSigningKey(jwtConfig.getSecret())
          .parseClaimsJws(jwt);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }

  @Override
  public String getUsername(String jwt) {
    return Jwts.parser()
        .setSigningKey(jwtConfig.getSecret())
        .parseClaimsJws(jwt)
        .getBody()
        .getSubject();
  }

}
