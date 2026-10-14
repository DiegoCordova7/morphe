package dev.morphe.api.exception;

import dev.morphe.api.dto.ExecutionResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Prevents unhandled errors from returning Spring's default error page
 * and guarantees the frontend always receives the same JSON response shape.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExecutionResultDto> handleValidationError(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .orElse("Invalid request");
    return ResponseEntity.badRequest().body(new ExecutionResultDto(false, null, message, null, null));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExecutionResultDto> handleUnexpectedError(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ExecutionResultDto(false, null, "Internal server error", null, null));
  }
}