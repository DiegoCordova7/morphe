package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.ArrayCreationExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidArrayDimensionTypeException;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Performs semantic analysis of {@link ArrayCreationExpression} nodes.
 *
 * <p>An array creation expression explicitly specifies the type of its
 * elements and the expressions determining its dimensions.</p>
 *
 * <p>Each dimension expression must evaluate to
 * {@link PrimitiveType#INTEGER}. The resulting array type is constructed
 * by wrapping the element type in an {@link ArrayType} for each dimension.</p>
 *
 * <p>For example, {@code new Integer[10]} produces
 * {@code Array<Integer>}, while {@code new Integer[10][20]} produces
 * {@code Array<Array<Integer>>}.</p>
 */
public final class ArrayCreationExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  /**
   * Creates an array creation analyzer.
   *
   * @param visitor the expression visitor used to analyze dimension
   *     expressions
   * @throws NullSemanticArgumentException if {@code visitor} is null
   */
  public ArrayCreationExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  /**
   * Analyzes an array creation expression and returns its resulting array
   * type.
   *
   * <p>Every dimension expression must have type
   * {@link PrimitiveType#INTEGER}.</p>
   *
   * @param expression the array creation expression to analyze
   * @param context the semantic analysis context
   * @return the resulting array type
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws InvalidArrayDimensionTypeException if a dimension does not
   *     evaluate to {@link PrimitiveType#INTEGER}
   */
  public IASTType analyze(ArrayCreationExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    for (IExpression dimension : expression.dimensions()) {
      IASTType dimensionType = dimension.accept(visitor, context).type();
      if (dimensionType != PrimitiveType.INTEGER) throw new InvalidArrayDimensionTypeException(dimensionType);
    }
    IASTType resultType = expression.elementType();
    for (int i = 0; i < expression.dimensions().size(); i++) resultType = new ArrayType(resultType);
    return resultType;
  }
}
