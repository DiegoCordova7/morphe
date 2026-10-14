package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.operator.UpdateOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents a postfix update expression that increments or decrements
 * an operand.
 *
 * <p>An update expression modifies the value represented by its operand
 * and produces the operand's value before the update is applied.</p>
 *
 * <p>For example, {@code value++} is represented by an
 * {@code UpdateExpression} whose operator is
 * {@link UpdateOperator#INCREMENT} and whose operand is the expression
 * {@code value}.</p>
 */
public final class UpdateExpression implements IExpression {

  private final SourceRange range;
  private final IExpression operand;
  private final UpdateOperator operator;

  /**
   * Creates an update expression.
   *
   * @param range the source range occupied by the expression
   * @param operand the expression being updated
   * @param operator the update operator
   * @throws NullArgumentException if {@code range}, {@code operand},
   * or {@code operator} is null
   */
  public UpdateExpression(SourceRange range, IExpression operand, UpdateOperator operator) {
    if (range == null) throw new NullArgumentException("range");
    if (operand == null) throw new NullArgumentException("operand");
    if (operator == null) throw new NullArgumentException("operator");
    this.range = range;
    this.operand = operand;
    this.operator = operator;
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
   * Returns the expression being updated.
   *
   * @return the update operand
   */
  public IExpression operand() {
    return operand;
  }

  /**
   * Returns the update operator.
   *
   * @return the update operator
   */
  public UpdateOperator operator() {
    return operator;
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