package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents an access to a named field of an expression in the Morphe
 * abstract syntax tree.
 *
 * <p>The target expression identifies the value whose field is accessed,
 * while the field name identifies the field being referenced. Resolution
 * of the target type and validation of the field are performed during
 * semantic analysis.</p>
 *
 * <p>Nested field accesses are represented by composing
 * {@code FieldAccessExpression} nodes. For example,
 * {@code person.address.city} is represented by nesting field access
 * expressions.</p>
 */
public final class FieldAccessExpression implements IExpression {

  private final SourceRange range;
  private final IExpression target;
  private final String field;

  /**
   * Creates a field access expression.
   *
   * @param range the source range occupied by the expression
   * @param target the expression whose field is being accessed
   * @param field the name of the accessed field
   * @throws NullArgumentException if {@code range}, {@code target}, or
   * {@code field} is null
   */
  public FieldAccessExpression(SourceRange range, IExpression target, String field) {
    if (range == null) throw new NullArgumentException("range");
    if (target == null) throw new NullArgumentException("target");
    if (field == null) throw new NullArgumentException("field");
    this.range = range;
    this.target = target;
    this.field = field;
  }

  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the expression whose field is being accessed.
   *
   * @return the target expression
   */
  public IExpression target() {
    return target;
  }

  /**
   * Returns the name of the accessed field.
   *
   * @return the field name
   */
  public String field() {
    return field;
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