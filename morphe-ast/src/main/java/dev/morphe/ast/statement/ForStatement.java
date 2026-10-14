package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a for loop statement in the Morphe abstract syntax tree.
 *
 * <p>A for statement consists of an optional initialization statement,
 * an optional condition, an optional update statement, and a body.
 * The initialization is executed before the first iteration, the
 * condition controls whether another iteration is performed, and the
 * update is executed after each iteration.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * for (initialization; condition; update) {
 *     statement;
 * }
 * }</pre>
 *
 * <p>Each of the initialization, condition, and update components may
 * be omitted.</p>
 *
 * <p>The semantic validity of the condition and the individual loop
 * components is determined during semantic analysis.</p>
 */
public final class ForStatement implements IStatement {

  private final SourceRange range;
  private final IStatement initialization;
  private final IExpression condition;
  private final IStatement update;
  private final BlockStatement body;

  /**
   * Creates a for statement.
   *
   * @param range the source range occupied by the statement
   * @param initialization the optional initialization statement
   * @param condition the optional expression controlling the loop
   * @param update the optional update statement
   * @param body the block executed by the loop
   * @throws NullArgumentException if {@code range} or {@code body} is null
   */
  public ForStatement(SourceRange range, IStatement initialization, IExpression condition,
                      IStatement update, BlockStatement body) {
    if (range == null) throw new NullArgumentException("range");
    if (body == null) throw new NullArgumentException("body");
    this.range = range;
    this.initialization = initialization;
    this.condition = condition;
    this.update = update;
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
   * Returns the optional initialization statement.
   *
   * @return the initialization statement, or null if omitted
   */
  public IStatement initialization() {
    return initialization;
  }

  /**
   * Returns the optional expression controlling the loop.
   *
   * @return the loop condition, or null if omitted
   */
  public IExpression condition() {
    return condition;
  }

  /**
   * Returns the optional update statement.
   *
   * @return the update statement, or null if omitted
   */
  public IStatement update() {
    return update;
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