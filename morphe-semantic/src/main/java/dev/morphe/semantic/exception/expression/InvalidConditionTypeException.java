package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when an expression used as a condition does not have a boolean
 * type.
 *
 * <p>Conditions in Morphe must evaluate to
 * {@code PrimitiveType.BOOLEAN}.</p>
 */
public final class InvalidConditionTypeException extends SemanticException {

  /**
   * Creates an exception for an invalid condition type.
   *
   * @param actualType the type found in the condition
   */
  public InvalidConditionTypeException(IASTType actualType) {
    super("Condition must be BOOLEAN, but found " + actualType);
  }
}
