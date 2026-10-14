package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents an array literal expression.
 *
 * <p>An array literal creates an array from a sequence of expressions.
 * Each element may be any {@link IExpression}, allowing array literals
 * to contain nested expressions and other array literals.</p>
 *
 * <p>For example, the expression {@code [1, 2, 3]} is represented by an
 * {@code ArrayLiteralExpression} containing three element expressions.</p>
 *
 * <p>The element list is immutable and cannot contain Java {@code null}
 * references. A Morphe null value, when supported, is represented by an
 * appropriate AST expression rather than a Java {@code null} reference.</p>
 */
public final class ArrayLiteralExpression implements IExpression {

  private final SourceRange range;
  private final List<IExpression> elements;

  /**
   * Creates an array literal expression.
   *
   * @param range the source range occupied by the expression
   * @param elements the expressions representing the array elements
   * @throws NullArgumentException if {@code range} or {@code elements}
   * is null, or if any element is null
   */
  public ArrayLiteralExpression(SourceRange range, List<IExpression> elements) {
    if (range == null) throw new NullArgumentException("range");
    if (elements == null) throw new NullArgumentException("elements");
    if (elements.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("element");
    this.range = range;
    this.elements = List.copyOf(elements);
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
   * Returns the expressions representing the elements of this array.
   *
   * <p>The returned list is immutable.</p>
   *
   * @return an immutable list of array elements
   */
  public List<IExpression> elements() {
    return elements;
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