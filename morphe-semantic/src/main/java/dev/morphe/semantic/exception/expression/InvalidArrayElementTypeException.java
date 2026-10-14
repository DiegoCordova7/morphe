package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Indicates that an element of an array literal has a type that is
 * incompatible with the expected array element type.
 */
public final class InvalidArrayElementTypeException extends SemanticException {

  /**
   * Creates an exception for an incompatible array element type.
   *
   * @param expectedType the type expected for the array element
   * @param actualType the type found in the array element
   */
  public InvalidArrayElementTypeException(IASTType expectedType, IASTType actualType) {
    super("Invalid array element type: expected " + expectedType + " but found " + actualType);
  }
}