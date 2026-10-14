package dev.morphe.parser.exception.argument;

/**
 * Thrown when a required parser argument is {@code null}.
 */
public final class NullArgumentException extends ArgumentException {

  /**
   * Creates an exception for a null argument.
   *
   * @param argument the name of the null argument
   */
  public NullArgumentException(String argument) {
    super("Argument '%s' cannot be null", argument);
  }
}