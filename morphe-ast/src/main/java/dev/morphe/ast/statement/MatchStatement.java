package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a match statement in the Morphe abstract syntax tree.
 *
 * <p>A match statement evaluates a value and selects the first matching
 * case according to its pattern. Each case contains a block of statements
 * that is executed when its pattern matches.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * match (value) {
 *     1 => {
 *         statement;
 *     }
 *     2 | 3 => {
 *         otherStatement;
 *     }
 *     default => {
 *         fallbackStatement;
 *     }
 * }
 * }</pre>
 *
 * <p>The semantic validity of the patterns and their compatibility with
 * the matched value is determined during semantic analysis.</p>
 */
public final class MatchStatement implements IStatement {

  private final SourceRange range;
  private final IExpression value;
  private final List<MatchCase> cases;

  /**
   * Creates a match statement.
   *
   * @param range the source range occupied by the statement
   * @param value the expression whose value is matched
   * @param cases the cases handled by the statement
   * @throws NullArgumentException if {@code range}, {@code value}, or
   * {@code cases} is null, or if any case is null
   */
  public MatchStatement(SourceRange range, IExpression value, List<MatchCase> cases) {
    if (range == null) throw new NullArgumentException("range");
    if (value == null) throw new NullArgumentException("value");
    if (cases == null) throw new NullArgumentException("cases");
    if (cases.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("case");
    this.range = range;
    this.value = value;
    this.cases = List.copyOf(cases);
  }

  /**
   * Returns the source range occupied by this statement.
   *
   * @return the statement source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the expression whose value is matched.
   *
   * @return the matched value expression
   */
  public IExpression value() {
    return value;
  }

  /**
   * Returns the cases handled by this statement.
   *
   * @return an immutable list of match cases
   */
  public List<MatchCase> cases() {
    return cases;
  }

  /**
   * Accepts a statement visitor and dispatches this statement to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this statement
   * @param <S> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <S, C> S accept(IStatementVisitor<S, C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }

  /**
   * Represents a single case in a match statement.
   *
   * <p>A match case associates a pattern with a statement that is
   * executed when the pattern matches the value being examined.</p>
   *
   * <p>The body may be a single statement or a block of statements.</p>
   */
  public static final class MatchCase {

    private final IPattern pattern;
    private final IStatement body;

    /**
     * Creates a match case.
     *
     * @param pattern the pattern used to match the value
     * @param body the statement executed when the pattern matches
     * @throws NullArgumentException if {@code pattern} or {@code body}
     * is null
     */
    public MatchCase(IPattern pattern, IStatement body) {
      if (pattern == null) throw new NullArgumentException("pattern");
      if (body == null) throw new NullArgumentException("body");
      this.pattern = pattern;
      this.body = body;
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
     * Returns the statement executed when this case matches.
     *
     * @return the case body
     */
    public IStatement body() {
      return body;
    }
  }
}