package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a return statement in the Morphe abstract syntax tree.
 *
 * <p>A return statement terminates the execution of the current function
 * and may optionally provide a value to be returned.</p>
 *
 * <p>A return statement without a value represents a return from a
 * function whose result does not require a value:</p>
 *
 * <pre>{@code
 * return;
 * }</pre>
 *
 * <p>A return statement with a value provides the result of the current
 * function:</p>
 *
 * <pre>{@code
 * return expression;
 * }</pre>
 *
 * <p>The validity of the return statement, including whether a value is
 * required and whether the returned value is compatible with the
 * enclosing function's return type, is determined during semantic
 * analysis.</p>
 */
public final class ReturnStatement implements IStatement {

  private final SourceRange range;
  private final IExpression value;

  /**
   * Creates a return statement.
   *
   * @param range the source range occupied by the statement
   * @param value the optional expression being returned
   * @throws NullArgumentException if {@code range} is null
   */
  public ReturnStatement(SourceRange range, IExpression value) {
    if (range == null) throw new NullArgumentException("range");

    this.range = range;
    this.value = value;
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
   * Returns the expression being returned.
   *
   * @return the returned expression, or {@code null} if no value is returned
   */
  public IExpression value() {
    return value;
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