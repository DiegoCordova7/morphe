package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

public final class IncompatibleIfBranchesException extends SemanticException {

  public IncompatibleIfBranchesException(IASTType thenType, IASTType elseType) {
    super("If branches have incompatible types: '" + thenType + "' and '" + elseType + "'");
  }
}