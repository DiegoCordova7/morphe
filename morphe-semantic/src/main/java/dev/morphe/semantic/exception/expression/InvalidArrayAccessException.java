package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when an array access expression is semantically invalid.
 *
 * <p>An array access is invalid when the accessed expression does not
 * have an array type or when its index expression does not have the
 * required integer type.</p>
 */
public final class InvalidArrayAccessException extends SemanticException {

  /**
   * Creates an exception for an invalid array access.
   *
   * @param type the type that caused the invalid access
   */
  public InvalidArrayAccessException(IASTType type) {
    super("Invalid array access involving type: " + type);
  }
}