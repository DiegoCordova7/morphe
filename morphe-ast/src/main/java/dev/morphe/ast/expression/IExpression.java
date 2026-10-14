package dev.morphe.ast.expression;

import dev.morphe.ast.IASTNode;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents an expression in the Morphe abstract syntax tree.
 *
 * <p>
 * Expressions represent constructs that produce a value when evaluated.
 * Concrete expression nodes define the specific operation or construct
 * represented by the expression.
 */
public interface IExpression extends IASTNode {

  /**
   * Accepts an expression visitor and dispatches this expression to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this expression
   * @param context the operational context passed through the traversal
   * @param <E> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   */
  <E, C> E accept(IExpressionVisitor<E, C> visitor, C context);
}