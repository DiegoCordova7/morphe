package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents an array pattern in the Morphe abstract syntax tree.
 *
 * <p>An array pattern matches the structure of an array by applying
 * individual patterns to its elements. It may optionally contain a rest
 * pattern that matches the remaining elements of the array.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * [first, second, third]
 * }</pre>
 *
 * <p>or:</p>
 *
 * <pre>{@code
 * [first, second, ...rest]
 * }</pre>
 *
 * <p>The patterns contained by this node may themselves be composed of
 * other pattern types.</p>
 *
 * <p>The syntactic and semantic validity of the pattern, including
 * restrictions on the position or use of the rest pattern, is determined
 * during parsing and semantic analysis.</p>
 */
public final class ArrayPattern implements IPattern {

  private final SourceRange range;
  private final List<IPattern> elements;
  private final IPattern rest;

  /**
   * Creates an array pattern.
   *
   * @param range the source range occupied by the pattern
   * @param elements the patterns applied to the array elements
   * @param rest the optional pattern matching the remaining elements
   * @throws NullArgumentException if {@code range} or {@code elements}
   *     is null, or if any element pattern is null
   */
  public ArrayPattern(SourceRange range, List<IPattern> elements, IPattern rest) {
    if (range == null) throw new NullArgumentException("range");
    if (elements == null) throw new NullArgumentException("elements");
    if (elements.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("element");
    this.range = range;
    this.elements = List.copyOf(elements);
    this.rest = rest;
  }

  /**
   * Returns the source range occupied by this pattern.
   *
   * @return the pattern source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the patterns applied to the individual array elements.
   *
   * @return an immutable list of element patterns
   */
  public List<IPattern> elements() {
    return elements;
  }

  /**
   * Returns the optional pattern matching the remaining array elements.
   *
   * @return the rest pattern, or {@code null} if none is present
   */
  public IPattern rest() {
    return rest;
  }

  /**
   * Accepts a pattern visitor and dispatches this pattern to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this pattern
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <C> Void accept(IPatternVisitor<C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}