package dev.morphe.ast.visitor;

import dev.morphe.ast.statement.*;

/**
 * Visitor for traversing Morphe statement nodes.
 *
 * <p>The visitor decouples operations performed on statement nodes from
 * the nodes themselves. Implementations can provide different behaviors
 * such as semantic analysis, compilation, or AST inspection without
 * requiring statement nodes to depend on those operations.</p>
 *
 * <p>Each concrete statement type has a corresponding {@code visit}
 * operation, allowing implementations to perform type-specific processing
 * without relying on runtime type checks.</p>
 *
 * @param <S> the result type produced by the visitor
 * @param <C> the contextual data passed during traversal
 */
public interface IStatementVisitor<S, C> {

  /**
   * Visits a block statement.
   *
   * @param statement the block statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(BlockStatement statement, C context);

  /**
   * Visits an expression statement.
   *
   * @param statement the expression statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(ExpressionStatement statement, C context);

  /**
   * Visits a variable declaration statement.
   *
   * @param statement the variable declaration statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(VariableDeclarationStatement statement, C context);

  /**
   * Visits an assignment statement.
   *
   * @param statement the assignment statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(AssignmentStatement statement, C context);

  /**
   * Visits an if statement.
   *
   * @param statement the if statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(IfStatement statement, C context);

  /**
   * Visits a match statement.
   *
   * @param statement the match statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(MatchStatement statement, C context);

  /**
   * Visits a while statement.
   *
   * @param statement the while statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(WhileStatement statement, C context);

  /**
   * Visits a do-while statement.
   *
   * @param statement the do-while statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(DoWhileStatement statement, C context);

  /**
   * Visits a for statement.
   *
   * @param statement the for statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(ForStatement statement, C context);

  /**
   * Visits a break statement.
   *
   * @param statement the break statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(BreakStatement statement, C context);

  /**
   * Visits a continue statement.
   *
   * @param statement the continue statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(ContinueStatement statement, C context);

  /**
   * Visits a return statement.
   *
   * @param statement the return statement being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  S visit(ReturnStatement statement, C context);
}