package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

public final class InvalidIfConditionException extends SemanticException {

  public InvalidIfConditionException(IASTType type) {
    super("If condition must be boolean, but found '" + type + "'");
  }
}