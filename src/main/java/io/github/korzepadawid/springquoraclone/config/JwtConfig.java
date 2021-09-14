package io.github.korzepadawid.springquoraclone.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("jwt.config")
public class JwtConfig {

  private String secret;
  private Long accessTokenExpirationInSeconds;
}
