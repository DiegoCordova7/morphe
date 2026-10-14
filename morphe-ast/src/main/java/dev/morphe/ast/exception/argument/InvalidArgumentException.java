package dev.morphe.ast.exception.argument;

/**
 * Thrown when an argument does not satisfy the requirements
 * of an AST component or operation.
 */
public final class InvalidArgumentException extends ArgumentException {

  /**
   * Creates an invalid argument exception.
   *
   * @param argumentName the name of the invalid argument
   * @param message the reason why the argument is invalid
   */
  public InvalidArgumentException(String argumentName, String message) {
    super("Invalid argument '%s': %s", argumentName, message);
  }
}