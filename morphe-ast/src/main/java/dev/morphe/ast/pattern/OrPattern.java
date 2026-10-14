package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a pattern composed of multiple alternative patterns.
 *
 * <p>An or pattern matches when any of its contained patterns matches.
 * It is represented by the {@code |} operator in Morphe source code.</p>
 *
 * <p>For example, {@code 1 | 2 | 3} is represented by an
 * {@code OrPattern} containing three literal patterns.</p>
 *
 * <p>This node does not enforce semantic restrictions on the contained
 * patterns. Such restrictions are handled during later analysis phases.</p>
 */
public final class OrPattern implements IPattern {

  private final SourceRange range;
  private final List<IPattern> patterns;

  /**
   * Creates an or pattern.
   *
   * @param range the source range occupied by the pattern
   * @param patterns the alternative patterns
   * @throws NullArgumentException if {@code range} or {@code patterns}
   * is null, or if any contained pattern is null
   */
  public OrPattern(SourceRange range, List<IPattern> patterns) {
    if (range == null) throw new NullArgumentException("range");
    if (patterns == null) throw new NullArgumentException("patterns");
    if (patterns.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("pattern");
    this.range = range;
    this.patterns = List.copyOf(patterns);
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
   * Returns the alternative patterns contained in this pattern.
   *
   * @return an immutable list of alternative patterns
   */
  public List<IPattern> patterns() {
    return patterns;
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