package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents a binary expression composed of two operands and a binary
 * operator.
 *
 * <p>Both operands are represented as {@link IExpression} instances,
 * allowing binary expressions to be nested and composed with any other
 * expression type.</p>
 *
 * <p>For example, the expression {@code a + b} is represented by a
 * {@code BinaryExpression} whose left operand is {@code a}, whose operator
 * is {@link BinaryOperator#ADD}, and whose right operand is {@code b}.</p>
 */
public final class BinaryExpression implements IExpression {

  private final SourceRange range;
  private final IExpression left;
  private final BinaryOperator operator;
  private final IExpression right;

  /**
   * Creates a binary expression.
   *
   * @param range the source range occupied by the expression
   * @param left the left operand
   * @param operator the binary operator
   * @param right the right operand
   * @throws NullArgumentException if {@code range}, {@code left},
   * {@code operator}, or {@code right} is null
   */
  public BinaryExpression(SourceRange range, IExpression left, BinaryOperator operator, IExpression right) {
    if (range == null) throw new NullArgumentException("range");
    if (left == null) throw new NullArgumentException("left");
    if (operator == null) throw new NullArgumentException("operator");
    if (right == null) throw new NullArgumentException("right");
    this.range = range;
    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  /**
   * Returns the source range occupied by this expression.
   *
   * @return the source range of this expression
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the left operand of this expression.
   *
   * @return the left operand
   */
  public IExpression left() {
    return left;
  }

  /**
   * Returns the binary operator applied by this expression.
   *
   * @return the binary operator
   */
  public BinaryOperator operator() {
    return operator;
  }

  /**
   * Returns the right operand of this expression.
   *
   * @return the right operand
   */
  public IExpression right() {
    return right;
  }

  /**
   * Accepts an expression visitor and dispatches this node to it.
   *
   * @param visitor the visitor processing this expression
   * @param context the operational context passed through the traversal
   * @param <R> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <R, C> R accept(IExpressionVisitor<R, C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}