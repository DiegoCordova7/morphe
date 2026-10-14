package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a field access expression is semantically invalid.
 *
 * <p>This exception is raised when a field is accessed on a value that
 * does not support field access, or when the requested field does not
 * exist in the target composite type.</p>
 */
public final class InvalidFieldAccessException extends SemanticException {

  /**
   * Creates an invalid field access exception.
   *
   * @param field the field the invalid access
   */
  public InvalidFieldAccessException(String field, IASTType target) {
    super("Cannot access field '" + field + "' on type '" + target + "'");
  }
}