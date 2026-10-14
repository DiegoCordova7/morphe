package dev.morphe.semantic.exception.pattern;

import dev.morphe.ast.type.IASTType;

/**
 * Indicates that a range pattern is used with an unsupported AST type.
 *
 * <p>Range patterns are only valid for types that support ordered numeric
 * ranges. The specific supported types are determined by semantic analysis.</p>
 */
public final class UnsupportedRangeTypeException extends PatternException {

  /**
   * Creates an exception for an unsupported range pattern type.
   *
   * @param type the AST type that does not support range patterns
   */
  public UnsupportedRangeTypeException(IASTType type) {
    super("Type does not support range patterns: " + type);
  }
}