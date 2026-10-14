package dev.morphe.semantic.exception.pattern;

import dev.morphe.ast.type.IASTType;

public final class UnsupportedPatternTypeException extends PatternException {
  /**
   * Creates a pattern semantic exception with the specified message.
   *
   * @param expectedType the expected type of pattern.
   */
  public UnsupportedPatternTypeException(IASTType expectedType) {
    super("Unsoppresent pattern type: " + expectedType);
  }
}