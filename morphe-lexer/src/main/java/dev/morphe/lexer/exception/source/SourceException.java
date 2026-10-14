package dev.morphe.lexer.exception.source;

import dev.morphe.lexer.exception.LexerException;

/**
 * Base exception for errors involving Morphe source locations
 * and source ranges.
 */
public abstract class SourceException extends LexerException {

  /**
   * Creates a source-related exception.
   *
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected SourceException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates a source-related exception with an underlying cause.
   *
   * @param cause the underlying cause of this exception
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected SourceException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}