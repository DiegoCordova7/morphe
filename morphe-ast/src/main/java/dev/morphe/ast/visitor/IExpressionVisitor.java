package dev.morphe.ast.visitor;

import dev.morphe.ast.expression.*;

/**
 * Visitor for traversing Morphe expression nodes.
 *
 * <p>The visitor decouples operations performed on expression nodes from
 * the nodes themselves. Implementations can provide different behaviors
 * such as semantic analysis, compilation, or AST inspection without
 * requiring expression nodes to depend on those operations.</p>
 *
 * <p>Each concrete expression type has a corresponding {@code visit}
 * operation, allowing implementations to perform type-specific
 * processing without relying on runtime type checks.</p>
 *
 * @param <E> the result type produced by the visitor
 * @param <C> the contextual data passed during traversal
 */
public interface IExpressionVisitor<E, C> {

  /**
   * Visits a literal expression.
   *
   * @param expression the literal expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(LiteralExpression expression, C context);

  /**
   * Visits a variable expression.
   *
   * @param expression the variable expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(VariableExpression expression, C context);

  /**
   * Visits a binary expression.
   *
   * @param expression the binary expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(BinaryExpression expression, C context);

  /**
   * Visits a unary expression.
   *
   * @param expression the unary expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(UnaryExpression expression, C context);

  /**
   * Visits an update expression.
   *
   * @param expression the update expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(UpdateExpression expression, C context);

  /**
   * Visits an array literal expression.
   *
   * @param expression the array literal expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(ArrayLiteralExpression expression, C context);

  /**
   * Visits an array access expression.
   *
   * @param expression the array access expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(ArrayAccessExpression expression, C context);

  /**
   * Visits an array creation expression.
   *
   * @param expression the array creation expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(ArrayCreationExpression expression, C context);

  /**
   * Visits an if expression.
   *
   * @param expression the if expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(IfExpression expression, C context);

  /**
   * Visits a match expression.
   *
   * @param expression the match expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(MatchExpression expression, C context);

  /**
   * Visits an attempt expression.
   *
   * @param expression the attempt expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(AttemptExpression expression, C context);

  /**
   * Visits a function call expression.
   *
   * @param expression the function call expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(FunctionCallExpression expression, C context);

  /**
   * Visits a lambda expression.
   *
   * @param expression the lambda expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(LambdaExpression expression, C context);

  /**
   * Visits a composite instance expression.
   *
   * @param expression the composite instance expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(CompositeInstanceExpression expression, C context);

  /**
   * Visits a field access expression.
   *
   * @param fieldAccessExpression the field access expression being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  E visit(FieldAccessExpression fieldAccessExpression, C context);
}