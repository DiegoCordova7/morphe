package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a composite instance expression references a type that
 * cannot be resolved in the current semantic scope.
 *
 * <p>This exception indicates that the type name used by the expression
 * does not correspond to any known type declaration.</p>
 */
public final class UndefinedTypeException extends SemanticException {

  /**
   * Creates an undefined type exception.
   *
   * @param name the name of the unresolved type
   */
  public UndefinedTypeException(String name) {
    super("Type '" + name + "' is not defined");
  }
}