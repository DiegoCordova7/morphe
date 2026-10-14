package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

/**
 * Represents a variable binding pattern in the Morphe abstract syntax tree.
 *
 * <p>A variable pattern matches a value and binds that value to the
 * specified name within the scope established by the enclosing pattern
 * matching construct.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * match value {
 *     x => ...
 * }
 * }</pre>
 *
 * <p>The pattern itself does not resolve or create the binding. Binding
 * validity and scope rules are determined during semantic analysis.</p>
 */
public final class VariablePattern implements IPattern {

  private final SourceRange range;
  private final String name;

  /**
   * Creates a variable pattern.
   *
   * @param range the source range occupied by the pattern
   * @param name the name introduced by the pattern
   * @throws NullArgumentException if {@code range} or {@code name} is null
   */
  public VariablePattern(SourceRange range, String name) {
    if (range == null) throw new NullArgumentException("range");
    if (name == null) throw new NullArgumentException("name");
    this.range = range;
    this.name = name;
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
   * Returns the name introduced by this pattern.
   *
   * @return the variable binding name
   */
  public String name() {
    return name;
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