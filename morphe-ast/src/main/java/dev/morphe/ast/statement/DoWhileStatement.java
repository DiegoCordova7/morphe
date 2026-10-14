package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a do-while loop statement in the Morphe abstract syntax tree.
 *
 * <p>A do-while statement executes its body once and then repeatedly
 * executes it while its condition evaluates to true. Unlike a while
 * statement, the condition is evaluated after the first execution of
 * the body.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * do {
 *     statement;
 * } while (condition);
 * }</pre>
 *
 * <p>The semantic validity of the condition, including whether it
 * evaluates to a boolean value, is determined during semantic analysis.</p>
 */
public final class DoWhileStatement implements IStatement {

  private final SourceRange range;
  private final BlockStatement body;
  private final IExpression condition;

  /**
   * Creates a do-while statement.
   *
   * @param range the source range occupied by the statement
   * @param body the block executed by the loop
   * @param condition the expression controlling subsequent iterations
   * @throws NullArgumentException if any argument is null
   */
  public DoWhileStatement(SourceRange range, BlockStatement body, IExpression condition) {
    if (range == null) throw new NullArgumentException("range");
    if (body == null) throw new NullArgumentException("body");
    if (condition == null) throw new NullArgumentException("condition");

    this.range = range;
    this.body = body;
    this.condition = condition;
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
   * Returns the block executed by the loop.
   *
   * @return the loop body
   */
  public BlockStatement body() {
    return body;
  }

  /**
   * Returns the expression controlling subsequent iterations.
   *
   * @return the loop condition
   */
  public IExpression condition() {
    return condition;
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