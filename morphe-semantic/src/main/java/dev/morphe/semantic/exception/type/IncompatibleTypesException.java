package dev.morphe.semantic.exception.type;

import dev.morphe.ast.type.IASTType;

/**
 * Thrown when two types are incompatible according to the semantic
 * type rules of the Morphe language.
 */
public final class IncompatibleTypesException extends TypeResolutionException {

  /**
   * Creates an exception for two incompatible types.
   *
   * @param expected the type expected by the semantic operation
   * @param actual the type that was provided
   */
  public IncompatibleTypesException(IASTType expected, IASTType actual) {
    super("Incompatible types: expected " + expected + ", but found " + actual);
  }
}