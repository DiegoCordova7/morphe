package dev.morphe.semantic.exception.statement;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Indicates that an expression cannot be used as an assignment target.
 *
 * <p>Only expressions that represent assignable locations, such as mutable
 * variables, array elements, or mutable composite fields, may be used as
 * assignment targets.</p>
 */
public final class InvalidAssignmentTargetException extends SemanticException {

  /**
   * Creates an exception for an invalid assignment target.
   *
   * @param target the type of the invalid target expression
   */
  public InvalidAssignmentTargetException(Object target) {
    super("Expression " + target + " cannot be used as an assignment target");
  }
}