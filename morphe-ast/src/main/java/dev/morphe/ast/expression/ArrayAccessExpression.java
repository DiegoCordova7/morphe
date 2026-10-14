package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents an array access expression.
 *
 * <p>An array access expression retrieves the element at a specified
 * index from an array expression.</p>
 *
 * <p>For example, the expression {@code array[index]} is represented
 * by an {@code ArrayAccessExpression} whose array is the expression
 * {@code array} and whose index is the expression {@code index}.</p>
 *
 * <p>Both the array and index are represented as {@link IExpression}
 * instances, allowing array accesses to be composed with other
 * expressions.</p>
 */
public final class ArrayAccessExpression implements IExpression {

  private final SourceRange range;
  private final IExpression array;
  private final IExpression index;

  /**
   * Creates an array access expression.
   *
   * @param range the source range occupied by the expression
   * @param array the expression representing the array being accessed
   * @param index the expression representing the element index
   * @throws NullArgumentException if {@code range}, {@code array},
   * or {@code index} is null
   */
  public ArrayAccessExpression(SourceRange range, IExpression array, IExpression index) {
    if (range == null) throw new NullArgumentException("range");
    if (array == null) throw new NullArgumentException("array");
    if (index == null) throw new NullArgumentException("index");
    this.range = range;
    this.array = array;
    this.index = index;
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
   * Returns the expression representing the accessed array.
   *
   * @return the array expression
   */
  public IExpression array() {
    return array;
  }

  /**
   * Returns the expression representing the accessed index.
   *
   * @return the index expression
   */
  public IExpression index() {
    return index;
  }

  /**
   * Accepts an expression visitor and dispatches this node to it.
   *
   * @param visitor the visitor processing this expression
   * @param context the operational context passed through the traversal
   * @param <E> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <E, C> E accept(IExpressionVisitor<E, C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}