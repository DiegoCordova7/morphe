package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents a conditional expression.
 *
 * <p>An if expression evaluates a condition and produces the value
 * of either the then branch or the else branch depending on the
 * result of that condition.</p>
 *
 * <p>Both branches are required because an expression must produce
 * a value regardless of the condition's result.</p>
 *
 * <p>For example, the expression
 * {@code if condition valueTrue else valueFalse} is represented by
 * an {@code IfExpression} containing the condition, then branch,
 * and else branch.</p>
 */
public final class IfExpression implements IExpression {

  private final SourceRange range;
  private final IExpression condition;
  private final IExpression thenBranch;
  private final IExpression elseBranch;

  /**
   * Creates an if expression.
   *
   * @param range the source range occupied by the expression
   * @param condition the expression determining which branch is evaluated
   * @param thenBranch the expression evaluated when the condition is true
   * @param elseBranch the expression evaluated when the condition is false
   * @throws NullArgumentException if {@code range}, {@code condition},
   * {@code thenBranch}, or {@code elseBranch} is null
   */
  public IfExpression(SourceRange range, IExpression condition, IExpression thenBranch, IExpression elseBranch) {
    if (range == null) throw new NullArgumentException("range");
    if (condition == null) throw new NullArgumentException("condition");
    if (thenBranch == null) throw new NullArgumentException("thenBranch");
    if (elseBranch == null) throw new NullArgumentException("elseBranch");
    this.range = range;
    this.condition = condition;
    this.thenBranch = thenBranch;
    this.elseBranch = elseBranch;
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
   * Returns the expression determining which branch is evaluated.
   *
   * @return the condition expression
   */
  public IExpression condition() {
    return condition;
  }

  /**
   * Returns the expression evaluated when the condition is true.
   *
   * @return the then branch expression
   */
  public IExpression thenBranch() {
    return thenBranch;
  }

  /**
   * Returns the expression evaluated when the condition is false.
   *
   * @return the else branch expression
   */
  public IExpression elseBranch() {
    return elseBranch;
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