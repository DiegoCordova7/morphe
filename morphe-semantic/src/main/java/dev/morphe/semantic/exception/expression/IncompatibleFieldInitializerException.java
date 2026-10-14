package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when the value assigned to a composite field is not compatible
 * with the field's declared type.
 */
public final class IncompatibleFieldInitializerException extends SemanticException {

  /**
   * Creates an incompatible field initializer exception.
   *
   * @param field the name of the field being initialized
   * @param expected the type declared by the field
   * @param actual the type produced by the initializer expression
   */
  public IncompatibleFieldInitializerException(String field, IASTType expected, IASTType actual) {
    super(
        "Initializer for field '" + field + "' has incompatible type '" + actual + "'; expected '" + expected + "'"
    );
  }
}