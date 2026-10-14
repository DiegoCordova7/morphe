package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when the attempted body and fallback expression of an attempt
 * expression produce incompatible types.
 *
 * <p>An attempt expression may produce either the value of its attempted
 * body or the value of its fallback expression. Therefore, both results
 * must have compatible types.</p>
 */
public final class IncompatibleAttemptResultException extends SemanticException {

  private final IASTType bodyType;
  private final IASTType fallbackType;

  /**
   * Creates an exception for incompatible attempt result types.
   *
   * @param bodyType the type produced by the attempted body
   * @param fallbackType the type produced by the fallback expression
   */
  public IncompatibleAttemptResultException(IASTType bodyType, IASTType fallbackType) {
    super("Incompatible attempt result types: body produces " + bodyType + " but fallback produces " + fallbackType);
    this.bodyType = bodyType;
    this.fallbackType = fallbackType;
  }

  /**
   * Returns the type produced by the attempted body.
   *
   * @return the body result type
   */
  public IASTType bodyType() {
    return bodyType;
  }

  /**
   * Returns the type produced by the fallback expression.
   *
   * @return the fallback result type
   */
  public IASTType fallbackType() {
    return fallbackType;
  }
}