package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.ast.operator.UnaryOperator;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidUnaryOperandException;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Performs semantic analysis of {@link UnaryExpression} nodes.
 *
 * <p>The analyzer first delegates the analysis of the operand to the
 * {@link SemanticExpressionVisitor} and then verifies that the resulting
 * operand type is valid for the unary operator.</p>
 *
 * <p>Morphe supports the following unary operations:</p>
 *
 * <ul>
 *   <li>{@link UnaryOperator#MINUS} for integer and double operands</li>
 *   <li>{@link UnaryOperator#NOT} for boolean operands</li>
 * </ul>
 *
 * <p>The result type of {@code PLUS} and {@code MINUS} is the same as the
 * operand type. The result type of {@code NOT} is {@link PrimitiveType#BOOLEAN}.</p>
 *
 * <p>This analyzer does not evaluate the expression. It only validates the
 * operand type and determines the type produced by the expression.</p>
 */
public final class UnaryExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  /**
   * Creates a unary expression analyzer using the specified expression
   * visitor.
   *
   * <p>The visitor is used to recursively analyze the operand expression
   * before validating its type.</p>
   *
   * @param visitor the expression visitor used to analyze operands
   * @throws NullSemanticArgumentException if {@code visitor} is null
   */
  public UnaryExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  /**
   * Analyzes a unary expression and determines its resulting AST type.
   *
   * <p>The operand is first analyzed through the configured
   * {@link SemanticExpressionVisitor}. The resulting type must be valid for
   * the unary operator used by the expression.</p>
   *
   * @param expression the unary expression to analyze
   * @param context the semantic analysis context
   * @return the AST type produced by the unary expression
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws InvalidUnaryOperandException if the operand type is not valid
   *     for the unary operator
   */
  public IASTType analyze(UnaryExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    IASTType operandType = expression.operand().accept(visitor, context).type();
    return resolveResultType(expression.operator(), operandType);
  }

  /**
   * Determines the resulting type of a unary operation.
   *
   * <p>{@code PLUS} and {@code MINUS} require an integer or double operand
   * and preserve the operand type. {@code NOT} requires a boolean operand
   * and produces a boolean result.</p>
   *
   * @param operator the unary operator being applied
   * @param operandType the type of the operand
   * @return the resulting AST type
   * @throws InvalidUnaryOperandException if the operand type is invalid
   *     for the specified operator
   */
  private IASTType resolveResultType(UnaryOperator operator, IASTType operandType) {
    return switch (operator) {
      case MINUS -> {
        if (operandType != PrimitiveType.INTEGER && operandType != PrimitiveType.DOUBLE)
          throw new InvalidUnaryOperandException(operator, operandType);
        yield operandType;
      }
      case NOT -> {
        if (operandType != PrimitiveType.BOOLEAN) throw new InvalidUnaryOperandException(operator, operandType);
        yield PrimitiveType.BOOLEAN;
      }
    };
  }
}