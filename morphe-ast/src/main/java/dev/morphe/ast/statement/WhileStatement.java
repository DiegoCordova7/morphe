package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a while loop statement in the Morphe abstract syntax tree.
 *
 * <p>A while statement repeatedly executes its body while its condition
 * evaluates to true.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * while (condition) {
 *     statement;
 * }
 * }</pre>
 *
 * <p>The semantic validity of the condition, including whether it
 * evaluates to a boolean value, is determined during semantic analysis.</p>
 */
public final class WhileStatement implements IStatement {

  private final SourceRange range;
  private final IExpression condition;
  private final BlockStatement body;

  /**
   * Creates a while statement.
   *
   * @param range the source range occupied by the statement
   * @param condition the expression controlling the loop
   * @param body the block executed while the condition is true
   * @throws NullArgumentException if any argument is null
   */
  public WhileStatement(SourceRange range, IExpression condition, BlockStatement body) {
    if (range == null) throw new NullArgumentException("range");
    if (condition == null) throw new NullArgumentException("condition");
    if (body == null) throw new NullArgumentException("body");
    this.range = range;
    this.condition = condition;
    this.body = body;
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
   * Returns the expression controlling the loop.
   *
   * @return the loop condition
   */
  public IExpression condition() {
    return condition;
  }

  /**
   * Returns the block executed while the condition is true.
   *
   * @return the loop body
   */
  public BlockStatement body() {
    return body;
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