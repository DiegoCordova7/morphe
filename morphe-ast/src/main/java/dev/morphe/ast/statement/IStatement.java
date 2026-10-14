package dev.morphe.ast.statement;

import dev.morphe.ast.IASTNode;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a statement in the Morphe abstract syntax tree.
 *
 * <p>Statements represent constructs that perform an operation during
 * program execution. Concrete statement nodes define the specific operation
 * or construct represented by the statement.</p>
 */
public interface IStatement extends IASTNode {

  /**
   * Accepts a statement visitor and dispatches this statement to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this statement
   * @param context the operational context passed through the traversal
   * @param <S> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   */
  <S, C> S accept(IStatementVisitor<S, C> visitor, C context);
}