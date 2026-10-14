package dev.morphe.semantic.exception.statement;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a {@code break} statement appears outside a valid
 * breakable control-flow context.
 */
public final class InvalidBreakContextException extends SemanticException {

  /**
   * Creates an exception indicating that a break statement is not
   * contained within a valid loop context.
   */
  public InvalidBreakContextException() {
    super("Break statement is not inside a loop");
  }
}