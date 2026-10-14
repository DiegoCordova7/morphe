package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a field is initialized more than once within a composite
 * instance expression.
 *
 * <p>Each field of a composite instance may be initialized at most once.</p>
 */
public final class DuplicateFieldInitializerException extends SemanticException {

  /**
   * Creates a duplicate field initializer exception.
   *
   * @param field the name of the field initialized more than once
   */
  public DuplicateFieldInitializerException(String field) {
    super("Field '" + field + "' is initialized more than once");
  }
}