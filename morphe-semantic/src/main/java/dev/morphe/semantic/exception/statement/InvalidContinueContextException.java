package dev.morphe.semantic.exception.statement;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a {@code continue} statement appears outside a valid
 * loop context.
 */
public final class InvalidContinueContextException extends SemanticException {

  /**
   * Creates an exception indicating that a continue statement is not
   * contained within a valid loop context.
   */
  public InvalidContinueContextException() {
    super("Continue statement is not inside a loop");
  }
}