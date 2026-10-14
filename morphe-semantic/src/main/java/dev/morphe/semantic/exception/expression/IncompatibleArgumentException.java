package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

public final class IncompatibleArgumentException extends SemanticException {

  public IncompatibleArgumentException(int index, IASTType expected, IASTType actual) {
    super("Argument at index " + index + " is incompatible: expected '" + expected + "' but found '" + actual + "'");
  }
}