package dev.morphe.semantic.exception.argument;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a required semantic component receives a {@code null}
 * argument.
 */
public final class NullSemanticArgumentException extends SemanticException {

  /**
   * Creates a null semantic argument exception.
   *
   * @param argument the name of the argument that cannot be {@code null}
   */
  public NullSemanticArgumentException(String argument) {
    super("Argument '" + argument + "' cannot be null");
  }
}