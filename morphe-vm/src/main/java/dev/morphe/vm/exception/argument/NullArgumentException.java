package dev.morphe.vm.exception.argument;

/**
 * Thrown when a required argument provided to a Morphe VM operation
 * or component is {@code null}.
 */
public final class NullArgumentException extends ArgumentException {

  /**
   * Creates an exception for a null argument.
   *
   * @param argumentName the name of the argument that is null
   */
  public NullArgumentException(String argumentName) {
    super("Argument '%s' cannot be null", argumentName);
  }

  /**
   * Creates an exception for a null argument with an underlying cause.
   *
   * @param cause the underlying cause
   * @param argumentName the name of the argument that is null
   */
  public NullArgumentException(Throwable cause, String argumentName) {
    super(cause, "Argument '%s' cannot be null", argumentName);
  }
}