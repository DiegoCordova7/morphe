package dev.morphe.lexer.exception.argument;

/**
 * Indicates that a required lexer argument was {@code null}.
 */
public final class NullArgumentException extends ArgumentException {

  /**
   * Creates a null-argument exception.
   *
   * @param argumentName the name of the argument that was null
   */
  public NullArgumentException(String argumentName) {
    super("Argument '%s' cannot be null", argumentName);
  }
}