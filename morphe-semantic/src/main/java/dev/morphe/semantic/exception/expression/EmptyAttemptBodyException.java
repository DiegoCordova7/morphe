package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when an attempt expression contains an empty body.
 *
 * <p>An attempt body must contain at least one statement so that its
 * resulting value can be determined from its last expression statement.</p>
 */
public final class EmptyAttemptBodyException extends SemanticException {

  /**
   * Creates an exception indicating that an attempt body is empty.
   */
  public EmptyAttemptBodyException() {
    super("Attempt expression body cannot be empty");
  }
}