package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidBinaryOperandException;
import dev.morphe.semantic.type.BinaryOperationResolver;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Performs semantic analysis of {@link BinaryExpression} nodes.
 *
 * <p>The analyzer determines the types of both operands through the
 * {@link SemanticExpressionVisitor} and validates that the selected
 * {@link BinaryOperator} can be applied to them.</p>
 *
 * <p>Binary operators are grouped according to their semantic rules:</p>
 *
 * <ul>
 *   <li>Arithmetic operators require numeric operands and produce either
 *       {@link PrimitiveType#INTEGER} or {@link PrimitiveType#DOUBLE}.</li>
 *   <li>Equality operators require mutually compatible operand types and
 *       produce {@link PrimitiveType#BOOLEAN}.</li>
 *   <li>Relational operators require numeric operands and produce
 *       {@link PrimitiveType#BOOLEAN}.</li>
 *   <li>Logical operators require boolean operands and produce
 *       {@link PrimitiveType#BOOLEAN}.</li>
 * </ul>
 *
 * <p>Arithmetic operations preserve integer results when both operands are
 * integers. If either operand is a double, the result is a double.</p>
 *
 * <p>Equality compatibility is checked in both directions. This is
 * significant for directional structural compatibility and for
 * {@code null}, which is compatible with every type except {@code void}.</p>
 *
 * <p>This analyzer validates only the semantic type rules of binary
 * expressions. Runtime evaluation and the actual execution of operators
 * belong to later stages of the Morphe pipeline.</p>
 */
public final class BinaryExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;
  private final BinaryOperationResolver operationResolver = new BinaryOperationResolver();

  /**
   * Creates a binary expression analyzer.
   *
   * @param visitor the expression visitor used to analyze operands
   * @throws NullSemanticArgumentException if {@code visitor} is null
   */
  public BinaryExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  /**
   * Analyzes a binary expression and determines its resulting AST type.
   *
   * <p>Both operands are analyzed recursively through the expression visitor
   * before the operator-specific compatibility rules are applied.</p>
   *
   * @param expression the binary expression to analyze
   * @param context the semantic context used during analysis
   * @return the AST type produced by the binary expression
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws InvalidBinaryOperandException if the operand types are invalid
   *     for the selected operator
   */
  public IASTType analyze(BinaryExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    IASTType leftType = expression.left().accept(visitor, context).type();
    IASTType rightType = expression.right().accept(visitor, context).type();
    return operationResolver.resolve(expression.operator(), leftType, rightType, context.checker());
  }

}