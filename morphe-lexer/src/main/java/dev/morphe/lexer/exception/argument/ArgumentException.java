package dev.morphe.lexer.exception.argument;

import dev.morphe.lexer.exception.LexerException;

/**
 * Base exception for errors caused by invalid arguments provided
 * to lexer components.
 */
public abstract class ArgumentException extends LexerException {

  /**
   * Creates an argument-related exception.
   *
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates an argument-related exception with an underlying cause.
   *
   * @param cause the underlying cause of this exception
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}