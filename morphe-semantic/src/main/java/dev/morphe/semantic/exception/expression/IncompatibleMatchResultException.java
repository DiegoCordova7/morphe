package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when the result of a match case is incompatible with the result
 * type established by the other cases of the match expression.
 *
 * <p>A match expression must produce a single semantically compatible type
 * regardless of which case is selected. This exception is raised when a
 * case produces a value whose type cannot be reconciled with the expected
 * match result type.</p>
 */
public final class IncompatibleMatchResultException extends SemanticException {

  /**
   * Creates an incompatible match result exception.
   *
   * @param expectedType the type expected for the match result
   * @param actualType the type produced by the incompatible case
   */
  public IncompatibleMatchResultException(IASTType expectedType, IASTType actualType) {
    super("Incompatible match result: expected type '" + expectedType + "' but found '" + actualType + "'");
  }
}