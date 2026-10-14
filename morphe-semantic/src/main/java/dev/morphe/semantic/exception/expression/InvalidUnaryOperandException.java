package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.operator.UnaryOperator;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a unary operator is applied to an incompatible operand type.
 *
 * <p>Unary operators impose semantic restrictions on the types of their
 * operands. For example, numeric unary operators such as {@code PLUS} and
 * {@code MINUS} require a numeric operand, while {@code NOT} requires a
 * boolean operand.</p>
 *
 * <p>This exception indicates that the operand type does not satisfy the
 * requirements of the specified unary operator.</p>
 */
public final class InvalidUnaryOperandException extends SemanticException {

  /**
   * Creates an exception for an invalid unary operand.
   *
   * @param operator the unary operator being applied
   * @param operandType the type of the operand
   */
  public InvalidUnaryOperandException(UnaryOperator operator, IASTType operandType) {
    super("Invalid operand type '" + operandType + "' for unary operator '" + operator + "'.");
  }
}
