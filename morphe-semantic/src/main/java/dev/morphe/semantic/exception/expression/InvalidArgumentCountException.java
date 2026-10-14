package dev.morphe.semantic.exception.expression;

import dev.morphe.semantic.exception.SemanticException;

public final class InvalidArgumentCountException extends SemanticException {

  public InvalidArgumentCountException(int expected, int actual) {
    super("Function expects " + expected + " arguments, but " + actual + " were provided");
  }
}