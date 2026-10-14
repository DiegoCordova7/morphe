package dev.morphe.lexer.exception.argument;

/**
 * Thrown when an argument has an invalid value.
 */
public final class InvalidArgumentException extends ArgumentException {

  /**
   * Creates an exception for an invalid argument.
   *
   * @param argument the name of the invalid argument
   * @param reason the reason why the argument is invalid
   */
  public InvalidArgumentException(String argument, String reason) {
    super("Invalid argument '%s': %s", argument, reason);
  }
}