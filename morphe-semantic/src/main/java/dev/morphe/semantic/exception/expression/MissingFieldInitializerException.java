package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a required field of a composite type is not initialized
 * by a composite instance expression.
 *
 * <p>All fields declared by the composite type must be explicitly
 * initialized when creating an instance.</p>
 */
public final class MissingFieldInitializerException extends SemanticException {

  /**
   * Creates a missing field initializer exception.
   *
   * @param field the name of the field without an initializer
   * @param type the name of the composite type
   */
  public MissingFieldInitializerException(String field, String type) {
    super("Field '" + field + "' is not initialized in composite type '" + type + "'");
  }
}