package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

public final class InvalidFunctionCallException extends SemanticException {

  public InvalidFunctionCallException(IASTType type) {
    super("Cannot call value of type '" + type + "' as a function");
  }
}