package dev.morphe.vm.exception.internal.type;

import dev.morphe.runtime.exception.type.TypeException;

/**
 * Thrown when an array operation expects a nested array type
 * but encounters a different runtime type.
 */
public final class ExpectedArrayTypeException extends TypeException {

  /**
   * Creates an exception for an unexpected array type.
   *
   * @param actualType the actual type encountered
   */
  public ExpectedArrayTypeException(String actualType) {
    super("Expected nested array type but found: %s", actualType);
  }
}