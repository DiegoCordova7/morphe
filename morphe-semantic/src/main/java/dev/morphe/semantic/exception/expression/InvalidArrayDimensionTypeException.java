package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

public final class InvalidArrayDimensionTypeException extends SemanticException {

  public InvalidArrayDimensionTypeException(IASTType actualType) {
    super("Array dimension must be of type INTEGER, but found: " + actualType);
  }
}