package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when an update expression attempts to modify a symbol that is not
 * mutable.
 *
 * <p>Only mutable symbols, such as variables and parameters, may be used
 * as operands of update expressions.</p>
 */
public final class InvalidUpdateTargetException extends SemanticException {

  /**
   * Creates an invalid update target exception.
   *
   * @param name the name of the symbol that cannot be updated
   */
  public InvalidUpdateTargetException(String name) {
    super("Cannot update immutable symbol '" + name + "'");
  }
}