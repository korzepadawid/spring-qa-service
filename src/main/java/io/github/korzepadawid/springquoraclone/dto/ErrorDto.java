package io.github.korzepadawid.springquoraclone.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorDto {

  private Integer code;
  private String message;
  private final Map<String, String> errors = new HashMap<>();
}
