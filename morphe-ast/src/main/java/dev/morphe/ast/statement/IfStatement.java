package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents an if statement in the Morphe abstract syntax tree.
 *
 * <p>An if statement evaluates a condition and executes its then branch
 * when the condition evaluates to true. An optional else branch is
 * executed when the condition evaluates to false.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * if (condition) {
 *     statement;
 * }
 *
 * if (condition) {
 *     statement;
 * } else {
 *     otherStatement;
 * }
 * }</pre>
 *
 * <p>The semantic validity of the condition, including whether it
 * evaluates to a boolean value, is determined during semantic analysis.</p>
 */
public final class IfStatement implements IStatement {

  private final SourceRange range;
  private final IExpression condition;
  private final BlockStatement thenBranch;
  private final BlockStatement elseBranch;

  /**
   * Creates an if statement.
   *
   * @param range the source range occupied by the statement
   * @param condition the expression controlling the branch
   * @param thenBranch the block executed when the condition is true
   * @param elseBranch the optional block executed when the condition is
   * false
   * @throws NullArgumentException if {@code range}, {@code condition}, or
   * {@code thenBranch} is null
   */
  public IfStatement(SourceRange range, IExpression condition, BlockStatement thenBranch, BlockStatement elseBranch) {
    if (range == null) throw new NullArgumentException("range");
    if (condition == null) throw new NullArgumentException("condition");
    if (thenBranch == null) throw new NullArgumentException("thenBranch");
    this.range = range;
    this.condition = condition;
    this.thenBranch = thenBranch;
    this.elseBranch = elseBranch;
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
   * Returns the condition controlling the statement.
   *
   * @return the condition expression
   */
  public IExpression condition() {
    return condition;
  }

  /**
   * Returns the block executed when the condition is true.
   *
   * @return the then branch
   */
  public BlockStatement thenBranch() {
    return thenBranch;
  }

  /**
   * Returns the block executed when the condition is false.
   *
   * @return the else branch, or null if no else branch is present
   */
  public BlockStatement elseBranch() {
    return elseBranch;
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