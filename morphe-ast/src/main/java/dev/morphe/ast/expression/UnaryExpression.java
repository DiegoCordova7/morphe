package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.operator.UnaryOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents a unary expression composed of an operand and a unary
 * operator.
 *
 * <p>A unary expression applies a single operator to one expression.
 * The operand may be any {@link IExpression}, allowing unary expressions
 * to be nested and composed with other expressions.</p>
 *
 * <p>For example, the expression {@code -value} is represented by a
 * {@code UnaryExpression} whose operator is {@link UnaryOperator#MINUS}
 * and whose operand is the expression {@code value}.</p>
 */
public final class UnaryExpression implements IExpression {

  private final SourceRange range;
  private final UnaryOperator operator;
  private final IExpression operand;

  /**
   * Creates a unary expression.
   *
   * @param range the source range occupied by the expression
   * @param operator the unary operator
   * @param operand the operand to which the operator is applied
   * @throws NullArgumentException if {@code range}, {@code operator},
   * or {@code operand} is null
   */
  public UnaryExpression(SourceRange range, UnaryOperator operator, IExpression operand) {
    if (range == null) throw new NullArgumentException("range");
    if (operator == null) throw new NullArgumentException("operator");
    if (operand == null) throw new NullArgumentException("operand");
    this.range = range;
    this.operator = operator;
    this.operand = operand;
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
   * Returns the unary operator applied by this expression.
   *
   * @return the unary operator
   */
  public UnaryOperator operator() {
    return operator;
  }

  /**
   * Returns the operand of this expression.
   *
   * @return the operand expression
   */
  public IExpression operand() {
    return operand;
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