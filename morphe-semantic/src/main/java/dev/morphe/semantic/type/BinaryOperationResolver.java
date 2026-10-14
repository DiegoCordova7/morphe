package dev.morphe.semantic.type;

import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.expression.InvalidBinaryOperandException;

public final class BinaryOperationResolver {

  public BinaryOperationResolver() {}

  /**
   * Determines the resulting type according to the binary operator.
   *
   * @param operator the binary operator
   * @param left the type of the left operand
   * @param right the type of the right operand
   * @return the resulting AST type
   */
  public IASTType resolve(BinaryOperator operator, IASTType left, IASTType right, TypeChecker checker) {
    return switch (operator) {
      case ADD -> resolveAddition(left, right);
      case SUBTRACT, MULTIPLY, DIVIDE, MODULO -> resolveArithmetic(operator, left, right);
      case EQUAL, NOT_EQUAL -> resolveEquality(operator, left, right, checker);
      case STRUCT_MATCH -> resolveStructMatch(left, right, checker);
      case GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL -> resolveComparison(operator, left, right);
      case AND, OR -> resolveLogical(operator, left, right);
    };
  }

  /**
   * Resolves the result type of an addition operation.
   *
   * <p>Addition supports numeric operands and string concatenation.
   * Numeric operations produce an integer result when both operands are
   * integers, otherwise a double result. String concatenation produces a
   * string result.</p>
   *
   * @param left the left operand type
   * @param right the right operand type
   * @return the resulting AST type
   * @throws InvalidBinaryOperandException if the operand types are invalid
   */
  private IASTType resolveAddition(IASTType left, IASTType right) {
    if (left == PrimitiveType.STRING || right == PrimitiveType.STRING) return PrimitiveType.STRING;
    if (!isNumeric(left) || !isNumeric(right)) throw new InvalidBinaryOperandException(BinaryOperator.ADD, left, right);
    if (left == PrimitiveType.DOUBLE || right == PrimitiveType.DOUBLE) return PrimitiveType.DOUBLE;
    return PrimitiveType.INTEGER;
  }

  /**
   * Resolves the result type of an arithmetic operation.
   *
   * <p>Both operands must be numeric. Integer operations produce an integer
   * result, while any operation involving a double produces a double result.</p>
   *
   * @param operator the arithmetic operator
   * @param left the left operand type
   * @param right the right operand type
   * @return {@link PrimitiveType#INTEGER} or {@link PrimitiveType#DOUBLE}
   * @throws InvalidBinaryOperandException if either operand is not numeric
   */
  private IASTType resolveArithmetic(BinaryOperator operator, IASTType left, IASTType right) {
    if (!isNumeric(left) || !isNumeric(right)) throw new InvalidBinaryOperandException(operator, left, right);
    if (left == PrimitiveType.DOUBLE || right == PrimitiveType.DOUBLE) return PrimitiveType.DOUBLE;
    return PrimitiveType.INTEGER;
  }

  /**
   * Resolves the result type of an equality operation.
   *
   * <p>Equality is allowed when either operand type is compatible with the
   * other. Checking both directions accommodates directional structural
   * compatibility and allows {@code null} to participate in equality
   * comparisons.</p>
   *
   * @param operator the equality operator
   * @param left the left operand type
   * @param right the right operand type
   * @param checker the type compatibility checker
   * @return {@link PrimitiveType#BOOLEAN}
   * @throws InvalidBinaryOperandException if the operand types are
   *     incompatible
   */
  private IASTType resolveEquality(BinaryOperator operator, IASTType left, IASTType right, TypeChecker checker) {
    if (!checker.areCompatible(left, right) && !checker.areCompatible(right, left))
      throw new InvalidBinaryOperandException(operator, left, right);
    return PrimitiveType.BOOLEAN;
  }

  /**
   * Resolves the result type of a structural match operation.
   *
   * <p>Structural match requires compatible operand types and always produces
   * a boolean result.</p>
   *
   * @param left the left operand type
   * @param right the right operand type
   * @param checker the type compatibility checker
   * @return {@link PrimitiveType#BOOLEAN}
   * @throws InvalidBinaryOperandException if the operand types are incompatible
   */
  private IASTType resolveStructMatch(IASTType left, IASTType right, TypeChecker checker) {
    if (!(left instanceof CompositeType) || !(right instanceof CompositeType))
      throw new InvalidBinaryOperandException(BinaryOperator.STRUCT_MATCH, left, right);
    return PrimitiveType.BOOLEAN;
  }

  /**
   * Resolves the result type of a relational comparison.
   *
   * <p>Both operands must be numeric. Relational operators always produce
   * a boolean result.</p>
   *
   * @param operator the relational operator
   * @param left the left operand type
   * @param right the right operand type
   * @return {@link PrimitiveType#BOOLEAN}
   * @throws InvalidBinaryOperandException if either operand is not numeric
   */
  private IASTType resolveComparison(BinaryOperator operator, IASTType left, IASTType right) {
    if (!isNumeric(left) || !isNumeric(right)) throw new InvalidBinaryOperandException(operator, left, right);
    return PrimitiveType.BOOLEAN;
  }

  /**
   * Resolves the result type of a logical operation.
   *
   * <p>Both operands must be boolean. Logical operators always produce a
   * boolean result.</p>
   *
   * @param operator the logical operator
   * @param left the left operand type
   * @param right the right operand type
   * @return {@link PrimitiveType#BOOLEAN}
   * @throws InvalidBinaryOperandException if either operand is not boolean
   */
  private IASTType resolveLogical(BinaryOperator operator, IASTType left, IASTType right) {
    if (left != PrimitiveType.BOOLEAN || right != PrimitiveType.BOOLEAN)
      throw new InvalidBinaryOperandException(operator, left, right);
    return PrimitiveType.BOOLEAN;
  }

  /**
   * Determines whether a type represents a numeric primitive.
   *
   * @param type the AST type to inspect
   * @return {@code true} if the type is integer or double
   */
  private boolean isNumeric(IASTType type) {
    return type == PrimitiveType.INTEGER || type == PrimitiveType.DOUBLE;
  }
}