package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a match expression in the Morphe abstract syntax tree.
 *
 * <p>A match expression evaluates a value and selects the result of
 * the first matching case.</p>
 *
 * <p>Each case associates a pattern with an expression that produces
 * the result of that case.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * match (value) {
 *   1 => "one"
 *   2 | 3 => "two or three"
 *   4 .. 6 => "fourth to six"
 *   _ => "other"
 * }
 * }</pre>
 *
 * <p>Semantic restrictions such as requiring a default case, preventing
 * duplicate patterns, or determining whether cases are exhaustive are
 * handled by later analysis phases.</p>
 */
public final class MatchExpression implements IExpression {

  private final SourceRange range;
  private final IExpression value;
  private final List<MatchCase> cases;

  /**
   * Creates a match expression.
   *
   * @param range the source range occupied by the expression
   * @param value the expression whose value is matched
   * @param cases the cases used to determine the resulting value
   * @throws NullArgumentException if {@code range}, {@code value},
   * {@code cases}, or any case is null
   */
  public MatchExpression(SourceRange range, IExpression value, List<MatchCase> cases) {
    if (range == null) throw new NullArgumentException("range");
    if (value == null) throw new NullArgumentException("value");
    if (cases == null) throw new NullArgumentException("cases");
    if (cases.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("case");
    this.range = range;
    this.value = value;
    this.cases = List.copyOf(cases);
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
   * Returns the expression whose value is matched.
   *
   * @return the expression being matched
   */
  public IExpression value() {
    return value;
  }

  /**
   * Returns the cases contained in this match expression.
   *
   * @return an immutable list of match cases
   */
  public List<MatchCase> cases() {
    return cases;
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

  /**
   * Represents a single case within a match expression.
   *
   * <p>A match case associates a pattern with an expression whose
   * value is produced when the pattern matches.</p>
   */
  public static final class MatchCase {

    private final IPattern pattern;
    private final IExpression result;

    /**
     * Creates a match expression case.
     *
     * @param pattern the pattern used to match the value
     * @param result the expression producing the case result
     * @throws NullArgumentException if {@code pattern} or {@code result}
     * is null
     */
    public MatchCase(IPattern pattern, IExpression result) {
      if (pattern == null) throw new NullArgumentException("pattern");
      if (result == null) throw new NullArgumentException("result");
      this.pattern = pattern;
      this.result = result;
    }

    /**
     * Returns the pattern associated with this case.
     *
     * @return the case pattern
     */
    public IPattern pattern() {
      return pattern;
    }

    /**
     * Returns the expression producing the result of this case.
     *
     * @return the case result expression
     */
    public IExpression result() {
      return result;
    }
  }
}