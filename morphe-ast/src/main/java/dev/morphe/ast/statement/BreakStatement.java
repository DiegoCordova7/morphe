package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a break statement in the Morphe abstract syntax tree.
 *
 * <p>A break statement terminates the nearest enclosing loop or other
 * breakable control-flow construct.</p>
 *
 * <p>The validity of a break statement, including whether it appears
 * within a valid breakable context, is determined during semantic
 * analysis.</p>
 */
public final class BreakStatement implements IStatement {

  private final SourceRange range;

  /**
   * Creates a break statement.
   *
   * @param range the source range occupied by the statement
   * @throws NullArgumentException if {@code range} is null
   */
  public BreakStatement(SourceRange range) {
    if (range == null) throw new NullArgumentException("range");
    this.range = range;
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