package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a requested field does not exist in a composite type.
 */
public final class FieldNotFoundException extends SemanticException {

  /**
   * Creates a field not found exception.
   *
   * @param field the detail message describing the missing field
   */
  public FieldNotFoundException(String field, String name) {
    super("Field '" + field + "' does not exist in composite type '" + name + "'");
  }
}