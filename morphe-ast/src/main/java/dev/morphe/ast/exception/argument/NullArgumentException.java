package dev.morphe.ast.exception.argument;

/**
 * Thrown when a required AST argument is {@code null}.
 */
public final class NullArgumentException extends ArgumentException {

  /**
   * Creates a null argument exception.
   *
   * @param argumentName the name of the argument that was {@code null}
   */
  public NullArgumentException(String argumentName) {
    super("Argument '%s' cannot be null", argumentName);
  }
}