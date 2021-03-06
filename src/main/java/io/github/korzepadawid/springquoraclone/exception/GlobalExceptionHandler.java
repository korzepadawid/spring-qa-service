package io.github.korzepadawid.springquoraclone.exception;

import io.github.korzepadawid.springquoraclone.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessLogicException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleBusinessLogicException(
      BusinessLogicException exception) {
    return ErrorDto.builder()
        .message(exception.getMessage())
        .code(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(),
        "A validation error has occurred.");
    exception.getBindingResult()
        .getAllErrors()
        .forEach(error -> errorDto.getErrors().put(
            ((FieldError) error).getField(),
            error.getDefaultMessage()
        ));
    return errorDto;
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorDto handleAuthenticationException(
      AuthenticationException exception) {
    return ErrorDto.builder()
        .message(exception.getMessage())
        .code(HttpStatus.FORBIDDEN.value())
        .build();
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorDto handleResourceNotFound(
      ResourceNotFoundException exception) {
    return ErrorDto.builder()
        .message(exception.getMessage())
        .code(HttpStatus.NOT_FOUND.value())
        .build();
  }
}
