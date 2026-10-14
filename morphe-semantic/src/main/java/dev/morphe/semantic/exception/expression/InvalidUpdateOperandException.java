package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when an update expression is applied to an invalid operand.
 *
 * <p>An update expression can only be applied to a mutable numeric
 * expression. This exception is raised when the operand cannot be
 * updated or when its type does not support increment or decrement
 * operations.</p>
 */
public final class InvalidUpdateOperandException extends SemanticException {

  /**
   * Creates an invalid update operand exception.
   *
   * @param target the type of the invalid update operand
   */
  public InvalidUpdateOperandException(IASTType target) {
    super("Cannot apply update operation to type '" + target + "'");
  }
}