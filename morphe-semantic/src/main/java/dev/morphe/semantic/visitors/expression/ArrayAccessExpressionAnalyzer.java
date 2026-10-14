package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.ArrayAccessExpression;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidArrayAccessException;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Performs semantic analysis of {@link ArrayAccessExpression} nodes.
 *
 * <p>The array expression must resolve to an {@link ArrayType} and the
 * index expression must resolve to {@link PrimitiveType#INTEGER}.</p>
 *
 * <p>The resulting type of the access is the element type of the array.
 * This naturally supports multidimensional arrays because the element type
 * may itself be another {@link ArrayType}.</p>
 */
public final class ArrayAccessExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  /**
   * Creates an array access analyzer.
   *
   * @param visitor the expression visitor used to analyze the array and
   *                index expressions
   * @throws NullSemanticArgumentException if {@code visitor} is null
   */
  public ArrayAccessExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  /**
   * Analyzes an array access expression.
   *
   * @param expression the array access expression to analyze
   * @param context the semantic analysis context
   * @return the type of the accessed element
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws InvalidArrayAccessException if the array expression is not an
   *     array or the index expression is not an integer
   */
  public IASTType analyze(ArrayAccessExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    IASTType arrayType = expression.array().accept(visitor, context).type();
    if (!(arrayType instanceof ArrayType)) throw new InvalidArrayAccessException(arrayType);
    IASTType indexType = expression.index().accept(visitor, context).type();
    if (!PrimitiveType.INTEGER.equals(indexType)) throw new InvalidArrayAccessException(indexType);
    return ((ArrayType) arrayType).elementType();
  }
}