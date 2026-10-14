package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a statement consisting of an expression whose resulting value
 * is discarded.
 *
 * <p>Expression statements allow expressions to be evaluated for their
 * side effects without requiring their resulting value to be used.</p>
 *
 * <p>For example, a function call may be used as an expression statement:</p>
 *
 * <pre>{@code
 * print(value);
 * }</pre>
 *
 * <p>Likewise, an update expression can be evaluated as a statement:</p>
 *
 * <pre>{@code
 * counter++;
 * }</pre>
 *
 * <p>Whether a particular expression is meaningful when used as a statement
 * is determined during semantic analysis.</p>
 */
public final class ExpressionStatement implements IStatement {

  private final SourceRange range;
  private final IExpression expression;

  /**
   * Creates an expression statement.
   *
   * @param range the source range occupied by the statement
   * @param expression the expression being evaluated
   * @throws NullArgumentException if {@code range} or {@code expression}
   * is null
   */
  public ExpressionStatement(SourceRange range, IExpression expression) {
    if (range == null) throw new NullArgumentException("range");
    if (expression == null) throw new NullArgumentException("expression");
    this.range = range;
    this.expression = expression;
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
   * Returns the expression evaluated by this statement.
   *
   * @return the statement expression
   */
  public IExpression expression() {
    return expression;
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
}