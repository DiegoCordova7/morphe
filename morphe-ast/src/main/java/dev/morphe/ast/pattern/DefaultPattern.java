package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

/**
 * Represents the default pattern in a Morphe match construct.
 *
 * <p>The default pattern matches any value that has not been matched
 * by a preceding pattern.</p>
 *
 * <p>It is represented by the underscore character {@code _} in
 * Morphe source code.</p>
 */
public final class DefaultPattern implements IPattern {

  private final SourceRange range;

  /**
   * Creates a default pattern.
   *
   * @param range the source range occupied by the pattern
   * @throws NullArgumentException if {@code range} is null
   */
  public DefaultPattern(SourceRange range) {
    if (range == null) throw new NullArgumentException("range");
    this.range = range;
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