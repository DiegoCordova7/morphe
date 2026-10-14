package dev.morphe.semantic.exception.statement;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a return statement produces a value whose type is not
 * compatible with the return type expected by the current function.
 */
public final class InvalidReturnTypeException extends SemanticException {

  private final IASTType expectedType;
  private final IASTType actualType;

  /**
   * Creates an invalid return type exception.
   *
   * @param expectedType the type expected by the current function
   * @param actualType the type produced by the returned expression
   */
  public InvalidReturnTypeException(IASTType expectedType, IASTType actualType) {
    super(
        "Invalid return type: expected " + expectedType
            + " but found " + actualType
    );
    this.expectedType = expectedType;
    this.actualType = actualType;
  }

  /**
   * Returns the type expected by the current function.
   *
   * @return the expected return type
   */
  public IASTType expectedType() {
    return expectedType;
  }

  /**
   * Returns the type produced by the returned expression.
   *
   * @return the actual return type
   */
  public IASTType actualType() {
    return actualType;
  }
}