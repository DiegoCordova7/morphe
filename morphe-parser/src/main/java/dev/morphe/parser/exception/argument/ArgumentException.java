package dev.morphe.parser.exception.argument;

import dev.morphe.parser.exception.ParserException;

/**
 * Base exception for errors involving invalid parser arguments.
 */
public abstract class ArgumentException extends ParserException {

  /**
   * Creates an argument exception with a formatted message.
   *
   * @param message the exception message format
   * @param arguments the arguments used to format the message
   */
  public ArgumentException(String message, Object... arguments) {
    super(String.format(message, arguments));
  }

  /**
   * Creates an argument exception with a cause and formatted message.
   *
   * @param cause the underlying cause
   * @param message the exception message format
   * @param arguments the arguments used to format the message
   */
  public ArgumentException(Throwable cause, String message, Object... arguments) {
    super(cause, String.format(message, arguments));
  }
}