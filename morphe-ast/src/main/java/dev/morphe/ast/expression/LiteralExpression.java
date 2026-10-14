package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents a literal expression in the Morphe abstract syntax tree.
 *
 * <p>
 * A literal expression represents a value written directly in source code,
 * such as a numeric, boolean, string, or {@code null} literal.
 *
 * <p>
 * The stored value represents the syntactic value of the literal and is
 * independent from the runtime value representation used during execution.
 * A {@code null} Java value represents the Morphe {@code null} literal.
 */
public final class LiteralExpression implements IExpression {

  private final SourceRange range;
  private final Object value;

  /**
   * Creates a literal expression.
   *
   * @param range the source range occupied by the literal
   * @param value the value represented by the literal
   * @throws NullArgumentException if {@code range} or {@code value} is {@code null}
   */
  public LiteralExpression(SourceRange range, Object value) {
    if (range == null) throw new NullArgumentException("range");
    this.range = range;
    this.value = value;
  }

  /**
   * Returns the source range occupied by this literal.
   *
   * @return the literal source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the value represented by this literal.
   *
   * @return the literal value
   */
  public Object value() {
    return value;
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