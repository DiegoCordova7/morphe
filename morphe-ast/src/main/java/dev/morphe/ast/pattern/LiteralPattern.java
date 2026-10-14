package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

/**
 * Represents a literal pattern in the Morphe abstract syntax tree.
 *
 * <p>A literal pattern matches a value against a specific literal value.
 * It can be used in match constructs to match values such as integers,
 * strings, booleans, and other literal values supported by Morphe.</p>
 *
 * <p>For example, the pattern {@code 10} is represented by a
 * {@code LiteralPattern} containing the value {@code 10}.</p>
 */
public final class LiteralPattern implements IPattern {

  private final SourceRange range;
  private final Object value;

  /**
   * Creates a literal pattern.
   *
   * @param range the source range occupied by the pattern
   * @param value the literal value matched by the pattern
   * @throws NullArgumentException if {@code range} or {@code value} is null
   */
  public LiteralPattern(SourceRange range, Object value) {
    if (range == null) throw new NullArgumentException("range");
    if (value == null) throw new NullArgumentException("value");
    this.range = range;
    this.value = value;
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
   * Returns the literal value matched by this pattern.
   *
   * @return the literal pattern value
   */
  public Object value() {
    return value;
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