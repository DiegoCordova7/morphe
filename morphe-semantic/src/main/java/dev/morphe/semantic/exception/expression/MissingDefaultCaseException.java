package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a match expression does not contain a default case.
 *
 * <p>A match expression must provide a default case so that it produces
 * a value when none of its explicit patterns match the value being
 * evaluated.</p>
 */
public final class MissingDefaultCaseException extends SemanticException {

  /**
   * Creates a missing default case exception.
   */
  public MissingDefaultCaseException() {
    super("Match expression must contain a default case");
  }
}