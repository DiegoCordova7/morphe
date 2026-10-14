package dev.morphe.parser.exception;

/**
 * Base exception for errors encountered during Morphe parsing.
 */
public abstract class ParserException extends RuntimeException {

  /**
   * Creates a parser exception with the specified message.
   *
   * @param message the exception message
   */
  public ParserException(String message) {
    super(message);
  }

  /**
   * Creates a parser exception with the specified cause and message.
   *
   * @param cause the underlying cause
   * @param message the exception message
   */
  public ParserException(Throwable cause, String message) {
    super(message, cause);
  }
}