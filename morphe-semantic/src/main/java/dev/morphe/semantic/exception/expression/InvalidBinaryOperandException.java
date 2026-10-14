package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a binary operator is applied to operands whose types are not
 * valid for that operator.
 *
 * <p>The exception records the binary operator and the types of both
 * operands involved in the invalid operation.</p>
 */
public final class InvalidBinaryOperandException extends SemanticException {

  /**
   * Creates an exception for an invalid binary operation.
   *
   * @param operator the binary operator being applied
   * @param leftType the type of the left operand
   * @param rightType the type of the right operand
   */
  public InvalidBinaryOperandException(BinaryOperator operator, IASTType leftType, IASTType rightType) {
    super("Invalid operands for binary operator " + operator + ": " + leftType + " and " + rightType);
  }
}