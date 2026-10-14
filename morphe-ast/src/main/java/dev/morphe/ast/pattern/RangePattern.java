package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

/**
 * Represents a range pattern in the Morphe abstract syntax tree.
 *
 * <p>A range pattern matches values within a range defined by a start
 * pattern and an end pattern. It is represented by the range operator
 * {@code ..} in Morphe source code.</p>
 *
 * <p>For example, {@code 1 .. 10} is represented by a
 * {@code RangePattern} containing a literal pattern for {@code 1}
 * and a literal pattern for {@code 10}.</p>
 *
 * <p>The validity and semantics of the range bounds are determined by
 * later analysis phases.</p>
 */
public final class RangePattern implements IPattern {

  private final SourceRange range;
  private final IPattern start;
  private final IPattern end;

  /**
   * Creates a range pattern.
   *
   * @param range the source range occupied by the pattern
   * @param start the pattern defining the beginning of the range
   * @param end the pattern defining the end of the range
   * @throws NullArgumentException if {@code range}, {@code start},
   * or {@code end} is null
   */
  public RangePattern(SourceRange range, IPattern start, IPattern end) {
    if (range == null) throw new NullArgumentException("range");
    if (start == null) throw new NullArgumentException("start");
    if (end == null) throw new NullArgumentException("end");
    this.range = range;
    this.start = start;
    this.end = end;
  }

  /**
   * Returns the source range occupied by this pattern.
   *
   * @return the source range of this pattern
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the pattern defining the beginning of the range.
   *
   * @return the range start pattern
   */
  public IPattern start() {
    return start;
  }

  /**
   * Returns the pattern defining the end of the range.
   *
   * @return the range end pattern
   */
  public IPattern end() {
    return end;
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