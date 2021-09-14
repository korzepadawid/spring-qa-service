package io.github.korzepadawid.springquoraclone.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.korzepadawid.springquoraclone.dto.ErrorDto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    ErrorDto errorResponse = ErrorDto.builder()
        .code(HttpStatus.UNAUTHORIZED.value())
        .message("Access denied.")
        .build();

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      response.setStatus(errorResponse.getCode());
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    } catch (JsonProcessingException exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }
}
