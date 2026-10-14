package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents an array creation expression.
 *
 * <p>An array creation expression creates a new array whose element type
 * is specified by {@code elementType} and whose dimensions are determined
 * by a list of expressions.</p>
 *
 * <p>For example, an expression such as:</p>
 *
 * <pre>
 * new Integer[10]
 * </pre>
 *
 * <p>is represented by an {@code ArrayCreationExpression} with
 * {@link dev.morphe.ast.type.PrimitiveType#INTEGER} as its element type
 * and one dimension containing the literal expression {@code 10}.</p>
 *
 * <p>Multidimensional arrays are represented by providing multiple
 * dimension expressions. For example:</p>
 *
 * <pre>
 * new Integer[10][20]
 * </pre>
 *
 * <p>is represented by an element type of {@code Integer} and two
 * dimension expressions containing {@code 10} and {@code 20}.</p>
 *
 * <p>This node represents array creation independently from the bytecode
 * and runtime type systems.</p>
 */
public final class ArrayCreationExpression implements IExpression {

  private final SourceRange range;
  private final IASTType elementType;
  private final List<IExpression> dimensions;

  /**
   * Creates an array creation expression.
   *
   * @param range the source range occupied by the expression
   * @param elementType the type of the elements stored in the array
   * @param dimensions the expressions determining the array dimensions
   * @throws NullArgumentException if {@code range}, {@code elementType},
   *     or {@code dimensions} is null, or if any dimension is null
   */
  public ArrayCreationExpression(SourceRange range, IASTType elementType, List<IExpression> dimensions) {
    if (range == null) throw new NullArgumentException("range");
    if (elementType == null) throw new NullArgumentException("elementType");
    if (dimensions == null) throw new NullArgumentException("dimensions");
    if (dimensions.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("dimension");
    this.range = range;
    this.elementType = elementType;
    this.dimensions = List.copyOf(dimensions);
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
   * Returns the type of the elements contained in the created array.
   *
   * @return the array element type
   */
  public IASTType elementType() {
    return elementType;
  }

  /**
   * Returns the expressions determining the dimensions of the created
   * array.
   *
   * <p>The order of the expressions corresponds to the order of the
   * dimensions in the source code.</p>
   *
   * @return an immutable list of dimension expressions
   */
  public List<IExpression> dimensions() {
    return dimensions;
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